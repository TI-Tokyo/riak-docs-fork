---
title: "Moving from MySQL to Riak"
description: "Learn about the advantages Riak has over MySQL and other relational databases."
project: community
lastmod: 2015-05-28T19:23:33+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2014-04-23T13:00:49+00:00
---
April 23, 2014
Traditional database architectures were the default option for many pre-Internet use cases and architectures, such as MySQL, remain common today. However, these traditional solutions have limits that quickly become apparent as companies (and data) grow. Modern companies have changing priorities: downtime (planned or unplanned) is never acceptable; customers require a fast and unified experience; and data of all types is growing at unimaginable rates. Solutions such as Riak are designed to handle these shifting priorities.
Top Reasons to Move to Riak

Zero Downtime: Distributed NoSQL solutions like Riak are designed for always-on availability. This means data is always read/write accessible and the system never goes down. Downtime, planned or unplanned, can make or break a customer experience.
Ease-of-Scale: Traffic can be unpredictable. Businesses need to scale up quickly to handle peak loads during holidays or major releases, but then need to scale back down to save money. Riak makes it easy to add and remove any number of nodes as needed and automatically redistributes data across the cluster. Scaling up or down never needs to be a burden again.
Flexible Data Model: From user generated data to machine-to-machine (M2M) activity, unstructured data is now commonplace. Riak can store any type of data easily with its simple key/value architecture.
Global Data Locality: Every company is a global company and needs to provide consistent, low-latency experiences to everyone, regardless of physical location. Riak’s multi-datacenter replication makes it easy to set up datacenters wherever users are, for both geo-data locality and maintaining active backups.

Users That Switched to Riak
Many top companies have already moved from relational architectures to Riak. Here’s a look at a few that have made the switch.
Bump (acquired by Google)
Bump, acquired by Google in 2013, allows users to share contact information and photos by bumping two phones together. Bump uses Riak to store almost all of its user data: contacts, communications sent and received, handset information, social network OAuth tokens, etc. Bump moved from MySQL to Riak due to its operational qualities: “No longer will we have to do any master/slave song and dance, nor will we fret about performance, capacity, or scalability; if we need more, we’ll just add nodes to the cluster.” Learn more about their move in their case study.
Alert Logic
Alert Logic helps companies defend against security threats and address compliance mandates, such as PCI and HIPAA. Alert Logic switched from MySQl to Riak to collect and process machine data and to perform real-time analytics, detect anomalies, ensure compliance, and proactively respond to threats. Alert Logic processes nearly 5TB/day in Riak and has achieved performance results of up to 35k operations/second. Learn more about how Alert Logic improved performance through Riak in our blog post.
The Weather Company
The Weather Company provides millions of people every day with the world’s best weather forecasts, content and data, connecting with them through television, online, mobile and tablet screens. Riak is central to The Weather Company’s weather data services platform that delivers real-time weather services to aerospace, insurance, energy, retail, media, government, and hospitality industries. Check out our blog to see why The Weather Company selected Riak over MySQL to support their massive big data needs.
Dell
Dell uses Riak as the core distributed database technology underlying its customer cloud management solutions. Riak is used to collect and manage data associated with customer application provisioning and scaling, application configuration management, usage governance, and cloud utilization monitoring. In 2012, Enstratius (acquired by Dell) switched to Riak from MySQL in order to provide cross-datacenter redundancy, high write availability, and fault tolerance. Check out the full Enstratius case study.
Data Modeling in Riak
Riak has a “schemaless” design. Objects are comprised of key/value pairs, which are stored in flat namespaces called buckets. Below is a chart with some simple approaches to building common application types with a key/value model.



Application Type
Key
Value


Session
User/Session ID
Session Data


Advertising
Campaign ID
Ad Content


Logs
Date
Log File


Sensor
Date, Date/Time
Sensor Updates


User Data
Login, eMail, UUID
User Attributes


Content
Title, Integer
Text, JSON/XML/HTML Document, Images, etc.




To learn more about the benefits of Riak over relational databases, check out the whitepaper, “From Relational to Riak.” To get started with Riak, Contact Us or download it now.
Riak
