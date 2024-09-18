---
title_supertext: "Configuring:"
title: "Next Gen Replication"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.0:
    name: "Next Gen Replication"
    identifier: "nextgen_rep"
    weight: 200
    parent: "configuring"
version_history:
  in: "2.9.1+"
toc: true
commercial_offering: false
aliases:
---

[configure tictacaae]: ../active-anti-entropy/tictac-aae/
[configure nextgenrepl fullsync]: ./fullsync/
[configure nextgenrepl realtime]: ./realtime/
[configure nextgenrepl queuing]: ./queuing/
[tictacaae folds]: ../../using/tictac-aae-fold/

NextGenRepl provides a considerable improvement over the legacy replication engines. It is faster, more efficient, and more reliable. NextGenRepl is the recommended replication engine to use.

## Overview

NextGenRepl comprises of four main parts.

- On the source cluster:
  - A queuing system holding multiple queues with references to changed objects
  - FullSync to populate the queuing system with Riak objects that are different from the sink cluster
  - Realtime to populate the queuing system on each change of a Riak object
- On the sink cluster:
  - A consumer process that reads the queue from any source clusters and updates the sink cluster

The configuration is now kept in the `riak.conf` configuration file. The CLI cannot be used to configure NextGenRepl.

All management is now done via `riak attach` and not through the CLI like before.

Best performance and lowest overheads is provided by using the ProtocolBuffer API instead of the HTTP API. Security (TLS and certificate authentication) will only work with ProtocolBuffer API.

{{% note %}}
NextGenRepl relies on [TicTac AAE](../active-anti-entropy/tictac-aae/), so this must be enabled.
{{% /note %}}

## Verify Settings

Once your configuration is set, you can verify its correctness by
running the `riak` command-line tool:

```bash
riak chkconfig
```

## Queuing System

The heart of the NextGenRepl system is the queuing system.

You can have as many queues are you like with different filters on each queue.

Each queue has 3 levels of priority:

1. RealTime changes - these are normally copies of the Riak object, but can be references to the Riak object if the queue gets too large. These are populated automatically when a PUT (which includes inserts, updartes and deletes) occurs.
2. FullSync changes - these are references to Riak objects and are populated on the source cluster when the FullSync manager finds differenecs betweem the source cluster and the sink cluster.
3. Admin changes - these are references to Riak objects and are populated when the administrator performs actions via the [TicTac AAE Fold][tictacaae folds] commands.

The sink side replication manager will connect to its list of replication sources and replicate objects using these priorities - so RealTime changes first, FullSync differences second, and finally the admin changes.

The queuing system is always active.

[Learn More >>][configure nextgenrepl queuing]

## FullSync replication

NextGenRepl's fullsync works by a source cluster node checking on a schedule for changes with the sink cluster node (or load balancer) and pushing the changes found to a specific configured queue in the queuing system.

A source node can connect to 1 sink peer using an IP address or FQDN to check for differences. This can be the IP or FQDN of a load balancer for the sink cluster.

A source node will sync data from all nodes in the source cluster.

A source node will run fullsync according to the schedule on that specific source node. The source nodes will co-ordinate to 

If a source node or sink peer is offline for any reason, Riak will wait until the node is repaired before continuing. You should ensure that sufficient redundancies are in place to ensure uptime. This can be done by having multiple source nodes connecting to the same sink cluster, and by using a load balancer in front of the sink cluster.

Each source node can have the the same or different sink peer settings.

The number of different clusters you can fullsync to is defined by the number of Riak KV nodes in the source cluster.

[Learn More >>][configure nextgenrepl fullsync]

## RealTime replication

NextGenRepl's realtime replication system works by changes to the source cluster being added to the replication queuing system and then the sink nodes continuously pulling changes.

The source nodes are configured to publish changes to Riak objects to a specific named queue.

The sink nodes are configured to pull changes from a specific named queue from a list of source nodes.

A source node can be the source for multiple sink clusters.

It is better to list multiple source nodes in a cluster than to use a load balancer in front of the source cluster.

If a specific source node is offline for any reason, the sink node will automatically change how often it checks that specifc source node for updates. So if a source node is taken down for admin or hardware failure reasons, the sink node will adapt automatically.

You can list as many source nodes from as many clusters as you like for a given sink node, but they must all use the same queue name.

[Learn More >>][configure nextgenrepl realtime]

## riak.conf Settings

