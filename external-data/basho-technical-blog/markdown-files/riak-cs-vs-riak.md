---
title: "Riak CS vs. Riak"
description: "Learn about the differences between Riak CS and Riak."
project: community
lastmod: 2016-10-20T07:21:29+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "John Daily"
pub_date: 2013-10-02T09:00:29+00:00
---
October 2, 2013
What Is Riak CS?
Riak CS is Riak’s open source large object (aka cloud storage) software, built on the rock-solid Riak database. It is API-compatible with Amazon’s S3 and OpenStack’s Swift object storage solutions.
In May of this year, we posted the top 5 questions we heard from customers and our community about Riak CS; today we’ll take a deeper dive into the technical details, specifically the differences between Riak CS and Riak itself.
Riak CS as Compared to Riak
Both Riak CS and Riak are, at their core, places to store objects. Both are open source and both are designed to be used in a cluster of servers for availability and scalability.
The fundamental distinction between the two is simple: Riak CS can be used for storing very large objects, into the terabyte size range, while Riak is optimized for fast storage and retrieval of small objects (typically no more than a few megabytes).
There are subtle differences; however, that can be obscured by the similarities between the two.
Why Would I Use Riak CS?
Riak CS is used for a variety of reasons. Some examples:

Private object storage services, for example for companies that want to store sensitive data behind their own firewalls.
Large binary object storage as part of a voice or video service.
An integrated component in an OpenStack cloud solution, storing and serving VM images on demand.

Tier 3, Yahoo! Japan, Datapipe, and Turner Broadcasting are just a few of the big names using Riak CS today.
What Does Riak CS Do That Riak Doesn’t?
Chunking
Riak CS carves large objects into small chunks of data to be distributed throughout a Riak cluster and, when used with Riak CS Enterprise, synchronized with remote data centers.
S3/OpenStack APIs
Without Riak CS, developers have the choice of using Riak’s native HTTP or Protocol Buffers APIs when developing solutions.
Riak CS adds compatibility with Amazon’s S3 and OpenStack’s Swift APIs. These offer very different semantics than Riak, and the advanced search capabilities in Riak such as Secondary Indexes and full text search are not available using S3 or Swift clients.
We strongly advise against it, but it is possible to work with Riak’s standard APIs “under the hood” when deploying a Riak CS solution.
Multi-Tenancy
The latest release of Riak offers no way to differentiate between clients. Riak CS, on the other hand, supports both authentication and authorization.
Work is actively underway to add a security model to Riak in the upcoming 2.0 release.
Buckets or Buckets?
Users of Riak CS store their objects in virtual containers (called buckets in Amazon S3 parlance, containers in OpenStack).
Riak also relies heavily on buckets for data storage and configuration but, despite the names, these buckets are not the same.
As an example of how this can cause confusion: the replication factor in Riak (the number of times a piece of data is stored in a cluster) is configurable per-bucket. Because Riak’s buckets do not underly the user buckets in Riak CS, this feature cannot be used to create tiered services.
Strong Consistency
Riak is designed to maximize availability; the price paid for that is delayed consistency when the network is split and clients are writing to both sides of the cluster.
Creating user accounts in Riak CS; however, led to the need for a mechanism to maintain strong consistency. If two people attempt to create user accounts with the same username on either side of a network partition, both cannot be allowed to succeed, or else a conflict will occur that is very difficult to automatically recover from.
Furthermore, user buckets in S3 (and OpenStack APIs as implemented in Riak CS) reside in a global rather than a user-specific namespace, so bucket creation must also be handled carefully.
Riak CS introduced a service named Stanchion that is designed to handle these specific requests to avoid conflicts. Stanchion is a single process running on a single Riak server (thus introducing a single point of failure for user account and bucket creation requests).
While it is possible to deploy Stanchion using common system tools to make a daemon process run in a highly available manner, Riak recommends doing so carefully and testing it thoroughly. Since the only impact of failure is to prevent user and bucket creation, it may be preferable to monitor and alert on failure. If two copies of Stanchion are running due to a network partition, its strong consistency guarantees will be lost.
With strong consistency options targeted for Riak 2.0, expect to see some changes.
Other Differences
Replication
Riak offers multi-datacenter replication with its Enterprise software licenses, and Riak CS Enterprise takes full advantage of that feature. Data can be written to one or more clusters in multiple data centers and be synchronized automatically between them.
There are two types of synchronization: real-time, which occurs as objects are written, and full sync, which happens on a periodic basis to compare the full contents of each cluster for any changes to be merged.
One key difference is that Riak CS maintains manifest files to track the chunks it creates, and it is these manifests that are distributed between clusters during real-time sync. The individual chunks are not synchronized until a full sync replication occurs, or until someone requests the file from a remote cluster. The manifest is made active for someone to retrieve the chunks after the original upload to the source cluster is complete.
Backends
A common mistake while installing Riak CS is to configure it using information specific to Riak rather than Riak CS. As an example, per the Riak CS installation instructions the relevant backend data store must be configured to riak\_cs\_kv\_multi\_backend, which is forked from Riak’s riak\_kv\_multi\_backend. Using the latter will cause problems.
Riak (CS) Control
Riak Control is a web management console for Riak clusters; Riak CS Control is a web management console for Riak CS user accounts. Both are optional and both are useful in a Riak CS cluster.
Exposure to Internet
Exposing any database directly to the Internet is risky. Riak, currently lacking any concept of authentication, absolutely must not be accessible to untrusted networks.
Riak CS; however, is designed with Internet access in mind. It is still advisable to place a load balancer or proxy in front of a Riak CS cluster, for example to ease cluster maintenance/upgrades and to provide a central location to log and block potentially hostile access.
Riak CS servers will still have open Riak ports that must be protected from the Internet as you would any Riak servers.
Where to Next for Riak CS?
2013 has been a big year for Riak CS: it was released as open source in the spring, with OpenStack support added this summer. Still, there is much to do.
As mentioned above, improving or replacing Stanchion is a high priority.
We will continue to expand the API coverage for Riak CS. The next major targets are the copy object operations that Amazon S3 and OpenStack Swift offer.
Compression and more granular replication controls are also under consideration for future releases.
By building Riak CS atop the most robust open source distributed database in the world, we’ve created a very operationally friendly, powerful storage solution that can evolve to meet present and future needs. Feel free to give it a try if you aren’t already using it.
If you’re interested in hearing from the engineers who’ve made this software possible (and seeing just how far a highly available data storage solution can take you), join us October 29-30th for RICON West. RICON West is where Riak brings together industry and academia to discuss the rapidly expanding world of distributed systems, including Riak and Riak CS.
John Daily
