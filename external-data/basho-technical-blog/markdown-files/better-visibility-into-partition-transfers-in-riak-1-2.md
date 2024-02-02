---
title: "Better Visibility Into Partition Transfers In Riak 1.2"
description: "June 25, 2012 The riak-admin transfers command contains additional information in the upcoming Riak 1.2 release. It should help users more easily discern what transfers are actively taking place along with the current status of each. Up until now, calling riak-admin transfers would result in o"
project: community
lastmod: 2015-05-28T19:24:11+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Ryan Zezeski"
pub_date: 2012-06-25T00:00:00+00:00
---
June 25, 2012
The riak-admin transfers command contains additional information in the upcoming Riak 1.2 release. It should help users more easily discern what transfers are actively taking place along with the current status of each.
Up until now, calling riak-admin transfers would result in output like the following.
$ riak-admin transfers
'dev3@127.0.0.1' waiting to handoff 6 partitions
'dev2@127.0.0.1' waiting to handoff 4 partitions
'dev1@127.0.0.1' waiting to handoff 6 partitions
This shows how many partitions each node is waiting to transfer, but that’s it. By checking the logs, partitions currently under transfer can be determined but even then there is no easy way to discover the approximate status or the rate at which they are moving. If that weren’t enough, this command can actually block handoff if called too frequently. I decided it was high time that these issues were addressed so that users have at least some insight into the status of transfers in the system. The updated transfers command looks like the following:
$ riak-admin transfers
'dev3@127.0.0.1' waiting to handoff 6 partitions
'dev2@127.0.0.1' waiting to handoff 4 partitions
'dev1@127.0.0.1' waiting to handoff 6 partitions
Active Transfers:
transfer type: ownership\_handoff
vnode type: riak\_kv\_vnode
partition: 365375409332725729550921208179070754913983135744
started: 2012-04-24 18:43:44 [5.96 s ago]
last update: 2012-04-24 18:43:48 [1.91 s ago]
objects transferred: 8651
2135 Objs/s
dev3@127.0.0.1 =======================> dev1@127.0.0.1
17.62 MB/s
The first thing to notice is that the old information is still there. I still find it useful to know how many partitions each node is waiting to handoff. The data that follows is the new information that was added for the 1.2 release. Here is the breakdown of the various fields:

transfer type – This is the type of transfer taking place, either ownership\_handoff or hinted\_handoff. They are similar but different. The former occurs during ownership change such as adding or removing nodes while; the latter is for moving data from a temporary fallback to a primary vnode. In other words, this tells you why a transfer is happening.
vnode type – This is the type of vnode being transferred. Types of vnodes are KV, Search, and Pipe. Basically, this is anything that has implemented a vnode and is registered as a service in Riak.
partition – This is the hash-ring index of the partition being transferred. This is useful because you can correlate the transfer with other things such as amount of data on disk, vnode pid, etc.
started – This is the start date-time (human friendly in brackets) of the transfer.
last update – This is the last time that an update was received about this transfer. This is an implementation detail, but the transfer mechanism sends out occasional updates, versus polling on every call, and this lets you know the last time an update was seen.
objects transferred – The total number of objects transferred.

As for the ASCII diagram on the bottom.
=======================>
The bar in the middle does NOT represent amount complete. It is a static arrow. The bytes-per-s is printed out in human friendly format.
Some things to note are:

A call to the transfers command will make a remote call to all nodes in the cluster.
A mixed cluster with older nodes will result in reduced information for some transfers.
Until the first update arrives there will be limited data.
This data is currently only exposed on the command line but is a perfect candidate for an HTTP resource.

To wrap up, in Riak 1.2 the transfers command will:

Describe number of partitions waiting to be transferred as it did before.
Provide additional information in the form of type and status of all active transfers across the entire cluster.
No longer block transfers if called too frequently.

Enjoy. And go download Riak.
Ryan
