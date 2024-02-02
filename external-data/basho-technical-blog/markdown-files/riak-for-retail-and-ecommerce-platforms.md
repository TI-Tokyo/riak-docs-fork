---
title: "Riak for Retail and eCommerce Platforms"
description: "Retail on Riak- A Technical Introduction."
project: community
lastmod: 2016-10-20T07:18:27+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-01-22T21:22:10+00:00
---
January 22, 2013
Traditionally, most retailers have used relational databases to manage their platforms and eCommerce sites. However, with the rapid growth of data and business requirements for high availability and scale, more retailers are looking at non-relational solutions like Riak.
Riak is a masterless, distributed database that provides retailers with high read and write availability, fault-tolerance and the ability to grow with low operational cost. Architectural, operational and development benefits for retailers include:

“Always On” Shopping Experience: Based on architectural principles from Amazon, Riak is designed to favor data availability, even in the event of hardware failure or network partition. For retailers, failure to accept additions to a shopping cart, or serve product information quickly, has a direct and negative impact on revenue. Riak is architected to ensure the system can always accept writes and serve reads at low-latency.
Resilient Infrastructure: At scale, hardware malfunction, network partition, and other failure modes are inevitable. Riak provides a number of mechanisms to ensure that retail infrastructure is resilient to failure. Data is replicated automatically within the cluster so nodes can go down but the system still responds to requests. This ensures read and write availability, even in serious failure conditions.
Low-Latency Data Storage: Many retailers now operate online and mobile experiences with an API or data services platform. In order to provide a fast and available experience to end users, Riak is designed to serve predictable, low-latency requests as part of a service-oriented infrastructure and is accessible via HTTP API, protocol buffers, or Riak’s many client libraries.
Scale to Peak Loads with Low Operational Cost: During major holidays and other periods of peak load, retailers may have to significantly increase their database capacity quickly. When new nodes are added, Riak automatically distributes data evenly to naturally prevent hot spots in the database, and yields a near-linear increase in performance and throughput when capacity is added.
Global Data Locality and Redundancy: Riak Enterprise’s multi-site replication allows replication of data to multiple data centers, providing both a global data footprint and the ability to survive datacenter failure.

Top retailers using Riak include Best Buy and ideel. Best Buy selected Riak as an integral part in the transformation push to re-platform its eCommerce platform. For more information about how Best Buy is using Riak, check out this video.
ideel uses Riak to serve HTML documents and user-specific products. ideel chose Riak to provide its highly available, event-based shopping experience – Riak gives them the ability to serve user information at low latency and provides ease of use and scale to ideel’s operations team. For more information on ideel’s use of Riak check out the complete case study.
Common use cases for Riak in the retail/eCommerce space include shopping carts (due to Riak’s “always-on” capabilities), product catalogs (Riak is well suited for the storage of rapidly growing content that needs to be served at low-latency), API platforms (Riak’s flexible, schemaless design allows for rapid application development), and mobile applications (Riak is ideal for powering mobile experiences across platforms due to its low-latency, always-available small object storage capabilities).
To help retailers evaluate and adopt Riak, we’ve published a technical overview: “Retail on Riak: A Technical Introduction.” We discuss more in-depth information on modeling applications for common use cases, switching from a relational architecture, querying, multi-site replication and more.
Riak
