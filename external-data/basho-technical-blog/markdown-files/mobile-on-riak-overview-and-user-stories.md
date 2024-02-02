---
title: "Mobile on Riak: Overview and User Stories"
description: "Technical overview about how mobile platforms can use Riak."
project: community
lastmod: 2015-05-28T19:23:41+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-02-26T14:00:19+00:00
---
February 26, 2013
Mobile platforms and applications pose unique infrastructure challenges for today’s companies. These applications require low-latency, always-available small object storage that can scale to millions or more users, and support highly concurrent access and traffic spikes.
Riak provides a number of benefits for these platforms, including:

Low-Latency Data Storage: Riak is designed to serve predictable, low-latency requests to provide a fast, available experience to all users.
Straightforward Data Model: Riak uses a simple key-value data model, which is ideal for storing and serving mobile content, user information, events, and session data. Riak is content agnostic, so there are no restrictions on content type.
Accommodates Peak Loads Gracefully: To handle increasing user data and accommodate peak loads during events, Riak makes it easy to add additional capacity and scale out quickly. Riak automatically rebalances data when new nodes are added, while its consistent hashing methodology prevents hot spots in the database.
Multi-Datacenter Replication: Riak Enterprise’s multi-datacenter replication allows mobile platforms to serve low-latency content to users all over world by maintaining a global data footprint.
For a full overview, download our new whitepaper on building mobile services with Riak

User Stories
Bump is a popular mobile app that makes it easy for users to share their contact information, photos, and other objects by simply “bumping” their smartphones. They use Riak to store user data and currently run 25 nodes of Riak storing about 3TB of data.
For more details about how Bump uses Riak and how they designed their application, check out Bump’s presentation at RICON2012, Riak’s 2012 developer conference. You can also read the complete case study for more information about why Bump chose Riak.

Voxer is a popular Walkie Talkie application for smartphones that allows users to send instant voice messages to one or more friends. They switched to Riak due to its fault-tolerance and ability to scale quickly and easily. They currently run more than 50 machines on Riak to support their huge growth and user base. For more details about how Voxer uses Riak, check out the complete case study and watch Matt Ranney’s talk at a Riak Meetup in San Francisco.

To learn more about how mobile platforms can use Riak for their data needs, check out the complete overview, “Mobile on Riak: A Technical Introduction.”
Riak
