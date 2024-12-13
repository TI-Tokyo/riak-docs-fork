---
title_supertext: "Installing on"
title: "RHEL and CentOS"
description: ""
project: "riak_kv"
project_version: "2.1.1"
lastmod: 2015-05-05T00:00:00-00:00
sitemap:
  priority: 0.1
menu:
  riak_kv-2.1.1:
    name: "RHEL & CentOS"
    identifier: "installing_rhel_centos"
    weight: 304
    parent: "installing"
toc: true
aliases:
  - /riak/2.1.1/ops/building/installing/Installing-on-RHEL-and-CentOS
  - /riak/kv/2.1.1/ops/building/installing/Installing-on-RHEL-and-CentOS
  - /riak/2.1.1/installing/rhel-centos/
  - /riak/kv/2.1.1/installing/rhel-centos/
---

[install source index]: {{<baseurl>}}riak/kv/2.1.1/setup/installing/source
[install source erlang]: {{<baseurl>}}riak/kv/2.1.1/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/2.1.1/setup/installing/verify

Riak KV can be installed on CentOS- or Red-Hat-based systems using a binary
package or by [compiling Riak from source code][install source index]. The following steps have been tested to work with Riak on
CentOS/RHEL 5.10, 6.5, and 7.0.1406.

> **Note on SELinux**
>
> CentOS enables SELinux by default, so you may need to disable SELinux if
you encounter errors.

## Installing with rpm

For versions of Riak prior to 2.0, Basho used a self-hosted
[apt](http://en.wikipedia.org/wiki/Advanced_Packaging_Tool) repository
for RHEL and CentOS packages. For versions 2.0 and later, TI Tokyo hosts these at https://files.tiot.jp.

Platform-specific pages are linked below:

* [el5](https://files.tiot.jp/riak/kv/2.1/2.1.1/rhel/5/riak-2.1.1-1.el5.x86_64.rpm)
* [el6](https://files.tiot.jp/riak/kv/2.1/2.1.1/rhel/6/riak-2.1.1-1.el6.src.rpm)
* [el7](https://files.tiot.jp/riak/kv/2.1/2.1.1/rhel/7/riak-2.1.1-1.el7.centos.src.rpm)
* [Fedora 19](https://files.tiot.jp/riak/kv/2.1/2.1.1/fedora/19/riak-2.1.1-1.fc19.src.rpm)

Our documentation also includes instructions regarding signing keys and
sources lists, which can be found in the section immediately below.

## Installing From Package

If you wish to install the RHEL/CentOS packages by hand, follow these
instructions.

### For Centos 5 / RHEL 5

You can install the `.rpm` package manually:

```bash
wget https://files.tiot.jp/riak/kv/2.1/2.1.1/rhel/5/riak-2.1.1-1.el5.x86_64.rpm
sudo rpm -Uvh riak-2.1.1-1.el5.x86_64.rpm
```

### For Centos 6 / RHEL 6

You can install the `.rpm` package manually:

```bash
wget https://files.tiot.jp/riak/kv/2.1/2.1.1/rhel/6/riak-2.1.1-1.el6.src.rpm
sudo rpm -Uvh riak-2.1.1-1.el6.x86_64.rpm
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
wget http://s3.amazonaws.com/downloads.basho.com/riak/2.1/2.1.1/riak-2.1.1.tar.gz
tar zxvf riak-2.1.1.tar.gz
cd riak-2.1.1
make rel
```

You will now have a fresh build of Riak in the `rel/riak` directory.

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].
