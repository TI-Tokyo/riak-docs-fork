---
title: "Backend Configuration"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2022-12-30T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.0:
    name: "Backend Configuration"
    identifier: "configuring_backend"
    weight: 110
    parent: "configuring"
toc: true
aliases:
---

[plan backend leveldb]: {{<baseurl>}}riak/kv/3.2.0/setup/planning/backend/leveldb
[plan backend leveled]: {{<baseurl>}}riak/kv/3.2.0/setup/planning/backend/leveled
[plan backend bitcask]: {{<baseurl>}}riak/kv/3.2.0/setup/planning/backend/bitcask
[plan backend memory]: {{<baseurl>}}riak/kv/3.2.0/setup/planning/backend/memory
[plan backend multi]: {{<baseurl>}}riak/kv/3.2.0/setup/planning/backend/multi

## LevelDB

Configurable parameters for Riak's [LevelDB][plan backend leveldb] storage backend.

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>leveldb.block_cache_threshold</code></td>
<td>This setting defines the limit past which block cache memory can no
longer be released in favor of the page cache. This setting has no
impact in favor of file cache. The value is set on a per-vnode basis.
</td>
<td><code>32MB</code></td>
</tr>

<tr>
<td><code>leveldb.compaction.trigger.tombstone_count</code></td>
<td>Controls when a background compaction initiates solely due to the
number of delete tombstones within an individual <code>.sst</code> table
file. A value of <code>off</code> disables the feature.</td>
<td><code>1000</code></td>
</tr>

<tr>
<td><code>leveldb.compression</code></td>
<td>Enabling this setting (<code>on</code>), which is the default,
saves disk space. Disabling it may reduce read latency but increase
overall disk activity. This option can be changed at any time, but it
will not impact data on disk until the next time a file requires
compaction.</td>
<td><code>on</code></td>
</tr>

<tr>
<td><code>leveldb.compression.algorithm</code></td>
<td>This setting is used to select which compression algorithm
	is selected when <code>leveldb.compression</code> is on.
	In new riak.conf files, this is explicitly set to
	<code>lz4</code>; however when this setting is not provided,
	<code>snappy</code> will be used for backward-compatibility.
	<br /><br />
	When you determine that you will no longer need backward-compatibility,
	setting this to <code>lz4</code> will cause future compactions
	to use the LZ4 algorithm for compression.</td>
<td><code>lz4</code> in new riak.conf files<br /><br />
	<code>snappy</code> when not provided
</td>
</tr>

<tr>
<td><code>leveldb.data_root</code></td>
<td>The directory in which LevelDB will store its data.</td>
<td><code>./data/leveldb</code></td>
</tr>

<tr>
<td><code>leveldb.fadvise_willneed</code></td>
<td>Option to override LevelDB's use of <code>fadvise(DONTNEED)</code>
with <code>fadvise(WILLNEED)</code> instead. <code>WILLNEED</code> can
reduce disk activity on systems where physical memory exceeds the
database size.</td>
<td><code>false</code></td>
</tr>

<tr>
<td><code>leveldb.maximum_memory</code></td>
<td>This parameter defines the server memory (in bytes) to assign to
LevelDB. Also see <code>leveldb.maximum_memory.percent</code> to set
LevelDB memory as a percentage of system total.</td>
<td><code>80</code></td>
</tr>

<tr>
<td><code>leveldb.maximum_memory.percent</code></td>
<td>This parameter defines the percentage of total server memory to
assign to LevelDB. LevelDB will dynamically adjust its internal cache
sizes to stay within this size. The memory size can alternately be
assigned as a byte count via <code>leveldb.maximum_memory</code>
instead.</td>
<td><code>70</code></td>
</tr>

<tr>
<td><code>leveldb.threads</code></td>
<td>The number of worker threads performing LevelDB operations.</td>
<td><code>71</code></td>
</tr>

<tr>
<td><code>leveldb.verify_checksums</code></td>
<td>Enables or disables the verification of the data fetched from
LevelDB against internal checksums.</td>
<td><code>on</code></td>
</tr>

<tr>
<td><code>leveldb.verify_compaction</code></td>
<td>Enables or disables the verification of LevelDB data during
compaction.</td>
<td><code>on</code></td>
</tr>

