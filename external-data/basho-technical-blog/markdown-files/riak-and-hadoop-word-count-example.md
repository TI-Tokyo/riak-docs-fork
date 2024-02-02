---
title: "Riak and Hadoop Word Count Example"
description: "An example Riak-Hadoop map/reduce job."
project: community
lastmod: 2015-05-28T19:24:13+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Russell Brown"
pub_date: 2011-12-01T00:00:00+00:00
---
December 1, 2011
My last post was a brief description of the integration between Riak and Hadoop MapReduce. This, the follow up post, will be a bit more hands on and walk you through an example riak-hadoop MapReduce job. If you want to have a go, you need to follow these steps. I won’t cover all the details of installing everything, but I’ll point you at resources to help.
Getting Set Up
First you need both a Riak and Hadoop install. I went with a local devrel Riak cluster and a local Hadoop install in pseudo distributed mode. (A singleton node of both Hadoop and Riak installed from a package will do, if you’re pushed for time.)
The example project makes use of Riak’s Secondary Indexes (“2i”) so you will need to switch the backend on Riak to use LevelDB. (NOTE: This is not a requirement for riak-hadoop, but this demo uses 2i, which does require it). So, for each Riak node, change the storage backend from bitcask to eleveldb by editing the app.config.
riak\_kv, [
%% Storage\_backend specifies the Erlang module defining the storage
%% mechanism that will be used on this node.
{storage\_backend, riak\_kv\_eleveldb\_backend},
%% …and the rest
]},
Now start up your Riak cluster, and start up Hadoop:
for d in dev/dev\*; do $d/bin/riak start; done
cd $HADOOP\_HOME
bin/start-dfs.sh
bin/start-mapred.sh
Next you need to pull the riak-hadoop-wordcount sample project from GitHub. Checkout the project and build it:
git clone https://github.com/russelldb/riak-hadoop-wordcount
cd riak-hadoop-wordcount
mvn clean install
A Bit About Dependencies…
Warning: Hadoop has some class loading issues. There is no class namespace isolation. The Riak Java Client depends on Jackson for JSON handling, and so does Hadoop, but different versions (naturally). When the Riak-Hadoop driver is finished it will come with a custom classloader. Until then, however, you’ll need to replace your Hadoop lib/jackson\*.jar libraries with the ones in the lib folder of this repo on yourJobTracker / Namenode only. On your tasknodes, you need only remove the Jackson jars from your hadoop/lib directory, since the classes in the job jar are at least loaded. (There is an open bug about this in Hadoop’s JIRA. It has been open for 18 months, so I doubt it will be fixed anytime soon. I’m very sorry about this. I will address it in the next version of the driver.)
Loading Data
The repo includes a copy of Mark Twain’s Adventures Of Huckleberry Finn from Project Gutenberg, and a class that chunks the book into chapters and loads the chapters into Riak.
To load the data run the Bootstrap class. The easiest way is to have maven do it for you:
mvn exec:java -Dexec.mainClass=”com.basho.riak.hadoop.Bootstrap”
-Dexec.classpathScope=runtime
This will load the data (with an index on the Author field.) Have a look at the Chapter class if you’d like to see how easy it is to store a model instance in Riak.
Bootstrap assumes that you are running a local devrel cluster. If your Riak install isn’t listening on the PB interface on 127.0.0.1 on port 8081 then you can specify the transport and address like this:
mvn exec:java -Dexec.mainClass=”com.basho.riak.hadoop.Bootstrap”
-Dexec.classpathScope=runtime -Dexec.args=”[pb|http PB\_HOST:PB\_PORT|HTTP\_URL]”
That should load one item per chapter into a bucket called wordcount. You can check it succeeded by running (being sure to adjust the url based on your configuration):
curl http://127.0.0.1:8091/riak/wordcount?keys=stream
Package The Job Jar
If you’re running the devrel cluster locally you can just package up the jar now with:
mvn clean package
Otherwise, first edit the RiakLocations in the RiakWordCount class to point at your Riak cluster/node, e.g.
conf = RiakConfig.addLocation(conf, new RiakPBLocation(“127.0.0.1”, 8081));
conf = RiakConfig.addLocation(conf, new RiakPBLocation(“127.0.0.1”, 8082));
Then simply package the jar as before.
Run The job
Now we’re finally ready to run the MapReduce job. Copy the jar from the previous step to your hadoop install directory and kick off the job.
cp target/riak-hadoop-wordcount-1.0-SNAPSHOT-job.jar $HADOOP\_HOME
cd $HADOOP\_HOME
bin/hadoop jar riak-hadoop-wordcount-1.0-SNAPSHOT-job.jar
And wait… Hadoop helpfully provides status messages as it distributes the code and orchestrates the MapReduce execution.
Inspect The Results
If all went well there will be a bucket in your Riak cluster named wordcount\_out. You can confirm it is populated by listing its keys:
curl http://127.0.0.1:8091/riak/wordcount\_out?keys=stream
Since the WordCountResult output class has RiakIndex annotations for both the count and word fields, you can perform 2i queries on your data. For example, this should give you an idea of the most common words in Huckleberry Finn:
curl 127.0.0.1:8091/buckets/wordcount\_out/index/count\_int/1000/3000
Or, if you wanted to know which "f" words Twain was partial too, run the following:
curl 127.0.0.1:8091/buckets/wordcount\_out/index/word\_bin/f/g
Summary
We just performed a full roundtrip MapReduce, starting with data stored in Riak, feeding it to Hadoop for the actual MapReduce processing, and then storing the results back into Riak. It was a trivial task with a small amount of data, but it illustrates the principle and the potential. Have a look at the RiakWordCount class. You can see that only a few lines of configuration and code are needed to perform a Hadoop MapReduce job with Riak data. Hopefully the riak-hadoop-wordcount repo can act as a template for some further exploration. If you have any trouble running this example, please let me know by raising a GitHub issue against the project or by jumping on the Riak Mailing List to ask some questions.
Enjoy.
Russell
