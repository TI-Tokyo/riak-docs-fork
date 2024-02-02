---
title: "Kick off the New Year with Riak TS v1.5 and the Riak Spark Connector!"
description: "I am very excited to announce the release of Riak TS v1.5 and an update to the Riak Spark Connector. Since our open-source release of Riak TS in April 2016, we have been working together with customers to add new features as quickly as possible. As a leading open source company, we enjoy working in"
project: community
lastmod: 2017-01-17T10:49:57+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Pavel Hardak"
pub_date: 2017-01-17T10:39:37+00:00
---
I am very excited to announce the release of Riak TS v1.5 and an update to the Riak Spark Connector. Since our open-source release of Riak TS in April 2016, we have been working together with customers to add new features as quickly as possible. As a leading open source company, we enjoy working in a transparent and agile way. This allows us to quickly prioritize feedback from customers and rapidly deliver new features. We completed four Riak TS public releases in 2016 and plan to do even more in 2017. Needless to say, we are committed to making upgrades very easy, including rolling cluster upgrades without downtime.
About a week before the end of the year, we released Riak TS v1.5. This release packs in new and improved features plus performance improvements. It was released in both open source and Enterprise editions.
Riak TS v1.5 New features:

ORDER BY clause in SELECT statement
LIMIT clause in SELECT statement
ASC / DESC keywords in CREATE TABLE definition
BLOB data type to store unstructured (e.g. binary) or opaque (e.g JSON) data in Riak TS tables
SHOW CREATE TABLE command to review SQL definition and replication parameters
DELETE command to remove Riak TS records using ‘riak-shell’
EXPLAIN command to review query execution plan
Ubuntu 16 (xenial) support (dropped support for Ubuntu 12)

Riak TS v1.5 Improved or Updated Features:

Enhanced NULL handling in SELECT statements
Reduced query latency and execution plan, especially for read queries spanning multiple quanta, new configuration parameters
Enhanced usability of the riak shell tool, added multi-line paste functionality, built-in help for SQL commands, and enhanced error handling
Integration of features from Riak KV 2.2 codebase (Riak Core Fundamentals)
Plus, many documentation and performance improvements (Release Notes)

Note: If you are upgrading from a previous release of Riak TS, please review configuration changes. These changes include added and deprecated parameters in riak.conf.
In addition to releasing Riak TS v1.5, we also release Riak Spark Connector v1.6.2. This release includes support for Riak TS v1.5, Apache Spark v1.6.x and several fixes for bugs, reported by the community.
Get Started Now with Riak TS v1.5

Download Open Source
Sign In to download Enterprise Edition
Learn more about developing with and deploying Riak TS

For a demo or to talk to one of our Riak Solution Architects, please contact us.
This was a great team effort at Riak. I am extremely thankful to Engineering, DevOps and Documentation teams, who worked really hard to make it happen. After a short and well-deserved vacation, we are back working on the next release for the community of Riak users. Stay tuned!
Happy New Year and best wishes for 2017!
Pavel Hardak
Director of Product Management, Riak TS and Integrations
@PavelHardak