<tr>
<td><code>leveldb.block.size_steps</code></td>
<td>Defines the number of incremental adjustments to attempt between the
<code>block.size</code> value and the maximum <code>block.size</code>
for an <code>.sst</code> table file. A value of zero disables the
underlying dynamic <code>block_size</code> feature.</td>
<td><code>16</code></td>
</tr>

<tr>
<td><code>leveldb.block.restart_interval</code></td>
<td>Defines the key count threshold for a new key entry in the key
index for a block. Most deployments should leave this parameter alone.
</td>
<td><code>16</code></td>
</tr>

<tr>
<td><code>leveldb.block.size</code></td>
<td>Defines the size threshold for a block/chunk of data within one
<code>.sst</code> table file. Each new block gets an index entry in the
<code>.sst</code> table file's master index.</td>
<td><code>4KB</code></td>
</tr>

<tr>
<td><code>leveldb.bloomfilter</code></td>
<td>Each database <code>.sst</code> table file can include an optional
"bloom filter" that is highly effective in shortcutting data queries
that are destined to not find the requested key. The Bloom filter
typically increases the size of an <code>.sst</code> table file by about
2%.</td>
<td><code>on</code></td>
</tr>

<tr>
<td><code>leveldb.write_buffer_size_min</code></td>
<td>Each vnode first stores new key/value data in a memory-based write
buffer. This write buffer is in parallel to the recovery log mentioned
in the <code>sync</code> parameter. Riak creates each vnode with a
randomly sized write buffer for performance reasons. The random size is
somewhere between <code>write_buffer_size_min</code> and
<code>write_buffer_size_max</code>.</td>
<td><code>30MB</code></td>
</tr>

<tr>
<td><code>leveldb.write_buffer_size_max</code></td>
<td>See <code>leveldb.write_buffer_size_min</code> directly above.</td>
<td><code>60MB</code></td>
</tr>

<tr>
<td><code>leveldb.limited_developer_mem</code></td>
<td>This is a Riak-specific option that is used when a developer is
testing a high number of vnodes and/or several VMs on a machine with
limited physical memory. Do <em>not</em> use this option if making
performance measurements. This option overwrites values given to
<code>write_buffer_size_min</code> and
<code>write_buffer_size_max</code>.</td>
<td><code>off</code></td>
</tr>

<tr>
<td><code>leveldb.sync_on_write</code></td>
<td>Whether LevelDB will flush after every write.<br /><br />
<strong>Note</strong>: If you are familiar with fsync, this is analogous
to calling fsync after every write.</td>
<td><code>off</code></td>
</tr>

<tr>
<td><code>leveldb.tiered</code></td>
<td>The level number at which LevelDB data switches from the faster to
the slower array. The default of <code>off</code> disables the
feature.</td>
<td><code>off</code></td>
</tr>

<tr>
<td><code>leveldb.tiered.path.fast</code></td>
<td>The path prefix for <code>.sst</code> files below the level set by
<code>leveldb.tiered</code>.</td>
<td></td>
</tr>

<tr>
<td><code>leveldb.tiered.path.slow</code></td>
<td>The path prefix for <code>.sst</code> files below the level set by
<code>leveldb.tiered</code>.</td>
<td></td>
</tr>

</tbody>
</table>

## Leveled

Configurable Parameters for Riak's [leveled][plan backend leveled] storage backend

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>leveled.data_root</code></td>
<td>A path under which leveled data files will be stored.</td>
<td><code>$(platform_data_dir)/leveled</code>
</tr>

<tr>
<td><code>leveled.sync_strategy</code>
<td>Strategy for flushing data to disk - Can be set to <code>riak_sync</code>, <code>sync</code> (if OTP > 16) or <code>none</code>. Use <code>none</code>, and the OS will flush when most efficient. Use <code>riak_sync</code> or <code>sync</code> to flush after every PUT (not recommended wihtout some hardware support e.g. flash drives and/or
Flash-backed Write Caches)</td>
<td><code>none</code></td>
</tr>

<tr>
<td><code>leveled.compression_method</code></td>
<td>Can be lz4 or native (which will use the Erlang native zlib compression) within term_to_binary</td>
<td><code>native</code></td>
</tr>

