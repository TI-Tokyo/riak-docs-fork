---
title: "Installing Riak KV"
description: ""
project: "riak_kv"
project_version: "2.9.0p5"
lastmod: 2019-11-21T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-2.9.0p5:
    name: "Installing"
    identifier: "installing"
    weight: 101
    parent: "setup_index"
    pre: cog
toc: true
aliases:
  - /riak/2.9.0p5/ops/building/installing
  - /riak/kv/2.9.0p5/ops/building/installing
  - /riak/2.9.0p5/installing/
  - /riak/kv/2.9.0p5/installing/
  - /riak/2.9.0p5/setup/installing/
  - /riak/2.9.0/setup/installing/
  - /riak/kv/2.9.0/setup/installing/
  - /riak/kv/2.9.0p1/setup/installing/
  - /riak/kv/2.9.0p2/setup/installing/
  - /riak/kv/2.9.0p3/setup/installing/
  - /riak/kv/2.9.0p4/setup/installing/
---

[install mac osx]: {{<baseurl>}}riak/kv/2.9.0p5/setup/installing/mac-osx
[install aws]: {{<baseurl>}}riak/kv/2.9.0p5/setup/installing/amazon-web-services
[install debian & ubuntu]: {{<baseurl>}}riak/kv/2.9.0p5/setup/installing/debian-ubuntu
[install freebsd]: {{<baseurl>}}riak/kv/2.9.0p5/setup/installing/freebsd
[install oracle linux]: {{<baseurl>}}riak/kv/2.9.0p5/setup/installing/oracle-linux
[install rhel & centos]: {{<baseurl>}}riak/kv/2.9.0p5/setup/installing/rhel-centos
[upgrade index]: {{<baseurl>}}riak/kv/2.9.0p5/setup/upgrading

## Supported Platforms

Riak is supported on numerous popular operating systems and virtualized
environments. The following information will help you to
properly install or upgrade Riak in one of the supported environments:

  * [Amazon Web Services][install aws]
  * [Debian & Ubuntu][install debian & ubuntu]
  * [FreeBSD][install freebsd]
  * [RHEL & CentOS][install rhel & centos]
  * [Oracle Linux][install oracle linux]
  * [Raspbian]
  * [Mac OS X][install mac osx]

## Building from Source

If your platform isn’t listed above, you may be able to build Riak from source. See [Installing Riak from Source][install source index] for instructions.

## Community Projects

Check out [Community Projects][community projects] for installing with tools such as [Chef](https://www.chef.io/chef/), [Ansible](http://www.ansible.com/), or [Cloudsoft](http://www.cloudsoftcorp.com/).

## Upgrading

For information on upgrading an existing cluster see [Upgrading Riak KV][upgrade index].
