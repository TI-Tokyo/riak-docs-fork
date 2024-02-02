---
title: "Relational to Riak, Part 1- High Availability"
description: "A look at Riak's architectural design for high availability, and the benefits of Riak over relational databases."
project: community
lastmod: 2015-05-28T19:23:43+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-01-10T00:00:00+00:00
---
January 10, 2013
This is the first in a series of blog posts that discusses a high-level overview of the benefits and tradeoffs of Riak versus traditional relational databases. If this is relevant to your projects or applications, register for our “From Relational to Riak” webcast on January 24.
One of the biggest differences between Riak and relational systems is the focus on availability and how the underlying architecture deals with failure modes.
Most relational databases leverage a master/slave architecture to replicate data. This approach usually means the master coordinates all write operations, working with the slave nodes to update data. If the master node fails, the database will reject write operations until the failure is resolved – often involving failover or leader election – to maintain correctness. This can result in a window of write unavailability.

Conversely, Riak uses a masterless system with no single point of failure, meaning any node can serve read or write requests. If a node experiences an outage, other nodes can continue to accept read and write requests. Additionally, if a node fails or becomes unavailable to the rest of the cluster due to a network partition, a neighboring node will take over responsibilities for the unavailable node. Once this node becomes available again, the neighboring node will pass over any updates through a process called “hinted handoff.” This is another way that Riak maintains availability and resilience even despite serious failure.

Because Riak’s system allows for reads and writes, even when multiple nodes are unavailable, and uses an eventually consistent design to maintain availability, in rare cases different replicas may contain different versions of an object. This can occur if multiple clients update the same piece of data at the exact same time or if nodes are down or laggy. These conflicts happen a statistically small portion of the time, but are important to know about. Riak has a number of mechanisms for detecting and resolving these conflicts when they occur. For more on how Riak achieves availability and the tradeoffs involved, see our documentation on the subject.
For many use cases today, high availability and fault tolerance are critical to the user experience and the company’s revenue. Unavailability has a negative impact on your revenue, damages user trust and leads to a poor user experience. For use cases such as online retail, shopping carts, advertising, social and mobile platforms or anything with critical data needs, high availability is key and Riak may be the right choice.
Sign up for the webcast here or read our whitepaper on moving from relational to Riak.
Riak
