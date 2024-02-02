---
title: "Introducing Riak 1.3: Active Anti-Entropy, a New Look for Riak Control, and Faster Multi-Datacenter Replication"
description: "A summary of the major enhancements delivered in Riak 1.3."
project: community
lastmod: 2015-05-28T19:23:42+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-02-21T14:14:43+00:00
---
February 21, 2013
Today we are excited to announce the latest version of Riak. Here is a summary of the major enhancements delivered in Riak 1.3:

Introduced Active Anti-Entropy. Riak now has active anti-entropy. In distributed systems, inconsistencies can arise between replicas due to failure modes, concurrent updates, and physical data loss or corruption. Pre-1.3 Riak already had several features for repairing this “entropy”, but they all required some form of user intervention. Riak 1.3 introduces automatic, self-healing properties that repair entropy on an ongoing basis.
Improved Riak Enterprise’s multi-datacenter replication performance. New advanced mode for multi-datacenter replication capabilities, with better performance, more TCP connections and easier configuration. Read more in this write up from GigaOM.
Improved graphical user experience. Riak Control, the user interface for managing and monitoring Riak, has a brand new look.
Expanded IPv6 support. IPv6 support in Riak now is supported by all interfaces.
Improved MapReduce. Riak MapReduce has improved back-pressure to reduce the risk of overwhelming endpoint processes during large tasks.
Simplified log management. Riak can now optionally send log messages to syslog.

Ready to get started or upgrade? Download the new release here, check out the official release notes, or read on for more details. Documentation for all products and releases is available on the documentation site. For an introduction to Riak and what’s new in Riak 1.3, sign up for our webcast on Thursday, March 7.
More on What’s in Riak 1.3
Active Anti-Entropy
A key feature of Riak is its ability to regenerate lost or corrupted data from replicated data stored on other nodes. Prior to this release, Riak provided two methods to repair data:

Read Repair: Riak compares the replies from all replicas during a read request, repairing any replica that is divergent or missing data. (K/V data only)
Repair Command via Riak Console: Introduced in Riak 1.2, the repair command enables users to trigger a repair of a specific partition. The partition is rebuilt based on a subset of data stored on adjacent nodes in the Riak ring. All data is rebuilt, not just missing or divergent data. (K/V and Search data)

Riak 1.3 introduces active anti-entropy, a continuous background process that compares and repairs any divergent, missing, or corrupted replicas (K/V data only). Unlike read repair, which is only triggered when data is read, the active anti-entropy system ensures the integrity of all data stored in Riak. This is particularly useful in clusters containing “cold data”: data that may not be read for long periods of time, potentially years. Furthermore, unlike the repair command, active anti-entropy is an automatic process, requiring no user intervention and is enabled by default in Riak 1.3.
Riak’s active anti-entropy feature is based on hash tree exchange, which enables differences between replicas to be determined with minimal exchange of information. Specifically, the amount of information exchanged in the process is proportional to the differences between two replicas, not the amount of data that they contain. Approximately the same amount of information is exchanged when there are 10 differing keys out of 1 million keys as when there are 10 differing keys out of 10 billion keys. This enables Riak to provide continuous data protection regardless of cluster size.
Additionally, Riak uses persistent, on-disk hash trees rather than purely in-memory trees, a key difference from similar implementations in other products. This allows Riak to maintain anti-entropy information for billions of keys with minimal additional memory usage, as well as allows Riak nodes to be restarted without losing any anti-entropy information. Furthermore, Riak maintains the hash trees in real time, updating the tree as new write requests come in. This reduces the time it takes Riak to detect and repair missing/divergent replicas. For added protection, Riak periodically (default: once a week) clears and regenerates all hash trees from the on-disk K/V data. This enables Riak to detect silent data corruption to the on-disk data arising from bad disks, faulty hardware components, etc.
New Look for Riak Control
Riak Control is a UI for managing and monitoring your Riak cluster. Riak Control lets you start and re-start Riak nodes, view a “health check” for your cluster, see all nodes and their current status, and have visibility into their partitions and services. Riak Control now has a brand new look and feel. Check out the Riak Control Github page to get up and running.
Expanded IPv6 Support
While Riak’s HTTP interface has always supported IPv6, not all of its interfaces have been as current. In Riak 1.3, the protocol buffers interfaces can now listen on IPv6 or IPv4 addresses. Riak handoff (which is responsible for data transfer when nodes are added or removed, and for handing off update responsibilities when nodes fail) also supports IPv6. It should also be noted that community member Tom Lanyon started the work on this feature. Thanks, Tom!
Improved Backpressure in Riak MapReduce
Riak has Javascript and Erlang MapReduce for performing aggregation and analytics tasks. Backpressure is an important aspect of the MapReduce system, keeping processes from being overwhelmed or memory consumption getting out of control. In Riak 1.3, tunable backpressure is extended to the MapReduce sink to prevent these types of problems at endpoint processes.
Riak Enterprise: Advanced Multi-Datacenter Replication Capabilities
With hundreds of companies using Riak Enterprise, a commercial extension of Riak, we’ve been lucky to work with many teams pushing the limits of multi-datacenter replication performance and resiliency. We’ve learned a lot and are excited to announce these capabilities are now available in advanced mode.

Previously, multi-datacenter replication had one TCP connection over which data was streamed from one cluster to another. This could create a performance bottleneck, especially when run on nodes constrained by per-instance bandwidth limits, such as in a cloud environment. In the new version of multi-datacenter replication, multiple concurrent TCP connections (approximately one per physical node) and processes are used between sites.
Configuration of multi-datacenter replication is easier. Use a shell command to name your clusters, then connect both clusters using a simple ip:port combination.
Better per-connection statistics for both full-sync and real-time modes.
New ability to tweak full-sync workers per node and per cluster, allowing customers to dial-in performance.

The new replication improvements are already used in production by customers and yielding significant performance improvements. For now, the new replication technology is available in advanced mode: it’s optional to turn on. It currently doesn’t have all of the features of the default mode – including SSL, NAT support and full-sync scheduling. Both default and advanced modes are available in the 1.3 release and function independently. In the future, “advanced mode” will become the default.
For more details about multi-datacenter replication, download our whitepaper, “Multi-Datacenter Replication: A Technical Overview.”
Riak
