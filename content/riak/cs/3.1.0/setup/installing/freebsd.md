---
title_supertext: "Setup > Installing:"
title: "FreeBSD"
menu:
  riak_cs-3.1.0:
    name: "FreeBSD"
    identifier: "install-freebsd"
    weight: 130
    parent: installing
project: "riak_cs"
toc: true
project_version: "3.1.0"
aliases:
---

[files freebsd]:     https://files.tiot.jp/riak/cs/3.1/3.1.0/freebsd/
[downloads freebsd]: {{<baseurl>}}riak/cs/3.1.0/downloads#freebsd
[configure cs]:      {{<baseurl>}}riak/cs/3.1.0/configuring

## Installing Riak CS on FreeBSD

Riak CS packages for FreeBDF are hosted on
[files.tiot.jp][files freebsd].

Platform-specific pages are available on the [Downloads][downloads freebsd] page.

### pkg Installation (FreeBSD)

For the simplest installation process on LTS (Long-Term Support)
releases, download the appropriate package for your OS and OTP preference
and use `pkg -i` to install it.

For example, for the FreeBDF 13.1 with OTP 25 release of Riak CS 3.1.0 for a 64-bit architecture one would run:

```bash
wget https://files.tiot.jp/riak/cs/3.1/3.1.0/freebsd/13.1/riak_cs-3.1.0-OTP25.pkg
sudo pkg -i riak_cs-3.1.0-OTP25.pkg
```

## What's Next?

Once you've completed installation of Riak CS and Riak, you're ready to
learn more about [configuring Riak CS][configure cs].
