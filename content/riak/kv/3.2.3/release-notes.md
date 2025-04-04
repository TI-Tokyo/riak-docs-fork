---
title: "Riak KV 3.2.3 Release Notes"
description: ""
project: "riak_kv"
project_version: "3.2.3"
lastmod: 2024-12-09T00:00:00-00:00

sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.3:
    name: "Release Notes"
    identifier: "index_release_notes"
    weight: 101
    parent: index
toc: false
aliases:
  - /riak/3.2.3/community/release-notes
  - /riak/kv/3.2.3/intro-v20
  - /riak/3.2.3/intro-v20
  - /riak/kv/3.2.3/introduction
---

Released Dec 09, 2024.

## Overview

Minor fixes and Enhancements, includes NHS releases 3.2.1, 3.2.2 and 3.2.2p1.

* Fix an issue with the (riak admin services)[https://github.com/OpenRiak/riak/issues/8] command. this is of particular relevance for larger stores (by key count) i.e. >> 1m object keys per vnode.
* Fix a potential cause of stalling in (leveldb)[https://github.com/martinsumner/leveled/issues/459]; this is of particular relevance for larger stores (by key count) i.e. >> 1m object keys per vnode.
* Resolved an issue with binary memory  management when using nextgenrepl to replicate objects with keys bigger than 64 bytes to clusters using the leveled backend. Some utility functions have been added to riak_kv_utils, that were helpful in investigating this issue.
* Resolved an issue with handling of spaces in Riak commands (e.g. within JSON-based definitions of bucket properties, or riak eval statements).
* Added support for both zstd compression and no compression in the leveled backend.
* Tidied up the closing of processes within leveled.
* Improvements to the CPU efficiency of leveled, specificaly when handling secondary index queries and aae folds.
* Upgraded the json library used in producing 2i query results to the library scheduled for inclusion in OTP 27.
* Added data size estimation to the riak_kv_leveled_backend to allow for progress reporting on transfers.
* Prevented the start of replication processes before riak_kv startup has completed.
* Improved repair-mode efficiency, so that each vnode is only repaired once, significantly reducing the cluster-wide overhead of entering repair mode (for AAE based full-sync).
* Prevented a potential race condition whereby a queued tree rebuild may lead to an incorrect segment in a cached tree.
* Reverted to a version of eleveldb based on the 3.0 path, whereby the version of snappy is specifically referenced rather than depending on the OS-supported version.
* Improved the monitoring of the node worker pools.
* Minor fixes to build and packaging, as well as addition of further VM configuration options.


## Previous Release Notes

Please see the KV 3.2.0 release notes [here]({{<baseurl>}}riak/kv/3.2.0/release-notes/).

