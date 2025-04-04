---
title: "TicTac Active Anti-Entropy"
description: "An Active Anti-Entropy library"
project: "riak_kv"
project_version: "3.2.5"
lastmod: 2025-03-24T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.5:
    name: "TicTac Active Anti-Entropy"
    identifier: "cluster_operations_tictac_aae"
    weight: 108
    parent: "managing_cluster_operations"
toc: true
version_history:
  in: "2.9.0p5+"
aliases:
  - /riak/kv/3.2.5/ops/advanced/tictacaae/
  - /riak/3.2.5/ops/advanced/ticktacaae/
---

Riak's [active anti-entropy](../../../learn/concepts/active-anti-entropy/) \(AAE) subsystem is a set of background processes that repair object inconsistencies stemming from missing or divergent object values across nodes. Riak operators can turn AAE on and off and configure and monitor its functioning.

## TicTac AAE

The version of TicTac AAE included in 2.9 releases is a working prototype with limited testing. The intention is to full integrate the library into the KV 3.0 release.

TicTac Active Anti-Entropy makes two changes to the way Anti-Entropy has previously worked in Riak. The first change is to the way Merkle Trees are contructed so that they are built incrementally. The second change allows the underlying Anti-entropy key store to be key-ordered while still allowing faster access to keys via their Merkle tree location or the last modified date of the object.

## Configuring AAE

Riak's [configuration files](../../../configuring/reference/) enable you not just to turn TicTac AAE on and
off but also to fine-tune your cluster's use of TicTac AAE to suit your requirements.

