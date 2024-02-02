---
title: "Druid and Riak CS"
description: "Learn how to set up Riak CS as deep storage in Druid."
project: community
lastmod: 2015-05-28T19:23:34+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2014-03-12T13:00:57+00:00
---
March 12, 2014
Druid is an open source analytics platform designed for real-time, exploratory queries on large-scale data sets. Druid is useful for use cases requiring interactive and fast exploration of large amounts of data (10s of billions of events added per day, 10s of TB of data added per day) and always-on availability.
The output of Druid’s indexing process is stored in deep storage. Deep storage provides a durable store for segments that feed the Druid query nodes. As long as Druid nodes can see this storage infrastructure and access the segments stored on it, there will be no data loss no matter how many Druid nodes are lost. For deep storage, Druid supports local mounts, HDFS, and S3 or S3-compatible APIs (like Riak CS). S3-compatible API is the default for deep storage.
For a cost-effective, highly available alternative to S3, Riak CS fits nicely with Druid. Druid has released a guide to walk users through how to use Riak CS as deep storage in Druid. The full guide is available here: https://github.com/metamx/druid/wiki/Stand-Alone-With-Riak-CS
For more information about Riak CS, visit riak.com/riak-cloud-storage/. To get started, download Riak CS.
Riak
