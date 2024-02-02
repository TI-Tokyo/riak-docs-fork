---
title: "Rolling with Eventual Consistency"
description: "Using the example of averaging many values, we explore some concerns that we need to address in a distributed, eventually consistent system like Riak."
project: community
lastmod: 2016-07-31T22:30:42+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2012-09-18T05:00:04+00:00
---
September 18, 2012
In a previous post I wrote about the different mindset that a software engineer should have when building for a key-value database as opposed to a relational database. When working with a relational database, you describe the model first and then query the data later. With a key-value database, you focus first on what you want the result of the query to look like, and then work backward toward a model.
As an example, I described averaging some value:
Some questions came in about what that would specifically look like in code. The rest of this post will explore a solution that takes into account the distributed, eventually consistent nature of Riak.
Average the Scenario
Say we have a DataPoint object. An object of this type can be created, but for simplicity let us say that it is never changed or deleted. Every DataPoint has a value property which is an integer. In our application, we sometimes want the average of all of the value properties for all of the DataPoint objects. Oh, and let’s say we have trillions of DataPoint objects.
Notice that according to my previous definition, this usage scenario is ad-hoc because we don’t know exactly when the application needs the average, but it is not dynamic because we know that we will be querying for the average of the value property, and not the average of some property defined at query time.
Using the client library ripple, we might define the object as follows:

class DataPoint
include Ripple::Document
property :value, Integer, :presence => true
end
The ripple library stores objects as JSON behind the scenes. The following request sent to Riak:

curl http://127.0.0.1:8091/buckets/data\_points/keys/somekey
might return something like:

{"value":23,"\_type":"DataPoint"}
Pretty simple so far. Now how should we calculate the average? The naïve solution would be to read all of the DataPoint objects out, read their value property, and average them. This might work for small sets of objects, but remember we have trillions of them. Fetching all of that data for one simple query is not a realistic solution.
We know that we want the average at the application level. Riak is really good at serving single objects. So instead of querying for the average, we should simply fetch the average as its own data object in Riak. In ripple this might look like:

class Statistic
include Ripple::Document
property :average, Float, :presence => true
end
Now we just have to hook the two together. We can add a hook to the DataPoint object so that every time it is saved, it updates the Statistic object.

class DataPoint
include Ripple::Document
property :value, Integer, :presence => true
after\_save :update\_statistics

 def update\_statistics
 id = 'data\_point\_document'
 statistic = StatisticDocument.find(id) || StatisticDocument.new
 statistic.key = id
 statistic.update\_with self.value
 end
end

We need to define some new properties and the update\_with method for the Statistic object.

class Statistic
include Ripple::Document
property :average, Float, :presence => true
property :count, Integer, :presence => true

 def update\_with(value)
 self.average = (self.average \* self.count + value) / (self.count + 1)
 self.count = self.count + 1
 self.save
 end
end

By storing the additional information of the number of objects in the count property, we have enough information to reconstruct a rolling average.
Fast Answers
Now whenever we want the average value, we simply fetch the one Riak object:
Statistic.find('data\_point\_document').average
Our answer comes back really fast, because it has been pre-computed.
But Wait!
The example above works great for one single-threaded application. To preserve the distributed and fault-tolerant features that Riak provides for us, we need to do a little more work.
Consider the following scenario. One copy of the application, let’s call it ClientA, is talking to the Riak cluster. Another, ClientB, is also talking to the cluster. ClientA and ClientB both want to add a DataPoint object. After saving their respective DataPoint objects, they both fetch the Statistic object, which currently has average property set to 10.0, and compute a new average. ClientA thinks the new average property is 12.0. But ClientB added a different value, so it thinks the new average property is 9.0. Both save to the Riak cluster, and we have a classic race condition. Which client wins? It doesn’t matter, because both answers are wrong. Both fail to take into account the DataPoint object handled by the other client.
To get the correct answer, let’s separate the clients. Both clients can save to the same object, but we will partition the data within the object. Formerly the json object looked like:
{"average":10.0,"count":4,"\_type":"Statistic"}
We want it to look more like:
{"client\_data":{"ClientA":{"average":10.0,"count":4},"ClientB":{"average":10.0,"count":4}}"\_type":"Statistic"}
The data model is more complicated, but we now have enough information to compute the correct answer. We will change the model so that ClientA will only change the “ClientA” portion of the data object, and ClientB will only change the “ClientB” portion of the object. The application will know when we ask for the answer to compute the average across all clients.
The Statistic object now looks something like this:
class Statistic
include Ripple::Document
property :client\_data, Hash, :presence => true

 def update\_with(value)
 self.reload
 self.client\_data ||= {}
 statistic = self.client\_data[Client.id] || {'average' => 0.0, 'count' => 0}
 statistic['average'] = (statistic['average'] \* statistic['count'] + value) / (statistic['count'] + 1)
 statistic['count'] = (statistic['count'] + 1)
 self.client\_data[Client.id] = statistic
 self.save
 end

 def count
 self.client\_data.map{|h| h[1]['count']}.inject(0, &:+)
 end

 def average
 self.client\_data.map{|h| h[1]['count'] \* h[1]['average']}.inject(0, &:+).to\_f / self.count
 end
