---
title: "A Deeper Look At Riak's MapReduce Enhancements"
description: "We officially released Riak 0.14 yesterday. Some of the biggest enhancements were in and around Riak's MapReduce functionality. Here's a more in-depth look at what you can look forward to in 0.14 if you're into Mapping and Reducing."
project: community
lastmod: 2016-09-14T15:07:53+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2010-01-06T06:38:29+00:00
---
January 6, 2010
We officially released Riak 0.14 yesterday. Some of the biggest enhancements were in and around Riak’s MapReduce functionality. Here’s a more in-depth look at what you can look forward to in 0.14 if you’re into Mapping and Reducing.
Key Filtering
Performing any type of sophisticated query on a strictly key/value store is notoriously hard. Past releases of Riak were limited to MapReduce-ing over either entire buckets or a discrete set of user-supplied inputs. The problem with these approaches is neither facilitated the kind of robust querying many applications required. For example, let’s examine an application which logs application events to a Riak bucket. Each entry is a JSON hash containing a timestamp, the user generating the event, and some information about the event. Part of our example application requires querying these log entries based on timestamp and user.
In current releases of Riak, the application would have to map over the entire bucket and examine each entry to find the relevant set. This type of query is usable when the bucket is small. But when the bucket gets bigger these types of queries begin to exhibit performance problems. Scanning the bucket and loading objects from disk only to discard them is an expensive proposition.
This is exactly the use case where key filtering can help. If the application can store meaningful data in the keys then key filtering can query just the keys and load only the objects whose keys pass the filter to be processed by the MapReduce job. For our example app we could combine the timestamp and user id to form each entry’s key like this: “1292943572.pjohnson”. Using key filters we can locate all the user entries for “pjohnson” between “12/1/2010” and “12/7/2010 “and count them via MapReduce:
“`javascript
{“inputs”: {
“bucket”: “user\_logs”,
“key\_filters”: [[“and”, [[“tokenize”, “.”, 1],
[“string\_to\_int”],
[“between”, 1291161600, 1291852799]],
[[“tokenize”, “.”, 2],
[“matches”, “pjohnson”]]]]},
“query”: [{“map”:{
“language”: “javascript”,
“source”: “function(obj) { return [1]; }”}},
“reduce”:{
“language”: “javascript”,
“name”: “Riak.reduceSum”}]}
“`
Key filtering will support boolean operators (and, or, not), url decoding, string tokenizing, regular expressions, and various string to numeric conversions. Client support will initially be limited to the Java and Ruby clients (and Python support is already being attacked by the community). More clients will be added in subsequent releases.
Next Steps

We’ve added extensive documentation to the Riak Wiki that is all about Key Filters.
If reading source code is your thing, you can start here.

MapReduce Query Planner
One of the biggest obstacles to improving Riak’s MapReduce performance was the way map functions were scheduled around the cluster. The original implementation was fairly naive and scheduled map functions around the vnodes in the order listed in the replica list for each bucket/key pair. This approach resulted in vnode hotspots, especially in smaller clusters, as many bucket/key pairs would hash to the same vnode. We also sent each bucket/key pair to be mapped in a separate Erlang message which reduced throughput on larger jobs as they wound up generating significant messaging traffic.
The new planner addresses many of these problems. Each batch of 50 bucket/key pairs are analyzed and scheduled around the cluster to maximize vnode coverage. In other words, the planner schedules many bucket/key pairs onto a common vnode in a single message. This reduces the chattiness of jobs overall and also improves throughput as the underlying map dispatch code can operate in batches rather than single values.
Segregated Javascript VM Pools
Contention for Javascript VMs in a busy cluster can be a significant source of performance problems. The contention is caused by each cluster node having a single pool of Javascript VMs for all Javascript calls: map functions, reduce functions, and pre-commit hooks.
0.14 supports three separate pools of Javascript VMs to reduce overall contention. By tweaking a few lines of code in your app.config file, users will be able to tailor the size of each pool to their particular needs. Does your app use MapReduce and ignore hooks? Turn the hook pool size down to zero and save yourself some CPU and memory. Do you always submit MapReduce jobs to a particular node in the cluster? You can bump up the reduce pool size on the node receiving the jobs while setting it to zero on the other nodes. This uses the fact that reduce phases aren’t distributed to use resources where they are most needed in the cluster.
As you can see, we’ve put a lot of work into refining MapReduce in the latest release, and we’re dedicated to continuing this work in upcoming releases. If you want to get your hands dirty with MapReduce right now, check out:

Loading Data and Running MaReduce Queries on the Riak Fast Track
Riak Function Contrib, a library of functions built by Riak and the Riak Community
Extensive MapReduce Documentation on The Riak Wiki

Enjoy!
The Riak Team
