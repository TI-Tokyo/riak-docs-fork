---
title: "Why MapReduce Is Hard"
description: "As soon as you throw it at a distributed system, MapReduce suddenly becomes hard. Let's find out why."
project: community
lastmod: 2016-10-20T07:43:10+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mathias Meyer"
pub_date: 2011-04-20T00:00:00+00:00
---
April 20, 2011
At the highest level, MapReduce can be a fundamentally hard concept to grasp. In our last blog post on MapReduce, you learned how easy it is. After all, it’s just code, right?
The story gets a bit more complicated as soon as you run MapReduce in a distributed environment like Riak. Some of these caveats are just things to be aware of, others are simply in the nature of both Riak’s implementation and the ideas in MapReduce itself. Adding to that, it’s just a hard concept to grasp. I’ll be the first to admit it took me a long time to understand.
MapReduce is hard because it’s just code
Wait, didn’t I just tell you that MapReduce is easy because it’s just code? I believe I did, Bob.
Code requires knowledge of what it does. Code requires the discipline to keep it clean and easy to grasp. Code requires knowledge of the data structures involved. Code requires knowledge of how Riak treats your MapReduce functions depending on their phase in a particular MapReduce request.
Take Riak’s map functions. Depending on whether you’re intending to chain multiple functions in one request, they must either return a bucket/key pair or the data for either the client or the following reduce function.
One way to solve this would be to add an extra argument, based on which the map function can decide whether it returns a bucket/key pair or the data. Why would you do this? Consider a scenario where you want to extract a set of objects based on a match in one attribute and then run a second map matching another attribute. Here’s an example:
javascript
var extractByMatchingAttribute = function(value, keydata, args) {
var doc = Riak.mapKeyValuesJson(value)[0];
if (doc.host.match(/riak.com/)) {
if (args.last) {
return [1];
} else {
return [[value.bucket, value.key]];
}
}
}
Now you can pass in an argument object (e.g. {last: true}) to every phase in a MapReduce request. This is a valid solution, but makes the map function painfully more aware of the environment in which it’s running.
“But hey, ” you wonder, “couldn’t I just make these into a single map function instead and return the value immediately?” You could, but depending on other MapReduce scenarios you could end up repeating a lot of code across a lot of functions. Once again, you could extract it, ignore it, or try to abstract, making map functions chainable through extra arguments, which then can be passed to every single map phase independently.
I told you, MapReduce is hard because it’s just code, didn’t I?
MapReduce is hard because it doesn’t (yet) have a common language
Wouldn’t it be nice if we could just use a simple language to express what we want our Riak cluster to do instead? Something like the following, where we have a bucket full of log entries, every one of them represented by a JSON object, which handily includes a host attribute:
sql
LOAD log\_entries FROM KEY '2011-04-08' TO KEY '2011-05-10' INTO entries
FILTER entries BY host MATCHING /riak.com/ AND
GROUP entries BY host
There, three lines of easily readable text in a purely hypothetical example, although slightly inspired by Apache Pig. “Oh hey,” you’re saying, “doesn’t that look a bit like SQL?” Maybe it does, maybe it doesn’t, but that’s not the point. More important than that, it’s easier to read.
Will we see something along these lines in Riak? No comment.
MapReduce is hard because accessing lots of data is expensive
MapReduce is the technique du jour to query and filter data in Riak. It’s intended to be used as a tool to transform and analyze a set of data. You hand in a bunch of bucket/key pairs, and Riak feeds them into your map and reduce functions. Simple enough.
It has been our experience, however, that what users want is to feed the data from a whole bucket into a MapReduce request or a whole range of them. Thankfully you can do the latter using the rather nice key filters.
Still, fetching all the keys from a bucket requires a lot of resources. Key filters can certainly help to reduce it, because they fold the list of keys before the data is loaded from disk, which is certainly faster than loading everything right away. But it still takes a lot of time when there’s millions of keys stored in a Riak cluster. Not to mention the fact that every node in the cluster is involved in both fetching all objects from a bucket and running key filters.
If this is what you’re trying to achieve, you should have a look at Riak
Search. It does a much better job at these reverse lookup queries (which some call secondary indexes), and you can neatly feed the results into a MapReduce request, something we’ve implemented in Riaktant, our little syslog collector and aggregator. But we’ll leave dissecting that code to another blog post.
We have some more tricks up our sleeve which should be appearing in the near future, so stay tuned. I’m sure it’ll blow your mind just like it blew mine.
MapReduce is hard because going distributed requires coordination
Whenever you fire off a MapReduce request in a Riak cluster, the node you’re requesting becomes the coordinator of this particular request. This poor node suddenly has the responsibility of sending your map request to all the relevant nodes in the cluster, using the the preference list where applicable.
The coordinating node suddenly has the job of sending out the map requests and waiting for them to return, triggering the timeout if it doesn’t. If the map phases return bucket/key pairs and there’s another map phase to run, it starts over, sending new requests to the nodes responsible for the pairs.
Map phases in a Riak cluster always run close to the data to reduce overhead both on network traffic and the general load in the cluster, as it greatly reduces the amount of data sent across the wire, and lets the nodes responsible for the data get some of that MapReduce action as well.
That’s a lot of work for the coordinating node, but except for the reduce part, it’s not necessarily heavy computationally. It does, however, have to track state of all the phases sent out to the cluster, collect the results and control behaviour in case of failure of just a single node in the cluster.
What can you do about this? Other than keep your initial data set for a MapReduce set as small as possible, the ball is pretty much in our court. It’s Riak’s task to make sure a MapReduce request is handled and coordinated properly.
MapReduce is hard because reduce runs recursively
Enter the brain bender called re-reduce. If you haven’t run into it, you’re in for a treat. A reduce function returns a list of aggregation results based on a list of inputs. Depending on how large the initial result set from the function is going to be, Riak’s MapReduce will feed it the first round of results again, a technique commonly called re-reduce.
In general your reduce functions should be oblivious to the kind of data they’re fed, they should work properly in both the normal and the re-reduce case. Two options here:

