---
title: "Riak Control"
description: "Riak Control is Riak's new OSS, REST-driven, user-interface for their NoSQL database 'Riak'. You can find more information on setting up RIak Control on the Riak Wiki."
project: community
lastmod: 2016-10-20T07:38:56+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Jeff Massung"
pub_date: 2012-02-22T00:00:00+00:00
---
February 22, 2012
Riak Control is Riak’s new OSS, REST-driven, user-interface for Riak. The code has been available for a few months now, but it’s officially supported in Riak 1.1, so we wanted share some details on what it’s about and why you should be excited about it.
Lowering the Barrier for Entry
Once a Riak cluster is up and running we want it to be as hands-free to administer as possible. Things should “just work,” like plumbing. But we’ll be the first to admit that a new user’s initial welcoming with Riak isn’t always as pleasant as it should be.
Some steps are unavoidable: downloading, installing, and/or building from source, etc. But once the initial work is done, the experience should be as inviting as possible. Riak is a very powerful database with numerous options and commands. Riak Control allows you to easily manage/inspect your cluster while ignoring many of these until needed.
Empowering Riak Administrators
When we first sat down to decide what the more important features for any Riak interface should be, one theme stood out above all the others: cluster management. We wanted to give developers and administrators the ability to quickly build a cluster, inspect nodes, and diagnose the health of their cluster. And we wanted it to happen fast.
Riak is about large datasets and clusters replicating that dataset for maximum availability and persistence. We’re working hard to help companies that write many, many GBs per day to clusters containing 50+ nodes. Riak Control is a tool that brings issues and risks front-and-center. And it gives customers the ability to take action in real-time.
The Two-Minute Tour
Riak Control is currently broken up into nested levels of detail. Each page in Riak Control is designed to give you just as much information as you need, nothing more. As you navigate the UI, you’ll gradually be taken deeper into the rabbit hole.
The Overview/Snapshot

The Snapshot is what you’ll see when you first fire up Riak Control. It should give you a warm-fuzzy feeling when everything is A-okay: an unmistakable, beautiful green check mark.
For times when things aren’t perfect, you will be presented with a list of concern areas. Each will have links to other pages of Riak Control where you can take a closer look at the problem.
The Cluster

The cluster page is where you can get a quick look at all of the nodes in your cluster and manage membership.
With a glance you can see which nodes are partitioned from the rest of the cluster or offline, which are leaving or joining the cluster, view partition ownership, monitor memory, and more. And with a click you can add nodes to the cluster, take nodes offline for maintenance, and leave the cluster.
The Ring

One level deeper than the cluster view is the ring page. This is where you can see the health of each partition. Most of the time, your ring will be too large to really manage from the ring view. But with the filters you can immediately find which partitions are owned by which nodes, partitions whose primary nodes are unreachable, current handoffs, and more.
What’s Next?
Riak Control is not standing still. Riak 1.1 includes Riak Control in its early stages so we can begin to gather feedback. We want to know what it does right and what it does wrong. Your feedback and ideas are encouraged. Additionally, we have a list of features and functionality slated for future releases. None of these are set in stone, but here is a list of what we have planned…
Pluggable
While Riak Control is – at its heart – a simple REST API, we’re working to modularize it in a way that allows you to write your own modules/plugins. We want to see Riak Control become a collection of pieces that all snap and work together, empowering you to manage your cluster in the way that best fits your needs.
Event streaming
Currently Riak Control uses a pull model to gather information about the cluster. While this isn’t a performance issue, we very much want to make it a push-system. As things happen to the cluster, the cluster should notify Riak Control of the changes, which in-turn will notify the user.
Node Statistics
Clicking on a node name from anywhere should take you to a page giving details specifically about that node, similar to the data you would get from a riak-admin status command.
Bucket & Object Inspection
While low-level object manipulation isn’t designed to be a primary feature of Riak Control, it is a very handy tool to have, and extremely valuable when initially setting up Riak for the first time. More importantly, Riak buckets will be available to create and inspect.
MapReduce Queries
Riak Control will feature a powerful interface for creating MapReduce queries. You will be able to debug, save, load, and execute previously saved queries with ease.
Customer Support Tools
In addition to the general tools provided for manipulation of the cluster and data, we also are planning for improve monitoring tools.

View the log files of individual nodes
See graphs of load, memory latency, disk usage, etc.
Coalesce and bundle data for support tickets
File support tickets

Any Comments, Questions, or Feature Requests?
Anything you’d like to share or ask? Join the Riak-Users Mailing List and tell us what you think. The other option is to fork the code and make your opinions known with a pull request or by filing an issue. You can also find some formal documentation on the Riak Wiki.
Thanks for being a part of Riak.
Jeff Massung
