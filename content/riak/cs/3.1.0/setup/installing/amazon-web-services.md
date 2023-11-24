---
title_supertext: "Setup > Installing:"
title: "Amazon Web Services"
menu:
  riak_cs-3.1.0:
    name: "Amazon Web Services"
    identifier: "install-amazon-web-services"
    weight: 110
    parent: installing
project: "riak_cs"
toc: true
project_version: "3.1.0"
aliases:
---

[files amazon]:      https://files.tiot.jp/riak/cs/3.1/3.1.0/amazon/
[downloads amazon]:  {{<baseurl>}}riak/cs/3.1.0/downloads#amazon-linux
[configure cs]:      {{<baseurl>}}riak/cs/3.1.0/configuring

## Installing Riak CS on Amazon Linux 2

Riak CS packages for Amazon Linux are hosted on
[files.tiot.jp][files amazon].

Platform-specific pages are available on the [Downloads][downloads amazon] page.

### yum Installation

For the simplest installation process on LTS (Long-Term Support)
releases, download the appropriate package for your OS and OTP preference
and use `yum localinstall` to install it.

For example, for the Amazon Linux 2 with OTP 25 release of Riak CS 3.1.0 for a 64-bit architecture one would run:

```bash
wget https://files.tiot.jp/riak/cs/3.1/3.1.0/amazon/2/riak-cs-3.1.0.OTP25-1.amzn2.x86_64.rpm
sudo yum localinstall -y riak-cs-3.1.0.OTP25-1.amzn2.x86_64.rpm
```

## What's Next?

Once you've completed installation of Riak CS and Riak, you're ready to
learn more about [configuring Riak CS][configure cs].
