---
title: "TicTac AAE Folds"
description: ""
project: "riak_kv"
project_version: 2.9.4
menu:
  riak_kv-2.9.4:
    name: "TicTac AAE Folds"
    identifier: "cluster_operations_tictac_aae_fold"
    weight: 108
    parent: "managing_cluster_operations"
toc: true
since: 2.9.4
aliases:
---
[code riak_kv_vnode]: https://github.com/basho/riak_kv/blob/develop-3.0/src/riak_kv_vnode.erl
[riak attach]: ../../../using/admin/riak-cli/#attach
[config reference]: ../../../configuring/reference/#tictac-active-anti-entropy
[config tictacaae]: ../../../configuring/active-anti-entropy/tictac-aae
[tictacaae overview]: ../../../
[filter-by bucket]: #filter-by-bucket-name
[filter-by key-range]: #filter-by-key-range
[filter-by segment]: #filter-by-segment
[filter-by modified]: #filter-by-date-modified

Since Riak KV 2.9.1, the new AAE system, [TicTac AAE][tictacaae overview], has added several useful functions that make performing keylisting and tombstone management tasks quicker and more efficient by using TicTacAAE's Merkle trees instead of iterating over the keys in a bucket.

These functions stablisied in Riak KV 2.9.4, and so are not recommended before that version.

## Configuration settings in `riak.conf`

For a comprehensive guide to TicTac AAE's configuration settings, please see the [TicTac AAE configuration settings][config tictacaae] documentation.

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

## General usage

Use [Riak attach][riak attach] to run these commands.

### The Riak Client

For these calls to work, you will need a riak client. This will create one in a reusable variable called `Client`:

```riakattach
1> {ok, Client} = riak:local_client().
```

```erlang
{ok, Client} = riak:local_client().
```

`Client` can now be used for the rest of the `riak attach` session.

### Timeouts

The calls to `aae_fold` are synchronous calls with a 1 hour timeout, but they start an asynchronous process in the background.

If your command takes longer than 1 hour, then you will get `{error,timeout}` as a response after 1 hour. Note that the requested command continues to run in the background, so re-calling the same method will take up more resources.

To timeout you typically have to have a very large number of keys in the bucket.

#### How to check if finished after a timeout

After your experience a timeout, the current number of commands waiting to execute can be checked by asking for the size of the assured forwarding pool `af4_pool`. Once it reaches 0, there are no more workers as all commands have finished. The size of the pool can checked using this command:

```erlang
{_, _, _, [_, _, _, _, [_, _, {data, [{"StateData", {state, _, _, MM, _, _}}]}]]} = 
    sys:get_status(af4_pool), 
io:format("af4_pool has ~b workers\n", [length(MM)]), 
f().
```
{{% note title="Warning: existing variables cleared" %}}
`f()` will unbind any existing variables, which may not be your intention. If you remove `f()` then please remember that `MM` will remain bound to the first value. For re-use, you should change the variable name or restart the `riak attach` session.
{{% /note %}}

#### How to avoid timeouts

To reduce the chance of getting a timeout, reduce the number of keys checked by using the [bucket][filter-by bucket] and [key range][filter-by key-range] filters. 

The [modified][filter-by modified] filter will not reduce the number of keys checked, and only acts as a filter on the result.

## Filters

[Filter by bucket name][filter-by bucket]
- Without a bucket type
- With a bucket type
- All
[Filter by key range][filter-by key-range]
- From -> To
- All
[Filter by segment][filter-by segment]
[Filter by modified date][filter-by modified]
- From -> To
- All

### Filter by bucket name

This will reduce the number of keys checked.

#### Buckets without a bucket type

Use the name of the bucket as a binary. For example, to query bucket "cars", one would use:

```
{<<"cars">>}
```

This example will count the number of keys in the bucket "cars":

