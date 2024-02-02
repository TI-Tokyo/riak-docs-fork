---
title: "Introducing Riak 2.0: Data Types, Strong Consistency, Full-Text Search, and Much More"
description: "The Technical Preview of Riak 2.0 is now available for download."
project: community
lastmod: 2016-09-06T06:48:00+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-10-29T15:57:11+00:00
---
October 29, 2013
Today at RICON West in San Francisco, we announced the Technical Preview of Riak 2.0 is now available. This major release adds a number of new features that many of you have been waiting for.
Throughout RICON West, we will be discussing many of the Riak 2.0 features (both in track sessions or during lightning talks), so keep your eyes on the live stream over the next two days. Videos of all sessions will also be made available after the conference.
Here is a look at some of the major enhancements available in Riak 2.0:

Riak Data Types. Building on the eventually consistent counters introduced in Riak 1.4, Riak 2.0 adds sets and maps as new distributed data types. These Riak Data Types simplify application development without sacrificing Riak’s availability and partition tolerance characteristics.
Strong Consistency. Developers have the flexibility to choose whether buckets should be eventually consistent (the default Riak configuration today that provides high availability) or strongly consistent, based on data requirements.
Full-Text Search Integration with Apache Solr. Riak Search is completely redesigned in Riak 2.0, leveraging the Apache Solr engine. Riak Search in 2.0 supports the Solr client query APIs, enabling integration with a wide range of existing software and commercial solutions.
Security. Riak 2.0 adds the ability to administer access rights and utilize plug-in authentication models. Authentication and Authorization is provided via client APIs.
Simplified Configuration Management. Riak 2.0 continues to improve Riak’s operational simplicity by changing how, and where, configuration information is stored in an easy-to-parse and transparent format.
Reduced Replicas for Multiple Data Centers. Riak Enterprise 2.0 can optionally store fewer copies of replicated data across multiple data centers to better maintain a balance between storage overhead and availability.

Ready to get started? Download the Technical Preview.
Please note that this is only a Technical Preview of Riak 2.0. This means that it has been tested extensively, as we do with all of our release candidates, but there is still work to be completed to ensure it’s production hardened. Between now and the final release, we will be continuing manual and automated testing, creating detailed use cases, gathering performance statistics, and updating the documentation for both usage and deployment.
As we are finalizing Riak 2.0, we welcome your feedback for our Technical Preview. We are always available to discuss via the Riak Users mailing list, IRC (#riak on freenode), or contact us.
Riak 2.0 Technical Preview: Deep Dive
Riak Data Types
In distributed systems, we are forced to trade consistency for availability (see: CAP Theorem) and this can complicate some aspects of application design. In Riak 2.0, we have integrated cutting-edge research on data types known as called CRDTs (Conflict-Free Replicated Data Types) pioneered by INRIA to create Riak Data Types. By adding counters, sets, maps, registers, and flags, these Riak Data Types enable developers to spend less time thinking about the complexities of vector clocks and sibling resolution and, instead, focusing on using familiar, distributed data types to support their applications’ data access patterns.
A more detailed overview of Riak Data Types is available that examines implementation considerations and the basics of usage.
Strong Consistency
In all prior versions, Riak was classified as an eventually consistent system. With the 2.0 release, Riak now lets developers choose when operations should be strongly or eventually consistent. This gives developers a choice between these semantics for different types of data. At the same time, operators can continue to enjoy the operational simplicity of Riak. Consistency preferences are defined on a per bucket type basis, in the same cluster.
A RICON West 2012 talk entitled, Bringing Consistency to Riak, shares much of the initial thinking behind this effort. In addition, the pull request that adds consistency to riak\_kv provides detailed information about related repositories and the implementation approach.
Redesigned Full-Text Search
Riak is a key/value store and the values are simply stored on disk as binary. With previous versions of Riak Search, Riak developers have long been able to index the content of these stored values. In Riak 2.0, Riak Search (code-named Yokozuna) has been completely redesigned and now uses the Apache Solr full-text document indexing engine directly. Together, Riak and Solr provide a reliable full-text context indexing solution that is highly available and built for scale. In addition, Riak Search 2.0 also fully supports the Solr client query APIs, which enables integration with existing software solutions (either homegrown or commercial).
The Riak engineers responsible for Yokozuna have created a resources page that includes recorded talks, Solr documentation links, and books on the topic.
Security
Riak designed Riak with critical data in mind. Whether it’s data that affects revenue, user experience, or even a patient’s health (as is the case with the NHS), Riak ensures that this critical data is always available. However, often this critical data is also sensitive data. Riak 2.0 adds security to this data through the ability to administer access rights and plug-in various secure authentication models commonly used today.
The initial RFC that describes the security effort, including related Pull Requests, is available at github.com/basho/riak/issues/355.
Simplified Configuration Management
At Riak, we pride ourselves on providing operationally friendly software that functions smoothly when dealing with the challenges of a distributed system. In the past, configuration of Riak occurred in two files: app.config and vm.args. Riak 2.0 changes how and where configuration information is stored. It no longer uses Erlang-specific syntax but, rather, provides a layout more suited for all operators and automated deployment tools. This layout is easy to parse and transparent for Riak administrators.
More information on the vision and specific implementation considerations are contained in the repository at github.com/basho/cuttlefish.
Bucket Types
In versions of Riak prior to 2.0, keys were made up of two parts: the bucket they belong to and a unique identifier within that bucket. Buckets act as a namespace and allow for similar keys to be grouped. In addition, they provide a means of configuring how previous versions of Riak treated that data.
In Riak 2.0, several new features (security and strong consistency in particular) need to interact with groups of buckets. To this end, Riak 2.0 includes the concept of a Bucket Type. In addition to allowing new features without special prefixes in Bucket names, Riak developers and operators are able to define a group of buckets that share the same properties and only store information about each Bucket Type, rather than individual buckets.
More information about Bucket Types can be found in the Github Issue at github.com/basho/riak/issues/362. This issue describes the planned functionality, discussions about implementation, and includes related pull requests.
Change in Defaults for Sibling Resolution
Riak has always supported both application-side and timestamp and vector clock-based Last Write Wins server-side resolution. Prior to Riak 2.0, vector clock-based Last Write Wins has been the default. Moving forward, new clusters will hand off siblings to applications by default. This is the safest way to work with Riak, but requires developers to be aware of sibling resolution.
In a blog series entitled, Understanding Riak’s Configurable Behaviours, Riak Evangelist John Daily discusses the configuration of Last Write Wins, and many other options, in great detail.
More Efficient Use of Physical Memory
Riak nodes are designed to manage the changing demands of a cluster as it experiences network, hardware, and other failures. To do this, Riak balances each node’s resources accordingly. Riak 2.0 has vastly improved LevelDB’s use of available physical memory (RAM) by allowing local databases to dynamically change their cache sizes as the cluster fluctuates under load.
In the past, it was necessary to specify RAM allocation for different LevelDB caches independently. This is no longer the case. In Riak 2.0, LevelDB databases that manage key/value or active anti-entropy data share a single pool of memory, and administrators are free to allocate as much of the available RAM to LevelDB as they feel is appropriate in their deployment. Detailed implementation documentation can be found in the basho/leveldb wiki.
Riak Ruby Vagrant Project
If you are interested in testing Riak 2.0, in a contained environment with the Riak Ruby Client, Riak engineer Bryce Kerley has put together the Riak-Ruby-Vagrant repository. In addition, this environment can be easily adapted to usage with other clients for testing the new features of Riak 2.0.
Riak
