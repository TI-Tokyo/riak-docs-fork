---
title_supertext: "Installing on"
title: "RHEL and CentOS"
description: ""
project: "riak_kv"
project_version: "2.1.4"
lastmod: 2016-04-07T00:00:00-00:00
sitemap:
  priority: 0.1
menu:
  riak_kv-2.1.4:
    name: "RHEL & CentOS"
    identifier: "installing_rhel_centos"
    weight: 304
    parent: "installing"
toc: true
aliases:
  - /riak/2.1.4/ops/building/installing/Installing-on-RHEL-and-CentOS
  - /riak/kv/2.1.4/ops/building/installing/Installing-on-RHEL-and-CentOS
  - /riak/2.1.4/installing/rhel-centos/
  - /riak/kv/2.1.4/installing/rhel-centos/
---

[install source index]: {{<baseurl>}}riak/kv/2.1.4/setup/installing/source
[install source erlang]: {{<baseurl>}}riak/kv/2.1.4/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/2.1.4/setup/installing/verify

Riak KV can be installed on CentOS- or Red-Hat-based systems using a binary
package or by [compiling Riak from source code][install source index]. The following steps have been tested to work with Riak on
CentOS/RHEL 5.10, 6.5, and 7.0.1406.

> **Note on SELinux**
>
> CentOS enables SELinux by default, so you may need to disable SELinux if
you encounter errors.

For versions of Riak prior to 2.0, Basho used a self-hosted
[apt](http://en.wikipedia.org/wiki/Advanced_Packaging_Tool) repository
for RHEL and CentOS packages. For versions 2.0 and later, TI Tokyo hosts these at https://files.tiot.jp.

Platform-specific pages are linked below:

* [el5](https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/5/riak-2.1.4-1.el5.x86_64.rpm)
* [el6](https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/6/riak-2.1.4-1.el6.x86_64.rpm)
* [el7](https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/7/riak-2.1.4-1.el7.centos.x86_64.rpm)
* [Fedora 19](https://files.tiot.jp/riak/kv/2.1/2.1.4/fedora/19/riak-2.1.4-1.fc19.x86_64.rpm)

## Installing with Yum

For the simplest installation process on Long-Term Support (LTS)
releases, use yum:

#### For CentOS 5 / RHEL 5:

```bash
sudo yum install riak-2.1.4-1.el5.x86_64
```

#### For CentOS 6 / RHEL 6:

```bash
sudo yum install riak-2.1.4-1.el6.x86_64
```

## Installing with Yum and Packages

If you wish to install the RHEL/CentOS packages by hand, follow these
instructions.

#### For Centos 5 / RHEL 5

Download the package and install:

```bash
wget https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/5/riak-2.1.4-1.el5.x86_64.rpm
sudo yum install riak-2.1.4-1.el5.x86_64
```

#### For Centos 6 / RHEL 6

Download the package and install:

```bash
wget https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/6/riak-2.1.4-1.el6.x86_64.rpm
sudo yum install riak-2.1.4-1.el6.x86_64
```

## Installing with rpm

#### For Centos 5 / RHEL 5

```bash
wget https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/5/riak-2.1.4-1.el5.x86_64.rpm
sudo rpm -Uvh riak-2.1.4-1.el5.x86_64.rpm
```

#### For Centos 6 / RHEL 6

```bash
wget https://files.tiot.jp/riak/kv/2.1/2.1.4/rhel/6/riak-2.1.4-1.el6.x86_64.rpm
sudo rpm -Uvh riak-2.1.4-1.el6.x86_64.rpm
```

## Installing From Source

Riak requires an [Erlang](http://www.erlang.org/) installation.
Instructions can be found in [Installing Erlang][install source erlang].

Building from source will require the following packages:

* `gcc`
* `gcc-c++`
* `glibc-devel`
* `make`
* `pam-devel`

You can install these with yum:

```bash
sudo yum install gcc gcc-c++ glibc-devel make git pam-devel
```

Now we can download and install Riak:

```bash
wget http://s3.amazonaws.com/downloads.basho.com/riak/2.1/2.1.4/riak-2.1.4.tar.gz
tar zxvf riak-2.1.4.tar.gz
cd riak-2.1.4
make rel
```

You will now have a fresh build of Riak in the `rel/riak` directory.

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].
