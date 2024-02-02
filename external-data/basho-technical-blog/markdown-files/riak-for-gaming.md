---
title: "Riak for Gaming"
description: "Learn why gaming platforms are switching to Riak."
project: community
lastmod: 2017-04-28T16:00:56+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2014-04-16T13:00:34+00:00
---
April 16, 2014
The world of gaming can be unpredictable. It can be hard to judge if a game is going to be the next Angry Birds and experience exponential, global growth. Riak is designed to help gaming platforms handle this uncertainty with ease. Its focus on high availability means that all data remains accessibility, even during node failure. Its flexible data model and redundant, fault-tolerant design easily allows gaming platforms to store any type of data needed. Riak is also built for operational simplicity at scale, so Riak will seamlessly grow with data and popularity. Finally, the option for multi-datacenter replication means that gamers all over the world will get the same low-latency experience across multiple devices.
Top Use Cases for Riak in Gaming

Player Data: Riak provides low-latency, highly available data storage for key player data, including user and profile information, game performance, statistics and rankings, and more. Riak also provides many different tools for querying and indexing this data, such as Riak Search, Secondary Indexing, and MapReduce.
Session Storage: Riak is frequently used to store and serve session data with predictable low-latency – necessary for game play. Riak imposes no restrictions on the type of content stored (since all objects are stored on disk as binaries), so session data can be encoded in many ways and can evolve without administrative changes to schemas.
Social Information: Riak provides flexible, robust storage for social data such as social graph information, player profiles and relationships, and social authentication tokens.
Global Data Locality: When gaming, players require a low-latency experience, regardless of where they’re physically located. Otherwise, interrupted or slow game play can lead to poor user experience and possible user abandonment. Riak Enterprise’s multi-datacenter capabilities allow game data to be physically close to players and serve them data no matter where they happen to be.

Riak in Production
Riak is already in production by many top gaming platforms. Here’s a look at a few that have switched to Riak.
Rovio
Rovio is the creator of the popular mobile game, Angry Birds. Since user growth can be hard to predict, they needed an infrastructure that could support unexpected viral growth without failing or causing downtime. They selected Riak due to its ease-of-scale and fault tolerance. Riak now powers their new cartoon series, Angry Birds Toons, and new mobile games. Learn more about why they moved to Riak in this case study and video from GDC.
Hibernum
Hibernum is a creator and developer of unique gaming experiences that combine the latest in social gaming, top quality visuals and animations, and cutting edge design. They switched from a relational database to Riak due to the high availability, ability to scale to peak loads, and predictable operational cost. Riak is used to store user game information for one of their most popular social games. Check out the complete case study, Hibernum Selects Riak for User Data Storage.
Kiip
Kiip is a platform for building rewards and achievements into your games. Kiip replaced MongoDB with Riak in order to achieve low read/write latencies and horizontal scalability. Kiip uses Riak for storing and serving session and device data. Learn more from the video on scaling Riak to 25MM Ops/Day.
Riot Games
Riot Games is the creator of League of Legends and faced some challenges with supporting millions of concurrent players at any given moment. They switched to Riak from MySQL for their next generation stats system, which tracks gameplay statistics and stores terabytes of data that gets aggregated and presented to players in near real-time. More information on how they use Riak and why they selected it can be found here.
Data Modeling in Riak
Riak has a “schemaless” design. Objects are comprised of key/value pairs, which are stored in flat namespaces called buckets. Here are some common approaches to structuring gaming data with Riak’s key/value design:



Data Type
Key
Value


Player Data
Login, Email, UUID
Player Attributes (often stored as a JSON document); Player Rewards and Stats


Social Data
Login, Email, UUID
Player Profiles, Social Graph Information, Facebook/Twitter Tokens


Session Information
User/Session ID
Session Data


Image or Video Content
Content Name, ID or Integer
.JPG .PNG, .GIF or other image format; .MOV, .MPG, .MP4 or other video file format




To get started with Riak, Contact Us or download it now.
Riak
