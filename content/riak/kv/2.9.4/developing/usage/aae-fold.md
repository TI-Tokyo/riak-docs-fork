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

Since Riak KV 2.9.1, the new AAE system, [TicTac AAE](), has added several useful functions that make performing keylisting and tombstone management  tasks quicker and more efficient by using TicTacAAE's Merkle trees instead of iterating over the keys in a bucket.

These functions stablisied in Riak KV 2.9.4, and so are not recommended before that version.

# Configuration settings in `riak.conf`

## TicTacAAE

Turn on TicTacAAE. It works independantly of the legacy AAE system, so can be run in parallel or without the legacy system. For more settings, check the [Configuration}(../../configuring/reference/#tictac-active-anti-entropy) page.

`tictacaae_active = active`

Note that this will use up more memory and disk space as more metadata is being stored.

## Storeheads

Turn on TicTacAAE storeheads. This will ensure that TicTacAAE will store more information about each key, including the size, modified date, and tombstone status. Without setting this to `true`, the `aae_fold` functions on this page will not work as expected.

`tictacaae_storeheads = enabled`

Note that this will use up more memory and disk space as more metadata is being stored.

# General usage

Use [Riak attach](../../using/admin/riak-cli/#attach) to run these commands.

## The Riak Client

For these calls to work, you will need a riak client. This will get one for in:

```
{ok, Client} = riak:local_client().
```

`Client` can now be used for the rest of the `riak attach` session.

## Timeouts

The calls to `aae_fold` are synchronous calls with a 1 hour timeout, but they start an asynchronous process in the background.

If your command takes longer than 1 hour, then you will get `{error,timeout}` as a response after 1 hour. Note that the requested command continues to run in the background, so re-calling the same method will take up more resources.

To timeout you typically have to have a very large number of keys in the bucket.

After your experience a timeout, the current number of commands waiting to execute can be checked by asking for the size of the assured forwarding pool `af4_pool`. Once it reaches 0, there are no more workers as all commands have finished. The size of the pool can checked using this command:

```
{_, _, _, [_, _, _, _, [_, _, {data, [{"StateData", {state, _, _, MM, _, _}}]}]]} = sys:get_status(af4_pool), 
io:format("af4_pool has ~b workers\n", [length(MM)]), 
f().
```

## Tuning

You can increase the number of simultaneous commands by changing the `af4_worker_pool_size` value. The default is `1` per node.

# Filters

## Filter by bucket name

### Buckets without a bucket type

Use the name of the bucket as a binary. For example, to query bucket "cars", one would use:

```
{<<"cars">>}
```

This will count the number of keys in the bucket "cars":

```
riak_client:aae_fold({object_stats,{<<"cars">>}, all, all}, Client).
```

Note: `Client` from the section "The Riak Client" above is used.

### Buckets with a bucket type

Use the name of the bucket type and the bucket as a tuple pair of binaries. For example, to query bucket "dogs" with bucket type "animals", one would use:

```
{<<"animals">>, <<"dogs">>}
```

This will count the number of keys in the bucket "dogs" of bucket type "animals":

```
riak_client:aae_fold({object_stats,{<<"animals">>, <<"dogs">>}, all, all}, Client).
```

Note: `Client` from the section "The Riak Client" above is used.

### All buckets

To query all buckets, just use `all` for the bucket filter. This will count the number of keys in all buckets:

```
riak_client:aae_fold({object_stats, all, all, all}, Client).
```

Note: `Client` from the section "The Riak Client" above is used.

## Filter by key range

### From -> To

TictacAAE stores keys in a bucket in a tree ordered by key name. This means that if you want a key starting with `n`, you would typically have to go through all keys starting with `a` to `m` before reaching your starting point - and then continue on past to the very last key. This is very inefficient if you want only a specific subset of keys. You can make `aae_fold` jump straight to the starting key and stop at the ending key using the key range filter.

Use the name of the key you want to start and end at as a tuple pair of binaries. For example, to query keys starting with `n`, you would filter by `n` to `o`:

```
{<<"n">>, <<"o">>}
```

This will count the number of keys in the bucket `cars` that start with `n`:

```
riak_client:aae_fold({object_stats, {<<"cars">>}, {<<"n">>,<<"o">>}, all}, Client).
```

Note: `Client` from the section "The Riak Client" above is used.
Note: as the key filters are binary strings, they are case sensitive. So `a` and `A` are not the same.


### All keys

To query all keys, just use `all` for the key range filter. This will count all keys in the bucket `cars`:

```
riak_client:aae_fold({object_stats, {<<"cars">>}, all, all}, Client).
```

Note: `Client` from the section "The Riak Client" above is used.

## Segment filter

This filter is used internally by TictacAAE and for custom replication functions. It's usage is not covered by this guide.

## Date modified filter

This filter is used when you need to location keys modified in a certain time frame. 

The values are passed in a tuple with 3 values. The `from` and `to` values are seconds since `1970-01-01 00:00:00`.

```
{date,from,to}
```

For example, to get all keys modified between 1970-01-01 00:01:00 and 1970-01-01 00:02:00, one would use:

```
{date,60,120}
```

This command returns all keys in the bucket "cars" modified between modified between 1970-01-01 00:01:00 and 1970-01-01 00:02:00:

```
riak_client:aae_fold({object_stats, {<<"cars">>}, all, {date,60,120}}, Client).
```

### Working out the number of seconds

The number of seconds can be worked out using this helper function:

```
Modified_Filter_Calculator = fun (StartDateTime, EndDateTime) ->
  EpochTime = calendar:datetime_to_gregorian_seconds({{1970,1,1},{0,0,0}}),
  LowTS = calendar:datetime_to_gregorian_seconds(StartDateTime) - EpochTime,
  HighTS = calendar:datetime_to_gregorian_seconds(EndDateTime) - EpochTime,
  {date, LowTS, HighTS}
end.
```

This can then be used like so to get the filter value for the range of "2022-01-01 00:00:00" to "2022-02-01 00:00:00" (i.e. all of January 2022):

```
Modified_Filter_Value = Modified_Filter_Calculator(
  {{2022,1,1},{0,0,0}},
  {{2022,2,1},{0,0,0}}
).

riak_client:aae_fold({object_stats, {<<"cars">>}, all, Modified_Filter_Value}, Client).
```

# Useful functions of `aae_fold`

## object_stats

Returns a count of objects
Example call, response, explanation of response

## reap_tombs
For dealing with tombstones that are unreaped

### method `count`
Returns a count of unreaped tombstones
Example call, response, explanation of response

### Method `local`
Reaps unreaped tombstones
Example call, response, explanation of response

## find_tombs

## find_keys

### By sibling count
sibling_count

### By object size
object_size

## list_buckets

# Other functions not covered

`aae_fold` has various other functions that can be called, but are mostly for internal use by Riak. These functions should not be used without a good understanding of the source code, but are provided here for reference:

- `erase_keys`
- `fetch_clocks_nval`
- `fetch_clocks_range`
- `merge_branch_nval`
- `merge_root_nval`
- `merge_tree_range`
- `repair_keys_range`
- `repl_keys_range`
