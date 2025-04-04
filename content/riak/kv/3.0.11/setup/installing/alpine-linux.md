﻿---
title_supertext: "Installing on"
title: "Alpine Linux"
description: "installing Riak on Alpine Linux"
project: "riak_kv"
project_version: "3.0.11"
lastmod: 2022-10-12T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.0.11:
    name: "Alpine Linux"
    identifier: "installing_alpine_linux"
    weight: 301
    parent: "installing"
since: 3.0.9
version_history:
  in: "3.0.9+"
toc: true
aliases:
  - /riak/3.0.11/ops/building/installing/installing-on-alpine-linux
  - /riak/kv/3.0.11/ops/building/installing/installing-on-alpine-linux
  - /riak/3.0.11/installing/alpine-linux/
  - /riak/kv/3.0.11/installing/alpine-linux/
---

[security index]: {{<baseurl>}}riak/kv/3.0.11/using/security/
[install source erlang]: {{<baseurl>}}riak/kv/3.0.11/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.0.11/setup/installing/verify

Riak KV can be installed on Alpine Linux using a binary
package from the Riak repository.

The following steps have been tested to work with Riak KV on:

* Alpine Linux 3.16 using x86_64
* Alpine Linux 3.16 using aarch64

## Riak 64-bit Installation

To install Riak on Alpine Linux:

1. Add the Riak repository:
   * Run `sudo echo "https://files.tiot.jp/alpine/v3.16/main" >> /etc/apk/repositories`
2. Change directory to place the repository key:
   * Run `cd  /etc/apk/keys/`
3.  Download and install the Riak repository public key:
   * Run `sudo curl http://files.tiot.jp/alpine/alpine@tiot.jp.rsa.pub -O`
4. Update your list of packages:
   * Run `apk update`
5. Install Riak:
   * For the latest version, run `apk add riak`
   * For a specific version, run `apk add riak=3.0.11-r1`

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].
