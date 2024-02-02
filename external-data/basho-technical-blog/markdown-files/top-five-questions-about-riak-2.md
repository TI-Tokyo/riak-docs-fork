---
title: "Top Five Questions About Riak"
description: "A look at five commonly asked questions about Riak."
project: community
lastmod: 2016-10-20T08:10:11+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-04-17T10:00:06+00:00
---
April 17, 2013
This post looks at five commonly asked questions about Riak. For more questions and answers, check out our Riak FAQ.
What hardware should I use with Riak?
Riak is designed to be run on commodity hardware and is run in production on a variety of different server types on both private and public infrastructure. However, there are several key considerations when choosing the right infrastructure for your Riak deployment.
RAM is one of the most important factors – RAM availability directly affects what Riak backend you should use (see question below), and is also required for complex MapReduce queries. In terms of disk space, Riak automatically replicates data according to a configurable n\_val. A bucket-level property that defaults to 3, n\_val determines how many copies of each object will be stored, and provides the inherent redundancy underlying Riak’s fault-tolerance and high availability. Your hardware choice should take into consideration how many objects you plan to store and the replication factor, however, Riak is designed for horizontal scale and lets you easily add capacity by joining additional nodes to your cluster. Additional factors that might affect choice of hardware include IO capacity, especially for heavy write loads, and intra-cluster bandwidth. For additional factors in capacity planning, check out our documentation on cluster capacity planning.
Riak is explicitly supported on several cloud infrastructure providers. Riak provides free Riak AMIs for use on AWS. We recommend using large, extra large, and cluster compute instance types on Amazon EC2 for optimal performance. Learn more in our documentation on performance tuning for AWS. Engine Yard provides hosted Riak solutions, and we also offer virtual machine images for the Microsoft VM Depot.
What backend is best for my application?
Riak offers several different storage backends to support use cases with different operational profiles. Bitcask and LevelDB are the most commonly used backends.
Bitcask was developed in-house at Riak to offer extremely fast read/write performance and high throughput. Bitcask is the default storage engine for Riak and ships with it. Bitcask uses an in-memory hash-table of all keys you write to Riak, which points directly to the on-disk location of the value. The direct lookup from memory means Bitcask never uses more than one disk seek to read data. Writes are also very fast with Bitcask’s write-once, append-only design. Bitcask also offers benefits like easier backups and fast crash recovery. The inherent limitation is that your system must have enough memory to contain your entire keyspace, with room for a few other operational components. However, unless you have an extremely large number of keys, Bitcask fits many datasets. Visit our documentation for more details on Bitcask, and use the Bitcask Capacity Calculator to assist you with sizing your cluster.
LevelDB is an open-source, on-disk key-value store from Google. Riak maintains a version of LevelDB tuned specifically for Riak. LevelDB doesn’t have Bitcask’s memory constraints around keyspace size, and thus is ideal for deployments with a very large number of keys. In addition to this advantage, LevelDB uses Google Snappy data compression, which provides particular efficiency for text data like raw text, Base64, JSON, HTML, etc. To use LevelDB with Riak, you must the change the storage backend variable in the app.config file. You can find more details on LevelDB here in https://docs.riak.com.
Riak also offers a Memory storage backend that does not persist data and is used simply for testing or small amounts of transient state. You can also run multiple backends within a single Riak instance, which is useful if you want to use different backends for different Riak buckets or use a different storage configuration for some buckets. For in-depth information on Riak’s storage backends, see our documentation on choosing a backend.
How do I model data using Riak’s key/value design?
Riak uses a key/value design to store data. Key/value pairs comprise objects, which are stored in buckets. Buckets are flat namespaces with some configurable properties, such as the replication factor. One frequent question we get is how to build applications using the key/value scheme. The unique needs of your application should be taken into account when structuring it, but here are some common approaches to typical use cases. Note that Riak is content-agnostic, so values can be any content type.



Data Type
Key
Value




Session
User/Session ID
Session Data


Content
Title, Integer
Document, Image, Post, Video, Text, JSON/HTML, etc.


Advertising
Campaign ID
Ad Content


Logs
Date
Log File


Sensor
Date, Date/Time
Sensor Updates


User Data
Login, Email, UUID
User Attributes



For more comprehensive information on building applications with Riak’s key/value design, view the use cases section of our documentation.
What other options, besides strict key/value access, are there for querying Riak?
Most operations done with Riak will be reading and writing key/value pairs to Riak. However, Riak exposes several other features for searching and accessing data: MapReduce, full-text search, and secondary indexing.
MapReduce provides non-primary key based querying that divides work across the Riak distributed database. It is useful for tasks such as filtering by tags, counting words, extracting links, analyzing log files, and aggregation tasks. Riak provides both Javascript and Erlang MapReduce support. Jobs written in Erlang are generally more performant. You can find more details about Riak MapReduce here.
Riak also provides Riak Search, a full-text search engine that indexes documents on write and provides an easy, robust query language and SOLR-like API. Riak Search is ideal for indexing content like posts, user bios, articles, and other documents, as well as indexing JSON data. For more information, see the documentation on Riak Search.
Secondary indexing allows you to tag objects in Riak with one or more queryable values. These “tags” can then be queried by exact or range value for integers and strings. Secondary indexing is great for simple tagging and searching Riak objects for additional attributes. Check out more details here.
How does Riak differ from other databases?
We often get asked how Riak is different from other databases and other technologies. While an in-depth analysis is outside the scope of this post, the below should point you in the right direction.
Riak is often used by applications and companies with a primary background in relational databases, such as MySQL. Most people who move from a relational database to Riak cite a few reasons. For one, Riak’s masterless, fault-tolerant, read/write available design make it a better fit for data that must be highly available and resilient to failure scenarios. Second, Riak’s operational profile and use of consistent hashing means data is automatically redistributed as you add machines, avoiding hot spots in the database and manual resharding efforts. Riak is also chosen over relational databases for the multi-datacenter capabilities provided in Riak Enterprise. A more detailed look at the difference between Riak and traditional databases and how to make the switch can be found in this whitepaper, From Relational to Riak.
A more detailed look at the technical differences between Riak and other NoSQL databases can be found in the comparisons section of our documentation, which covers databases such as MongoDB, Couchbase, Neo4j, Cassandra, and others.
Ready to get started? You can download Riak here. For more in-depth information about Riak, we also offer Riak Workshops in New York and San Francisco. Learn more here.
Riak
