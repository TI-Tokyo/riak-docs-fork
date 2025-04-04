---
title_supertext: "Configuring: Next Gen Replication"
title: "RealTime"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.0:
    name: "Realtime"
    identifier: "nextgen_rep_realtime"
    weight: 102
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

RealTime will ensure that the data in the sink cluster is updated as quickly as possible from the source clusters.

{{% note %}}
NextGenRepl relies on [TicTac AAE](../../active-anti-entropy/tictac-aae/), so this must be enabled.
{{% /note %}}

## Overview

As changes occur on the source cluster, NextGenRepl's RealTime replication system will add them to one or more configurable queues within the [replication queuing system][configure nextgenrepl queuing]. 

A source node can be the source for multiple sink clusters by using multiple queues. 

{{% note %}}
Currently all changes listed in this documentation to NextGenRepl must be made by changing the values in the `riak.conf` file.
{{% /note %}}

## Enable RealTime

RealTime changes are added to the queuing system by setting:

```
replrtq_enablesrc = enabled
```

By default, RealTime is turned off (`disabled`).

## Queues

At least one replication queue should allow RealTime objects to be added. The easiest way to do this is to have a queue filter of `any`, but [other options are available][configure nextgenrepl queue filters].

By default, there is no queue setup for RealTime. To set the default queue to also allow RealTime queues, change:

```
replrtq_srcqueue = q1_ttaaefs:block_rtq
```

to 

```
replrtq_srcqueue = q1_ttaaefs:any
```

To add a new queue called `my-replication-queue` that allowed RealTime replication for any bucket, you would add `my-replication-queue:any` to the `replrtq_srcqueue` setting. For example, to keep the default FullSync-only queue and add a second queue for RealTime you would set:

```
replrtq_srcqueue = q1_ttaaefs:block_rtq|my-replication-queue:any
```