<tr>
<td><code>leveled.compression_point</code></td>
<td>The point at which compression is applied to the Journal (the Ledger is always compressed).  Use on_receipt or on_compact.  on_compact is suitable
when values are unlikely to yield much benefit from compression(compression is only attempted when compacting)</td>
<td><code>on_receipt</code></td>
</tr>

<tr>
<td><code>leveled.log_level</code></td>
<td>Can be debug, info, warn, error or critical. Set the minimum log level to be used within leveled. Leveled will log many lines to allow for stats to be etracted by those using log indexers such as Splunk</td>
<td><code>info</code></td>
</tr>

<tr>
<td><code>leveled.journal_size</code></td>
<td> The approximate size (in bytes) when a Journal file should be rolled. Normally keep this as around the size of o(100K) objects.</td>
<td><code>1000000000</code></td>
</tr>

<tr>
<td><code>leveled.compaction_runs_perday</code></td>
<td>The number of journal compactions per vnode per day, The higher the value, the more compaction runs, and the sooner space is recovered. But each run has a cost.</td>
<td><code>24</code></td>
</tr>

<tr>
<td><code>leveled.compaction_low_hour</code></td>
<td>The hour of the day in which journal compaction can start. Use Low hour of 0 and High hour of 23 to have no compaction window (i.e. always compactregardless of time of day)</td>
<td><code>0</code></td>
</tr>

<tr>
<td><code>leveled.compaction_top_hour</code></td>
<td>The hour of the day, after which journal compaction should stop. If low hour > top hour then, compaction will work overnight between low hour and top hour (inclusive).  Timings rely on server's view of local time</td>
<td><code>23</code></td>
</tr>

<tr>
<td><code>leveled.max_run_length</code></td>
<td>In a single compaction run, what is the maximum number of consecutive files which may be compacted.</td>
<td><code>4</code></td>
</tr>

<tr>
<td><code>leveled_reload_recalc</code></td>
<td>Enable the `recalc` compaction strategy within the leveled backend in riak.</td>
<td><code>disabled</code></td>
</tr>
</table>

## Bitcask

Configurable parameters for Riak's [Bitcask][plan backend bitcask] storage backend.

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>bitcask.data_root</code></td>
<td>The directory under which Bitcask will store its data.</td>
<td><code>./data/bitcask</code></td>
</tr>

<tr>
<td><code>bitcask.io_mode</code></td>
<td>Configure how Bitcask writes data to disk. If set to
<code>erlang</code>, writes are made via Erlang's built-in file API; if
set to <code>nif</code>, writes are made via direct calls to the POSIX C
API. The <code>nif</code> mode provides higher throughput for certain
workloads, but has the potential to negatively impact the Erlang VM,
leading to higher worst-case latencies and possible throughput collapse
</td>
<td><code>erlang</code></td>
</tr>

<tr>
<td><code>bitcask.expiry</code></td>
<td>By default, Bitcask keeps all of your data around. If your data has
limited time value, or if you need to purge data for space reasons, you
can set the <code>expiry</code> option. For example, if you need to
purge data automatically after 1 day, set the value to <code>1d</code>.
<code>off</code> disables automatic expiration</td>
<td><code>off</code></td>
</tr>

<tr>
<td><code>bitcask.expiry.grace_time</code></td>
<td>By default, Bitcask will trigger a merge whenever a data file
contains an expired key. This may result in excessive merging under some
usage patterns. To prevent this you can set the
<code>bitcask.expiry.grace_time</code> option. Bitcask will defer
triggering a merge solely for key expiry by the configured number of
seconds. Setting this to <code>1h</code> effectively limits each cask to
merging for expiry once per hour.</td>
<td><code>0</code></td>
</tr>

<tr>
<td><code>bitcask.hintfile_checksums</code></td>
<td>Whether to allow the CRC to be present at the end of hintfiles.
Setting this to <code>allow_missing</code> runs Bitcask in a
backwards-compatible mode in which old hint files will still be accepted
without CRC signatures.</td>
<td><code>strict</code></td>
</tr>

<tr>
<td><code>bitcask.fold.max_puts</code></td>
<td>See the description for the <code>bitcask.fold.max_age</code>
config directly below.</td>
<td><code>0</code></td>
</tr>

