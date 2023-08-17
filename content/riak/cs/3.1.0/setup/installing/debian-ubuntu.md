---
title: "Debian & Ubuntu"
title_supertext: "Installing on"
menu:
  riak_cs-3.1.0:
    name: "Debian & Ubuntu"
    identifier: "install-debian-ubuntu"
    weight: 120
    parent: installing
project: "riak_cs"
toc: true
project_version: "3.1.0"
aliases:
---

[files debian]:      https://files.tiot.jp/riak/cs/3.1/3.1.0/debian/
[files ubuntu]:      https://files.tiot.jp/riak/cs/3.1/3.1.0/ubuntu/
[downloads debian]:  {{<baseurl>}}riak/cs/3.1.0/downloads#debian
[downloads ubuntu]:  {{<baseurl>}}riak/cs/3.1.0/downloads#ubuntu
[configure cs]:      {{<baseurl>}}riak/cs/3.1.0/configuring

## Installing Riak CS on Debian

Riak CS packages for Debian are hosted on
[files.tiot.jp][files debian].

Platform-specific pages are available on the [Downloads][downloads debian] page.

### dpkg Installation

For the simplest installation process on LTS (Long-Term Support)
releases, download the appropriate package for your OS and OTP preference
and use `dpkg -i` to install it.

For example, for the Debian 11 with OTP 25 release of Riak CS 3.1.0 for a 64-bit architecture one would run:

```bash
wget https://files.tiot.jp/riak/cs/3.1/3.1.0/debian/11/riak-cs_3.1.0-OTP25_amd64.deb
sudo dpkg -i riak-cs_3.1.0-OTP25_amd64.deb
```

## Installing Riak CS on Ubuntu

Riak CS packages for Ubuntu are hosted on
[files.tiot.jp][files ubuntu].

Platform-specific pages are available on the [Downloads][downloads ubuntu] page.

### dpkg Installation

For the simplest installation process on LTS (Long-Term Support)
releases, download the appropriate package for your OS and OTP preference
and use `dpkg -i` to install it.

For example, for the Ubuntu 22.04 (Jammy) with OTP 25 release of Riak CS 3.1.0 for a 64-bit architecture one would run:

```bash
wget https://files.tiot.jp/riak/cs/3.1/3.1.0/ubuntu/jammy_64/riak-cs_3.1.0-OTP25_amd64.deb
sudo dpkg -i riak-cs_3.1.0-OTP25_amd64.deb
```

## What's Next?

Once you've completed installation of Riak CS and Riak, you're ready to
learn more about [configuring Riak CS][configure cs].
