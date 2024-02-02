---
title: "What in the HeLL is HyperLogLog?"
description: "The first time someone hears the term, HyperLogLog, a common response is to wonder what in the HeLL they are talking about. Personally, it sounds like something from a sci-fi movie; but it isn’t. I am going to introduce you to what HyperLogLog is, why you might want to use it, and how to use it with"
project: community
lastmod: 2017-01-17T12:30:24+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Christopher Mancini"
pub_date: 2017-01-04T14:28:04+00:00
---
The first time someone hears the term, HyperLogLog, a common response is to wonder what in the HeLL they are talking about. Personally, it sounds like something from a sci-fi movie; but it isn’t. I am going to introduce you to what HyperLogLog is, why you might want to use it, and how to use it with Riak KV.
With a basic understanding of what HyperLogLog is, we can better appreciate how it can be valuable to any business that has large sets of data. In the simplest of terms, HyperLogLog is an algorithm that makes it easy to estimate the number of unique values within a very large set, which is also known as cardinality. For instance, if I define a set of 8 numbers, {4,3,6,2,2,6,1,7}, the cardinality of the membership set would be 6.
HyperLogLog shines with sets of data that contain very large numbers of values, for instance, reporting the number of searches on Google performed by end users within a day. Trying to pull all of the searches into memory to work with them would be impossible because the memory required would be proportional to the number of Google searches in a day. HyperLogLog converts the data into a hash of random numbers representing the cardinality of the data supplied, solving this problem with as little as 1.5kB of memory.
It is not within the scope of this post to detail how HyperLogLog accomplishes its tasks, however, there is plenty of content available on the subject including the following:

Wikipedia HyperLogLog article
Hyperloglog Datatypes in Riak by Zeeshan Lakhani

One of the most important things to take away is that HyperLogLog is most effective when you are creating a structure for getting the answers to questions the business will need before you begin collecting the data. This is due to how HyperLogLog converts the data into a new form, making it impossible to retrieve the original values from the set. For large data sets, it is impractical to repopulate the data using the HyperLogLog algorithm every time you want to check the cardinality of the data. This forces us to use HyperLogLog with data that is streamable, like our Google search queries example above. Google could create a new HyperLogLog set for every day of the year, then add an entry to the HyperLogLog hash for every query received on their server as it happens. This makes it possible to easily view the number of unique search queries for each day as well as the progress of the current day.
HyperLogLog’s calculation of the cardinality is not perfect and operates with a margin of error of 2%. This means that given any set, there is a chance that the HyperLogLog will produce a cardinality that is slightly inaccurate and the significance of that margin of error can be higher for smaller sets.
With the release of Riak KV 2.2, we introduced HyperLogLog support as a new data type. The new HyperLogLog data type is easy to use, allowing you to add new values like you would with a Riak Set and retrieve the cardinality value like you would fetch the value from a Riak Counter.
Let’s say we have an online retail store and we want to determine distinct users who complete a purchase by State. We could create a HyperLogLog data type for each state and add the customer’s unique identifier every time an order is completed.
curl -XPOST http://localhost:8098/types/hlls/buckets/orders\_by\_state/datatypes/south\_carolina \
-H "Content-Type: application/json" \
-d '{"add\_all":["001","007"]}'

And here is an example of fetching the cardinality of that data:
curl http://localhost:8098/types/hlls/buckets/orders\_by\_state/datatypes/south\_carolina

# Response
{"type":"hll","value":"2"}

Some time has passed and we have added more orders to South Carolina:
curl -XPOST http://localhost:8098/types/hlls/buckets/orders\_by\_state/datatypes/south\_carolina \
-H "Content-Type: application/json" \
-d '{"add\_all":["007","032","056"]}'

curl http://localhost:8098/types/hlls/buckets/orders\_by\_state/datatypes/south\_carolina

# Response
{"type":"hll","value":"4"}

More time has passed and we have added more orders:
curl -XPOST http://localhost:8098/types/hlls/buckets/orders\_by\_state/datatypes/south\_carolina \
-H "Content-Type: application/json" \
-d '{"add\_all":["001","047","088"]}'

curl http://localhost:8098/types/hlls/buckets/orders\_by\_state/datatypes/south\_carolina

# Response
{"type":"hll","value":"6"}

As we add more and more values to the orders\_by\_state bucket, we may start to see inaccuracies in the values returned for cardinality. This is perfectly normal as the HyperLogLog algorithm is designed to be an estimate, hence the 2% margin of error I mentioned above. This means that if your application had stored 100 unique values in the south\_carolina key, the HyperLogLog algorithm could return values within the range of 98 to 102.
Please note, these examples are using sets that are incredibly small and generally wouldn’t be well suited for the HyperLogLog data type, however, they illustrate the power of the new type and how it can be used within Riak.
You can get more information about the Riak KV 2.2 release on the Riak blog at: /posts/technical/riak-kv-2-2-release-highlights/
Christopher
Software Engineer – Client Libraries, Riak Technologies
@chrismancini
mancini.io

References:

http://blog.kiip.me/engineering/sketching-scaling-everyday-hyperloglog/
http://stackoverflow.com/questions/12327004/how-does-the-hyperloglog-algorithm-work
https://github.com/basho/riak\_kv/blob/develop/docs/hll/hll.pdf
https://en.wikipedia.org/wiki/HyperLogLog
http://highscalability.com/blog/2012/4/5/big-data-counting-how-to-count-a-billion-distinct-objects-us.html
https://pkghosh.wordpress.com/2014/11/16/counting-unique-mobile-app-users-with-hyperloglog/

