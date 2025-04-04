﻿---
title_supertext: "Installing on"
title: "Alpine Linux"
description: "installing Riak on Alpine Linux"
project: "riak_kv"
project_version: "3.2.5"
lastmod: 2025-03-24T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.5:
    name: "Alpine Linux"
    identifier: "installing_alpine_linux"
    weight: 301
    parent: "installing"
since: 3.0.9
version_history:
  in: "3.0.9+"
toc: true
aliases:
  - /riak/3.2.5/ops/building/installing/installing-on-alpine-linux
  - /riak/kv/3.2.5/ops/building/installing/installing-on-alpine-linux
  - /riak/3.2.5/installing/alpine-linux/
  - /riak/kv/3.2.5/installing/alpine-linux/
---

[security index]: {{<baseurl>}}riak/kv/3.2.5/using/security/
[install source erlang]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/verify

Riak KV can be installed on Alpine Linux using a binary
package from the Riak repository.

The following steps have been tested to work with Riak KV on:

* Alpine Linux 3.21 using x86_64
* Alpine Linux 3.21 using aarch64

## Riak 64-bit Installation

To install Riak on Alpine Linux:

1. Add the Riak repository:

   * Run `echo https://files.tiot.jp/alpine/v3.21/main >> /etc/apk/repositories`

2. Download and install the Riak repository public key:
   * Run `wget http://files.tiot.jp/alpine/alpine@tiot.jp.rsa.pub -O /etc/apk/keys/alpine@tiot.jp.rsa.pub`
3. Update your list of packages:
   * Run `apk update`
4. Install Riak:
   * For the latest version, run `apk add riak`
   * For version 3.2.5 using OTP 24, run `apk add riak=3.2.5.24-r1`
   * For version 3.2.5 using OTP 25, run `apk add riak=3.2.5.25-r1`



## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].
