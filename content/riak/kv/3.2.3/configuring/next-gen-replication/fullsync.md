---
title_supertext: "Configuring: Next Gen Replication"
title: "FullSync"
description: ""
project: "riak_kv"
project_version: "3.2.3"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.3:
    name: "FullSync"
    identifier: "nextgen_rep_fullsync"
    weight: 103
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
[configure nextgenrepl queuing]: ../queuing/

NextGenRepl's FullSync feature provides a considerable improvement over the legacy fullsync engines. It is faster, more efficient, and more reliable. NextGenRepl is the recommended replication engine to use.

FullSync will ensure that the data in the source cluster is also in sink cluster.

{{% note %}}
NextGenRepl relies on [TicTac AAE](../../active-anti-entropy/tictac-aae/), so this must be enabled.
{{% /note %}}

## Overview

NextGenRepl's FullSync works on an automated schedule whereupon a source cluster node checks for changes with a predefined sink cluster node (or load balancer). It then pushes any changes found to a specific preconfigured queue in the queuing system.

A source node can connect to 1 sink node using an IP address or FQDN to check for differences. This can be the IP or FQDN of a load balancer for the sink cluster. Each source node can have the the same FullSync settings as the other source cluster nodes, or entirely different FullSync settings per node if needed.

A source node will sync data from all nodes in the source cluster.

A source node will run FullSync according to the schedule on that specific source node. The source nodes will co-ordinate to ensure that only one FullSync task runs at a time.

If a source node or sink peer is offline for any reason, Riak will wait until the node is repaired before continuing. You should ensure that sufficient redundancies are in place to ensure uptime. This can be done by having multiple source nodes connecting to the same sink cluster, and by using a load balancer in front of the sink cluster.

The number of different clusters you can FullSync to is defined by the number of Riak KV nodes in the source cluster.

{{% note %}}
Currently all changes listed in this documentation to NextGenRepl must be made by changing the values in the `riak.conf` file.
{{% /note %}}

## Enable

To turn on FullSync replication, a scope of operation (`ttaaefs_scope`) is needed. The default scope is `disabled` which means that FullSync replication is turned off. The scope can be set to:

- `disabled` - FullSync is disabled
- `all` - all buckets are replicated
- `bucket` - only the specified bucket is replicated
- `type` - only buckets of the specified bucket type are replicated

To FullSync replicate all buckets, use the `ttaaefs_scope` of `any`. For example, to FullSync replicate all buckets, set this value:

```
ttaaefs_scope = all
```

To FullSync replicate using a bucket name filter, use the `ttaaefs_scope` of `bucket` and the `ttaaefs_bucketfilter_name` setting. For example, to only FullSync replicate the bucket "my-bucket-name", set these values:

```
ttaaefs_scope = bucket
ttaaefs_bucketfilter_name = my-bucket-name
```

To FullSync replicate all buckets using a bucket type filter, use the `ttaaefs_scope` of `type` and the `ttaaefs_bucketfilter_type` setting. For example, to only FullSync replicate all buckets of bucket type "my-bucket-type", set these values:

```
ttaaefs_scope = type
ttaaefs_bucketfilter_type = my-bucket-type
```

## Queues

FullSync will send all changes from the sink cluster to the queue configured using the `ttaaefs_queuename` setting. The default for this is `q1_ttaaefs`. This can be any queue name, including the same queue name as used by RealTime replication.

For example, to set the FullSync queue name to the default of `q1_ttaaefs`, set `ttaaefs_queuename` like this:

```
ttaaefs_queuename = q1_ttaaefs
```

### Bi-directional FullSync

From Riak KV 3.0.10 onwards, it is possible to have the sink cluster also detect changes from the source cluster (bi-directional FullSync) and queue them on the sink clsuter side. This is configured using the `ttaaefs_queuename_peer` setting. The default for this setting is `disabled`.

For example, to set the sink cluster FullSync queue name to the standard name of `q1_ttaaefs`, set `ttaaefs_queuename_peer` like this:

```
ttaaefs_queuename_peer = q1_ttaaefs
```

## Read and write n_vals

When performing a GET on a Riak object in the source cluster, the FullSync client will read with an `r` value of `ttaaefs_localnval`. When performing a PUT of a Riak object in the sink cluster, the FullSync client will write with an `w` value of `ttaaefs_remotenval`. Both of these default to the standard Riak `n_val` of `3`.

To customise these values, use these settings:

```
ttaaefs_localnval = 3
ttaaefs_remotenval = 3
```

## Connections

Each source cluster node can connect to a single sink cluster node (or a load balancer). This is specificed in the settings of `ttaaefs_peerip`, `ttaaefs_peerport`, and `ttaaefs_peerprotocol`.

- `ttaaefs_peerip` - the IP address or FQDN of the sink cluster node.
- `ttaaefs_peerport` - the port to connect to on the sink cluster node.
- `ttaaefs_peerprotocol` - the protocol to use to talk to the sink cluster node. Use `pb` for the Protocol Buffer API, and use `http` fpr the HTTP API.

