---
title: "Understanding riak_core: Handoff"
description: "At Erlang Factory 2015, I presented a talk entitled “How to build applications on top of riak_core.” I wanted to do this talk because there is a serious lack of “one-stop” documentation around riak_core. In particular, implementing handoffs has been under documented and not well disseminated. To hel"
project: community
lastmod: 2016-10-17T07:14:04+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mark Allen"
pub_date: 2015-05-12T04:59:40+00:00
---
At Erlang Factory 2015, I presented a talk entitled “How to build applications on top of riak\_core.” I wanted to do this talk because there is a serious lack of “one-stop” documentation around riak\_core. In particular, implementing handoffs has been under documented and not well disseminated. To help, I have a few blog posts to share.
In my first post, we’ll explore some background, define a handoff and answer the question of “should I use riak\_core?”
But first, some background
If you’re even had a little sip of Riak kool-aid over the past six years, you’re probably sick of hearing about “the ring” unless you’re a fan of Japanese horror movie remakes.
But in case you’re just joining us, Riak stores data as a key/value pair and uses a special function called a consistent hash to turn a key into a location inside of a keyspace. The keyspace in Riak is divided up into equal sized partitions which are managed by “virtual nodes” or vnodes.
 
As data (in the form of key/value pairs) is written to a Riak cluster, keys are distributed to the primary vnodes tasked to manage the relevant portion of the keyspace. If one or more of these vnodes are not available, the data will be sent instead to secondary, or “fallback” vnodes and their partitions (often referred to as “replicas”). Handoff is the mechanism for migrating data on secondary partitions back to its proper location. When you’re ready to retrieve the data, Riak will ensure the read request goes to the primary vnode (if available) or to a replica to return the value.
Understanding consistent hashing, the ring and vnodes is important for understanding how riak\_core handles operations that your application might implement.
What is a handoff?
A handoff is a transfer over the network of the keys and associated values from one cluster member to another cluster member. There are four types of handoffs that are supported in riak\_core: ownership, hinted, repair, and resize. Of these, the most commonly encountered types are ownership and hinted. We will talk about them all in the following sections.
Repairs
A repair handoff happens when your application explicitly calls riak\_core\_vnode\_manager:repair/3 – an example implementation of this can be found in riak\_kv\_vnode:repair/1. You might use this when your application detects some kind of data error during a periodic integrity sweep – you have to roll your own error detection code; riak\_core can’t intuit your application semantics. Be aware that this operation is a big hammer and if there is a lot of data in a vnode, you will pay a significant performance and latency penalty while a repair is on-going between the (physical) nodes involved in the repair operation.
Resize
riak\_core is set up to split its hash key space into partitions. The number of keyspaces is defined internally by the “ring size”. By default the ring size is 64. (Currently this number must be a power of two.) As of Riak 2.0, the ring size can be dynamically changed using a command line application. (See here for further information about resize operations.) When the ring size is changed up or down, the number of partitions in the key space goes up or down too. riak\_core will figure out how to move vnode data around your cluster members as it conforms to this new partitioning directive and it uses the resize handoff type to achieve this.
Ownership
An ownership handoff happens when a cluster member joins or leaves the cluster. When a cluster is added or removed, riak\_core reassigns the (physical) nodes responsible for each vnode and it uses the ownership handoff type to move the data from its old home to its new home. (The reassignment activity occurs when the “cluster plan” command is executed and the data transfers begin once the “cluster commit” command is executed.)
Hinted
When the primary vnode for a particular part of the ring is offline, riak\_core still accepts operations on it and routes those to a backup partition or “fallback” as its sometimes known in the source code. When the primary vnode comes back online, riak\_core uses a hinted handoff type to sync the current vnode state from the fallback(s) to the primary. Once the primary is synchronized, operations are routed to the primary once again.
Should I Use riak\_core?
Before choosing to build an application on riak\_core, you should have a good grasp of the problem domain you’re working on. You can ask yourself:

Does the problem involve computation which can be spread across a uniform set of workers using some kind of “key-like” value to route jobs?
Does the problem space have an abstraction that could be considered a “key” and link to a value which resembles a blob of data?

If either of these questions come out as a “Yes,” your use case is a good candidate for building an application on top of riak\_core.
Mark Allen
Speakerdeck
GitHub
Twitter
