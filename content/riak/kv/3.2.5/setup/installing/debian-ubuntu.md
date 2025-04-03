---
title_supertext: "Installing on"
title: "Debian and Ubuntu"
description: ""
project: "riak_kv"
project_version: "3.2.5"
lastmod: 2022-12-30T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.5:
    name: "Debian & Ubuntu"
    identifier: "installing_debian_ubuntu"
    weight: 303
    parent: "installing"
toc: true
aliases:
  - /riak/3.2.5/ops/building/installing/Installing-on-Debian-and-Ubuntu
  - /riak/kv/3.2.5/ops/building/installing/Installing-on-Debian-and-Ubuntu
  - /riak/3.2.5/installing/debian-ubuntu/
  - /riak/kv/3.2.5/installing/debian-ubuntu/
---

[install source index]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/source/
[security index]: {{<baseurl>}}riak/kv/3.2.5/using/security/
[install source erlang]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/verify

Riak KV can be installed on Debian or Ubuntu-based systems using a binary
package or by compiling from source code.

The following steps have been tested to work with Riak KV on:

- Ubuntu 20.04
- Ubuntu 22.04
- Ubuntu 24.04
- Debian 9.0
- Debian 10.0
- Debian 11.0

## Installing From Package

If you wish to install the deb packages by hand, follow these
instructions.

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
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/ubuntu/focal64/riak_3.2.5-OTP25_amd64.deb
sudo dpkg -i riak_3.2.5-OTP25_amd64.deb
```

#### Ubuntu Jammy Jellyfix (OtP 24) (22.04.5)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/ubuntu/jammy64/riak_3.2.5-OTP25_amd64.deb
sudo dpkg -i riak_3.2.5-OTP25_amd64.deb
```

#### Ubuntu Noble Numbat (24.04.01)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/ubuntu/noble64/riak_3.2.5-OTP25_amd64.deb
sudo dpkg -i riak_3.2.5-OTP25_amd64.deb
```

#### Debian Buster (10.0)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/debian/10/riak_3.2.5-OTP25_amd64.deb
sudo dpkg -i riak_3.2.5-OTP25_amd64.deb
```

#### Debian bullseye (11.0)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/debian/11/riak_3.2.5-OTP25_amd64.deb
sudo dpkg -i riak_3.2.5-OTP25_amd64.deb
```

#### Debian Bookworm (12.0)

```bash
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/debian/12/riak_3.2.5-OTP25_amd64.deb
sudo dpkg -i riak_3.2.5-OTP25_amd64.deb
```

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].