<tr>
<td><code>bitcask.fold.max_age</code></td>
<td>Fold keys thresholds will reuse the keydir if another fold was
started less than <code>fold.max_age</code> ago and there were fewer
than <code>fold.max_puts</code> updates. Otherwise, it will wait until
all current fold keys complete and then start. Set either option to
<code>unlimited</code> to disable.</td>
<td><code>unlimited</code></td>
</tr>

<tr>
<td><code>bitcask.merge.thresholds.fragmentation</code></td>
<td>Describes which ratio of dead keys to total keys in a file will
cause it to be included in the merge. The value of this setting is a
percentage from 0 to 100. For example, if a data file contains 4 dead
keys and 6 live keys, it will be included in the merge at the default
ratio (which is 40). Increasing the value will cause fewer files to be
merged, decreasing the value will cause more files to be merged.</td>
<td><code>40</code></td>
</tr>

<tr>
<td><code>bitcask.merge.thresholds.dead_bytes</code></td>
<td>Describes the minimum amount of data occupied by dead keys in a file
to cause it to be included in the merge. Increasing the value will cause
fewer files to be merged, whereas decreasing the value will cause more
files to be merged.</td>
<td><code>128MB</code></td>
</tr>

<tr>
<td><code>bitcask.merge.thresholds.small_file</code></td>
<td>Describes the minimum size a file must have to be excluded from the
merge. Files smaller than the threshold will be included. Increasing
the value will cause more files to be merged, whereas decreasing the
value will cause fewer files to be merged.</td>
<td><code>10MB</code></td>
</tr>

<tr>
<td><code>bitcask.merge.triggers.dead_bytes</code></td>
<td>Describes how much data stored for dead keys in a single file will
trigger merging. If a file meets or exceeds the trigger value for dead
bytes, merge will be triggered. Increasing the value will cause merging
to occur less often, whereas decreasing the value will cause merging to
happen more often. When either of these constraints are met by any file
in the directory, Bitcask will attempt to merge files.</td>
<td><code>512MB</code></td>
</tr>

<tr>
<td><code>bitcask.merge.triggers.fragmentation</code></td>
<td>Describes which ratio of dead keys to total keys in a file will
trigger merging. The value of this setting is a percentage from 0 to
100. For example, if a data file contains 6 dead keys and 4 live keys,
then merge will be triggered at the default setting. Increasing this
value will cause merging to occur less often, whereas decreasing the
value will cause merging to happen more often.</td>
<td><code>60</code></td>
</tr>

<tr>
<td><code>bitcask.merge.window.end</code></td>
<td>See the description of the <code>bitcask.merge.policy</code> config
below.</td>
<td><code>23</code></td>
</tr>

<tr>
<td><code>bitcask.merge.window.start</code></td>
<td>See the description of the <code>bitcask.merge.policy</code> config
below.</td>
<td><code>0</code></td>
</tr>

<tr>
<td><code>bitcask.merge.policy</code></td>
<td>Lets you specify when during the day merge operations are allowed to
be triggered. Valid options are: <code>always</code>, meaning no
restrictions; <code>never</code>, meaning that merging will never be
attempted; and <code>window</code>, specifying the hours during which
merging is permitted, where <code>bitcask.merge.window.start</code> and
<code>bitcask.merge.window.end</code> are integers between 0 and 23. If
merging has a significant impact on performance of your cluster, or your
cluster has quiet periods in which little storage activity occurs, you
may want to change this setting from the default.</td>
<td><code>always</code></td>
</tr>

<tr>
<td><code>bitcask.merge_check_interval</code></td>
<td>Bitcask periodically runs checks to determine whether merges are
necessary. This parameter determines how often those checks take place.
Expressed as a time unit, e.g. `10s` for 10 seconds, `5m` for 5 minutes,
etc.</td>
<td><code>3m</code></td>
</tr>

<tr>
<td><code>bitcask.merge_check_jitter</code></td>
<td>In order to prevent merge operations from taking place on different
nodes at the same time, Riak can apply random variance to merge times,
expressed as a percentage of <code>bitcask.merge_check_interval</code>.
</td>
<td><code>30%</code></td>
</tr>

<tr>
<td><code>bitcask.max_merge_size</code></td>
<td>Maximum amount of data to merge in one go in the Bitcask backend.
</td>
<td><code>100GB</code></td>
</tr>

