---
title: "Secondary Indexes in Riak"
description: "Rusty Klophaus introduces Riak 1.0's new Secondary Indexes feature."
project: community
lastmod: 2016-10-20T08:48:49+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Rusty Klophaus"
pub_date: 2011-09-14T00:00:00+00:00
---
September 14, 2011
Developers building an application on Riak typically have a love/hate relationship with Riak’s simple key/value-based approach to storing data. It’s great that anyone can grok the basics (3 simple operations, get/put/delete) quickly. It’s convenient that you can store anything imaginable as an object’s value: an integer, a blob of JSON data, an image, an MP3. And the distributed, scalable, failure-tolerant properties that a key/value storage model enables can be a lifesaver depending on your use case.
But things get much less rosy when faced with the challenge of representing alternate keys, one-to-many relationships, or many-to-many relationships in Riak. Historically, Riak has shifted these responsibilities to the application developer. The developer is forced to either find a way to fit their data into a key/value model, or to adopt a polyglot storage strategy, maintaining data in one system and relationships in another.
This adds complexity and technical risk, as the developer is burdened with writing additional bookkeeping code and/or learning and maintaining multiple systems.
That’s why we’re so happy about Secondary Indexes. Secondary Indexes are the first step toward solving these challenges, lifting the burden from the backs of developers, and enabling more complex data modeling in Riak. And the best part is that it ships in our 1.0 release, just a few weeks from now.
How Do Secondary Indexes Work?
Update: Secondary Indexes use the new style HTTP API. See the Riak Wiki for more details.
From an application developer’s perspective, Secondary Indexes allow you to tag a Riak object with some index metadata, and later retrieve the object by querying the index, rather than the object’s primary key.
For example, let’s say you want to store a user object, accessible by username, twitter handle, or email address. You might pick the username as the primary key, while indexing the twitter handle and email address. Below is a curl command to accomplish this through the HTTP interface of a local Riak node:
bash
curl -X POST
-H 'x-riak-index-twitter\_bin: rustyio'
-H 'x-riak-index-email\_bin: rusty@riak.com'
-d '...user data...'
http://localhost:8098/buckets/users/keys/rustyk
Previously, there was no simple way to access an object by anything other than the primary key, the username. The developer would be forced to “roll their own indexes.” With Secondary Indexes enabled, however, you can easily retrieve the data by querying the user’s twitter handle:
Query the twitter handle…
curl localhost:8098/buckets/users/index/twitter\_bin/rustyio
Response…
{“keys”:[“rustyk”]}
Or the user’s email address:
Query the email address…
curl localhost:8098/buckets/users/index/email\_bin/rusty@riak.com
Response…
{“keys”:[“rustyk”]}
You can change an object’s indexes by simply writing the object again with the updated index information. For example, to add an index on Github handle:
bash
curl -X POST
-H 'x-riak-index-twitter\_bin: rustyio'
-H 'x-riak-index-email\_bin: rusty@riak.com'
-H 'x-riak-index-github\_bin: rustyio'
-d '...user data...'
http://localhost:8098/buckets/users/keys/rustyk
That’s all there is to it, but that’s enough to represent a variety of different relationships within Riak.
Above is an example of assigning an alternate key to an object. But imagine that instead of a twitter\_bin field, our object had an employer\_bin field that matched the primary key for an object in our employers bucket. We can now look up users by their employer.
Or imagine a role\_bin field that matched the primary key for an object in our security\_roles bucket. This allows us to look up all users that are assigned to a specific security role in the system.
Design Decisions
Secondary Indexes maintains Riak’s distributed, scalable, and failure tolerant nature by avoiding the need for a pre-defined schema, which would be shared state. Indexes are declared on a per-object basis, and the index type (binary or integer) is determined by the field’s suffix.
Indexing is real-time and atomic; the results show up in queries immediately after the write operation completes, and all indexing occurs on the partition where the object lives, so the object and its indexes stay in sync. Indexes can be stored and queried via the HTTP interface or the Protocol Buffers interface. Additionally, index results can feed directly into a Map/Reduce operation. And our Enterprise customers will be happy to know that Secondary Indexing plays well with multi data center replication.
Indexes are declared as metadata, rather than an object’s value, in order to preserve Riak’s view that the value of your object is as an opaque document. An object can have an unlimited number of index fields of any size (dependent upon system resources, of course.) We have stress tested with 1,000 index fields, though we expect most applications won’t need nearly that many. Indexes do contribute to the base size of the object, and they also take up their own disk space, but the overhead for each additional index entry is minimal: the vector clock information (and other metadata) is stored in the object, not in the index entry. Additionally, the LevelDB backend (and, likely, most index-capable backends) support prefix-compression, further shrinking ndex size.
This initial release does have some important limitations. Only single index queries are supported, and only for exact matches or range queries. The result order is undefined, and pagination is not supported. While this offers less in the way of ad-hoc querying than other datastores, it is a solid 80% solution that allows us to focus future energy where users and customers need it most. (Trust me, we have many plans and prototypes of potential features. Building something is easy, building the right thing is harder.)
Behind The Scenes
What is happening behind the scenes? A lot, actually.
At write time, the system pulls the index fields from the incoming object, parses and validates the fields, updates the object with the newly parsed fields, and then continues with the write operation. The replicas of the object are sent to virtual nodes where the object and its indexes are persisted to disk.
At query time, the system first calculates what we call a “covering” set of partitions. The system looks at how many replicas of our data are stored and determines the minimum number of partitions that it must examine to retrieve a full set of results, accounting for any offline nodes. By default, Riak is configured to store 3 replicas of all objects, so the system can generate a full result set if it reads from one-third of the system’s partitions, as long as it chooses the right set of partitions. The query is then broadcast to the selected partitions, which read the index data, generate a list of keys, and send them back to the coordinating node.
Storing index data is very different from storing key/value data: in general, any database that stores indexes on a disk would prefer to be able to store the index in a contiguous block and in the desired
order–basically getting as near to the final result set as possible. This minimizes disk movement and other work during a query, and provides faster read operations. The challenge is that index values rarely enter the system in the right order, so the database must do some shuffling at write time. Most databases delay this shuffling, they write to disk in a slightly sub-optimal format, then go back and “fix things up” at a later point in time.
None of Riak’s existing key/value-oriented backends were a good fit for index data; they all focused on fast key/value access. During the development of Secondary Indexes we explored other options. Coincidentally, the Riak team had already begun work to adapt LevelDB–a low-level storage library from Google–as a storage engine for Riak KV. LevelDB stores data in a defined order, exactly what Secondary Indexes needed, and it is actually versatile enough to manage both the index data AND the object’s value. Plus, it is very RAM friendly. You can learn more about LevelDB from this page on Google Code.
Want To Know More?
If you want to learn more about Secondary Indexes, you can read the slides from my talk at OSCON Data 2011: Querying Riak Just Got Easier. Alternatively, you can watch the video.
You can grab a pre-release version of Riak Version 1.0 on the Riak downloads site to try the examples above. Remember to change the storage backend to riak\_kv\_eleveldb\_backend!
Finally keep an eye out for documentation that will land on the newly re-organized Riak Wiki within the next two weeks.
— Rusty
