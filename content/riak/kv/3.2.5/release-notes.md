---
title: "Riak KV 3.2.5 Release Notes"
description: ""
project: "riak_kv"
project_version: "3.2.5"
lastmod: 2022-12-30T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.5:
    name: "Release Notes"
    identifier: "index_release_notes"
    weight: 101
    parent: index
toc: false
aliases:
  - /riak/3.2.5/community/release-notes
  - /riak/kv/3.2.5/intro-v20
  - /riak/3.2.5/intro-v20
  - /riak/kv/3.2.5/introduction
---

Released Jan 26, 2025.

## Overview

This release contains the following fixes and enhancements:

* Improve the performance of riak admin status requests whether via or console or web. Note that the statistic for sys_monitor_count will no longer produced as part of this change - but it can be checked if required using the riak_kv_util:sys_monitor_count/0 function.
* Fix an issue with the partial merge feature introduced to the leveled backend in Riak 3.2.3, which could cause vnodes to crash and restart.
* Improve the handling of handoff object folds in leveled to prevent handoff crashes due to bugs or inefficiency that could lead to timeouts.
* Although the issue with partial merge is only expected to occur in relatively rare circumstances, it is recommended that installations presently on Riak 3.2.3 and using the leveled backend, should schedule an upgrade to 3.2.5 as soon as possible.


## Previous Release Notes

Please see the KV 3.2.4 release notes [here]({{<baseurl>}}riak/kv/3.2.4/release-notes/).

