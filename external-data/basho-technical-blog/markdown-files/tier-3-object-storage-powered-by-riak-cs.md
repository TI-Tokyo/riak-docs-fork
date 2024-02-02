---
title: "Tier 3 Object Storage: Powered by Riak CS"
description: "Tier 3's cloud object storage brings geo-data locality to customers using Riak CS Enterprise."
project: community
lastmod: 2015-05-28T19:23:38+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-06-19T13:00:59+00:00
---
June 19, 2013
Today, Tier 3 announced the availability of their global cloud object storage product, powered by Riak CS. You can find the entirety of the release in our News Section entitled “Tier 3 Launches Global Cloud Object Storage.”
In particular, we are keenly interested in the unique geographic footprint that Tier 3 maintains. In conversations with customers, press, and analysts, we frequently hear people discussing “geo-data locality.” This phrase typically is used to express a desire to address regulatory compliance or to improve the end-customer experience through low-latency (in the case of mobile applications).
With the Tier 3 release, their geographic footprint — in addition to maximizing availability — leverages the inherent replication present in Riak CS to pre-determine the physical locations of specific data.
For geo-data locality, requests can be load balanced across geographies, with geo-based client requests directed to the appropriate datacenter. For example, US-based requests can be served out of a Tier 3 US-based datacenter, while EU-based requests can be served out of a Tier 3 European datacenter. For situations where not all data needs to be shared across all datacenters (or if certain data, such as user data, must only be stored in a specific geographic region to provide low-latency response and address privacy regulations), Riak CS Enterprise’s multi-datacenter replication can be configured on a per-bucket basis so only shared assets, popular assets, etc. are replicated.
Riak
