---
title: "Riak 2.1 and Riak CS 2.0 - Increased Performance and Product Enhancements"
description: "April 8, 2015 At the beginning of 2015, Adam Wray, our CEO and president, made a bold statement in a post entitled Riak is Back! Record Year and a Strong Start to 2015 he claimed: At Riak we are focused on establishing product value and trust, while projecting a vision that our customers and c"
project: community
lastmod: 2015-05-28T19:23:30+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Peter Coppola"
pub_date: 2015-04-08T22:00:59+00:00
---
April 8, 2015
At the beginning of 2015, Adam Wray, our CEO and president, made a bold statement in a post entitled Riak is Back! Record Year and a Strong Start to 2015 he claimed:
In my role as the VP of Product and Marketing, I have the opportunity to shape our product based on customer and partner feedback as well as research into market direction. We are committed to providing the best multi-model solution for Big Data applications that leverage unstructured data in their active workloads. In fact, Riak has led the industry in adoption of multi-model solutions since beginning to offer key/value and object storage in 2013.
Over the last week or so you have seen us release Riak CS 2.0 and updated, Riak supported client libraries for Node.js and .NET. We will also release Riak 2.1 in the next few days with key performance enhancements. Riak is, presently, the leader in high availability and scale for distributed, active workloads and our increased focus on performance will result in performance enhancements for both Riak KV and Riak CS throughout 2015.
Riak 2.1
The updates to Riak 2.1 include numerous changes driven by our perspective on market trends and direction. Chief among these is the emphasis on performance and simplification for both developers and operations.
Performance
Enhancements to Riak 2.1 have increased write speeds by more than 2x for write-heavy workloads.
Riak 2.1 introduces the concept of “write once” buckets, buckets whose entries are intended to be written exactly once, and never updated or over-written. These write once buckets optimize Riak performance for immutable data which is a key design pattern for many Big Data applications.
The write\_once property is applied to a bucket type and may only be set at bucket creation time. Once a bucket type has been set with this property and activated, the write\_once property may not be modified.
This capability is extremely important for our customers, partners, and prospects who are writing and deploying IoT applications and whose data model includes immutable data workflows. We will continue to invest in performance in 2015 to drive speeds for write-heavy and other common workloads.
Riak Supported Clients
Riak has always maintained a series of supported client libraries for popular languages. With Riak 2.1, we have broadened the support by adding support for additional key languages used in the development of business applications. We are pleased to announce the inclusion of Riak-supported client libraries for Node.js and .NET. In addition, we have enhanced our support for PHP enabling easier integration for those building real-time web applications.
New Monitoring Statistics & Integrations
Once a Big Data application itself has been built, it is necessary to ensure that the cluster can be actively monitored. The addition of more than 200 supplementary Riak statistics enables fine-grained monitoring of individual node and cluster health. For example, you can monitor statistics for each Riak Data Type (CRDTs) measuring Get, Put, Update and Merge times at multiple percentiles. In addition, you can measure index and query latency alongside throughput for Riak Search (Solr). These statistics enable you to monitor the impact your application design has on the cluster. In addition, Riak has integrated these monitoring statistics with Nagios, New Relic, and Zabbix further expanding integrations with both hosted and on-premise monitoring solutions.
OS X Installers
In addition to clients and monitoring, we have invested in several new and/or updated installation options for Riak. Many application developers use OS X as their primary development machine. Riak already provides a simple project, riak-dev-cluster, for quickly getting started with a 5 node Riak Cluster. Now we are making it even easier by offering an OS X installer that lets you locally deploy a single node of Riak, for development purposes, with a series of simple clicks.
Community
We continue our commitment to our community by working with the open-source contributors to our Chef, Puppet, and Ansible tools to ensure they are optimized for use with this release. In fact, improvements to the puppet-riak module make it one of the first to be built on Puppet 4.0, the latest release from Puppet Labs. To ensure clarity, and broader commitment to open-source development, we have arranged repositories driven by community contribution into the Riak Labs organization on Github. While our core codebase remains in the Riak organization, and undergo a rigorous review process, the Riak Labs invites community commitment and is actively monitored.
Partners
As if this wasn’t enough, we have also worked closely with Cloudsoft to release tested, optimized Riak blueprints. These blueprints enable the deployment of applications faster, and easier, across a variety of cloud service provider including AWS and SoftLayer. One-click, multiple providers.
Cloudsoft AMP blueprints are available to spin up a Riak cluster, a Riak cluster with an example application and Riak clusters in a multi-datacenter configuration.
Riak CS 2.0
It is with some pleasure that we are able to announce that Riak CS 2.0 is now generally available. This represents a major milestone in the lifecycle and development of Riak’s object storage offering. Riak provides the only true multi-model platform for the persistence and storage of a variety of unstructured data. With Riak CS 2.0, we have achieved seamless integration with the underlying Riak 2.0 codebase. This results in all the operational benefits of Riak 2.0 being included in Riak CS.
It would be remiss to not highlight that Riak CS 2.0 now provides enhanced conflict resolution that simplifies development, making it easier to reduce the likelihood of data conflicts and sibling growth in an eventually consistent system. This is achieved by leveraging the dotted version vector system introduced in Riak 2.0 enabling drastically simplified operational effort. This approach is coupled with the simplified configuration management presented initially in Riak 2.0 allowing for human-readable, and machine-parseable configuration files that are easily integrated with the orchestration tools that the enterprise prefers.
Next Steps
Getting started with Riak is easier than ever before thanks to the effort in simplifying the installation process for OS X. Designing and implementing a system for active workloads, whether a new design or replacement for existing infrastructure, often begins with a conversation with a member of our Solution Architecture team. They are available for onsite or remote discussions to educate your team on the practical considerations of implementing Riak for unstructured workloads and Big Data applications.

Sign up for a Tech Talk
Download Riak 2.1 (Available April 15)
Download Riak CS 2.0

Peter Coppola
Vice President, Product & Marketing
