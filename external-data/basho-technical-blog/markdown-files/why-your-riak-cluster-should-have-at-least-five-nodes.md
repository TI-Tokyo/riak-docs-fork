---
title: "Why Your Riak Cluster Should Have At Least Five Nodes"
description: "Here at Riak we want to make sure that your Riak implementations are set up from the beginning to succeed. While you can use the Riak Fast Track to quickly set up a 3-node dev/test environment, we recommend that all production deployments use a minimum of 5 nodes, ensuring you benefit from the arch"
project: community
lastmod: 2017-02-13T11:53:23+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Shanley Kane"
pub_date: 2012-04-26T00:00:00+00:00
---
April 26, 2012
Here at Riak, we want to make sure that your Riak implementations are set up from the beginning to succeed.  We recommend that all production deployments use a minimum of 5 nodes, ensuring you benefit from the architectural principles that underpin Riak’s availability, fault-tolerance, and scaling properties.
TL;DR: Deployments of five nodes or greater will provide a foundation for the best performance and growth as the cluster expands. Since Riak scales linearly with the addition of more nodes, users find improved performance, reliability, and throughput with larger clusters. Smaller deployments can compromise the fault-tolerance of the system: with a “sane” replication requirement for availability (we default to three copies), node failures in smaller clusters mean that replication requirements may not be met. This can result in degraded performance and risk of data loss. Additionally, clusters smaller than five nodes mean that with a sane replication requirement of 3, a high percentage (75-100% of the nodes) will need to respond to each request, putting undue load on the cluster that may degrade performance.
Let’s take a closer look in the scenario of a three- and four-node cluster.
Performance and Fault Tolerance Concerns in a 3-Node Cluster
To ensure that the cluster is always available to respond to read and write requests, Riak recommends a “sane default” for data replication: three copies of the data on three different nodes. The default configuration of Riak requires four nodes at minimum to insure no single node holds more than one copy of any particular piece of data. (In future versions of Riak we’ll be able to guarantee that each replica is living on a separate physical node. At this point it’s almost at 100%, but we won’t tell you it’s guaranteed until it is.) While it is possible to change the settings to ensure that the three replicas are on distinct nodes in a three node cluster, you still run into issues of replica placement during a node failure or network partition.
In the event of node failure or a network partition in a three-node cluster, the default requirement for replication remains three but there are only two nodes available to service requests. This will result in degraded performance and carries a risk of data loss.
Performance and Fault Tolerance Concerns in a 4-Node Cluster
With a requirement of three replicas, any one request for a particular piece of data from a 4-node cluster will require a response from 75 – 100% of the nodes in the cluster, which may result in degraded performance. In the event of node failure or a network partition in a 4-node cluster, you are back to the issues we outline above.
What if I want to change the replication default?
If using a different data replication number is right for your implementation, just make sure to use a cluster of N +2 nodes where N is the number of replicas for the reasons outlined above.
Going With 5 Nodes
As you add nodes to a Riak cluster that starts with 5 nodes, the percentage of the cluster required to service each request goes down. Riak scales linearly and predictably from this point on. When a node is taken out of service or fails, the number of nodes remaining is large enough to protect you from data loss.
So do your development and testing with smaller clusters, but when it comes to production, start with five nodes.
Happy scaling.
@basho
