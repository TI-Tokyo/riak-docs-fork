---
title: "Salted Riak"
description: "This article was authored by Riak's good friend Matt Davis and was originally posted on the OpenX blog earlier this month.  We thank OpenX and Matt for being an active contributor to the Riak Community and for allowing us to repost this blog. SALTED RIAK by Matt Davis — on salt, riak, config man"
project: community
lastmod: 2015-08-31T11:55:04+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Stephen Condon"
pub_date: 2015-08-30T13:57:09+00:00
---
This article was authored by Riak’s good friend Matt Davis and was originally posted on the OpenX blog earlier this month.  We thank OpenX and Matt for being an active contributor to the Riak Community and for allowing us to repost this blog.
SALTED RIAK
by Matt Davis — on salt, riak, config management – Original post date: 17 Aug 2015


The Imaginary Landscape
At OpenX we run some of the world’s largest bodies of data, managing billions of keys and petabytes of storage containing a wide range of species. Everything from bidder transactions and ad impression events to multidimensional quality control and audience demographics. For managing the infrastructure that comes alive with all this data, we turn to configuration management.
The trick is to refine the complexity, place careful control where we can, use elegant automation and orchestration to provide the structure and materials that will guarantee trusted repeatability.
If we make management easy, we give the data every chance to be awesome. Instead of attempting the nearly impossible task of imagining how big data behaves, we focus on the scaffolding that allows it to proliferate.
Riak
A handful of our big data use cases are built on Riak’s Riak, a distributed key/value store. When operating a distributed system like Riak, its health depends largely on being able to guarantee the homogeneity of its components. When server profiles and configurations are equivalent across the entire cluster, not only can we easily standardize equipment, but also provide solid ground for load patterns to spread evenly.
Yes, these oceans of data are difficult to maintain. It is a frighteningly indeterminate and heterogeneous landscape under the surface, affecting everything that connects and everything it feeds. Traffic in the ad tech space can fluctuate wildly, object dimensions can expand unpredictably, externalities and hardware failures don’t typically announce they’re coming. Just like our world’s largest bodies of water, it is a soup of interacting relationships creating complex rhythms that can be hard to navigate.
Equal distribution of resources matter, it enables our ability to quickly and efficiently manipulate the building blocks without fear; it must be as easy to replace a node as it is to let it die. To provide a framework that ensures Riak configurations are consistent and repeatable, we employ SaltStack.
Salt is Action
Orchestration itself is a compositional art. We carefully arrange components, the way they interact, their ranges and capacities, the methods by which jobs are layered and synchronized, pipelined and harmonized. SaltStack is an open source, python-based platform for configuration management and remote execution, and our tool for gluing this all together.
To this end, we count a great deal on an in-house central host inventory and metadata service. The instruments themselves. This gives us the raw materials for composing specific arrangements required for a particular role, known in Salt as States.
At a random minute every hour (or when a host is first deployed), a minion process on the Riak node reaches out to the Salt master server and requests something called a highstate: its complete set of configurations, everything from contents of /etc/riak to the actual packages and dependencies required. Anything that was not installed will be, everything required running will be started, and file integrity is maintained.
Defined within these states are Salt Modules that provide codified ways of performing actions (e.g. command execution, kernel tuning, user additions). Here we define a target repository and ensure the correct version is always installed from the same place using Salt Requisites:
openx-artifactory-riak:
 pkgrepo.managed:
 - humanname: OpenX Artifactory Riak Repo
 - baseurl: http://path/to/artifactory
 - gpgcheck: 0
 - require\_in:
 - pkg: riak-pkgs

riak-pkgs:
 pkg.installed:
 - fromrepo: openx-artifactory-riak
 - pkgs:
 - riak-ee: {{ version.riak\_ee }}
 - jre: {{ version.jre }}

Disabling any swap devices (recommended when running Riak) is easily accomplished with the command execution module:
swapoff\_cmd:
 cmd.run:
 - name: /sbin/swapoff -a ; /bin/sed -i '/swap/d' /etc/fstab

Instead of using some kind of supervisor to make sure riak stays running, we can use the service module. Every time that hourly highstate runs (itself enabled by a cron salt module), we know riak will be started if it isn’t already:
riak:
 service:
 - running
 - enable: True

State definitions and files are often peppered with Jinja templates. These are used within Salt state files and module definitions, providing a way to dynamically target things by either a defined variable or a stored key/value datapoint known as a Grain. These bits of information hold vital statistics about the host, like the IP address in /etc/riak/advanced.config:
{riak\_core, [
 {cluster\_mgr, {"{{ salt['network.interfaces']()['eth0']['inet'][0]['address'] }}", 9080 } }
]}

This can be integrated with our hardware inventory to classify things like environment, giving us methods for logical separation:
include:
 - role.database.riak.adquality.{{ grains.environment }}.config

All this configuration data comes from either a key/value collection in Salt called Pillar, or from similarly structured Jinja maps.
The end result is a harmonious ensemble that provides a flexible, repeatable and scalable way to manage disparate clusters in multiple colocation facilities all around the globe.
Scaling to the World
Salt with Riak greatly eases the management of large multi-datacenter replication meshes with hundreds of machines. In fact, with the ability to include and extend other states, even greater centralization can be achieved; in our case, all Riak clusters read exactly the same common core elements, configured exactly once within Salt. This kind of deterministic formula is perfect for maintaining distributed systems, because we always want that homogeneity to persist against the tide of eventually consistent data. We want to keep state, and make it as easy as possible to scale quickly.
Although we cannot precisely predict how multifaceted data systems behave any better than we can the movement of the Earth’s oceans, we can build elegant structures that allow it freedom of movement. This approach helps OpenX weather both seasonal and sudden flows of traffic. Not unlike the respect we keep for life-supporting seas, it ultimately allows us the trust a robust, highly available and partition tolerant distributed system deserves.