```
riak_client:aae_fold({
    object_stats,
    {<<"cars">>}, 
    all, 
    all
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

#### Buckets with a bucket type

Use the name of the bucket type and the bucket as a tuple pair of binaries. For example, to query bucket "dogs" with bucket type "animals", one would use:

```
{<<"animals">>, <<"dogs">>}
```

This example will count the number of keys in the bucket "dogs" of bucket type "animals":

```
riak_client:aae_fold({
    object_stats,
    {<<"animals">>, <<"dogs">>}, 
    all, 
    all
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

#### All buckets

To query all buckets, just use `all` for the bucket filter. This will count the number of keys in all buckets:

```
riak_client:aae_fold({
    object_stats, 
    all, 
    all, 
    all
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

### Filter by key range

This will reduce the number of keys checked.

#### From -> To

TicTacAAE stores keys in a bucket in a tree, so if you want a key starting with `n` you would have to go through all keys starting with `a` to `m` before reaching your starting point - and then continue on past to the very last key. This is very inefficient if you want only a specific subset of keys. Thankfully, TicTacAAE's trees are sorted by keyname, and you can make `aae_fold` jump straight to any key before starting and then automatically stop at any later key using the key range filter.

Use the name of the key you want to start and end at as a tuple pair of binaries. For example, to query keys starting with `n`, you would filter by `n` to `o`:

```
{<<"n">>, <<"o">>}
```

This example will count the number of keys in the bucket `cars` that start with `n`:

```
riak_client:aae_fold({
    object_stats, 
    {<<"cars">>}, 
    {<<"n">>,<<"o">>}, 
    all
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

{{% note title="Warning: case sensitive" %}}
As the values used for key filters are binary strings, they are case sensitive. So `a` and `A` are not the same.
{{% /note %}}


#### All keys

To query all keys, just use `all` for the key range filter. This will count all keys in the bucket `cars`:

```
riak_client:aae_fold({
    object_stats, 
    {<<"cars">>}, 
    all, 
    all
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

### Filter by segment

This filter is used internally by TictacAAE and for custom replication functions. It's usage is not covered by this guide.

### Filter by date modified

This will not reduce the number of keys checked, but will reduce the number of keys returned.

This filter is used when you need to locate keys modified in a certain time frame. 

The values are passed in a tuple with 3 values:

```
{date,From,To}
```

`date` is required. `From` and `To` are seconds since `1970-01-01 00:00:00`.

For example, to get all keys modified between 1970-01-01 00:01:00 (`From` = 60) and 1970-01-01 00:02:00 (`To` = 120), one would use:

```
{date,60,120}
```

This example returns all keys in the "cars" bucket that were modified between 1970-01-01 00:01:00 and 1970-01-01 00:02:00:

```
riak_client:aae_fold({
    object_stats, 
    {<<"cars">>}, 
    all, 
    {date,60,120}
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

{{% note title="Working out the number of seconds" %}}
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
),
riak_client:aae_fold({
    object_stats, 
    {<<"cars">>}, 
    all, 
    Modified_Filter_Value
    }, Client).
```

Or in one command to make it easily re-usable:

```
riak_client:aae_fold({
    object_stats, 
    {<<"cars">>}, 
    all, 
    Modified_Filter_Calculator({{2022,1,1},{0,0,0}}, {{2022,2,1},{0,0,0}})
    }, Client).
```
{{% /note %}}

## Useful functions of `aae_fold`

### object_stats

Returns a count of objects that meet the filter parameters.

```erlang
riak_client:aae_fold({
    object_stats, 
    bucket_filter, 
    key_range_filter, 
    modified_filter
    }, Client).
```
The [`bucket_filter`][filter-by bucket], [`key_range_filter`][filter-by key-range], and [`modified_filter`][filter-by modified] are detailed above and can be used to limit the keys counted.

For example, the following snippet will count all objects with the filters
- in the bucket "dogs" of bucket type "animals"
- whose keys are between "A" and "N"
- which were modified in January 2022

```erlang
riak_client:aae_fold({
    object_stats, 
    {<<"animals">>,<<"dogs">>}, 
    {<<"A">>,<<"N">>},
    {date,1640995200,1643673600}
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

The response will look something like this:

```
{ok,[{total_count,100},
     {total_size,500000},
     {sizes,[{1,91},{2,5},{3,4}]},
     {siblings,[{1,90},{2,6},{3,4}]}]}
```

Field | Example | Description
:-------|:--------|:--------
total_count | {total_count,100}  | The total number of objects. In the example, 100 objects were found.
total_size | {total_size,500000}  | The total size of all objects found in bytes. In the example, all found objects came to a total size of 500,000 bytes.
sizes | {sizes,[{1,90},{2,5},{3,4}]} | A set of tuples giving a histogram of object size. The first number in each tuple is the order of magnitude starting at 1=1KB (1024 bytes x 10^N). The second number is the number of objects of that magnitude. In the example, there are 90 objects under 1KB, 5 objects between 1KB and 10KB, and 4 objects between 10KB and 100KB.
siblings | {siblings,[{1,90},{2,6},{3,4}]} | A set of tuples giving the sibling count of objects. The first number in each tuple is the number of siblings. The second number in each tuple is the number of objects that have that many siblings. A sibling count of `1` means that there are no siblings (there is only 1 value). In the example, there are 90 objects with no siblings, 6 objects with 2 siblings, and 4 objects with 3 siblings.

{{% note title="`object_size` reference table" %}}
For quick reference, here is a table of magnitude and object size range for the first 10 orders of magnitude:

Magnitude | Minimum (bytes) | Maximum (bytes)
:--------:|---------:|---------:
1|0|1024
2|1,025|10,240
3|10,241|102,400
4|102,401|1,024,000
5|1,024,001|10,240,000
6|10,240,001|102,400,000
7|102,400,001|1,024,000,000
8|1,024,000,001|10,240,000,000
9|10,240,000,001|102,400,000,000
10|102,400,000,001|1,024,000,000,000

{{% /note %}}

### reap_tombs
Unreaped Riak tombstones are Riak objects that have been deleted, but have not been removed from the backend. Riak tracks this through tombstones. If automatic reaping is turned off (for eaxmple, by setting `delete_mode` = `keep`), then a large number of deleted objects can accumulate.

Use the `reap_tombs` call to count and remove these objects.

#### method `count`
Returns a count of tombstones that meet the filter parameters. Does NOT reap the tombstones.

```erlang
riak_client:aae_fold({
    reap_tombs, 
    bucket_filter, 
    key_range_filter, 
    segment_filter
    modified_filter,
    count
    }, Client).
```
The [`bucket_filter`][filter-by bucket], [`key_range_filter`][filter-by key-range], [`segment_filter`][filter-by segment], and [`modified_filter`][filter-by modified] are detailed above and can be used to limit the keys counted.

For example, the following snippet will count all tombstones with the filters
- in the bucket "dogs" of bucket type "animals"
- whose keys are between "A" and "N"
- which were modified in January 2022

```erlang
riak_client:aae_fold({
    reap_tombs, 
    {<<"animals">>,<<"dogs">>}, 
    {<<"A">>,<<"N">>},
    all,
    {date,1640995200,1643673600},
    count
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

The response will look something like this:

```
{ok,5}
```

This indicates that 5 tombstones were found meeting the filter parameters.

#### Method `local`
Marks tombstones for reaping that meet the filter parameters. Returns the number of tombstones marked by calling this function.

```erlang
riak_client:aae_fold({
    reap_tombs, 
    bucket_filter, 
    key_range_filter, 
    segment_filter
    modified_filter,
    local
    }, Client).
```
The [`bucket_filter`][filter-by bucket], [`key_range_filter`][filter-by key-range], [`segment_filter`][filter-by segment], and [`modified_filter`][filter-by modified] are detailed above and can be used to limit the keys counted.

For example, the following snippet will mark for reaping all tombstones with the filters
- in the bucket "dogs" of bucket type "animals"
- whose keys are between "A" and "N"
- which were modified in January 2022

```erlang
riak_client:aae_fold({
    reap_tombs, 
    {<<"animals">>,<<"dogs">>}, 
    {<<"A">>,<<"N">>},
    all,
    {date,1640995200,1643673600},
    local
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

The response will look something like this:

```
{ok,5}
```

This indicates that 5 tombstones were found meeting the filter parameters and were marked to be reaped.

### find_tombs

Returns tuples of bucket name, keyname, and object size of Riak tombstone objects that meet the filter parameters.

```erlang
riak_client:aae_fold({
    find_tombs, 
    bucket_filter, 
    key_range_filter, 
    segment_filter
    modified_filter
    }, Client).
```
The [`bucket_filter`][filter-by bucket], [`key_range_filter`][filter-by key-range], [`segment_filter`][filter-by segment], and [`modified_filter`][filter-by modified] are detailed above and can be used to limit the keys counted.

For example, the following snippet will find all tombstones with the filters
- in the bucket "dogs" of bucket type "animals"
- whose keys are between "A" and "N"
- which were modified in January 2022

```erlang
riak_client:aae_fold({
    find_tombs, 
    {<<"animals">>,<<"dogs">>}, 
    {<<"A">>,<<"N">>},
    all,
    {date,1640995200,1643673600}
    }, Client).
```

{{% note %}}
`Client` is from the section [The Riak Client](#the-riak-client) above.
{{% /note %}}

The response will be an array of `{bucket_name,key_name,object_size}` tuples that will look something like this:

```
{ok,[{{<<"animals">>,<<"dogs">>},<<"Barkie">>,550000},
    {{<<"animals">>,<<"dogs">>},<<"Lord Snuffles III">>,820000}]}
```

This indicates that 2 tombstones were found meeting the filter parameters. For each tombstone object found, an additional `{bucket_name,key_name,object_size}` tuple will be added to the array.

Field | Example | Description
:-------|:--------|:--------
bucket_name | `<<"cars">>` or `{<<"animals">>,<<"dogs">>}` | The bucket name as an Erlang binary. In the case of a bucket with a bucket type, a tuple of bucket type and bucket name.
key_name | `<<"Barkie">>` | The key name as an Erlang binary.
object_size | 550000 | The size in bytes of the tombstone object.


### find_keys

#### By sibling count
sibling_count

#### By object size
object_size

### list_buckets

## Other functions not covered

`aae_fold` has various other functions that can be called, but are mostly for internal use by Riak. These functions should not be used without a good understanding of the source code, but are provided here for reference:

- `erase_keys`
- `fetch_clocks_nval`
- `fetch_clocks_range`
- `merge_branch_nval`
- `merge_root_nval`
- `merge_tree_range`
- `repair_keys_range`
- `repl_keys_range`
