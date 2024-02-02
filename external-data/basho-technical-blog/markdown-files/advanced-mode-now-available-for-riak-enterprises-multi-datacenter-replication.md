---
title: "Advanced Mode Now Available for Riak Enterprise’s Multi-Datacenter Replication"
description: "An in-depth look at Riak Enterprise’s new multi-datacenter replication capabilities, now available with the 1.3 release."
project: community
lastmod: 2015-05-28T19:23:42+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-02-25T09:00:10+00:00
---
February 25, 2013 
This post takes an in-depth look at Riak Enterprise’s new multi-datacenter replication capabilities, available in the recent 1.3 release. Riak Enterprise’s multi-datacenter replication now ships with “advanced mode,” which features some performance and resiliency improvements that we’ve developed by working with production customers:

Instead of having only one TCP connection over which data is streamed from one cluster to another, this new version features multiple concurrent TCP connections (approximately one per physical node) and processes are used between sites. This prevents against possible performance bottlenecks, which can be especially common when run on nodes constrained by per-instance bandwidth limits (such as in a cloud environment).
Easier configuration of multi-datacenter replication. Simply use a shell command to name your clusters, then connect both clusters using an ip:port combination.
Better per-connection statistics for both full-sync and real-time modes.
New ability to tweak full-sync workers per node and per cluster, which allows customers to dial-in performance.

Details of the advanced mode capabilities are below. For more about the multi-datacenter replication upgrades and our 1.3 release, check out this recent article from GigaOM. For full technical details, check out our documentation on multi-datacenter replication. For an examination of common architectures and use cases for Riak Enterprise, including datacenter failover, active-active cluster configurations, availability zones, and cloud bursting, download our technical overview.
The new advanced mode of Riak Enterprise’s multi-datacenter replication takes the best features from the past single channel communications model and scales it up to one-to-one connections between peer nodes of clusters. With concurrent channels and the ability to constrain the maximum connections per node and per cluster, the new multi-datacenter replication allows the full capacity of the network and cluster size to scale the performance to available resources.
Simple Configuration
It begins with a much easier configuration command language and environment, with natural objects as sources, sinks, and cluster names. For example, real-time and full-sync enable/disable, start/stop, and status all use these human friendly symbols. All of the connections go through a single port, reducing network administration to a single firewall port forwarding. Riak then manages the different protocols on this port. Connections are persistent, resilient to outages, and adapt to changes in cluster names and IP addresses automatically.
Two Sync Modes
Real-time synchronization between clusters uses a new queueing mechanism that ensures maximum performance and graceful shutdown of nodes. This guarantees that there is no loss of replication data during upgrades or scheduled maintenance. It also automatically balances the load across all nodes of both the source and sink clusters and requires no configuration.
Full-synchronization between clusters uses a scheduling algorithm to maximize the transfer rate of data between peer nodes of the two clusters. Partitions are synchronized in parallel so that the maximum number of keys can be updated concurrently with the minimum overlap, which minimizes load and contention on both the source and sink clusters. Full-sync supports concurrent syncs between multiple clusters and optimizes the load dynamically, ensuring nodes never exceed their configured connectivity. This allows clusters to synchronize at maximum efficiency, without impacting their runtime performance for connected clients as they make PUT and GET requests.
We are also planning to include Secure Sockets Layer and Network Address Translation support in this advanced mode of multi-datacenter replication – it is currently only available in default mode. Additionally, future improvements will take advantage of the Active Anti-Entropy (that was introduced in Riak 1.3) to make full-sync differencing even faster. Stay tuned for more updates!
To learn more about Riak 1.3 and the new advanced mode for multi-datacenter replication, sign up for our webcast on Thursday, March 7th.
Riak