<tr>
<td><code>bitcask.max_file_size</code></td>
<td>Describes the maximum permitted size for any single data file in the
Bitcask directory. If a write causes the current file to exceed this
size threshold then that file is closed, and a new file is opened for
writes.</td>
<td><code>2GB</code></td>
</tr>

<tr>
<td><code>bitcask.sync.interval</code></td>
<td>See the description of the <code>bitcask.sync.strategy</code>
directly below.</td>
<td></td>
</tr>

<tr>
<td><code>bitcask.sync.strategy</code></td>
<td>Changes the durability of writes by specifying when to synchronize
data to disk. The default setting protects against data loss in the
event of application failure (process death) but leaves open a small
window in which data could be lost in the event of complete system
failure (e.g. hardware, OS, or power). The default mode,
<code>none</code>, writes data into operating system buffers which will
be written to the disks when those buffers are flushed by the operating
system. If the system fails, e.g. due power loss or crash, that data is
lost before those buffers are flushed to stable storage.  This is
prevented by the setting <code>o_sync</code>, which forces the operating
system to flush to stable storage at every write. The effect of flushing
each write is better durability, however write throughput will suffer as
each write will have to wait for the write to complete.  Available sync
strategies: <code>none</code>, which will let the operating system
manage syncing writes; <code>o_sync</code>, which will uses the
<code>O_SYNC</code> flag to force syncs on every write; and
<code>interval</code>, by which will force Bitcask to sync every
<code>bitcask.sync.interval</code> seconds.</td>
<td><code>none</code></td>
</tr>

<tr>
<td><code>bitcask.open_timeout</code></td>
<td>Specifies the maximum time Bitcask will block on startup while
attempting to create or open the data directory. You generally need not
change this value. If for some reason the timeout is exceeded on open
you'll see a log message of the form <code>Failed to start bitcask
backend: .... </code>. Only then should you consider a longer timeout.
</td>
<td><code>4s</code></td>
</tr>

</tbody>
</table>

## Memory Backend

Configurable parameters for Riak's [Memory][plan backend memory] backend.

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>memory_backend.ttl</code></td>
<td>Each value written will be written with this "time to live." Once
that object's time is up, it will be deleted on the next read of its
key. Minimum: <code>1s</code>.</td>
<td></td>
</tr>

<tr>
<td><code>memory_backend.max_memory_per_vnode</code></td>
<td>The maximum amount of memory consumed per vnode by the memory
storage backend. Minimum: <code>1MB</code>.</td>
<td></td>
</tr>

</tbody>
</table>

## Multi Backend

Configurable parameters for Riak's [Multi][plan backend multi] backend, which enables you to utilize multiple data backends in a single Riak cluster.

If you are using multiple backends, you can configure the backends
individually by prepending the setting with `multi_backend.$name`, where
`$name` is the name of the backend. `$name` can be any valid
configuration word, like `customer_data`, `my_data`, `foo_bar_backend`,
etc.

Below is the general form for setting multi-backend parameters:

```riakconf
multi_backend.$name.(existing_setting) = <setting>
# or
multi_backend.$name.$backend_type.(backend_specific_setting) = <setting>
```

Below is a listing of the available parameters:

<table class="riak-conf">
<thead>
<tr>
<th>Config</th>
<th>Description</th>
<th>Default</th>
</tr>
</thead>
<tbody>

<tr>
<td><code>multi_backend.$name.storage_backend</code></td>
<td>This parameter specifies the Erlang module defining the storage
mechanism that will be used on this node.</td>
<td><code>bitcask</code></td>
</tr>

<tr>
<td><code>multi_backend.default</code></td>
<td>The default name of a backend when one is not specified.</td>
<td></td>
</tr>

</tbody>
</table>

To give an example, if you have a LevelDB backend named
`customer_backend` and wish to set the `data_root` parameter to
`$(platform_data_dir)/leveldb_backends/customer_backend/`, you would
do so as follows:

```riakconf
multi_backend.customer_backend.storage_backend = leveldb
multi_backend.customer_backend.leveldb.data_root = $(platform_data_dir)/leveldb_backends/customer_backend
multi_backend.customer_backend.leveldb.maximum_memory.percent = 50
```

