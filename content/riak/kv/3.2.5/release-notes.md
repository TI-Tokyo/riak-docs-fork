---
title: "Riak KV 3.2.5 Release Notes"
description: ""
project: "riak_kv"
project_version: "3.2.5"
lastmod: 2025-03-24T00:00:00-00:00
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

This release contains a fix for the nextgen replication full-sync solution, fixing an issue for which there already exists a workaround in Riak 3.2.4. Unless clusters have very high key counts (i.e. around 10 billion objects are larger), the workaround in Riak 3.2.4 should generally be sufficient, and so the update is non-urgent.


## Previous Release Notes

Please see the KV 3.2.4 release notes [here]({{<baseurl>}}riak/kv/3.2.4/release-notes/).

