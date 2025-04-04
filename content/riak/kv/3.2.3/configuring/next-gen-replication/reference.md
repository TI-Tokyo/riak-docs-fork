---
title_supertext: "Configuring: Next Gen Replication"
title: "Configuration Reference"
description: ""
project: "riak_kv"
project_version: "3.2.3"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.3:
    name: "Reference"
    identifier: "nextgen_rep_referemce"
    weight: 300
    parent: "nextgen_rep"
version_history:
  in: "3.2.3+"
toc: true
commercial_offering: false
aliases:
---

[configure tictacaae]: ../../active-anti-entropy/tictac-aae/
[configure nextgenrepl fullsync]: ../fullsync/
[configure nextgenrepl realtime]: ../realtime/
[configure nextgenrepl sink]: ../sink/
[configure nextgenrepl queuing]: ../queuing/
[configure nextgenrepl queue filters]: ../queuing/#queue-filters

These settings should be set in `riak.conf`.

## Queuing System

Please see [Queuing System][configure nextgenrepl queuing] for more detail on how to configure these items.

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>replrtq_compressonwire</code></td>
<td>Controls if Riak objects are compressed before being pulled to the sink. Note that this can slow down replication due to the increased overhead, but can speed up transmission.</td>
<td><code>disabled</code></td>
</tr>

<tr>
<td><code>replrtq_srcobjectlimit</code></td>
<td>The maximum number of actual Riak objects that can be stored in a specific queue. This is only used for RealTime Riak objects as all other queued items are references to a Riak object.</td>
<td><code>1000</code></td>
</tr>

<tr>
<td><code>replrtq_srcobjectsize</code></td>
<td>The maximum size of an actual Riak object that can be stored in a specific queue. This is only used for RealTime Riak objects as all other queued items are references to a Riak object.</td>
<td><code>200K</code></td>
</tr>

<tr>
<td><code>replrtq_srcqueue</code></td>
<td>The default queue is setup for FullSync only.</td>
<td><code>q1_ttaaefs:block_rtq</code></td>
</tr>

<tr>
<td><code>replrtq_srcqueuelimit</code></td>
<td>The maximum number of copies of Riak objects or references to Riak objects that can be stored in a each queue at each level of priority.</td>
<td><code>300000</code></td>
</tr>

</tbody>
</table>

## RealTime on Source cluster

Please see [RealTime][configure nextgenrepl realtime] for more detail on how to configure these items.

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>replrtq_enablesrc</code></td>
<td>Enable RealTime by setting to <code>enabled</code>.</td>
<td><code>disabled</code></td>
</tr>

<tr>
<td><code>replrtq_srcqueue</code></td>
<td>At least one queue should have RealTime allowed. Set a queue to <code>any</code> or one of the other filter options.</td>
<td><code>q1_ttaaefs:block_rtq</code></td>
</tr>

</tbody>
</table>

## FullSync on source cluster

Please see [FullSync][configure nextgenrepl fullsync] for more detail on how to configure these items.

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
`ttaaefs_rangecheck` | `any` (integer) | `0` | How many times per 24hour period should the range_check be run.
`ttaaefs_logrepairs` | `enabled`, `disabled` | `enabled` | If Tictac AAE full-sync discovers keys to be repaired, should each key that is repaired be logged

## Sink cluster

Please see [Sink][configure nextgenrepl sink] for more detail on how to configure these items.

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>replrtq_enablesink</code></td>
<td>Set to `enabled` to activate replication on this node.</td>
<td><code>disabled</code></td>
</tr>
<tr>
<td><code>replrtq_sinkpeerlimit</code></td>
<td>The maximum number of workers that can talk to a specific source node.</td>
<td><code>24</code></td>
</tr>
<tr>
<td><code>replrtq_sinkpeers</code></td>
<td>A <code>|</code> deliminated list of source node connection strings. A connection string is a <code>:</code> deliminated tuple of IP/FQDN, port and protocol.</td>
<td><code></code></td>
</tr>
<tr>
<td><code>replrtq_sinkqueue</code></td>
<td>The name of the queue on the source nodes to use for replicatation. The same queue name is used for all source nodes.</td>
<td><code>q1_ttaaefs</code></td>
</tr>
<tr>
<td><code>replrtq_sinkworkers</code></td>
<td>The total number of worker processes that can talk to source nodes.</td>
<td><code>24</code></td>
</tr>

</tbody>
</table>
