---
title: "Riak KV 3.2.0"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2022-12-30T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.0:
    name: "Riak KV"
    identifier: "index"
    weight: 100
    pre: riak
toc: false
aliases:
  - /riak/3.2.0/
---

[aboutenterprise]: https://www.tiot.jp/en/about-us/contact-us/
[config index]: {{<baseurl>}}riak/kv/3.2.0/configuring
[downloads]: {{<baseurl>}}riak/kv/3.2.0/downloads/
[install index]: {{<baseurl>}}riak/kv/3.2.0/setup/installing/
[plan index]: {{<baseurl>}}riak/kv/3.2.0/setup/planning
[perf open files]: {{<baseurl>}}riak/kv/3.2.0/using/performance/open-files-limit
[install debian & ubuntu]: {{<baseurl>}}riak/kv/3.2.0/setup/installing/debian-ubuntu
[getting started]: {{<baseurl>}}riak/kv/3.2.0/developing/getting-started
[dev client libraries]: {{<baseurl>}}riak/kv/3.2.0/developing/client-libraries

Riak KV is a distributed NoSQL database designed to deliver maximum data availability by distributing data across multiple servers. As long as your Riak KV client can reach one Riak server, it should be able to write data.

This release is tested with OTP 22, OTP 24 and OTP 25; but optimal performance is likely to be achieved when using OTP 25.

## Supported Operating Systems

- Alpine Linux 3.16
- Alpine Linux 3.18
- Alpine Linux 3.21
- Amazon Linux 2 (AWS)
- Amazon Linux 2023
- CentOS 7
- CentOS 8
- CentOS 9
- Debian 9.0 ("Stretch")
- Debian 10.0 ("Buster")
- Debian 11.0 ("Bullseye")
- Oracle Linux 8
- Red Hat Enterprise Linux 7
- Red Hat Enterprise Linux 8
- Red Hat Enterprise Linux 9
- Raspbian Buster
- Ubuntu 18.04 ("Bionic Beaver")
- Ubuntu 20.04.4 ("Focal Fossa")
- Ubuntu 22.04 ("Jammy Jellyfish")

## Getting Started

Are you brand new to Riak KV? Start by [downloading][downloads] Riak KV, and then follow the below pages to get started:

1. [Install Riak KV][install index]
2. [Plan your Riak KV setup][plan index]
3. [Configure Riak KV for your needs][config index]

{{% note title="Developing with Riak KV" %}}
If you are looking to integrate Riak KV with your existing tools, check out the [Developing with Riak KV]({{<baseurl>}}riak/kv/3.2.0/developing) docs. They provide instructions and examples for languages such as: Java, Ruby, Python, Go, Haskell, NodeJS, Erlang, and more.
{{% /note %}}

## Popular Docs

1. [Open Files Limit][perf open files]
2. [Installing on Debian-Ubuntu][install debian & ubuntu]
3. [Developing with Riak KV: Getting Started][getting started]
4. [Developing with Riak KV: Client Libraries][dev client libraries]

