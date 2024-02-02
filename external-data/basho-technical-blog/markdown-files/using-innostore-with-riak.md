---
title: "Using Innostore with Riak"
description: "February 22, 2010 Innostore is an Erlang application that provides an API for storing and retrieving key/value data using the InnoDB storage system. This storage system is the same one used by MySQL for reliable, transactional data storage. It’s a proven, fast system and perfect for use with Riak i"
project: community
lastmod: 2015-05-28T19:24:18+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Dave Smith"
pub_date: 2010-02-22T22:09:26+00:00
---
February 22, 2010
Innostore is an Erlang application that provides an API for storing and retrieving key/value data using the InnoDB storage system. This storage system is the same one used by MySQL for reliable, transactional data storage. It’s a proven, fast system and perfect for use with Riak if you have a large amount of data to store. Let’s take a look at how you can use Innostore as a backend for Riak.
(Note: I assume that you have successfully built an instance of Riak for your platform. If you built Riak from source in ~/riak, then set $RIAK to ~/riak/rel/riak.”)
We first get started by grabbing a stable release of Innostore. You’ll need to download the source for a release from: https://github.com/basho/innostore

Looking in the “Tags & snapshots” section, you should download the source for the highest available RELEASE\_\* tag. In my case, RELEASE\_4 is the most recent release, so I’ll grab the bz2 file associated with it.
Once I have the source code, it’s time to unpack it and build:
 $ tar -xjf innostore-RELEASE\_4.tar.bz2
$ cd innostore
$ make
 
Depending on the speed of the machine you are building on, this may take a few minutes to complete. At the end, you should see a series of unit tests run, with the output ending:
 =======================================================
All 7 tests passed.
100222 7:43:58 InnoDB: Shutdown completed; log sequence number 90283
Cover analysis: /Users/dizzyd/src/public/innostore/.eunit/index.html
Now that we have successfully built Innostore, it’s time to install it into the Riak distribution:
 $ ./rebar install target=$RIAK/lib
If you look in the $RIAK/lib directory now, you should see the innostore-4 directory alongside a bunch of .ez files and other directories which compose the Riak release.
Now, we need to tell Riak to use the Innostore driver as a backend. Make sure Riak is not running. Edit $RIAK/etc/app.config, setting the value for “storage\_backend” as follows:
 {storage\_backend, innostore\_riak},
In addition, append the configuration for the Innostore application after the SASL section:
 {sasl, [ ....
]}, %% < — make sure you add a comma here!!
{innostore, [
{data\_home\_dir, “data/innodb”}, %% Where data files go
{log\_group\_home\_dir, “data/innodb”}, %% Where log files go
{buffer\_pool\_size, 2147483648} %% 2G in-memory buffer in bytes
]}
You may need to adjust the directories for your data\_home\_dir and log\_group\_home\_dirs to match where you want the inno data and log files to be stored. If possible, make sure that the data and log dirs are on separate disks — this can yield much better performance.
Once you’ve completed the changes to $RIAK/etc/app.config, you’re ready to start Riak:
 $ $RIAK/bin/riak console
As it starts up, you should see messages from Inno that end with something like:
100220 16:36:58 InnoDB: highest supported file format is Barracuda.
100220 16:36:58 Embedded InnoDB 1.0.3.5325 started; log sequence number 45764
That’s it! You’re ready to start using Riak for storing truly massive amounts of data.
Enjoy,
Dave Smith
