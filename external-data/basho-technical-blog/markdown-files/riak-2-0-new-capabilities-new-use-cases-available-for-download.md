---
title: "Riak 2.0 - New Capabilities, New Use Cases, Available for Download"
description: "September 16, 2014 We are excited to announce that Riak 2.0 the open source version, and Riak Enterprise 2.0 are now publicly available. Since the Technical Preview, we have been finalizing and testing features for the final release. After months of work, we are thrilled to present the groundbreakin"
project: community
lastmod: 2015-05-28T19:23:33+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Tyler Hannan"
pub_date: 2014-09-16T05:00:54+00:00
---
September 16, 2014
We are excited to announce that Riak 2.0 the open source version, and Riak Enterprise 2.0 are now publicly available. Since the Technical Preview, we have been finalizing and testing features for the final release. After months of work, we are thrilled to present the groundbreaking Riak 2.0. 
Riak 2.0 introduces or enhances several capabilities including:

Search
Security
Consistency
Data Types
Simplified Configuration
Bucket Types
Tiered Storage

Over the next few weeks, we will be exploring these new features in more depth. For now, here is a high-level view of 4 key features.
Riak Search
Riak is a distributed NoSQL system. Without traditional SQL interfaces, we have built in some querying capabilities to make Riak’s key/value design easy for developers. In addition to Secondary Indexes and MapReduce, we also offer full-text search capabilities with Riak Search.
With 2.0, we have added distributed Solr to Riak Search. For every instance of Riak, there is an instance of Solr. While this drastically improves full-text search, it also improves Riak’s overall functionality. Riak Search now allows for Riak to act as a document store (instead of key/value) if needed.
Be on the lookout for an in-depth comparison of Riak Search versus Elastic Search in a future blog, as well as some benchmarks for Riak Search queries..
Additional ResourcesRICON West 2013: Riak Search 2.0Riak Docs: Using Search
Riak Security
Riak 2.0 adds authentication and authorization to Riak, with more security features to come. With the added security, operators can dictate who can interact with Riak and what they’re allowed to do. This added security helps developers separate work in testing environments from production environment.
While you can still apply a network security layer on top of Riak if you wish, there are now security features built into Riak that protect Riak itself – not just its network. Riak Security allows you to both authorize users to perform specific tasks (from standard read/write/delete operations to search queries to managing bucket types) and authenticate users and clients using a variety of security mechanisms. In other words, Riak operators can control who a connecting client is and what that client is allowed to do (if anything).
Additional ResourcesRICON West 2013: Riak Security; Locking the Distributed Chicken CoopRiak Docs: Authentication and Authorization 
Strong Consistency
As many of you are aware, the CAP Theorem is a driving force when designing a database. It states that during periods of partition tolerance, a system can either be consistent or available. Riak is designed to choose availability, which means you will always have read/write access to your data. By maintaining high availability, Riak is also eventually consistent – meaning that the system will always respond to a request but it may not respond with the most updated object.
With Riak 2.0, you can now choose on a per bucket basis between availability and consistency. If you select strong consistency for a bucket in Riak, it ensures that the system won’t respond unless all replicas are updated. This replaces the need for adding extra tools (like Redis or Zookeeper) when serializing data writes.
Strong consistency can be valuable for a variety of use cases, such as tasks involving uniqueness checks or system changes made after a certain point in time. Examples might include locking inventory quantities during the checkout process (to prevent customers from purchasing sold out items) or rejecting bids after an auction has closed.
Please note, strong consistency is currently an open source only feature and is not yet commercially supported in Riak Enterprise.
Additional ResourcesRICON West 2013: Strong Consistency in RiakRiak Docs: Using Strong Consistency
Riak Data Types
Highly available Riak will still remain the default state for buckets, as most use cases don’t require strong consistency. To help developers resolve any conflicts that may arise from the high availability state, we have also added additional Riak Data Types to Riak 2.0.
The first Riak Data Type, counters, was announced with Riak 1.4. Now, we have added sets and maps to the mix. The addition of these familiar, distributed Data Types simplifies application development as Riak now automatically handles sibling resolution. This means developers can spend less time thinking about the complexities of vector clocks and sibling resolution and, instead, let Data Types support their applications’ data access patterns.
Additional ResourcesRICON West 2013: Riak Data TypesRiak Docs: Using Riak Data Types 
In addition to these three concepts, we have worked to simplify configuration, added tiered storage, and bucket types. For a full list of what’s changed, check out Github.
Riak 2.0 makes Riak the most flexible and functional distributed system available. With Riak 2.0, developers can spend less time worrying about their data persistence and more time actually developing.
Tyler Hannan
