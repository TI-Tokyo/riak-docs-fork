---
title: "Riak and Hadoop (Sitting in a tree)"
description: "A whistle stop tour of a new integration between Riak and Hadoop MapReduce."
project: community
lastmod: 2015-05-28T19:24:13+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Russell Brown"
pub_date: 2011-11-29T00:00:00+00:00
---
November 29, 2011
It has been pointed out on occasion that Riak MapReduce isn’t real “MapReduce”, often with reference to Hadoop, which is. There are many times that Riak’s data processing pipeline is exactly what you want, but in case it isn’t, and you want to leverage existing Hadoop expertise and investment, you may now use Riak as an input/output, instead of HDFS.
This started off as a tinkering project, and it is currently released as riak-hadoop-0.2. I wouldn’t recommend it for production use today, but it is ready for exploratory work, whilst we work on some more serious integration for the future.
Input
Hadoop M/R usually gets its input from HDFS, and writes its results to HDFS. Riak-hadoop is a library that extends Hadoop’s InputFormat and OutputFormat classes so that a Riak cluster can stand in for HDFS in a Hadoop M/R job. The way this works is pretty simple. When defining your Hadoop job, you declare the InputFormat to be of type RiakInputFormat. You configure you cluster members and locations using the JobConf, and a helper class called RiakConfig. Your Mapper class must also extend RiakMapper, since there are some requirements for handling eventual consistency that you must satisfy. Apart from that, you code your Map method as if for a typical Hadoop M/R job.
Keys, for the splits
When Hadoop creates a Mapper task it assigns an InputSplit to that task. An input split is the subset of data that the Mapper will process. In Riak’s case this is a set of keys. But how do we get the keys to Map over? When you configure your job, you specify a KeyLister. You can use any input to Hadoop M/R that you would use for Riak M/R: provide a list of bucket/key pairs, a 2i query, a Riak Search query, or, ill advisedly, a bucket. The KeyLister will fetch the keys for the job and partition them into splits for the Mapper tasks. The Mapper tasks then access the data for the keys using a RiakRecordReader. The record reader is a thin wrapper around a Riak client, it fetches the data for the current key when the Hadoop framework asks.
Output
In order to output reduce results to Riak your Reducer only need implement the standard Reducer interface. When you configure the Job, just specify that you wish to use the RiakOutputFormat, and declare an output bucket as a target for results. The keys/values from your reduce will then be written to Riak as regular Riak objects. You can even specify secondary indexes, Riak metadata and Riak links on your output values, thanks to the Riak Java Client’s annotations and object mapping (courtesy of Jackson’s object mapper.)
Hybrid
Of course you don’t need to use Riak for both input and output. You could read from HDFS, process and store results in Riak, or read from Riak and store results in HDFS.
Why do this?
This is really a proof of concept integration. It should be of immediate use to anyone who already has Hadoop knowledge and a Hadoop cluster. If you’re a Riak user with no Hadoop requirements right now, I’d say, don’t go there at once: setting up a Hadoop cluster is way more complex than running Riak, and maintaining it is, operationally, taxing. If, however, you already have Hadoop, adding Riak as a data source and sink is incredibly easy, and gives you a great, scalable, live database for serving reads and taking writes, and you can leverage your existing Hadoop investment to aggregate that data.
What next?
The thinking reader might be saying “Huh? You stream the data in and out over the network, piecemeal?”. Yes, we do. Ideally we’d do a bulk, incremental replication between Riak and Hadoop (and back) and that is the plan for the next phase of work.
Summary
Riak-Hadoop enables Hadoop users to use a Riak cluster as a source and sink for Hadoop M/R jobs. This exposes he entire Hadoop toolset to Riak data (including the query languages like Hive and Pig!) This is only a first phase pass at the integration problem, and though usable today, smarter sync is coming.
Please clone, build, and play with this project. Have at it. There’s a follow up post with a look at an example Word Count Hadoop Map/Reduce job coming soon. If you can’t wait, just add a dependency on riak-hadoop, version 0.2 to your pom.xml and get started. Let me know how you get on, via the Riak mailing list.
Russell
