---
title: "Understanding Riak's Configurable Behaviors: Part 1"
description: "May 7, 2013 This is part 1 of a 4-part series on subtleties and tradeoffs to consider when configuring write and read parameters for a Riak cluster. The full implications of the configuration options discussed here are rarely obvious and often are revealed only under a production load, hence t"
project: community
lastmod: 2016-10-20T07:57:37+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "John Daily"
pub_date: 2013-05-07T18:49:44+00:00
---
May 7, 2013
This is part 1 of a 4-part series on subtleties and tradeoffs to consider when configuring write and read parameters for a Riak cluster.
The full implications of the configuration options discussed here are rarely obvious and often are revealed only under a production load, hence this series.
More generally, I hope these documents serve to help illuminate some of the complexities involved when creating distributed systems. Data consistency on a single computer is (usually) straightforward; it is a different story altogether when 5, 10, or 100 servers share that responsibility.
Background material
What you should know first
This series is intended as a reasonably deep dive into the behavioral characteristics of Riak, and thus assumes that the reader has at least a passing familiarity with these key Riak concepts:

Virtual nodes (vnodes)
Vector clocks (vclocks)
Read repair
Active anti-entropy

If you’re not comfortable with those topics, the Riak Fast Track is highly recommended reading, and if you encounter vocabulary or concepts that are particularly challenging, the following links should be helpful:

https://docs.riak.com/riak/latest/references/appendices/concepts/ (Link no longer valid)
https://docs.riak.com/riak/latest/references/appendices/concepts/Riak-Glossary/ (Link no longer valid)

I’ll cover a few key concepts as an introduction/refresher.
Consistency, eventual or otherwise
As Eric Brewer’s CAP theorem established, distributed systems have to make hard choices. Network partition is
inevitable. Hardware failure is inevitable. When a partition occurs, a well-behaved system must choose its behavior from a spectrum of options ranging from “stop accepting any writes until the outage is resolved” (thus maintaining absolute consistency) to “allow any writes and worry about consistency later” (to maximize availability).
Riak is designed to favor availability, but allows read and write requests to be tuned on the fly to sacrifice availability for increased consistency, depending on the business needs of the data.
The concept of eventual consistency is the topic of many academic papers and conference talks, and is a vital part of the Riak story. See our page on eventual consistency for more information.
Repairing data
Because Riak operates on the assumption that networks and systems fail, it has automated processes to clean up inconsistent data after such an event.
The key data structure to do so with Riak today is the vector clock, which I’ll describe shortly.
Historically Riak has relied on read repair, a passive anti-entropy mechanism for performing cleanup whenever a key is requested. It assembles the best answer for any given read request and makes certain that value is shared among the vnodes which should have it.
With the 1.3 release, Riak has added a new active anti-entropy (AAE) feature to handle such repair
activities independently of read requests, thus reducing the odds of outdated values being reported to clients.
Vector clocks
Vector clocks are critical pieces of metadata that help Riak identify temporal relationships between changes. In a distributed system it is neither possible nor necessarily useful to establish an absolute ordering of events.
One behavioral toggle with a broad impact that will not be evaluated in these documents is vnode\_vclocks, which determines whether vector clocks are tied to client identifiers (Riak’s behavior prior to 1.0) or virtual node counters (standard behavior from 1.0 onward).
Setting vnode\_vclocks to true (the now-standard behavior, which we’ll assume throughout this series) has a slight negative impact on performance but helps keep the number of siblings under control.
Siblings
Siblings are Riak’s way of ducking responsibility for making decisions about conflicting data when there’s no obvious way to judge which value is “correct” based on the history of writes, and when Riak is not configured to simply choose the last written value.
Keep in mind that as far as Riak’s key/value store is concerned, the values are opaque. If your application can compare two values and find a way to merge them, you are encouraged to incorporate that logic, but Riak will not do that for you.
Sibling management adds overhead both to the client and the database, but if you want to always have the best data available, that overhead is unavoidable.
(Unavoidable, that is, until Convergent/Commutative Replicated Data Types (CRDTs) are available for production use. See Riak’s riak\_dt project for more information.)
Configuration challenges
Riak currently has several layers of configuration:

Defaults embedded in the source code
Environment variables
Configuration files
Client software (by manipulating bucket properties)
Capability system

Of particular interest is the Riak capability system, which is complex and hasn’t been well-communicated.
In short, the capability system is designed to help make upgrades run more smoothly by allowing nodes to negotiate when it is appropriate to start using a new or changed feature.
For example, the vnode\_vclocks behavior is preferred by Riak nodes, and unless explicitly configured otherwise through overrides, a cluster being upgraded from a version prior to 1.2 will negotiate that value to true once the rolling upgrade has completed.
See the release notes for Riak 1.2.0 for more details on capabilities.
Riak is interested in improving the documentation for our configuration systems, the process of setting configuration values, and the transparency of the current values in a cluster.
All of the configuration items we discuss in this series, with the exception of vnode\_vclocks, can be redefined at the bucket level for more granular control over desired behaviors and performance characteristics.
Conflict resolution
We’ve covered a lot of useful background material; now let’s dive in, tackling two configuration parameters that directly impact how Riak handles conflict resolution and eventual consistency.
allow\_mult, when set to true, specifies that siblings should be stored whenever there is conflicting vector clock information.
last\_write\_wins, when set to true, leads to code shortcuts such that vector clocks will be ignored when writing values, thus the vector clock only reflects what the client supplied, not what was already in the system.
The default behavior (with both values false), is that new data is always written. Vector clocks are constructed from the new and any old objects, and siblings are not created.
Setting both values to true is an unsupported configuration; currently it effectively means that allow\_mult is treated as false.
Read repair follows the same logic as a client put request, so these values impact its behavior as well.
There is no way to reject client writes that include outdated vector clocks, so either make certain your clients are well-behaved, or better yet set allow\_mult=true and deal with conflicts in your application.
One final warning: because Riak considers values to be opaque, siblings can be identical. If the value and application-provided metadata are identical, siblings will still be created (assuming allow\_mult=true) if the vector clocks do not allow for resolution.
What’s next?
In our next post, I’ll cover most of the configuration parameters that govern Riak’s key (pun intended) behaviors regarding reads and writes.
John R. Daily
