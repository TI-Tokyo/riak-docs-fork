---
title: "Installing Riak KV"
description: ""
project: "riak_kv"
project_version: "3.0.2"
lastmod: 2021-01-05T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.0.2:
    name: "Installing"
    identifier: "installing"
    weight: 101
    parent: "setup_index"
    pre: cog
toc: true
aliases:
  - /riak/3.0.2/ops/building/installing
  - /riak/kv/3.0.2/ops/building/installing
  - /riak/3.0.2/installing/
  - /riak/kv/3.0.2/installing/
---

[install aws]: {{<baseurl>}}riak/kv/3.0.2/setup/installing/amazon-web-services
[install debian & ubuntu]: {{<baseurl>}}riak/kv/3.0.2/setup/installing/debian-ubuntu
[install raspbian]: {{<baseurl>}}riak/kv/3.0.2/setup/installing/debian-ubuntu/#raspbian-bullseye
[install rhel & centos]: {{<baseurl>}}riak/kv/3.0.2/setup/installing/rhel-centos
[upgrade index]: {{<baseurl>}}riak/kv/3.0.2/setup/upgrading

## Supported Platforms

Riak is supported on numerous popular operating systems and virtualized
environments. The following information will help you to
properly install or upgrade Riak in one of the supported environments:

  * [Amazon Web Services][install aws]
  * [Debian & Ubuntu][install debian & ubuntu]
  * [RHEL & CentOS][install rhel & centos]
  * [Raspbian][install raspbian]

## Building from Source

If your platform isn’t listed above, you may be able to build Riak from source. See [Installing Riak from Source][install source index] for instructions.

## Community Projects

Check out [Community Projects][community projects] for installing with tools such as [Chef](https://www.chef.io/chef/), [Ansible](http://www.ansible.com/), or [Cloudsoft](http://www.cloudsoftcorp.com/).

## Upgrading

For information on upgrading an existing cluster see [Upgrading Riak KV][upgrade index].

