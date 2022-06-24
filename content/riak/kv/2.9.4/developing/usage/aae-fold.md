---
title: "TicTac AAE's `aae_fold`"
description: ""
project: "riak_kv"
project_version: 2.9.4
menu:
  riak_kv-2.9.4:
    name: "TicTacAAE Fold"
    identifier: "usage_aae_fold"
    weight: 108
    parent: "developing_usage"
toc: true
aliases:
---
Relevant code: https://github.com/basho/riak_kv/blob/develop-3.0/src/riak_kv_vnode.erl

The new AAE system, [TicTac AAE](), has a useful method of performing admin tasks quickly and efficiently using TicTacAAE's Merkle trees instead of iterating over the keys in a bucket.

# Configuration settings

## TicTac AAE
Set to on!

## Storeheads

Turn on storeheads in `riak.conf` to be actually usable

%% If storeheads is false, then will not check for tombstone. In
            %% this case fetch_clocks may be used, and an external get/delete
            %% be called


# General usage

From riak attach

1. Get client (e.g.)
2. Call

Call sync, process async.

# Common parameters

## bucket
Buckets
Bucket Types
## key range
A to B
## segment
For replication - not covered by this.
## date
Timestamp a to b, better in KV 3.0.?
### How to calc

# object_stats

Returns a count of objects
Example call, response, explanation of response

# reap_tombs
For dealing with tombstones that are unreaped

## method `count`
Returns a count of unreaped tombstones
Example call, response, explanation of response

## Method `local`
Reaps unreaped tombstones
Example call, response, explanation of response

# find_tombs


repl_keys_range
repair_keys_range
find_keys
- sibling_count
- object_size
erase_keys
list_buckets
merge_root_nval
merge_branch_nval
fetch_clocks_nval
fetch_clocks_range
merge_tree_range
repl_keys_range
repair_keys_range