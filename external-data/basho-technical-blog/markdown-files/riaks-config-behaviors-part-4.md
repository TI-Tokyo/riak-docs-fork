---
title: "Understanding Riak's Configurable Behaviors: Part 4"
description: "May 10, 2013 This is the last part of a 4-part series on configuring Riak’s oft-subtle behavioral characteristics  	Part 1 	Part 2 	Part 3 For my final post, I’ll tackle two very different objectives and show how to choose configuration values to support them. Fast and sloppy Let’s sa"
project: community
lastmod: 2016-10-17T07:39:17+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "John Daily"
pub_date: 2013-05-10T18:34:22+00:00
---
May 10, 2013
This is the last part of a 4-part series on configuring Riak’s oft-subtle behavioral characteristics

Part 1
Part 2
Part 3

For my final post, I’ll tackle two very different objectives and show how to choose configuration values to support them.
Fast and sloppy
Let’s say you’re reading and writing statistics that don’t need to be perfectly accurate. For example, if a website has been “liked” 300 times, it’s not the end of the world if that site reports 298 or 299 likes, but it absolutely is a problem if the page loads slowly because it takes too long to read that value.
Reasonably obvious

R=1 and W=1

We want to maximize performance by responding to the client as soon as we have read a value or confirmed a write.

DW=1

Before Riak 1.3.1, this would have been implied by W=1, but that is no longer true. In any event, better to be explicit: we only need one vnode to send the data to its backend before the client receives a response.

last\_write\_wins=true

We don’t need to spend time pulling and updating vector clocks; just write the latest value as quickly as possible.
Not so obvious

notfound\_ok=false
basic\_quorum=true

One problem with R=1 is that notfound\_ok=true by default. This means that if the first vnode to respond doesn’t have a copy, the failure to find a value will be treated as authoritative and thus the client will receive a notfound error.
If a webpage has been liked 5000 times, but one of the primary nodes has fallen over and a failover node is in place without a complete data set, you don’t want to report 0 likes.
Setting notfound\_ok=false instructs the coordinating node to wait for something other than a notfound error before reporting a value.
However, waiting for all 3 vnodes to report that there is no such data point may be overkill (and too much of a hit on performance), so you can use basic\_quorum=true to short circuit the process and report back a notfound status after only 2 replies.
Playing with fire

N=2

This is not for the faint of heart, but if you can accept the fact that that you will occasionally lose access to data if two nodes are unavailable, this will reduce the cluster traffic and thus potentially improve overall system performance. This is definitely not a recommended configuration.
Strict, slow, and failure-prone
At the other end of the spectrum, if you want to favor consistency at the expense of availability, you can certainly do so.
Obvious

PR=2
PW=2

Read Your Own Writes (RYOW), as we discussed in an earlier post. Be prepared for more frequent failures, however, since the cluster will not be at liberty to distribute reads and writes as widely as possible.
Reasonably obvious

allow\_mult=true

If a conflict somehow does occur, give all of the values to the application to resolve.
Not so obvious

delete\_mode=keep

By retaining the vector clocks for deleted objects, we enhance the overall data integrity of the database over time and sidestep problems with resurrected deleted objects as we saw in the previous blog post.
This comes at a price: more disk space will be used to retain the old objects, and there will be many more tombstones for clients to recognize and ignore. (You have written your code to cope with tombstones and even tombstone siblings, haven’t you?)
Concluding thoughts
I hope this series of blog posts has helped answer some of the mysteries, and will help you avoid some stumbling blocks, involved with running Riak.
If you occasionally wonder why relational databases have been around for so many decades and still have a hard time scaling horizontally, I suggest you think back to the tradeoffs and hard questions posed herein and consider this: all of this is to keep a simple key/value store running fast and answering questions as accurately as possible given the constraints imposed by inevitable hardware and network failure.
Imagine how much harder this is for a relational database with its dramatically more complex storage and retrieval model.
Distributed systems are not your father’s von Neumann machine.
Testing Riak
If you’d like to simulate production load or experiment with various failure modes, here are tools which may be of assistance.
Riak Bench
Riak Bench is a benchmarking tool that can generate repeatable performance tests.

GitHub repository
Cookbook

Jepsen
Kyle Kingsbury has recently released jepsen to simulate network partitions in distributed databases, in preparation for his RICON East presentation.
Quick summary of configuration parameters
The effective default value is listed for each value. Most of these are also covered in our online documentation.
As mentioned in part 2, the capitalized parameters I’ve been using are for aesthetic purposes, and the real values (e.g., n\_val in place of N, dw in place of DW) are shown below.
Conflict resolution

vnode\_vclocks=true
allow\_mult=false
last\_write\_wins=false

Reading and writing

n\_val=3
All of the remaining values under this heading can be no larger than n\_val
r=2
w=2
pr=0
pw=0
dw=2

Deleting keys

delete\_mode=3000
Value is in milliseconds, hence 3 seconds by default
Alternative values are keep or immediate
rw=2
Obsolete

Missing keys

notfound\_ok=true
basic\_quorum=false

Related reading

Brewer’s CAP theorem (Wikipedia)
Brewer’s article on the CAP theorem, 12 years later
Riak’s CRDT project
Original CRDT research paper (PDF)
Eventual consistency (Riak docs)
Vector clocks (Riak docs)
Dynamo paper with annotations for Riak’s architecture
Overview of Riak’s concepts
Riak Glossary
Failure scenarios in Riak, from Ryan Zezeski’s Try-Try-Try project

John R. Daily