For example, to connect to IP `10.2.34.56` on port `8087` using the Protocol Buffer API, these would be the settings to use:

```
ttaaefs_peerip = 10.2.34.56
ttaaefs_peerport = 8087
ttaaefs_peerprotocol = pb
```

For example, to connect to the FQDN `node01.source-cluster-a.mynetwork.com` on port `8098` using the HTTP API, these would be the settings to use:

```
ttaaefs_peerip = node01.source-cluster-a.mynetwork.com
ttaaefs_peerport = 8098
ttaaefs_peerprotocol = http
```

If you need to have TLS encryption and certificate-based authentication then you must exclusively use the Protocol Buffer API (`pb`) for replication.

### TLS encryption

TLS security is configured for replication using the settings of `repl_cacert_filename`, `repl_cert_filename` and `repl_key_filename` which operate in a similar manner to the protocol listener settings.

For example, you could use settings similar to these:

```
repl_cacert_filename = /etc/riak/cacert.pem
repl_cert_filename = /etc/riak/cert.pem
repl_key_filename = /etc/riak/key.pem
```

### Riak Security

If Riak Security is enabled on the sink cluster, then the username for replication can be set with the `repl_username` setting:

```
repl_username = source-cluster-replication-user
```

## Schedule

FullSync uses an automated scheduling tool based on a configurable number of slots in a 24-hour period.

FullSync has the ability to check different time ranges, so recent changes can be checked more often than very old changes.

These time ranges are:

- `ttaaefs_autocheck` - uses logic to decide the best form of FullSync time range to check; this is the default.
- `ttaaefs_allcheck` - checks all keys.
- `ttaaefs_daycheck` - checks keys changed in the last 24 hours.
- `ttaaefs_hourcheck` - checks keys changed in the last hour.
- `ttaaefs_rangecheck` - checks keys since the last successfull check.
- `ttaaefs_nocheck` - skips the check; this is useful for padding the schedule.

Each check is set to an integer, and the FullSync scheduler will distribute the checks evenly over a 24 hours period in proportion to the number of each type of check.

For example, this schedule will run autocheck every hour:

```
ttaaefs_autocheck = 24
ttaaefs_allcheck = 0
ttaaefs_daycheck = 0
ttaaefs_hourcheck = 0
ttaaefs_rangecheck = 0
ttaaefs_nocheck = 0
```

For example, this schedule will run allcheck once per day, daycheck 3 times per day, hour check 8 times per day, and range check 12 times per day (for a total of 24 checks):

```
ttaaefs_autocheck = 0
ttaaefs_allcheck = 1
ttaaefs_daycheck = 3
ttaaefs_hourcheck = 8
ttaaefs_rangecheck = 12
ttaaefs_nocheck = 0
```

### Tuning for autocheck

Autocheck can limit the use of allcheck by setting a window of time in which allcheck can be safely called. This is ideal for scenarios where there is a dip in activity in the source and sink clusters. By default `ttaaefs_allcheck.policy` is set to `always`. It can be set to `never` to not allow autocheck to use allcheck at all, or `window` to restrict the hours in which allcheck can be used.

For example, to stop autocheck from ever using allcheck, use this setting:

```
ttaaefs_allcheck.policy = never
```

To limit the hours autocheck can use allcheck to between 10pm and 6am, use these settings:

```
ttaaefs_allcheck.policy = window
ttaaefs_allcheck.window.start = 22
ttaaefs_allcheck.window.end = 6
```

## Tuning

### Results size

When performing a comparison between clusters, the keys are compared in chunks called segments. The number of chunks checked at one time can be set via the `ttaaefs_maxresults` setting. This is 32 chunks by default. To speed up comparisons but at the cost of more comparisons, reduce this value. If you intend to use autocheck or rangecheck in the scheduler, then this value can be reduced to as low as 16 and will apply to daycheck and hourcheck.

As a performance boost for rangecheck, `ttaaefs_rangeboost` will increase the number of chunks checked but only for rangecheck. This is a multipler, so the number of chunks checked will be `ttaaefs_maxresults` * `ttaaefs_rangeboost`.

For example, this will limit the daycheck and hourcheck to 32 chunks, but allow rangecheck to (32 * 16 =) 512 chunks:

```
ttaaefs_maxresults = 32
ttaaefs_rangeboost = 16
```

### Cluster slice

`ttaaefs_cluster_slice` helps space out queries between clusters if you have more than 2 clusters performing FullSync to the same sink cluster. This will stop two clusters with identical schedules from mutual full-syncs at the same time. Each cluster may be configured with `ttaaefs_cluster_slice` number between 1 and 4.

For example, this will set the `ttaaefs_cluster_slice` to `1`.

```
ttaaefs_cluster_slice = 1
```