Setting | Options | Default | Description
:-------|:--------|:--------|:-----------
`ttaaefs_scope` | `{disabled, all, bucket, type}` | **REQUIRED** | For Tictac full-sync does all data need to be sync'd, or should a specific bucket be sync'd (bucket), or a specific bucket type (type).Note that in most cases sync of all data is lower overhead than sync of a subset of data - as cached AAE trees will be used.
`ttaaefs_queuename` | `text` | `q1_ttaaefs` | For tictac full-sync what registered queue name on this cluster should be use for passing references to data which needs to be replicated for AAE full-sync. This queue name must be defined as a `riak_kv.replq<n>_queuename`, but need not be exlusive to full-sync (i.e. a real-time replication queue may be used as well).
`ttaaefs_maxresults` | `any` (integer) | `64` | or tictac full-sync what is the maximum number of AAE segments to be compared per exchange. Reducing this will speed up clock compare queries, but will increase the number of exchanges required to complete a repair.
`ttaaefs_rangeboost` | `any` (integer) | `8` | For tictac full-sync what is the maximum number of AAE segments to be compared per exchange. When running a range_check query this will be the ttaaefs_max results * ttaaefs_rangeboost.
`ttaaefs_bucketfilter_name` | `any`, (text)| `` | For Tictac bucket full-sync which bucket should be sync'd by this node. Only ascii string bucket definitions supported (which will be converted using list_to_binary).
`ttaaefs_bucketfilter_type` | `any` (text) | `default` | For Tictac bucket full-sync what is the bucket type of the bucket name. Only ascii string type bucket definitions supported (these definitions will be converted to binary using list_to_binary)
`ttaaefs_localnval` | `any` (integer) | `3` | For Tictac all full-sync which NVAL should be sync'd by this node. This is the `local` nval, as the data in the remote cluster may have an alternative nval.
`ttaaefs_remotenval` | `any` (integer) | `3` | For Tictac all full-sync which NVAL should be sync'd in the remote cluster.
`ttaaefs_peerip` | `127.0.0.1` (text) | `` | The network address of the peer node in the cluster with which this node will connect to for full_sync purposes. If this peer node is unavailable, then this local node will not perform any full-sync actions, so alternative peer addresses should be configured in other nodes.
`ttaaefs_peerport` | `8898` (integer) | `` | The port to be used when connecting to the remote peer cluster.
`ttaaefs_peerprotocol` | `http`, `pb` | `http` | The protocol to be used when conecting to the peer in the remote cluster. Could be http or pb (but only http currently being tested).
`ttaaefs_allcheck` | `any` (integer) | `24` | How many times per 24hour period should all the data be checked to confirm it is fully sync'd. When running a full (i.e. nval) sync this will check all the data under that nval between the clusters, and when the trees are out of alignment, will check across all data where the nval matches the specified nval.
`ttaaefs_nocheck` | `any` (integer) | `0` | How many times per 24hour period should no data be checked to confirm it is fully sync'd. Use nochecks to align the number of checks done by each node - if each node has the same number of slots, they will naurally space their checks within the period of the slot.
`ttaaefs_hourcheck` | `any` (integer) | `0` | How many times per 24hour period should the last hours data be checked to confirm it is fully sync'd.
`ttaaefs_daycheck` | `any` (integer) | `0` | How many times per 24hour period should the last 24-hours of data be checked to confirm it is fully sync'd.
`ttaaefs_rangecheck` | `any` (integer) | `0` | How many times per 24hour period should the a range_check be run.
`ttaaefs_logrepairs` | `enabled`, `disabled` | `enabled` | If Tictac AAE full-sync discovers keys to be repaired, should each key that is repaired be logged
`tictacaae_active` | `active`, `passive` | `passive` | Enable or disable tictacaae. Note that disabling tictacaae will set the use of tictacaae_active only at startup - setting the environment variable at runtime will have no impact.
`aae_tokenbucket` | `enabled`, `disabled` | `enabled` | To protect against unbounded queues developing and subsequent timeouts/crashes of the AAE process, back-pressure signalling is used to block the vnode should a backlog develop on the AAE process. This can be disabled.
`tictacaae_dataroot` | `` | `"$platform_data_dir/tictac_aae"` | Set the path for storing tree caches and parallel key stores. Note that at startup folders may be created for every partition, and not removed when that partition hands off (although the contents should be cleared).
`tictacaae_parallelstore` | `leveled_ko`, `leveled_so` | `leveled_so` | On startup, if tictacaae is enabled, then the vnode will detect of the vnode backend has the capability to be a "native" store. If not, then parallel mode will be entered, and a parallel AAE keystore will be started. There are two potential parallel store backends - leveled_ko (Key ordered leveled), and leveled_so(Segment ordered Leveled).
`tictacaae_rebuildwait` | `` | `336` | This is the number of hours between rebuilds of the Tictac AAE system for each vnode. A rebuild will invoke a rebuild of the key store (which is a null operation when in native mode), and then a rebuild of the tree cache from the rebuilt store.
`tictacaae_rebuilddelay` | `` | `345600` | Once the AAE system has expired (due to the rebuild wait), the rebuild will not be triggered until the rebuild delay which will be a random number up to the size of this delay (in seconds).
`tictacaae_storeheads` | `enabled`, `disabled` | `disabled` | By default when running a parallel keystore, only a small amount of metadata is required for AAE purposes, and with store heads disabled only that small amount of metadata is stored.
`tictacaae_exchangetick` | `` | `240000` | Exchanges are prompted every exchange tick, on each vnode. By default there is a tick every 4 minutes. Exchanges will skip when previous exchanges have not completed, in order to prevent a backlog of fetch-clock scans developing.
`tictacaae_rebuildtick` | `` | `3600000` | Rebuilds will be triggered depending on the riak_kv.tictacaae_rebuildwait, but they must also be prompted by a tick. The tick size can be modified at run-time by setting the environment variable via riak attach.
`tictacaae_maxresults` | `` | `256` | The Merkle tree used has 4096 * 1024 leaves. When a large discrepancy is discovered, only part of the discrepancy will be resolved each exchange - active anti-entropy is intended to be a background process for repairing long-term loss of data, hinted handoff and read-repair are the short-term and immediate answers to entropy. How much of the tree is repaired each pass is defined by the tictacaae_maxresults.

