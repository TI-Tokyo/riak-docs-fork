---
title: "Riak for Advertising Services and Platforms"
description: "An introduction about how advertisers can use Riak for their data needs."
project: community
lastmod: 2016-10-20T07:19:45+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-02-14T08:00:50+00:00
---
February 14, 2013
Advertisers need to provide highly available, low latency experiences to thousands of clients and partners and millions of users. They also need to serve large amounts of data all over the world and can experience significant traffic spikes. To meet these needs, more advertisers are considering distributed data solutions. This post looks at common use cases for Riak in the advertising space, and the stories of two existing advertising users. For a full technical overview, download our whitepaper on Riak for advertisers.
Top Use Cases for Riak in Advertising:

Serving Ad Content: Riak’s rapid storage and content agnosticism makes it ideal for storing ad content and handling influxes of ad traffic. For more information on serving ad content with Riak, check out our documentation.
Session Storage: This type of data is naturally a good fit for Riak’s key/value model. This data can also be encoded in many different ways and can evolve without any administrative changes to the schema. You can find more information on building a session store with Riak here.
Mobile: Riak is ideal for the low-latency, always-available small object storage needed to power mobile experiences across platforms.
Global Data Locality: Riak Enterprise’s multi-datacenter capabilities allow advertisers to maintain a global data footprint while providing an always-on, low-latency experience, anywhere in the world.

User Stories:
OpenX, the global leader in digital and mobile advertising technology, serves trillions of ads each year. They use Riak for handling user and trafficking data storage behind their data services API. Riak was selected due to its highly available, low-latency, redundant architecture. OpenX also uses Riak Enterprise’s multi-datacenter replication across several data centers. For more details about how OpenX uses Riak, check out the video of Anthony Molinaro, OpenX engineer, speaking at RICON2012, Riak’s 2012 developer conference.

Velti is a global marketing and advertising technology provider. Velti’s interactive subscriber services provide television broadcast audiences the ability to interact with programs using their mobile phone– voting on people or things, giving feedback, or participating in contests. They selected Riak because it is distributed, scalable, and highly available with the ability to handle large volumes of traffic. To minimize any potentially catastrophic outages, they also opted to build two geographically separated, mirrored sites using Riak Enterprise’s multi-datacenter replication feature. For more information on Velti’s use of Riak check out the complete case study.
To learn more about how advertisers can use Riak for their data needs, check out the complete overview, “Advertisers on Riak: A Technical Introduction,” or stay tuned for future blogs posts on data modeling and querying for advertising services built on Riak.
Riak
