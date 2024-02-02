---
title: "Relational to Riak - Tradeoffs"
description: "The third in a series of posts on how Riak differs from traditional relational databases."
project: community
lastmod: 2015-05-28T19:23:35+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-11-18T19:32:24+00:00
---
November 18, 2013
This series of blog posts will discuss how Riak differs from traditional relational databases. For more information about any of the points discussed, download our technical overview, “From Relational to Riak.” The previous post in the series discussed High Availability and Cost of Scale.

Eventual Consistency
In order to provide high availability, which is a cornerstone of Riak’s value proposition, the database stores several copies of each key/value pair.
This availability requirement leads to a fundamental tradeoff: in order to continue to serve requests in the presence of failure, we do not force all data in the cluster to stay in sync. Riak will allow writes and reads no matter how many servers (and their stored replicas) are offline or otherwise unreachable.
(Incidentally, this lack of strong coordination has another consequence beyond high availability: Riak is a very, very fast database.)
Riak does provide both active and passive self-healing mechanisms to minimize the window of time during which two servers may have different versions of data.
The concept of eventual consistency may seem unfamiliar, but if you’ve ever implemented a cache or used DNS, those are common examples of the idea. In a large enough system, it’s effectively the default state of all data.
However, with the forthcoming release of Riak 2.0, operators will be able to designate selected pieces of data to require coordination and maintain strong consistency over high availability. Writing such data will be slower and subject to failure if too many servers are unreachable, but the overall robust architecture of Riak will still provide a fast, highly available solution.
Data Modeling
Riak stores data using a simple key/value model, which offers developers tremendous flexibility to define access models that suit their applications. It is also content-agnostic, so developers can store arbitrary data in any convenient format.
Instead of forcing application-specific data structures to be mapped into (and out of) a relational database, they can simply be serialized and dropped directly into Riak. For records that will be frequently updated, if some of the fields are immutable and some aren’t, we recommend keeping the immutable data in one key/value pair and the rest organized into a single or multiple objects based on update patterns.
Relational databases are ingrained habits for many of us, but moving beyond them can be liberating. Further information about data modeling, including sample configurations, are available on Use Cases section of the documentation.
Tradeoffs
One tradeoff with this simpler data model is that there is no SQL or SQL-like language with which to query the data.
To achieve optimal performance, it is advisable to take advantage of the flexibility of the key/value model to define simple retrieval patterns. In other words, determine the most useful queries and write the results of those queries as the data is being processed.
Because it is not always possible to know in advance what questions will need to be asked of your data, Riak offers added functionality on top of the key/value model. Tools such as Riak Search (a distributed, full-text search engine), Secondary Indexing (ability to tag objects with queryable metadata), and MapReduce (leveraged for aggregation tasks) are available to perform ad hoc queries as needed.
For many users, the tradeoffs of moving to Riak are worthwhile due to the overall benefits; however, it can be a bit of an adjustment. To see why others have chosen to switch to Riak from both relational systems and other NoSQL databases, check out our Users Page.
Riak
