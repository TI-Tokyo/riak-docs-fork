---
title: "Booking.com: Using Riak as Events Storage - Part 4"
description: "Booking.com is the world’s leading online accommodation provider, operating across 220+ countries in 43 languages. They process billions of events per day, streaming at more than 100MB per second, and adding more than 6 TB of data per day. These events are schema-less and difficult for standard anal"
project: community
lastmod: 2017-02-07T12:35:40+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Dorothy Pults"
pub_date: 2017-02-07T12:35:40+00:00
---
Booking.com is the world’s leading online accommodation provider, operating across 220+ countries in 43 languages. They process billions of events per day, streaming at more than 100MB per second, and adding more than 6 TB of data per day. These events are schema-less and difficult for standard analytics tools to handle.
Two engineers at Booking.com, Damien Krotkine and Ivan Poponov, have published a 4 part blog series entitled “Using Riak as Events Storage”.  In this blog series, they describe the Booking.com data pipeline and go into detail on their events, data flow, data design, real-time events analysis, and optimizing data transformation. Part 4 in the series was recently published and explains how to use post-commit hooks to apply transformations to event data in Riak without using MapReduce jobs. We highly recommend that you start with Part 1 and read the blog posts sequentially. Each blog post builds on information in the previous post.
Using Riak as Events Storage – Part 1: explains how Booking.com collects and stores events from its back-end into central storage, and why they chose Riak for events storage.
Using Riak as Events Storage – Part 2: explains how Booking.com pushes data to Riak, how they read it, and how they perform real-time data processing to do event analysis.
Using Riak as Events Storage – Part 3: explains how Booking.com applies transformations to event data stored in Riak without the data leaving the cluster.
Using Riak as Events Storage – Part 4: explains how Booking.com uses post-commit hooks to apply transformation to event data stored in Riak.
Other Resources:
Riak KV Technical Overview
Riak TS Technical Overview
Riak Documentation
Contact Us for a techtalk
Dorothy Pults
@deepults
