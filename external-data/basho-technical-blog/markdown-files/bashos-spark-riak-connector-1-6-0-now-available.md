---
title: "Riak’s Spark-Riak Connector 1.6.0 Now Available"
description: "Riak’s Engineering team is proud to announce that version 1.6.0 of the Spark-Riak connector has been released. Over the past several months we have added new features, upgraded existing features, and fixed bugs to enable our customers to take full advantage of the combined power of Riak and Apache"
project: community
lastmod: 2016-10-19T16:04:55+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Korrigan Clark"
pub_date: 2016-09-07T16:14:00+00:00
---
Riak’s Engineering team is proud to announce that version 1.6.0 of the Spark-Riak connector has been released. Over the past several months we have added new features, upgraded existing features, and fixed bugs to enable our customers to take full advantage of the combined power of Riak and Apache Spark.
We’ve listened to our Community and included several key features:

Python support for Riak KV buckets
Support for Spark Streaming
Failover support for when a Riak node is unavailable during a Spark job

We also added performance and testing enhancements, upgraded example applications and added documentation. A full list of changes can be found in the CHANGELOG. All new features will work with TS tables and KV buckets in Riak TS 1.3.1+, and support for Riak KV will come with the release of Riak KV 2.3.[updated October 19, 2016]
Let’s take a peek at one of the more exciting features of this release: Python support for Riak KV buckets. For the purposes of this quick demonstration, [we will assume there is a Riak TS 1.4 node at 127.0.0.1:8087] and that the code will be run in a Jupyter notebook.
Before we can use Spark, we must set up a Spark context using the Spark-Riak connector:
import findspark
findspark.init()
import pyspark

import pyspark\_riak
import os

os.environ['PYSPARK\_SUBMIT\_ARGS'] = "--packages com.basho.riak:spark-riak-connector:1.6.0 pyspark-shell"
conf = pyspark.SparkConf().setAppName("My Spark Riak App")
conf.set("spark.riak.connection.host", "127.0.0.1:8087")
sc = pyspark.SparkContext(conf)

pyspark\_riak.riak\_context(sc)

Now that the Spark context has been properly initialized for use with Riak, let’s write some data to a KV bucket named ‘kv\_sample\_bucket’:
sample\_data = [ {'key0': {'data': 0}}, {'key1': {'data': 1}}, {'key2': {'data': 2}} ]

kv\_write\_rdd = sc.parallelize(sample\_data)

kv\_write\_rdd.saveToRiak(‘kv\_sample\_bucket’)

Now that Spark has written our data in parallel to a KV bucket, let’s pull that data out with a full bucket read:
kv\_read\_rdd = sc.riakBucket(‘kv\_sample\_bucket’).queryAll()

print(kv\_read\_rdd.collect()) # prints  [ {'key0': {'data': 0}}, {'key1': {'data': 1}}, {'key2': {'data': 2}} ]

There are several other ways to read data from a KV bucket including simple key queries, 2i tag queries, and 2i range queries. Additionally, with 2i range queries, custom partitioning and parallelization can be used to increase read performance.  More information on python support for the Spark-Riak connector can be found in the docs.
To get started using the new version of the Spark-Riak connector, we encourage you to visit the github repository and start playing around with all the new features.
Korrigan Clark
@kralCnagirroK
