---
title: "Riak KV 3.4.1 Release Notes"
description: ""
project: "riak_kv"
project_version: "3.4.1"
lastmod: 2026-09-10T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.4.1:
    name: "Release Notes"
    identifier: "index_release_notes"
    weight: 101
    parent: index
toc: false
aliases:
  - /riak/3.4.1/community/release-notes
  - /riak/kv/3.4.1/intro-v20
  - /riak/3.4.1/intro-v20
  - /riak/kv/3.4.1/introduction
---

Released September 10, 2026.

## Overview

This minor release makes the following external changes from Riak KV 3.4.0:

* The addition of two further accumulation options to the Query API, `queue_raw_keys` and `queue_raw_terms`. This allows unsorted results to be queued so that results can be pulled in batches, potentially by multiple clients, from any node in the cluster. Results are queued on disk, not in memory.
* The addition of the riak admin vnode-status command to view operational statistics about vnodes and their backends from across the cluster.
* An improvement to the automatic identification of unused files in the leveled backend, so that unused journal files are now recognised at startup as well as unused ledger files.
* The addition of virtual machine statistics to the stats endpoint, so that those statistics can be tracked against their limits.
* The addition of a new helper function to resync a bucket, and optimisations to nextgenrepl reconciliation to allow for accelerated resolution of large deltas without re-replicating whole buckets.
* The release includes a number of fixes, test improvements and internal changes. The full list of changes can be seen in the project status board.

The release can be used with either OTP 24 or OTP 26; with improved performance possible when choosing OTP 26.


## Previous Release Notes

Please see the KV 3.4.0 release notes [here]({{<baseurl>}}riak/kv/3.4.0/release-notes/).

