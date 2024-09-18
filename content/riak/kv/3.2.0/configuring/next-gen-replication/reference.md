---
title_supertext: "Configuring: Next Gen Replication"
title: "Configuration Reference"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.0:
    name: "Reference"
    identifier: "nextgen_rep_referemce"
    weight: 300
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
<td><code></code></td>
<td></td>
<td><code></code></td>
</tr>

</tbody>
</table>

## Sink

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
<td>A `|` deliminated list of source node connection strings. A connection string is a `:` deliminated tuple of IP/FQDN, port and protocol.</td>
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

## FullSync on Source cluster

Please see [FullSync][configure nextgenrepl fullsync] for more detail on how to configure these items.

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
<td><code></code></td>
<td></td>
<td><code></code></td>
</tr>

</tbody>
</table>

## FullSync on Sink cluster

Please see [FullSync][configure nextgenrepl fullsync] for more detail on how to configure these items.

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
<td><code></code></td>
<td></td>
<td><code></code></td>
</tr>

</tbody>
</table>