---
tile_supertext: "Configuring:"
title: "Next Gen Replication"
description: ""
project: "riak_kv"
project_version: "2.9.4"
menu:
  riak_kv-2.9.4:
    name: "Next Gen Replication"
    identifier: "nextgen_rep"
    weight: 200
    parent: "configuring"
version_history:
  in: "2.9.1+"
toc: true
commercial_offering: true
---

The configuration for Next Gen Replication is kept in
 the `riak.conf` configuration file. 

## Settings

Once your configuration is set, you can verify its correctness by
running the `riak` command-line tool:

```bash
riak chkconfig
```

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