Make your reduce functions self-aware.
They know what kind of data they get from a previous map phase, and what kind of data they return. But that means adding some sort of type checking to your reduce functions, which is cumbersome and, especially with JavaScript, not exactly beautiful and actually achievable. So let’s assume I never told you about this option.
Make sure they always return data in the format they receive it.
This is the way you’ll usually end up going. It’s just a lot easier to spend a little bit more time thinking how to build a result that you can re-run the same function and the result will either be unchanged or just aggregated again in the same fashion as before.

Here’s an example: a function that groups by an attribute. It assumes a list containing JavaScript objects with attributes as keys and numbers as values, e.g. {attribute: 1}. It simply adds up the values for every object, so that a list containing three pairs of the aforementioned example turns into {attribute: 3}. This makes it nicely
ignorant of where the data comes from and re-reduce will simply be fed the same data structures as the initial reduce.
The second parameter to the function is an argument you can pass to the reduce phase, telling it which particular attribute you’re interested in, making the function nicely reuseable along the way.
javascript
function(values, field) {
var result = {};
for (value in values) {
for (field in values[value]) {
if (field in result) {
result[field] += values[value][field];
} else {
result[field] = values[value][field];
}
}
}
return [result];
}
Re-reduce is a weird concept to grasp, but when you just think of it as two different ways Riak will pass data to your reduce function, it’s actually not that hard. Just make sure a reduce function returns data in a way it can run on again, and you’re fine.
MapReduce is hard because debugging errors is hard
Finding out where things go wrong in a distributed MapReduce environment like Riak is hard. The only useful way of inspecting data you have is by using the ejsLog() function in JavaScript, and that means you’ll have to go look on every node in the cluster to find out what’s wrong or if the data is in the format you expect it to be. The following dumps the list of objects for a map or reduce function as JSON into the log file /tmp/mapreduce.log.
javascript
ejsLog('/tmp/mapreduce', JSON.stringify(values));
The small fix: test your MapReduce functions on a cluster with a single node before cranking it up a notch, or have a look at Sean Cribbs’ riak-qc.js framework for testing Riak MapReduce functions.
The MapReduce bottom line
MapReduce is hard, but we’re not going shopping just yet. Instead, we’re keeping a close eye on what our users are trying to achieve, improving Riak along the way. For more details on Riak’s MapReduce, check out the wiki, read a blog post on more practical MapReduce, or watch last year’s webinar on MapReduce. If you’re feeling adventurous, have a look at the examples and MapReduce code used in Riaktant. I’ll be sure to get back to you with a proper introduction on how it uses MapReduce to sift through syslog data.
— Mathias
