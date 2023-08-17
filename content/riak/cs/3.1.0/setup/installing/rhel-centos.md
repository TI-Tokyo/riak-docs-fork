---
title: "RHEL & Centos"
title_supertext: "Installing on"
menu:
  riak_cs-3.1.0:
    name: "RHEL & Centos"
    identifier: "install-rhel-centos"
    parent: installing
    weight: 150
project: "riak_cs"
project_version: "3.1.0"
toc: true
aliases:
---

[files rhel-centos]:     https://files.tiot.jp/riak/cs/3.1/3.1.0/rhel/
[downloads rhel-centos]: {{<baseurl>}}riak/cs/3.1.0/downloads#oracle-linux
[configure cs]:          {{<baseurl>}}riak/cs/3.1.0/configuring

## Installing Riak CS on Oracle Linux

Riak CS packages for Oracle Linux are hosted on
[files.tiot.jp][files rhel-centos].

Platform-specific pages are available on the [Downloads][downloads rhel-centos] page.

### yum Installation

For the simplest installation process on LTS (Long-Term Support)
releases, download the appropriate package for your OS and OTP preference
and use `yum localinstall` to install it.

For example, for the Centos 8 with OTP 25 release of Riak CS 3.1.0 for a 64-bit architecture one would run:

```bash
wget https://files.tiot.jp/riak/cs/3.1/3.1.0/rhel/8/riak-cs-3.1.0.OTP25-1.el8.x86_64.rpm
sudo yum localinstall -y riak-cs-3.1.0.OTP25-1.el8.x86_64.rpm
```

## What's Next?

Once you've completed installation of Riak CS and Riak, you're ready to
learn more about [configuring Riak CS][configure cs].
