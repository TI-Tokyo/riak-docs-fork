---
title: "Configuring Riak CS Overview"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Configuring"
    identifier: "config"
    weight: 500
    pre: cog
project: "riak_cs"
project_version: "3.1.0"
aliases:
  - /riak/cs/3.1.0/configuring/
  - /riakcs/3.1.0/configuring/
---

In a Riak CS storage system, three components work in conjunction with one another, which means that you must configure each component to work with the others:

* Riak KV --- The database system that acts as the backend storage
* Riak CS --- The cloud storage layer over Riak which exposes the storage and billing APIs, storing files and metadata in Riak, and streaming them back to users
* Stanchion --- Manages requests involving globally unique system entities, such as buckets and users sent to a Riak instance, for example, to create users or to create or delete buckets

In addition, you must also configure the S3 client you use to communicate with your Riak CS system.

You should plan on having one Riak KV node for every Riak CS node in your system. Riak KV and Riak CS nodes can be run on separate physical machines, but in many cases it is preferable to run one Riak KV and one Riak CS node on the same physical machine. Assuming the single physical machine has sufficient capacity to meet the needs of both a Riak KV and a Riak CS node, you will typically see better performance due to reduced network latency.

If your system consists of several nodes, configuration primarily represents setting up the communication between components. Other settings, such as where log files are stored, are set to default values and need to be changed only if you want to use non-default values.

## Configuration of System Components

* [Configuring Riak KV for Riak CS]({{<baseurl>}}riak/cs/3.1.0/configuring/riak-kv-for-cs)
* [Configuring Riak CS]({{<baseurl>}}riak/cs/3.1.0/configuring/riak-cs)
* [Configuring Stanchion]({{<baseurl>}}riak/cs/3.1.0/configuring/stanchion)

## Configuration of Features

* [Configuring Load Balancing & Proxies]({{<baseurl>}}riak/cs/3.1.0/configuring/load-balancing-and-proxies)
* [Configuring Multi-Data Centre Replication]({{<baseurl>}}riak/cs/3.1.0/configuring/mdc)
* [Configuring Superclusters]({{<baseurl>}}riak/cs/3.1.0/configuring/superclusters)

## References

* [Configuration Reference]({{<baseurl>}}riak/cs/3.1.0/configuring/reference)

## Configuration of Clients

* [Configuring an S3 client]({{<baseurl>}}riak/cs/3.1.0/developing/apis/s3/s3-client-config)
