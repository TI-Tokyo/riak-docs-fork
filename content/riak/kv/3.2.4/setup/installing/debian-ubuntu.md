---
title_supertext: "Installing on"
title: "Debian and Ubuntu"
description: ""
project: "riak_kv"
project_version: "3.2.4"
lastmod: 2025-01-26T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.4:
    name: "Debian & Ubuntu"
    identifier: "installing_debian_ubuntu"
    weight: 303
    parent: "installing"
toc: true
aliases:
  - /riak/3.2.4/ops/building/installing/Installing-on-Debian-and-Ubuntu
  - /riak/kv/3.2.4/ops/building/installing/Installing-on-Debian-and-Ubuntu
  - /riak/3.2.4/installing/debian-ubuntu/
  - /riak/kv/3.2.4/installing/debian-ubuntu/
---

[install source index]: {{<baseurl>}}riak/kv/3.2.4/setup/installing/source/
[security index]: {{<baseurl>}}riak/kv/3.2.4/using/security/
[install source erlang]: {{<baseurl>}}riak/kv/3.2.4/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.2.4/setup/installing/verify

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
***Note on OTP version***
Packages for different OTP versions are available at https://files.tiot.jp

#### Ubuntu Focal Fossa (OTP 25) (20.04.6)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/ubuntu/focal64/riak_3.2.4-OTP25_amd64.deb
sudo dpkg -i riak_3.2.4-OTP25_amd64.deb
```

#### Ubuntu Jammy Jellyfix (OtP 24) (22.04.5)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/ubuntu/jammy64/riak_3.2.4-OTP25_amd64.deb
sudo dpkg -i riak_3.2.4-OTP25_amd64.deb
```

#### Ubuntu Noble Numbat (24.04.01)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/ubuntu/noble64/riak_3.2.4-OTP25_amd64.deb
sudo dpkg -i riak_3.2.4-OTP25_amd64.deb
```

#### Debian Buster (10.0)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/debian/10/riak_3.2.4-OTP25_amd64.deb
sudo dpkg -i riak_3.2.4-OTP25_amd64.deb
```

#### Debian bullseye (11.0)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/debian/11/riak_3.2.4-OTP25_amd64.deb
sudo dpkg -i riak_3.2.4-OTP25_amd64.deb
```

#### Debian Bookworm (12.0)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/debian/12/riak_3.2.4-OTP25_amd64.deb
sudo dpkg -i riak_3.2.4-OTP25_amd64.deb
```

#### Raspbian Bullseye

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.4/raspbian/bullseye/riak_3.2.4-OTP22_arm64.deb
sudo dpkg -i riak_3.2.4-OTP22_arm64.deb
```

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].

