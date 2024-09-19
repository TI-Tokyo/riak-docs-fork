---
title_supertext: "Configuring: Next Gen Replication"
title: "Queuing System"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.0:
    name: "Queues"
    identifier: "nextgen_rep_queuing"
    weight: 101
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
[configure nextgenrepl reference]: ../reference/
[tictacaae folds]: ../../../using/cluster-operations/tictac-aae-fold/

NextGenRepl's Queuing feature is the heart of the replication engine. It stores all changes to be replicated using multiple configuarble queues. It's limits and size can be configured for your specific NextGenRepl use case.

## Overview

You can have as many queues are you like with different filters on each queue.

Each queue has 3 levels of priority:

1. RealTime changes - these are normally copies of the Riak object, but can be references to the Riak object if the queue gets too large. These are populated automatically when a PUT (which includes inserts, updartes and deletes) occurs.
2. FullSync changes - these are references to Riak objects and are populated on the source cluster when the FullSync manager finds differenecs betweem the source cluster and the sink cluster.
3. Admin changes - these are references to Riak objects and are populated when the administrator performs actions via the [TicTac AAE Fold][tictacaae folds] commands.

The sink side replication manager will connect to its list of replication sources and replicate objects using these priorities - so RealTime changes first, FullSync differences second, and finally the admin changes.

The queuing system is always active.

## Maximum queue length

The default limit of the number of objects stored in a specific queue is `300000` Riak objects. Once this limit is reached, any new items will not be added to the queue.

This can value can be configured on a per-node basis by setting the `replrtq_srcqueuelimit` value to a posiitive integer.

## Maximum number and size of objects

When items are added to a queue, the normal mode is to add them as a reference to the Riak object and not the Riak object itself. However, as a performance boost for RealTime updates, a queue will contain a copy of the actual Riak object instead of a reference.

Having too many of these will increase memory load considerably, so there are two configuration values to limit the number of Riak objects and the maximum allowed size of each Riak object in the queue.

The maximum number of these objects can be set through `replrtq_srcobjectlimit` as a positive interger. The default is `1000` Riak objects.

The maximum size of these objects can be set through `replrtq_srcobjectsize` as positive interger. The default is `200KB`.

## Queue filters

A queue can be filtered in various ways using `replrtq_srcqueue`. As each sink node can only pull from one queue, this needs to be carefully planned. To replicate multiple queues to a specific sink cluster, then different sink cluster nodes will need to be configured to use different queue names.

The filters are:

- `any`: every RealTime, FullSync and AAE Fold update will be replicated.
- `block_rtq`: RealTime updates are blocked - only FullSync and AAE Fold updates will be replicated.
- `bucketname`: every RealTime, FullSync and AAE Fold update in any bucket matching this name (regardless of bucket type) will be replicated.
- `bucketprefix`: every RealTime, FullSync and AAE Fold update in any bucket name where the name starts with the configured ascii string (regardless of bucket type) will be replicated.
- `buckettype`: every RealTime, FullSync and AAE Fold update in any bucket of the given bucket type will be replicated.

For example, you could set `replrtq_srcqueue` to:

- `q1_ttaaefs:block_rtq` to have a queue called `q1_ttaaefs` that only has FullSync and AAE Fold updates. This is the default.
- `for_tokyo_cluster:bucketprefix:users_` to have a queue called `for_tokyo_cluster` that will replicate all changes to any bucket with the prefix `users_`.
- `backup_cluster:buckettype:financialtransactions` to have a queue called `backup_cluster` that will replicate all changes to any bucket with the bucket type of `financialtransactions`.

## Multiple queues

You can specific multiple queues by deliminating with the `|` character.

For example, this will create two queues - one with realtime updates called `allupdates` and one without realtime updates called `offsite_backup`:

```
replrtq_srcqueue = allupdates:any|offsite_backup:block_rtq
```

## Transmission compression

If bandwidth is a concern, objects can be compressed when they are requested by a sink peer. This can be turned on (`enabled`) and off (`disabled`) using `replrtq_compressonwire`.

## Configuration reference

Please check the [NextGenRepl configuration reference][configure nextgenrepl reference] for a complete reference.
