---
title: "Riak KV 3.0.2 Release Notes"
description: ""
project: "riak_kv"
project_version: 3.0.2
menu:
  riak_kv-3.0.2:
    name: "Release Notes"
    identifier: "index_release_notes"
    weight: 101
    parent: index
toc: false
aliases:
  - /riak/3.0.2/community/release-notes
  - /riak/kv/3.0.2/intro-v20
  - /riak/3.0.2/intro-v20
  - /riak/kv/3.0.2/introduction
---

Released Aug 7, 2021.


## Overview

There are four changes made in Release 3.0.2:

Inclusion of backend fixes introduced in 2.9.8.

The addition of the range_check in the Tictac AAE based full-sync replication, when replicating between entire clusters. This, along with the backend performance improvements delivered in 2.9.8, can significantly improve the stability of Riak clusters when resolving large deltas.

A number of issues with command-line functions and packaging related to the switch from node_package to relx have now been resolved.

Riak tombstones, empty objects used by Riak to replace deleted objects, will now have a last_modified_date added to the metadata, although this will not be visible externally via the API.

This release is tested with OTP 20, OTP 21 and OTP 22; but optimal performance is likely to be achieved when using OTP 22.

## Previous Release Notes

Please see the KV 3.0.1 release notes [here]({{<baseurl>}}riak/kv/3.0.1/release-notes/).





