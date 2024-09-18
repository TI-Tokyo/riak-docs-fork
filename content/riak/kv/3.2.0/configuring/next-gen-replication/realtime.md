---
title_supertext: "Configuring: Next Gen Replication"
title: "RealTime"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.0:
    name: "Realtime"
    identifier: "nextgen_rep_realtime"
    weight: 102
    parent: "nextgen_rep"
version_history:
  in: "2.9.1+"
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

RealTime will ensure that the data in the sink cluster is updated as quickly as possible from the source clusters.

{{% note %}}
NextGenRepl relies on [TicTac AAE](../../active-anti-entropy/tictac-aae/), so this must be enabled.
{{% /note %}}

## Overview

NextGenRepl's realtime replication system works by changes to the source cluster being added to the [replication queuing system][configure nextgenrepl queuing] and then the sink nodes continuously pulling changes.

- The source nodes are configured to publish changes to Riak objects to a specific named queue.
- The sink nodes are configured to pull changes from a specific named queue from a list of source nodes.
- A source node can be the source for multiple sink clusters.
- It is better to list multiple source nodes in a cluster than to use a load balancer in front of the source cluster.
- If a specific source node is offline for any reason, the sink node will automatically change how often it checks that specifc source node for updates. So if a source node is taken down for admin or hardware failure reasons, the sink node will adapt automatically.
- You can list as many source nodes from as many clusters as you like for a given sink node, but they must all use the same queue name.

## Source setup

### Enable RealTime

RealTime changes are added to the queuing system by setting:

```
replrtq_enablesrc = enabled
```

By default, RealTime is turned off (`disabled`).

### Queues

At least one replication queue should allow RealTime objects to be added.

The easiest way to do this is to have a queue filter of `any`, but [other options are available][configure nextgenrepl queue filters].

