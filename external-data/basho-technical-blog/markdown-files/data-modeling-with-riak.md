---
title: "Data Modeling with Riak"
description: "January 22, 2015 In speaking with Riak users, both open source and commercial, we are frequently told that Riak’s key/value model is more flexible and faster to develop against than a traditional relational database. Even though Riak is well suited for many applications, there are inevitable trad"
project: community
lastmod: 2015-05-28T19:23:31+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Tyler Hannan"
pub_date: 2015-01-22T16:39:14+00:00
---
January 22, 2015
In speaking with Riak users, both open source and commercial, we are frequently told that Riak’s key/value model is more flexible and faster to develop against than a traditional relational database. Even though Riak is well suited for many applications, there are inevitable tradeoffs in terms of query options and data types that are available. With a key/value model, there is no concept of columns or rows, therefore Riak does not have join operations. Riak can be queried either directly via HTTP, the protocol buffers API and through various client libraries. However, there is no SQL or SQL-like language that is currently available.
Riak’s key/value data model does not preclude queryability. There are several powerful querying options including:

Riak Search: Integration with Apache Solr provides full-text search and support for Solr’s client query APIs. 
Secondary Indexes: Secondary Indexes (2i) give developers the ability to tag an object stored in Riak with one or more query values. Indexes can be either integers or strings, and can be queried by either exact matches or ranges of values.
MapReduce: Developers can leverage Riak MapReduce for tasks like filtering documents by tag, counting words in documents, and extracting links to related data. 

For more information, check out the Riak documentation on Querying Data.
The table below illustrates key/value mappings for common application types. Remember that values in Riak are opaque and stored on disk as binaries – JSON or XML documents, images, text, etc. The way data is organized in Riak should take into account the unique needs of the application, including access patterns such as read/write distribution, latency differences between various operations, use of Riak features (including MapReduce, Search, Secondary Indexes), and more.



Application Type
Key
Value


Session
User/Session ID
Session Data


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
Login, eMail, UUID
User Attributes


Content
Title, Integer
Text, JSON/XML/HTML Document, Images, etc.




Consider, for example, one of the canonical use cases for Riak…storing user and session data. In a relational database, the “users” table is well known and, basically, provides a unique identifier per user, and then a series of identifying information about that user as individual columns such as:

First name
Last name
Interests
Counter of Site Visits
Paid Account Identifier

This data can then be used to correlate or count, paid users, common interests, etc. via a series of SQL queries against the row/column structure of the users table.
Riak, in contrast, provides flexibility in how this data can be modeled based upon the application use case. It may be desirable to create a Users bucket, with the UserName (or Unique Identifier) as the key and a JSON object storing all user attributes as the value. Or, as we describe in Data Modeling with Riak Data Types, leverage the power of Riak Data Types by creating a map type for each user storing:

first and last name strings in the register type,
interests as a set,
a counter for visits,
and a flag for paid account identifier.

One of the best ways to enable application interaction with objects (a key/value pair) in Riak is to provide structured bucket and key names for the objects. This approach often involves wrapping information about the object in the object’s location data itself.
For example, appending a timestamp, UUID, or Geographical coordinate, to a key’s name allows for fine grained queryability via simple lookup to locate and retrieve a specific set of information. Leveraging the same naming mechanism as created for users (UniqueID as the key) enables, in a separate sessions bucket, storing the UUID append with a timestamp as the key and the session data (in binary format) as the object. In this way, using the same UUID, I am able to obtain both user and session data stored in different buckets and in different formats.
For additional information, and more complex considerations such as modeling relationship and advanced social applications, see the Riak documentation on use cases and data modeling.
Resolving Data Conflicts
In any system that replicates data, conflicts can arise – e.g., if two clients update the same object at the exact same time or if not all updates have yet reached hardware that is experiencing lag. Riak is “eventually consistent” – while data is always available, not all replicas may have the most recent update at the same time, causing brief periods (generally on the order of milliseconds) of inconsistency while all state changes are synchronized.
However, Riak does provide features to detect and help resolve the statistically small number of incidents when data conflicts occur. When a read request is performed, Riak looks up all replicas for that object. By default, Riak will return the most updated version, determined by looking at the object’s vector clock. Vector clocks are metadata attached to each replica when it is created. They are extended each time a replica is updated to keep track of versions. Clients can also be allowed to resolve conflicts themselves.
Further, when an outdated object is discovered as part of a read request, Riak will automatically update the out-of-sync replica to make it consistent. Read Repair, a self-healing property of the database, will even update a replica that returns a “not\_found” in the event that a node loses it due to physical failure.
Riak also features “Active Anti-Entropy,” which is an automatic self-healing property that runs in the background. Rather than waiting for a read request to trigger a replica repair (as with Read Repair), Active Anti-Entropy constantly uses a hash tree exchange to compare replicas of objects and automatically repairs or updates any that are divergent, missing, or corrupt. This can be beneficial for large clusters storing “stale” data.
More information on vector clocks, dotted version vectors, and conflict resolution can be found in the online documentation in the section regarding Causal Context.
Multi-Datacenter Operations
Multi-site replication is quickly becoming critical for many of today’s platforms and applications. Not only does replication across multiple clusters provide geographic data locality – the ability to serve global traffic at low-latencies – it can also be an integral part of a disaster recovery or backup strategy. Other teams may use multi-site replication to maintain secondary data stores, both for failover as well as for performing intensive computation without disrupting production load. Multi-site replication is included in Riak’s commercial extension to Riak, Riak Enterprise, which also includes 24/7 support.
Multi-site replication in Riak works differently than the typical approach seen in the relational world, multi-master replication. In Riak’s multi-datacenter replication, one cluster acts as a “primary cluster.” The primary cluster handles replication request from one or more “secondary clusters” (generally located in datacenters in other regions or countries). If the datacenter with the primary cluster goes down, a secondary cluster can take over as the primary cluster. In this sense, Riak’s multi-datacenter capabilities are “masterless.”
In multi-datacenter replication, there are two primary modes of operation: full sync and real-time. In full sync mode, a complete synchronization occurs between primary and secondary cluster(s). In real-time mode, continual, incremental synchronization occurs – replication is triggered by new updates. Full sync is performed upon initial connection of a secondary cluster, and then periodically (by default, every 6 hours). Full sync is also triggered if the TCP connection between primary and secondary clusters is severed and then recovered.
Data transfer is unidirectional (primary->secondary). However, bidirectional synchronization can be achieved by configuring a pair of connections between clusters.
Full documentation for multi-datacenter replication in Riak Enterprise is available in the online documentation.
In Summary
Modeling data in any non-relational solution requires a different way of thinking about the data itself. Rather than an assumption that all data cleanly fits into a structure of rows and columns, the data domain can be overlayed on the core Key/Value store (Riak) in a variety of ways. There are, however, distinct tradeoffs and benefits to understand.
Relational Databases have:

Tables
Foreign keys and constraints
ACID
Sophisticated query planners
Declarative query language (SQL)

Riak has:

A Key/Value model where the value is any unstructured data
More data redundancy that provides better availability
Eventual consistency
Simplified query capabilities
Riak Search

What you will gain:

More flexible, fluid designs
More natural data representations
Scaling without pain
Reduced operational complexity

For more information on Data Modeling, or to chat with a member of the Riak team on the topic, please request a Tech Talk.
Tyler Hannan
