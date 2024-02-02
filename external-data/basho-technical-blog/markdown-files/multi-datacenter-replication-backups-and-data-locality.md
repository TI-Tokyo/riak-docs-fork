---
title: "Multi-Datacenter Replication: Backups and Data Locality"
description: "A look at some common implementations of multi-datacenter replication, including backups and data locality."
project: community
lastmod: 2015-05-28T19:23:41+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-02-27T10:00:21+00:00
---
February 27, 2013
Multi-datacenter replication is a critical part of modern infrastructure, providing essential business benefits for enterprise applications, platforms and services. Riak Enterprise offers multi-datacenter replication so that data stored in Riak can be replicated to multiple sites. Over the next two posts, we will look at some common implementations, starting with configurations for backups and data locality. For more information on Riak Enterprise architecture and configuration, download the complete whitepaper.
Primary Cluster with Failover
One of the most common architectural patterns in multi-datacenter replication is maintaining a primary cluster that serves traffic and a backup cluster for emergency failover. This configuration can be an important component of compliance with regulatory requirements, ensuring business continuity and access to data even in serious failure modes.
In this configuration, a primary cluster serves as the production cluster from which all read and write operations are served. The backup cluster(s) is maintained in another datacenter. In the event of a datacenter outage or critical failure at the primary site, requests can be directed to the backup cluster either by changing DNS configuration or rules for routing via a load balancer.
For this use case, we recommend that your failover strategy be tested periodically so any potential issues can be resolved in advance of a crisis. It’s also beneficial to have your failover strategy fully defined upfront – know the conditions in which a failover mode will be invoked, decide how traffic will be directed to the backup, and document and test the failover strategy to ensure success.
Active-Active Cluster Configuration
To achieve data locality, when clients are served at low-latency by whatever datacenter is nearest to them, you can maintain two (or more) active, synced clusters that are both responsible for serving data to clients. An added benefit of this approach is that in the event of a datacenter failure where one of the clusters is hosted, all traffic can be served from the other, still-functional site for business continuity.
For data locality, requests can be load balanced across geographies, with geo-based client requests directed to the appropriate datacenter. For example, US-based requests can be served out of a US-based datacenter while EU-based requests can be served out of a regional site. For situations where not all data needs to be shared across all datacenters (or if certain data, such as user data, must only be stored in a specific geographic region to meet privacy regulations), Riak Enterprise’s multi-datacenter replication can be configured on a per-bucket basis so only shared assets, popular assets, etc. are replicated.
For a more in-depth look at common architectures and use cases for Riak Enterprise, download our technical overview. You can also sign up for our webcast on Thursday, March 7th.
Riak
