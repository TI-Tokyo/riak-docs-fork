---
title: "LevelDB in Riak 1.2"
description: "In Riak 1.2, we made a host of improvements to eleveldb, Riak's implementation of Google's levelDB backend. In this blog we summarize these changes and provide benchmark data illustrating performance improvements."
project: community
lastmod: 2015-05-28T19:24:10+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Seth Benton"
pub_date: 2012-10-30T00:00:00+00:00
---
October 30, 2012
Google’s LevelDB has proven very versatile within Riak — LevelDB is implemented in Riak as eleveldb, an Erlang wrapper around levelDB. But Google’s target environment was much different than the large data environment of Riak’s users. Riak 1.2 contains the first wave of performance tuning for large data. These changes improve overall throughput and eliminate most instances where levelDB would hang for a few seconds trying to catch up. The new release also contains a fix for an infinite loop compaction condition, a bloom filter that greatly reduces time searching for non-existent keys, and several bug fixes. This blog details these improvements and also gives some internal benchmark results obtained using basho\_bench, Riak’s open source benchmarking tool.
What’s New?

Stalls: In Riak 1.1, individual vnodes in Riak (one levelDB database) could have long pauses before responding to individual get / put calls. Several stall sources were identified and corrected. On a test server, LevelDB in 1.1 saw stalls of 10 to 90 seconds every 3 to 5 minutes. In Riak 1.2, levelDB sometimes sees one stall every 2 hours for 10 to 30 seconds.
Throughput: While impacted by stalls, throughput is an independent code and tuning issue. The fundamental change made was to increase all on-disk file sizes to minimize the number of file opens and reduce the number of compactions. LevelDB in Riak 1.1 had a throughput of ~400 operations per second on a given server. These changes raised throughput to ~2,000 operations per second.
Infinite loop during compaction: In 1.1, the background compaction would get caught in an infinite loop if it encountered a file with a corrupt data block. The previous solution was to stop the node, manually execute “recovery”, then restart the node. The entire file (and all its data) was removed from the data store and copied to the “lost” directory during the recovery. Riak 1.2 creates one file, BLOCKS.bad, in the “lost” directory. The levelDB code then automatically removes the corrupted block from compaction processing and copies it to this file. The compaction then continues processing the remaining data in the file (and moves along without going into an infinite loop).
Merge of levelDB bloom filter code: Google has created a bloom filter to help levelDB more quickly identify keys that do not exist in the data store. The bloom filter code is merged into this release. While incrementally beneficial in its own right, the bloom filter enables changes to the file / level structure which dramatically improves overall throughput.
app.config eleveldb options: in Riak 1.1, most parameters set in app.config for the levelDB layer were never passed. This is corrected. Users should assume previous parameter tests / experiments to be invalid.

Benchmarks
The graphs below illustrate levelDB’s improvements in throughput and maximum latency. Test data was obtained using basho\_bench, Riak’s open source benchmarking tool. Raw data and configuration files can be downloaded here. In the benchmark presented, levelDB preloads a database with 10 million sequentially ordered keys.
As can be seen, levelDB 1.1 stalls regularly, whereas 1.2 seldom stalls due to stall management improvements. We can also see that levelDB in 1.2 has a higher ingest rate (we were able to input 10 million records in 44 minutes compared to 106 minutes in 1.1)
Throughput in levelDB 1.1

Throughput in levelDB 1.2

Maximum latency in levelDB 1.1

Maximum latency in levelDB 1.2

Roadmap
We have already identified further performance tuning for future work, including bloom filter modification and removing redundancy (bloat) during memory to level-0 file creation. Expect another wave of performance tuning in subsequent point releases and major releases.

Data backup: Theoretically there is no need to perform data backup on levelDB since Riak duplicates all data across several nodes. But many users have suggested they would still sleep better if there was a means to perform a direct backup by node/vnode anyway. Backups during live operation are a planned, next feature.
Infinite loops: Riak 1.2 contains fixes for a couple of the most common cases where compactions could enter infinite loops when the state of files on the disk does not match that of LevelDB’s internal state. However, there are still other, less frequent cases that can still cause infinite loops. These less frequent cases are high on the future work list.
Error correction: LevelDB has methods to repair and restore damaged vnodes. The time cost of executing a repair can be huge. The repair time is already better with release 1.2 (in one case the time was reduced from 6 weeks … really … to eleven hours). We already have a design waiting for programming resources that will further speed repair time.

Seth Benton and Matthew Von-Maszewski
