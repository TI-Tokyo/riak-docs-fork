---
title_supertext: "Configuring: Next Gen Replication"
title: "Sink Nodes"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.0:
    name: "Sink Nodes"
    identifier: "nextgen_rep_sink"
    weight: 200
    parent: "nextgen_rep"
version_history:
  in: "3.2.0+"
toc: true
commercial_offering: false
aliases:
---

[configure tictacaae]: ../../active-anti-entropy/tictac-aae/
[configure nextgenrepl fullsync]: ../fullsync/
[configure nextgenrepl realtime]: ../realtime/
[configure nextgenrepl queuing]: ../queuing/
[configure nextgenrepl queue filters]: ../queuing/#queue-filters

NextGenRepl's RealTime feature provides a considerable improvement over the legacy realtime engines. It is faster, more efficient, and more reliable. NextGenRepl is the recommended replication engine to use.



{{% note %}}
NextGenRepl relies on [TicTac AAE](../../active-anti-entropy/tictac-aae/), so this must be enabled.
{{% /note %}}

## Overview

Sink nodes pull changes from the [Queuing System][configure nextgenrepl queuing] on the source nodes. This will handle both FullSync and RealTime replication as configured on the source nodes.

{{% note %}}
Currently all changes listed in this documentation to NextGenRepl must be made by changing the values in the `riak.conf` file.
{{% /note %}}

## Enable sink

RealTime, FullSync and AAE fold changes will be pulled as part of the NextGenRepl sink process. This is the same process for all NextGenRepl replication types.

To turn on the NextGenRepl sink process, set this in the `riak.conf` on the sink nodes:

```
replrtq_enablesink = enabled
```

By default, NextGenRepl is turned off (`disabled`).

## Queue name

A specific node will always pull from a specific named queue from all configured source nodes. This queue name is specified by setting `replrtq_sinkqueue`. For example, to read from the source queue called `my-replication-queue` you would set:

```
replrtq_sinkqueue = my-replication-queue
```

This value can vary between nodes in a sink cluster if you want to read from multiple queues in the source cluster(s).

## Sink connections

A single sink node can pull changes from multiple sources. This could be for redundancy purposes (each sink node talking to every node in a source cluster) or for replication from multiple clusters (each sink node talking to a one or more nodes in several source clusters).

The queue checked on source nodes will be the queue specified in `replrtq_sinkqueue` regardless of the connection used. To use multiple queues, configure different sink nodes with different `replrtq_sinkqueue` values.

In general, do not use a load balancer as a source. Always use an actual Riak KV nodes unless carefully thought out.

The list of source nodes for the sink node to connect to is specified in `replrtq_sinkpeers`. This holds a `|` deliminated list of peer connection strings. Each peer connection string is a 3-value tuple deliminated by a `:` consisting of IP/FQDN, port, and protocol.

Some examples of a single peer connection string would be:

- `node01.source-cluster.mynetwork.com:8098:http` - connect to the FQDN `node01.source-cluster.mynetwork.com` on port `8098` using the HTTP API.
- `node01.source-cluster.mynetwork.com:8087:pb` - connect to the FQDN `node01.source-cluster.mynetwork.com` on port `8087` using the Protocol Buffer API.
- `10.2.34.56:8098:http` - connect to the IP address `10.2.34.56` on port `8098` using the HTTP API.

To specify multiple peer connection strings in `replrtq_sinkpeers`, join the individual peer connection strings together with a `|`. For example, this will connect to three nodes in the same source cluster using Procotol Buffers:

```
replrtq_sinkpeers = node01.source-cluster-a.mynetwork.com:8087:pb|node02.source-cluster-a.mynetwork.com:8087:pb|node03.source-cluster-a.mynetwork.com:8087:pb
```

As another example, this will connect to one node in 3 different source clusters using Procotol Buffers:

```
replrtq_sinkpeers = node01.source-cluster-a.mynetwork.com:8087:pb|node01.source-cluster-b.mynetwork.com:8087:pb|node01.source-cluster-c.mynetwork.com:8087:pb
```

As a third example, this will connect to one node in 3 different source clusters using HTTP:

```
replrtq_sinkpeers = node01.source-cluster-a.mynetwork.com:8098:http|node01.source-cluster-b.mynetwork.com:8098:http|node01.source-cluster-c.mynetwork.com:8098:http
```

It is also possible to mix HTTP and Protocol Buffer connection strings. For example, this will connect to one node using HTTP and other nodes using Protocol Buffers:

```
replrtq_sinkpeers = node01.source-cluster-a.mynetwork.com:8098:http|node01.source-cluster-b.mynetwork.com:8087:pb|node01.source-cluster-c.mynetwork.com:8087:pb
```

For the purposes of redundancy, it is best to have replication enabled on every sink node, and to have every sink node talk to every source node. If a source node becomes unavailable, Riak will automatically reduce how often that peer is checked until the peer becomes available again.

If you need to have TLS security and certificate-based authentication then you must exclusively use the Protocol Buffer API (`pb`) for replication.

## Tuning

There are two easily changed values for tuning.

The total number of worker processes per sink node that consume objects from the source nodes is defined in `replrtq_sinkworkers` and defaults to 24 simultaneous workers. If the queues on the source side are growing, then this value should be increased.

NextGenRepl will allocate workers to connections to each source node based on performance. A limit can be set for the maximum number of workers connected to a single node by setting `replrtq_sinkpeerlimit`. This defaults to 24 as well.
