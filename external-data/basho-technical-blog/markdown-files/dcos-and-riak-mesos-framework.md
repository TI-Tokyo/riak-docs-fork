---
title: "DC/OS 1.9 and Riak Mesos Framework"
description: "We are pleased today to announce with Mesosphere, the launch of DC/OS 1.9 and a major upgrade of the integration of Riak with Mesosphere DC/OS. DC/OS, built on Apache Mesos, is the premier platform for running data and containers elastically on shared infrastructure. Running Riak on DC/OS enables on"
project: community
lastmod: 2017-03-13T12:06:54+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Pavel Hardak"
pub_date: 2017-03-13T09:47:39+00:00
---
We are pleased today to announce with Mesosphere, the launch of DC/OS 1.9 and a major upgrade of the integration of Riak with Mesosphere DC/OS. DC/OS, built on Apache Mesos, is the premier platform for running data and containers elastically on shared infrastructure. Running Riak on DC/OS enables one-click installation, simple operations, and a scalable infrastructure.
We are very proud to release a major upgrade of the Riak Mesos Framework (RMF) for Riak KV and Riak TS. The Riak Mesos Framework is available open source in Riak-Labs Github repository and supports the latest DC/OS 1.9 release. This integration enables customers to easily deploy and efficiently run Riak products on DC/OS clusters, together with other best of breed technologies. When running on Mesosphere DC/OS, customers will get all the goodies of Apache Mesos and additional benefits in terms of security, enhanced networking and intuitive administrative tools. We added some DC/OS specific enhancements to Riak Mesos Framework as well.
After going through review by Mesosphere team, Riak Mesos Framework 2.0 is now available via the DC/OS Universe. We have a great partnership with Mesosphere and are proud to be supporting DC/OS 1.9.
Looking back – we started our early integration efforts with Apache Mesos almost two years ago and shared an early preview version in August 2016. Working directly with Mesosphere and with early adopters like Cisco and IBM, we learned a tremendous amount. Our new release is a totally re-designed Riak Mesos Framework. We have improved the architecture and leveraged advanced DC/OS functionalities to support distributed stateful applications (e.g. Persistent volumes and Dynamic Allocations). It integrates with Marathon and DC/OS CLI and includes Riak Explorer, which is integrated with DC/OS Administrative GUI. A short interactive preview can be viewed at http://basho-labs.github.io/riak-mesos/.
Riak Mesos Framework has been developed per Mesosphere design guidelines and fully implements all the components for stateful DC/OS services. It includes RMF Scheduler, deployed under Marathon, creates a number of Executors, which are deployed to Mesos client nodes and run Riak nodes. Supervised by Marathon, RMF Scheduler connects to Mesos Master and stores its configuration about Riak cluster in the DC/OS Zookeeper. The Scheduler also runs an instance of Riak Explorer, which allows monitoring and control of Riak cluster/s using an intuitive web UI. By default, RMF asks Marathon to deploy Riak nodes on separate physical instances to minimize the chance of resource competition, especially for disk drives. Several resource allocation strategies are supported and configurable by DC/OS administrator via configuration constraints.
RMF Executors are run under Mesos clients and each one manages one Riak node. Since network ports assigned to each instance on startup may vary, Executors store them in Zookeeper so that Riak nodes can communicate with each other.
Riak is extremely scalable and reliable, with low operational complexity. We designed RMF to extend these features into the DC/OS world. It is very easy to scale Riak clusters up and down using simple CLI commands and DC/OS takes care of resource allocations. RMF Scheduler knows how to recover in multiple fault tolerant scenarios, which might happen due to network partition or hardware failures.
We re-engineered RMF to leverage the latest Mesos HTTP APIs, instead of previous, less reliable integration with version-specific libmesos.so library. We have been working in stages, as new HTTP interfaces were introduced in more recent Mesos versions. For example,  Executor API endpoint was introduced in Mesos 0.28 as “Experimental” and was marked “Stable” in Mesos 1.0. Riak Mesos Framework 2.0 requires at least DC/OS 1.8 or Mesos 1.0.
Additionally, RMF includes Riak Mesos CLI Tools, which can be used via DC/OS CLI or stand-alone under Apache Mesos. Riak Mesos CLI provides Mesos-aware versions of many Riak commands usually found in riak and riak-admin tools, including getting node information or creating new bucket types. We also include an optional Riak Mesos Director tool, essentially Riak and Mesos-aware proxy, which makes it easier to develop and test your applications with Riak cluster running on DC/OS, without the need to configure individual nodes, IP addresses and network ports.
By using Riak Mesos Framework from DC/OS Universe, it becomes very easy to deploy and run Riak clusters in hybrid, on-premises or cloud environments, effectively sharing cluster hardware resources with other technology frameworks, like Apache Spark (using Riak Spark Connector), and custom micro-services. We hope you will make good use of RMF and are looking for feedback and contributors.
Prebuild RMF artifacts are available for Riak KV 2.2 and Riak TS 1.5.1 on Debian 8, Ubuntu 14 and Centos 7. For customers using Riak KV or Riak TS Enterprise, we plan to have RMF builds for Riak Enterprise available in the near future.
Read more about the Mesosphere DC/OS 1.9 release
Read more Mesosphere Blog on data services for DC/OS
Other Resources

    Riak Mesos Framework at Riak site
    Riak Mesos Framework documentation and sources at GitHub
    Mesosphere Universe
    Mesosphere DC/OS documentation
    Apache Mesos Documentation

Pavel Hardak
Director of Product Management, Riak TS and Integrations
@PavelHardak
