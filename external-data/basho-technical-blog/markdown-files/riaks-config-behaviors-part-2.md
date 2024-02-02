---
title: "Understanding Riak's Configurable Behaviors: Part 2"
description: "May 8, 2013 This is part 2 of our 4-part series illustrating how Riak’s configuration options impact its core behaviors. Part 1 covered background material and conflict resolution. In this post I’ll talk about reads and writes. N, R, W, and hangers-on All documentation lies by omission: ther"
project: community
lastmod: 2016-10-17T07:42:00+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "John Daily"
pub_date: 2013-05-08T18:42:58+00:00
---
May 8, 2013
This is part 2 of our 4-part series illustrating how Riak’s configuration options impact its core behaviors.
Part 1 covered background material and conflict resolution. In this post I’ll talk about reads and writes.
N, R, W, and hangers-on
All documentation lies by omission: there’s always something left out, deliberately or no.
These blog posts actively lie to you! There is no configuration parameter named N or R or W or any of the other 1- or 2-character capitalized parameters you’ll see below.
Instead, the real parameters are n\_val, r, w, etc.
This is purely an aesthetic choice: I find the text easier to scan when the values stand out as capital letters. (And n\_val just drives me batty.)
Replication
For each of the parameters I’ll describe in this section, the maximum value is N, the number of times which each piece of data is replicated through the cluster. N is specified globally, but can be redefined for each bucket.
The default value for N in Riak is 3, so I’ll assume N=3 for all of the examples below.
Symbolic names
It is possible to use names instead of integers for the configuration parameters:

all – All vnodes currently responsible for the replicated data must reply
Equivalent to setting the value to match N
one – Only 1 vnode need reply
quorum – A majority (half plus one) of the replicas must respond
default – Use the bucket (or global) default value
In the absence of configuration changes, this will be the same as quorum

Readin’ and Writin’ (R and W)
After N, the two most commonly-discussed parameters in Riak overviews are R and W.
These are, simply enough, the number of vnodes which must respond to a read (R) or write (W) request before the request is considered successful and a response is sent to the requesting client.
The request will be sent to all N vnodes that are known to be currently responsible for the data, and the coordinating node which is handling the request on behalf of the client will wait for all N vnodes to reply, but the client will be informed of a result after R or W responses.
These choices have implications that may not be immediately obvious:

Data accuracy is enhanced if the coordinating node waits for all N responses before replying – the last vnode to reply may have more recent data.
Client responsiveness is degraded (possibly dramatically so in a situation where a node is failing) if the coordinating node waits for all N responses.
The client will receive a failure message if we ask for R=N (or W=N) responses but one of the vnodes replies with an error.

