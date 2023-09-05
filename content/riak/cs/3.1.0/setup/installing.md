---
title_supertext: "Setup:"
title: "Installing Riak CS"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Installing Riak CS"
    identifier: "installing"
    weight: 100
    parent: setup
project: "riak_cs"
project_version: "3.1.0"
aliases:
  - /riak/cs/3.1.0/cookbooks/installing/
  - /riakcs/3.1.0/cookbooks/installing/
  - /riak/cs/3.1.0/setup/installing/chef/
  - /riak/cs/3.1.0/cookbooks/installing/Riak-CS-Using-Chef/
  - /riak/cs/3.1.0/cookbooks/installing/chef/
  - /riakcs/3.1.0/cookbooks/installing/Riak-CS-Using-Chef/
  - /riakcs/3.1.0/cookbooks/installing/chef/
---

[install alpine]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/alpine-linux
[install aws]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/amazon-web-services
[install debian & ubuntu]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/debian-ubuntu
[install freebsd]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/freebsd
[install oracle]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/oracle
[install rhel & centos]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/rhel-centos
[install windows azure]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/windows-azure
[install source index]: {{<baseurl>}}riak/cs/3.1.0/setup/installing/source

[community projects]: {{<baseurl>}}community/projects

[upgrade cs]: {{<baseurl>}}riak/cs/3.1.0/setup/upgrading
[version compat]: {{<baseurl>}}riak/cs/3.1.0/learning/version-compatibility/
[config kv for cs]: {{<baseurl>}}riak/cs/3.1.0/configuring/riak-kv-for-cs/

[setup kv]: {{<baseurl>}}riak/kv/latest/setup/

## Overview

To install Riak CS requires two steps:

1. Create a Riak KV cluster and configure it for Riak CS
2. Install Riak CS on each node and configure them

We strongly recommend using one of the documented [version combinations][version compat]
when installing and running Riak CS.

## Installing and Configuring Riak KV for Riak CS

Before installing Riak CS, Riak KV must be installed on each node in
your cluster. You can install Riak KV either as part of an OS-specific package
or from source.

More details can be found in the [Riak KV Setup][setup kv] documentation.

Remember that you must repeat this installation process on each node in
your cluster. For future reference, you should make note of the Riak KV
installation directory.

If you want to fully configure Riak KV prior to installing Riak CS, see our
documentation on [configuring Riak KV for CS][config kv for cs].

## Installing Riak CS

After you have created and (ideally) configured your Riak KV cluster, you can then
install Riak CS.

The following information will help you to properly install or upgrade Riak
in one of the supported environments:

- [Alpine][install alpine]
- [Amazon Web Services][install aws]
- [Debian & Ubuntu][install debian & ubuntu]
- [FreeBSD][install freebsd]
- [Orcale][install oracle]
- [RHEL & CentOS][install rhel & centos]

## Upgrading

For information on upgrading an existing cluster see [Upgrading Riak CS][upgrade cs].