end

The method Client.id in the above can be set in a couple of different ways. In this case, we set it using an environment variable passed in at runtime. We rely on a single thread per application to ensure that each client has a single, consistent client identifier, and that it always writes to its own portion of the data object.
We also make sure that we always fetch the object before we write to it, so that this client is always working with the latest copy of the object and not a stale one that was updated somewhere else in the application.
But Wait [Again]!
We solved part of the problem by keeping each client out of the other’s business, but we still have a race condition when the clients try to save the same Statistic object to the cluster. If Riak is operating on a last-write-wins principle, then whichever client has the later timestamp is going to overwrite the other. That gives us the wrong answer, and that won’t do.
We can rely on Riak’s vector clocks to solve this problem. First, we set the allow\_mult property on the bucket for Statistic objects so that Riak will store both copies of an object when two come in during a race condition.
Statistic.bucket.allow\_mult = true
For reasons outside of the scope of this post, we also want to make sure that we have a PR and a PW quorum greater than N. This helps us ensure that our answer isn’t misread from a fallback node when we reload the Statistic object. N is 3 by default, so the following does the trick by setting PR and PW both to 2.
Statistic.bucket.props = Statistic.bucket.props.merge(:pr => 2, :pw => 2)
When ripple reads an object, it gets a vector clock. When it saves the object, it sends back the same vector clock. Every time Riak changes an object, it changes the vector clock in such a way that it preserves the clock’s lineage. So if Riak gets back a different vector clock with an object than the one it currently has, it knows it has a collision and saves both values as siblings.
Our race condition plays out. We now have two sibling objects in Riak stored under the same key: one is up-to-date for ClientA, and the other is up-to-date for ClientB. The truth is somewhere in between.
The next time ripple reads the Statistic object, it gets back both objects with a new vector clock for the set. We now have to resolve the conflict to find the true answer. We know that ClientA‘s true answer is going to have the highest count property for “ClientA”, and we know that ClientB‘s true answer is going to have the highest count property for “ClientB”. Since we know that only one copy of a given client was writing at a time [single-threaded], we know that only the client data with the highest count is correct.
We can find the true answer by merging the siblings and comparing the data one client at a time. For each client, we always take the value with the highest count property and throw away the smaller counts, which represent older values.
When ripple saves back the object, it sends the new vector clock as well so that Riak knows to replace the old siblings with this new resolved object. We handle this with the on\_conflict method in ripple.
class StatisticDocument
include Ripple::Document
property :client\_data, Hash, :presence => true

 def update\_with(value)
 self.reload
 self.client\_data ||= {}
 statistic = self.client\_data[Client.id] || {'average' => 0.0, 'count' => 0}
 statistic['average'] = (statistic['average'] \* statistic['count'] + value).to\_f / (statistic['count'] + 1)
 statistic['count'] = (statistic['count'] + 1)
 self.client\_data[Client.id] = statistic
 self.save
 end

 def count
 self.client\_data.map{|h| h[1]['count']}.inject(0, &:+)
 end

 def average
 self.client\_data.map{|h| h[1]['count'] \* h[1]['average']}.inject(0, &:+).to\_f / self.count
 end

 on\_conflict do |siblings, c|
 resolved = {}
 siblings.reject!{|s| s.client\_data == nil}
 siblings.each do |sibling|
 resolved.merge! sibling.client\_data do |client\_id, resolved\_value, sibling\_value|
 resolved\_value['count'] > sibling\_value['count'] ? resolved\_value : sibling\_value
 end
 end
 self.client\_data = resolved
 end