The above facts lead to an unfortunate conclusion: at this point in time, there is no way to ask Riak for the best possible value in a single read request.
Primaries (PR and PW)
In the Riak model, there are N vnodes with primary responsibility for any given key; these are termed the primary vnodes.
However, because Riak is optimized for availability, the database will use failover vnodes to comply with the R and W parameters as necessitated by network or system failure.
By using the PR (primary read) or PW (primary write) configuration parameters with values greater than zero, Riak will only report success to the client if at least that number of primary vnodes reply with a successful outcome.
The downside is that requests are more likely to fail because the primary vnodes are unavailable.
As we’ll discuss in the next post, a failed PW write request can (and typically will) still succeed (eventually); what matters to Riak when responding to the client is that it cannot assure the client of the write’s success unless the primary vnodes respond affirmatively.
RYOW
As you can probably tell by the caveats and corner cases littering this document, Riak is cautious about making guarantees on data consistency.
However, as of Riak 1.3.1, it is safe to make one assertion: in the absence of other clients attempting to perform a write against the same key, if one client successfully executes a write request and then successfully executes a read request, and if for those requests PW + PR > N, then the value retrieved will be the same as the value just written.
This is termed Read Your Own Writes consistency (RYOW).
Even this guarantee has a loophole. If all of the primary vnodes fail at the same time, the new values will no longer be available to clients; if they fail in such a way that the data has not yet been durably written to disk, the values could be lost forever.
Riak is not immune to truly catastrophic scenarios.
Durable writes (DW)
At the cost of adding latency to write requests, the DW value can be set to a value between 1 and N to require that the key and value be written to the storage backend on that number of nodes before the request is acknowledged.
How quickly and robustly the data is written to disk is determined by the backend configuration (for more details, see the docs for Bitcask or LevelDB).
W and DW, oh my
By default, DW is set to quorum, and will be treated as 1 internally even if configured to be 0.
In versions of Riak prior to 1.3.1, if W is set to a value less than DW, the DW value would be implicitly demoted to W. This means that setting W=1 would result in DW=1 as well, which is a reasonable performance optimization and likely what a user would expect.
However, if the request indicated DW=3 while W=2 (the default), this optimization is much less desirable and not at all expected behavior.
In order to make the overall behavior much more explicit with v1.3.1, the effective DW value is no longer demoted to match W, so any requests using W=1 to shorten the request time should also explicitly set DW=1, or the performance will suffer significantly.
Delete quorum (RW)
Deleting a key requires the successful read of its value and successful write of a tombstone (we’ll talk a lot about tombstones in our next blog post), complying with all R, W, PR, and PW parameters along the way.
If somehow both R and W are undefined (which is probably not possible in recent releases of Riak), the RW parameter will substitute for read and write values during object deletes. If you want to test someone on obscure Riak facts, ask about RW.
(It should be no surprise to learn this parameter should already have been, and soon will be, deprecated.)
Illustrative scenarios
To recap much of what we’ve covered to this point, I’ll walk through some typical reads and writes and the handling of possible inconsistencies.
For these scenarios, all 3 vnodes responsible for the key are available. The same behavior should be exhibited if a vnode is down,
but the response may be delayed as the cluster detects the failure and sends the request to another vnode.
Assume N=3, R=2, and W=2 for each of these.
Reading
A normal read request (to retrieve the value associated with a key) is sent to all 3 primary vnodes.



Scenario
What happens




All 3 vnodes agree on the value
Once first 2 vnodes return the value, it is returned to the client


2 of 3 vnodes agree on the value, and those 2 are the first to reach the coordinating node
The value is returned returned to the client. Read repair will deal with the conflict per the later scenarios, which means that a future read may return a different value or siblings


2 conflicting values reach the coordinating node and vector clocks allow for resolution
The vector clocks are used to resolve the conflict and return a single value, which is propagated via read repair to the relevant vnodes


2 conflicting values reach the coordinating node, vector clocks indicate a fork in the object history, and allow\_mult is false
The object with the most recent timestamp is returned and propagated via read repair to the relevant vnodes


2 siblings or conflicting values reach the coordinating node, vector clocks indicate a fork in the object history, and allow\_mult is true
All keys are returned as siblings, optionally with associated values



Writing
Now, a write request.



Scenario
What happens




A vector clock is included with the write request, and is newer than the vclock attached to the existing object
The new value is written and success is indicated as soon as 2 vnodes acknowledge the write


A vector clock is included with the write request, but conflicts with the vclock attached to the existing object, and allow\_mult is true
The new value is created as a sibling for future reads


A vector clock is included with the write request, but conflicts with (or is older than) the vclock attached to the existing object, and allow\_mult is false
The new value overwrites the old


A vector clock is not included with the write request, an object already exists, and allow\_mult is true
The new value is created as a sibling for future reads


A vector clock is not included with the write request, an object already exists, and allow\_mult is false
The new value overwrites the existing value



What’s next?
In our next post, I’ll discuss scenarios where Riak’s eventual consistency can cause unexpected behavior. I’ll also tackle object deletions, a surprisingly complex topic.
John R. Daily
