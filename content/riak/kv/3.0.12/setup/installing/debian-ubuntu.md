---
title_supertext: "Installing on"
title: "Debian and Ubuntu"
description: ""
project: "riak_kv"
project_version: "3.0.12"
lastmod: 2022-12-20T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.0.12:
    name: "Debian & Ubuntu"
    identifier: "installing_debian_ubuntu"
    weight: 303
    parent: "installing"
toc: true
aliases:
  - /riak/3.0.12/ops/building/installing/Installing-on-Debian-and-Ubuntu
  - /riak/kv/3.0.12/ops/building/installing/Installing-on-Debian-and-Ubuntu
  - /riak/3.0.12/installing/debian-ubuntu/
  - /riak/kv/3.0.12/installing/debian-ubuntu/
---

[install source index]: {{<baseurl>}}riak/kv/3.0.12/setup/installing/source/
[security index]: {{<baseurl>}}riak/kv/3.0.12/using/security/
[install source erlang]: {{<baseurl>}}riak/kv/3.0.12/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.0.12/setup/installing/verify

Riak KV can be installed on Debian or Ubuntu-based systems using a binary
package or by compiling from source code.

The following steps have been tested to work with Riak KV on:

- Ubuntu 18.04
- Ubuntu 20.04
- Ubuntu 22.04
- Debian 9.0
- Debian 10.0
- Debian 11.0
- Raspbian Buster

## Installing From Package

If you wish to install the deb packages by hand, follow these
instructions.

### Installing on Non-LTS Ubuntu Releases

Typically we only package Riak for LTS releases to keep our build and
testing matrix focused.  In some cases, such as the historic Ubuntu 11.04 (Natty),
there are changes that affect how Riak is packaged, so we will release a
separate package for that non-LTS release. In most other cases, however,
if you are running a non-LTS release (such as 12.10) it is safe to
follow the below instructions for the LTS release prior to your release.
In the case of later subversions such as Ubuntu 12.10, follow the installation instructions for
Ubuntu 12.04.

### PAM Library Requirement for Ubuntu

One dependency that may be missing on your machine is the `libpam0g-dev`
package used for Pluggable Authentication Module (PAM) authentication,
associated with [Riak security][security index].

To install:

```bash
sudo apt-get install libpam0g-dev
```

### Riak 64-bit Installation

***Note on OTP version***
Packages for different OTP versions are available at https://iles.tiot.jp

#### Ubuntu Bionic Beaver (18.04)

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/ubuntu/bionic64/riak_3.0.12-OTP22.3_amd64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_amd64.deb
```

#### Ubuntu Focal Fossa (20.04)

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/ubuntu/focal64/riak_3.0.12-OTP22.3_amd64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_amd64.deb
```

#### Ubuntu Jammy Jellyfish (22.04)

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/ubuntu/jammy64/riak_3.0.12-OTP22.3_amd64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_amd64.deb
```

#### Debian Stretch (9.0)

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/debian/9/riak_3.0.12-OTP22.3_amd64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_amd64.deb
```

#### Debian Buster (10.0)

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/debian/10/riak_3.0.12-OTP22.3_amd64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_amd64.deb
```

#### Debian Bullseye (11.0)

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/debian/11/riak_3.0.12-OTP22.3_amd64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_amd64.deb
```

#### Raspbian Buster

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/raspbian/bullseye/riak_3.0.12-OTP22.3_arm64.deb
sudo dpkg -i riak_3.0.12-OTP22.3_arm64.deb
```

## Installing From Source

First, install Riak dependencies using apt:

```bash
sudo apt-get install build-essential libc6-dev-i386 git
```

Riak requires an [Erlang](http://www.erlang.org/) installation.
Instructions can be found in [Installing Erlang][install source erlang].

```bash
wget https://files.tiot.jp/riak/kv/3.0/3.0.12/riak-3.0.12.tar.gz
tar zxvf riak-3.0.12.tar.gz
cd riak-3.0.12
make rel
```

If the build was successful, a fresh build of Riak will exist in the
`rel/riak` directory.

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].