end

Voila! We now have a rolling average that gracefully handles race conditions caused by the distributed nature of a Riak cluster.
A working example of this solution is available in my riak-rolling-average repo. As usual, the good stuff is in the tests. You can run the tests with bundle exec rake CLIENT=someclient where someclient is unique to that Ruby thread.
Note that test\_example.rb runs five clients concurrently by shelling out to five rake tasks. Each rake task loads in a pre-defined chunk of data from the data file. This causes plenty of race conditions, which is what we want to simulate. We still get the correct answer.
The Path Not Traveled
If you aren’t familiar with my previous advice on approaching key-value architecture, you might be tempted to solve a use case like the average described above using Riak’s map/reduce functionality. There are several reasons why the solution above would be preferred, but let us play the devil’s advocate and explore the map/reduce solution.
You can view the entire code in the repo below, but if we solve this using javascript map/reduce functions, then we can extract the data from each DatePoint object in the following map phase:
function(riakObject){
match = riakObject.values[0].data.match(/"value":([d]+)/);
if(match){
return [[parseInt(match[1]), 1]];
} else {
return [null];
}
}
Then compute the average by combining the results together in the following reduce phase:

function(values){
var sum = 0.0;
var count = 0;
for(i=0; i 0) {
return [[(sum / count), count]];
} else {
return [];
}
}
If you look in the mapreduce branch of the riak-rolling-average repo, you will find the code for this example. On my local laptop, which I admit is not optimized for this type of operation, it takes about 3 seconds to fetch the answer with map/reduce searching over 5000 DataPoint objects. [A compiled Erlang map/reduce function would perform much better.] It only took a few milliseconds to fetch the answer using the pre-computing code.
Even if I did have a more powerful cluster on which to run this map/reduce, recall that we have trillions of DataPoint objects. Each object must be fetched from the key-value store and pulled into the javascript virtual machine for processing. This is essentially equivalent to the naïve approach described earlier, but performed on the server instead of the application client. If multiple users initiate the same map/reduce, contention for resources would quickly overwhelm the cluster’s hardware. In practice, we generally reserve Riak’s map/reduce for data migrations and offline analysis although there are exceptions to that guideline.
Conceptually, the map/reduce solution might be simpler to architect and it might seem more intuitive to people from an RDBMS background. Admittedly, the recommended solution above uses more resources during writes, opens the possibility of number of clients and vector clocks expanding the object size and complexity, and generally requires a more complex model; however, it also provides a much more performant answer and is more suitable to the eventual consistency and distributed nature of Riak.
Other Issues
Note that I did not address bootstrapping the average when DataPoint objects already exist, nor handling deletes or updates to existing DataPoint objects.
Happy Coding
Some of the ways we need to think about architecture problems when we write applications for Riak might not be intuitive at first, because the same problems are already solved by convention in the RDBMS world. Some of this shift in mindset is necessitated by the key-value nature of Riak. As the rolling average example here demonstrates, some concerns that we need to address in the application architecture are brought about by the distributed, eventually consistent nature of Riak as well. This shift in mindset is a tradeoff that you can choose to make for the sake of high availability, fault tolerance, horizontal scaling, and other nice features that Riak provides. If you enjoy learning new things to take advantage of new capabilities, then I’d wager you will enjoy developing applications with Riak.
Casey
Related

A working example of this code is available in my riak-rolling-average repo. Check out the mapreduce branch for the production-unfriendly version using Riak’s map/reduce.
Vector Clocks are Easy
Vector Clocks are Hard
Meangirls provides serializable data types for eventually consistent systems, similar to but more comprehensive than my example.
Hanover also provides eventually consistent data types.
The Statistic object is an eventually consistent data type, an example of a CRDT: Convergent or Commutative Replicated Data Type. Read the comprehensive research paper on CRDT.
Bryce Kerley speaking about CRDTs at SRCFringe Edinburgh 2012
I put some hours into a gem ripple-statistics which adds this and other simple calculations to the ripple library. Note the limitations of the current code there.

