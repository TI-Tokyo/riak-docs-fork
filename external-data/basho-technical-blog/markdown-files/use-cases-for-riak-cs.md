---
title: "Use Cases for Riak CS"
description: "Common use cases for open sourced Riak CS."
project: community
lastmod: 2015-05-28T19:23:40+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-04-03T10:00:32+00:00
---
April 3, 2013
As you might have heard, we recently open sourced Riak CS, cloud storage built on Riak. You can find all of the code on our GitHub account and download Riak CS here. To help you get started with Riak CS, here are some common use cases.

Large Object Storage For Applications and Services: Riak CS is built for storing large objects of all types. It is content agnostic so you can store images, text, video, documents, database backups, software binaries, or other data types. Riak CS can store objects into the terabyte size range using the new multipart upload feature. When an object is uploaded, Riak CS breaks it into smaller blocks that are streamed, stored, replicated in the underlying Riak cluster.
On-Demand Internal Storage Capacity: Riak CS provides highly available storage for internal business units. Built on Riak, Riak CS has a masterless, redundant design that ensures availability and fault-tolerance. Use cases might include document storage or backing for internal applications.
Storage Layer for Public Clouds/Cloud Services: Riak CS’ flexibility and scalability provide the ideal foundation for building public clouds or cloud services. Capacity can be added by installing Riak CS on a new physical node and joining it with the cluster. Riak automatically redistributes data and ownership so all nodes have equal responsibility, which prevents storage hot spots and decreases the operational burden of adding new nodes. Additionally, Riak CS is multi-tenant, a requirement of most public cloud services today.
Amazon S3 Compatibility: Riak CS is S3-compatible, making it easy for your developers to be productive quickly. Riak CS can be used with existing S3 clients and libraries. The HTTP REST API supports service, bucket, and object-level operations to easily store and retrieve data. Riak CS makes sense for companies that are trying to provide internal, S3-like services or using a hybrid approach with some public and some private cloud storage.
Disaster Recovery and Active Backups: Riak CS Enterprise extends Riak CS with multi-datacenter replication. By replicating data across datacenters using either real-time or full-sync, you can maintain redundant storage in case of disaster scenarios. Multi-datacenter replication can also be used to maintain active backups or create availability zones.

For more information about Riak CS, visit our site and download the technical overview.
Riak
