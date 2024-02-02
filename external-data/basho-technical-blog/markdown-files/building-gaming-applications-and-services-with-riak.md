---
title: "Building Gaming Applications and Services with Riak"
description: "A look at some common gaming use cases and how to start building them in Riak."
project: community
lastmod: 2016-08-29T15:00:58+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-03-13T10:00:46+00:00
---
March 13, 2013
For a complete overview, download the whitepaper, “Gaming on Riak: A Technical Introduction.” To see how other gaming companies are using Riak, visit us at the Game Developers Conference at Booth #202!
As discussed in our previous post, “Gaming on Riak: A Brief Overview and User Case Studies,” Riak can provide a number of advantages for gaming platforms. Content agnostic, an HTTP API, many client libraries, and a simple key/value data model, Riak is a flexible data store that can be used for a variety of different use cases in the gaming industry. This post looks at some common examples and how to start building them in Riak.
Use Cases
Player and Session Data: Riak can serve and store key player and session data with predictable low latency, and ensures it is available even in the event of node failure and network partition. This data may include user and profile information, game performance, statistics and rankings, and more. In Riak, all objects are stored on disk as binaries, providing flexible storage for many content types. Since Riak is schema-less, applications can evolve without changing an underlying schema, providing agility with growth and change.
Social Information: Riak can be used for social content such as social graph information, player profiles and relationships, social authentication accounts, and other types of social gaming data.
Content: Riak is often used to store text, documents, images, videos and other assets that power gaming experiences. This data often needs to be highly available and able to scale quickly to attract and keep users.
Global Data Locality: Gaming requires a low-latency experience, no matter where the players are located. Riak Enterprise’s multi-datacenter replication feature means data can be served to global users quickly.
Data Model
Below are some common approaches to structuring gaming data with Riak’s key/value model:

Riak offers robust additional functionality on top of the fundamental key/value model. For more information on these options as well as how to implement them, their architecture, and their limitations, check out the documentation on searching and accessing data in Riak.
Riak Search
Riak Search is a distributed, full-text search engine. It provides support for various MIME types & analyzers, and robust querying including exact matches, wildcards, range queries, proximity searches, and more.
Possible Use Cases: Searching player and game information.
Secondary Indexing
Secondary Indexing (2i) gives developers the ability, at write time, to tag an object stored in Riak with one or more queryable values. Indexes can be either integers or strings and can be queried by either exact matches or ranges of an index.
Possible Use Cases: Tagging player information with user relationships, general attributes, or other metadata.
MapReduce
Developers can leverage MapReduce for analytic and aggregation tasks. It offers support for both JavaScript and Erlang MapReduce.
Possible Use Cases: Filtering game data by tag, counting items, and extracting links to related data.
To learn more about how your gaming platform can benefit from Riak, download “Gaming on Riak: A Technical Introduction.” For more information about Riak, sign up for our webcast on Thursday, March 14.
Riak
