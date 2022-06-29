---
title_supertext: "Configuring:"
title: "Legacy Active Anti-Entropy"
description: ""
project: "riak_kv"
project_version: 2.9.4
menu:
  riak_kv-2.9.4:
    name: "Legacy AAE"
    identifier: "configuring_legacy_aae"
    weight: 103
    parent: "configuring-active-anti-entropy"
toc: true
aliases:
---

## Configuration settings in `riak.conf`

### TicTacAAE

Turn on TicTacAAE. It works independantly of the legacy AAE system, so can be run in parallel or without the legacy system. For more settings, check the [Configuration][config reference] page.

```
tictacaae_active = active
```

Note that this will use up more memory and disk space as more metadata is being stored.

### Storeheads

Turn on TicTacAAE storeheads. This will ensure that TicTacAAE will store more information about each key, including the size, modified date, and tombstone status. Without setting this to `true`, the `aae_fold` functions on this page will not work as expected.

```
tictacaae_storeheads = enabled
```

Note that this will use up more memory and disk space as more metadata is being stored.

### Tuning

You can increase the number of simultaneous workers by changing the `af4_worker_pool_size` value in `riak.conf`. The default is `1` per node.

```
af4_worker_pool_size = 1
```

