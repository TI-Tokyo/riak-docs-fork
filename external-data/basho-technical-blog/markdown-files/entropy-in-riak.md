---
title: "Entropy in Riak"
description: "Learn about the many ways Riak deals with data entropy as a distributed system."
project: community
lastmod: 2015-05-28T19:23:33+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "John Daily"
pub_date: 2014-03-26T13:00:19+00:00
---
March 26, 2014
Riak’s overarching design goal is simple: be maximally available. If your data center is on fire, Riak will be the last part of your stack to fail (and hopefully, you’ve purchased an enterprise license, so there’s another cluster in another data center ready to go at all times).
In order to make sure your data can survive server failures, Riak retains multiple copies (replicas) and allows lock-free, uncoordinated updates.
This then open ups the possibility that data will be out of sync across a cluster. Riak manages this issue in three distinct stages: entropy detection, correction, and conflict resolution.
Entropy Detection
Among the oldest and simplest tools in Riak is Read Repair, which, as its name implies, is triggered when a read request is received. If the server coordinating the operation notices that the servers responsible for the key do not agree on its value, correction is required.
A more recent feature in Riak is Active Anti-Entropy (often shortened to AAE). Effectively, this is the proactive version of read repair and runs in the background. Riak maintains hash trees to monitor for inconsistent data between servers; when divergent values are detected, correction is mandated.
Correction
As discussed in the blog post, Clocks Are Bad, Or, Welcome to the Wonderful World of Distributed Systems, automatically determining the “correct” value in the event of a conflict is not simple, and often not possible at the database layer.
Using contextual metadata called vector clocks, Riak will attempt to determine whether one of the discovered values is derived from the other. In that case, it can safely choose the most recent value. This value is written to all servers that have a copy of the data and conflict resolution is not required.
If Riak can’t verify such a causal relationship, things get more difficult.
Riak’s default behavior, is to fall back to server clocks to determine a winner. This can lead to unexpected results if concurrent updates are received but, on the positive side, conflict resolution will not be required.
If Riak is instead configured with allow\_mult=true to protect data integrity, even when independent writes are received, Riak will write both values to the servers as siblings for later conflict resolution.
Conflict Resolution
Conflict resolution means that Riak detects a conflict, can’t resolve it intelligently, and isn’t instructed to resolve it otherwise.
Next time the application attempts to read such a value, instead of receiving one answer, it’s going to receive (at least) two. It is now the application’s responsibility to deal with the conflict and provide a new value back to Riak for future reads.
This can be trivial (pick one value), obvious (merge all values), or tricky (apply business logic and come back with something different).
With Riak 2.0, Riak is introducing Riak Data Types, which are designed to handle conflict resolution automatically. If your data can be formulated as a set or a map (not terribly dissimilar from JSON), Riak can process and resolve the siblings for you when a read request is received.
Why?
Many databases, particularly distributed ones, are effectively non-deterministic in the presence of concurrent writes. If they try to enforce consistency on writes, they sacrifice availability and often data integrity. If they don’t enforce consistency, they may rely on server (or worse, client) clocks to pick a winner, if they even have a strategy at all.
Riak is unique in encouraging developers to think about conflict resolution. Why? Because, for large distributed systems, network and server failures are a fact of life. For very large distributed systems, data duplication and inconsistency is unavoidable. Since Riak is designed for scale, it’s better to provide a structure for proper resolution than to pretend conflicts don’t exist.
John Daily
