---
title: "Relational to Riak - Cost of Scale"
description: "The second in a series of posts on how Riak differs from traditional relational databases."
project: community
lastmod: 2015-05-28T19:23:35+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-11-14T11:00:07+00:00
---
November 14, 2013
This series of blog posts will discuss how Riak differs from traditional relational databases. For more information about any of the points discussed, download our technical overview, “From Relational to Riak.” The previous post in the series was Relational to Riak – High Availability.

Riak is designed for scalability, which truly separates it from relational systems. As described in the previous post, relational databases run best on a single server. If the dataset grows beyond the capacity of this single machine, it can become prohibitively expensive (or even impossible) to simply upgrade to a bigger machine. In such a scenario, the only option may be to add more machines and divide the dataset across them using a technique called sharding.
Sharding divides data into logical parts (such as alphabetical, by customer, or by geographic region) that can be distributed across multiple machines – often manually. If data continues to grow, this process may need to be repeated at great expense.
Sharding is not only difficult, it also will typically lead to hot spots – meaning certain machines are responsible for storing and serving a disproportionately high amount of both data and requests. Hot spots can cause unpredictable latency and degraded performance.
(And remember all the ways in which availability is a challenge? Combine sharding with a master/slave architecture for maximal expense and general unpleasantness.)
Instead of sharding, Riak evenly distributes data across a cluster using consistent hashing. In a Riak cluster, the data space is divided into partitions which are claimed by the servers. When new data is written to the database, these objects are evenly placed around the ring and replicated 3 times (by default). This ensures that your data will always be available, even when nodes fail.
When nodes are added or removed, data is rebalanced automatically. New machines assume ownership of some of the partitions and existing machines hand off relevant partitions and associated data until data ownership is equal amongst nodes.
By eliminating the manual requirements of sharding and making hot spots highly unlikely, Riak makes it significantly easier for companies to scale, whether it’s just for a few months to handle peak loads or to support long-term growth strategies.
Riak
