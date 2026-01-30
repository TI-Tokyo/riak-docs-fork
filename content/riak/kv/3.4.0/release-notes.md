---
title: "Riak KV 3.4.0 Release Notes"
description: ""
project: "riak_kv"
project_version: "3.4.0"
lastmod: 2026-01-16T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.4.0:
    name: "Release Notes"
    identifier: "index_release_notes"
    weight: 101
    parent: index
toc: false
aliases:
  - /riak/3.4.0/community/release-notes
  - /riak/kv/3.4.0/intro-v20
  - /riak/3.4.0/intro-v20
  - /riak/kv/3.4.0/introduction
---

Released January 12th, 2026.

## Overview

This release supports two major additional features, not available in Riak 3.2.6:

A new Query API that offers improved support for conjunction queries; either through the application of filter expressions to projected attributes appended to sort keys, or through set expressions to combine the results of different range queries. Support is also added for different accumulation options; so that queries can return counts and counts by specific attributes as well as lists of keys and keys/terms.
Extending conditional PUT logic to have token-based consensus on conditional checks; allowing for the stronger application of conditions on PUTs, to significantly reduce the probability of siblings resulting from concurrent updates within a cluster.
There are a number of other improvements in the release:

* Improved configurability of logging; allowing for logs of different types to be split between different handlers, and the addition of support for logging in a json format.
* Monitoring facilities for Tictac-based AAE via a Command Line Interface; allowing for the prompting of tree rebuilds via the command-line, and a view of the current status of the anti-entropy system.
* Improved efficiency of node repairs through the double `_pair and repair_` deferred configuration option; improving the efficiency of repairs under application load when using the leveled backend.
* The prompting of AAE folds via a Command Line Interface; with the capability to prompt long-running folds to have results written to disk on completion.
* The addition of a new bucket property `aae_tree_exclude`; whereby buckets with temporary data not intended to be replicated can be excluded from cached AAE trees.
* From this release, up-to-date documentation is now available, which will be maintained by the OpenRiak community and aligned with OpenRiak releases.

The release can be used with either OTP 24 or OTP 26; with improved performance expected when choosing OTP 26, in particular when using the leveled backend and the HTTP API.

To reduce maintenance overheads going forward, the release deprecates the following functionality:

* Use of the `eleveldb` backend; with improvements planned to make the `bitcask` backend support `HEAD` requests efficiently, to align with the `leveled` backend.
* Use of the `memory` backend; with future improvements planned for vnode-level caching, configurable via bucket properties.
* Use of `multi` backends; except where all backends are `bitcask` backends.
* Use of `map/reduce` querying of Riak stores; with further extensions planned for the Query API, in particular the ability to publish objects from query results to a queue, to be consumed in parallel by multiple external processes.
* Use of `riak_ensemble` backed strong consistency; with the preference to use the support for conditional PUT requests with token-based consensus, provided in this release, for tuning consistency.
* Support for `dtrace` within Riak; with a preference to support internal Erlang tooling for debugging and monitoring in the future.
* Use of `v1.4 counters` and `link-walking`; which have been deprecated since Riak 2.0.
* There is ongoing work for the Riak 4.0 release to provide a more flexible capability to merge objects, and this may change the future support status of CRDT data-types within Riak: although the aim will be to make any new feature sufficiently extensible to support backwards compatibility with existing CRDTs.

The NextGen replication functionality and the related Tictac-form of AAE are now considered to be stable and feature-complete; and so support for maintenance of legacy replication and anti-entropy mechanisms is not currently guaranteed for future releases.

Should the retirement of features in Riak 4.0 prove to be problematic for Riak users, the preference of the OpenRiak community is to seek support to prolong the availability of features by providing an OTP28 compatible Riak KV 3.6 release, rather than maintaining those features within Riak 4.0. Decisions on retirement and support in releases will continue to be considered via [OpenRiak discussions](https://github.com/orgs/OpenRiak/discussions), while being constrained by the level of support provided to the community by user groups and their associates.

## Previous Release Notes

Please see the KV 3.2.6 release notes [here]({{<baseurl>}}riak/kv/3.2.6/release-notes/).

