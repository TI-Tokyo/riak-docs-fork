---
title: "Rack Awareness"
description: "Rack awareness in Riak nodes"
project: "riak_kv"
project_version: 3.0.6
menu:
  riak_kv-3.0.6:
    name: "Errors"
    identifier: "Rack_awareness"
    weight: 101
    parent: "managing_rack_awareness"
toc: true
aliases:
  - /riak/3.0.6/ops/running/recovery/rack-awareness
  - /riak/kv/3.0.6/ops/running/recovery/rack-awareness
---

### Aim of rack awareness

The aim of rack awareness in Riak KV is to help make the cluster more resilient against location/site/availability zone/rack loss.

In KV 3.0.6+ a new location parameter has been introduced that can be set at runtime for each Riak node. When claiming a new ring, the list of nodes is ordered taking into consideration the location of the individual nodes, in a manner that adjacent nodes are preferably from different locations.
By default, every node in the cluster that does not have a location parameter set will be set to the location of `undefined`. This means that they will all be treated as being in the same location.
Figure 1 below shows how a cluster with 4 nodes and a 64 ring size could be configured with rack awareness.

**<figure id="figure-1" style="text-align:center;">
  <img src="/riak-docs/images/ring-location.png">
  <figcaption>
    Figure 1: Example of a rack-aware cluster.
  </figcaption>
</figure>**

### Lack of distinct locations

There are circumstances where the preferable node location assignment cannot be guaranteed.
For example, if the default `n_val` of `3` is used and there are only two distinct locations set, the following message will be shown: `WARNING: Not all replicas will be on distinct locations`
This means that at least one of the three replicas made by Riak KV will be on the same location as another, which may reduce the redunancy in the cluster.

### Improper distinct location count

Improper distinct location count could result in undesirable location distribution within the ring, meaning not every location has a replica of every piece of data. This could result in data being unavailable should one of the sites fail.
For example, there are 8 nodes on 3 distinct locations. To ensure that every site/location has a piece of data, `n_val` must be at least `4`.

You can check the staged changes to a cluster's location configuration via `riak attach`:

```Riak console
PlannedRing = element(1, lists:last(element(3, riak_core_claimant:plan()))).
riak_core_location:check_ring(PlannedRing, Nval = 4, MinimumNumberOfDistinctLocations = 3).
```

if `riak_core_location:check_ring/3` returns `[]` then there are no location violations.
