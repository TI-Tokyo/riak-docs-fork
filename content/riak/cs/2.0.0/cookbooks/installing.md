---
title: "Installing Riak CS"
description: ""
menu:
  riak_cs-2.0.0:
    name: "Installing Riak CS"
    identifier: "installing"
    weight: 200
    parent: "index"
project: "riak_cs"
project_version: "2.0.0"
lastmod: 2015-03-28T00:00:00-00:00
sitemap:
  priority: 0.1
aliases:
  - /riakcs/2.0.0/cookbooks/installing/Installing-Riak-CS/
  - /riak/cs/2.0.0/cookbooks/installing/Installing-Riak-CS/
---

Riak CS is supported on a variety of operating systems, including
Ubuntu, CentOS, Fedora, Solaris, SmartOS, FreeBSD, and OS X. Riak CS is
*not* supported on Microsoft Windows.

You can install Riak CS on a single node (for development purposes) or
using an automated deployment tool. Any Riak CS installation involves
three components, all of which must be installed separately:

* [Riak KV]({{<baseurl>}}riak/kv/2.0.7/) --- The distributed database on top of which Riak CS
is built
* Riak CS itself
* [Stanchion]({{<baseurl>}}riak/cs/2.0.0/theory/stanchion) --- An application used to manage [globally unique entities]({{<baseurl>}}riak/cs/2.0.0/theory/stanchion/#globally-unique-entities) such as users and buckets.

[Riak KV](#installing-riak) and [Riak CS](#installing-riak-cs-on-a-node) must be installed on each node in your cluster. [Stanchion](#installing-stanchion-on-a-node), however, needs to be installed on only one node.

## Version Compatibility

We strongly recommend using one of the documented [version combinations]({{<baseurl>}}riak/cs/2.0.0/cookbooks/version-compatibility/)
when installing and running Riak CS.

## Installing Riak KV

Before installing Riak CS, Riak KV must be installed on each node in
your cluster. You can install Riak KV either as part of an OS-specific package
or from source.

  * [Debian and Ubuntu]({{<baseurl>}}riak/kv/2.0.7/setup/installing/debian-ubuntu)
  * [RHEL and CentOS]({{<baseurl>}}riak/kv/2.0.7/setup/installing/rhel-centos)
  * [Mac OS X]({{<baseurl>}}riak/kv/2.0.7/setup/installing/mac-osx)
  * [FreeBSD]({{<baseurl>}}riak/kv/2.0.7/setup/installing/freebsd)
  * [SUSE]({{<baseurl>}}riak/kv/2.0.7/setup/installing/suse)
  * [From Source]({{<baseurl>}}riak/kv/2.0.7/setup/installing/source)

Riak KV is also officially supported on the following public cloud
infrastructures:

  * [Windows Azure]({{<baseurl>}}riak/kv/2.0.7/setup/installing/windows-azure)
  * [AWS Marketplace]({{<baseurl>}}riak/kv/2.0.7/setup/installing/amazon-web-services)

Remember that you must repeat this installation process on each node in
your cluster. For future reference, you should make note of the Riak KV
installation directory.

If you want to fully configure Riak KV prior to installing Riak CS, see our
documentation on [configuring Riak KV for CS]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/riak-for-cs/).

## Installing Riak CS on a Node

Riak CS and Stanchion packages are available on the [Download Riak CS]({{<baseurl>}}riak/cs/2.0.0/downloads/)
page. Similarly, Riak packages are available on the [Download Riak KV]({{<baseurl>}}riak/kv/2.0.7/downloads/) page.

After downloading Riak CS, Stanchion, and Riak, install them using your
operating system's package management commands.

> **Note on Riak CS and public ports**
>
> **Riak CS is not designed to function directly on TCP port 80, and
it should not be operated in a manner that exposes it directly to the
public internet**. Instead, consider a load-balancing solution
such as a dedicated device [HAProxy](http://haproxy.1wt.eu) or [Nginx](http://wiki.nginx.org/Main) between Riak CS and the outside world.

### Installing Riak CS on Mac OS X

To install Riak CS on OS X, first download the appropriate package from
the [downloads]({{<baseurl>}}riak/cs/2.0.0/downloads) page:

```bash
curl -O https://files.tiot.jp/riak/cs/2.0/2.0.0/osx/10.8/riak-cs-2.0.0-OSX-x86_64.tar.gz
```

Then, unpack the downloaded tarball:

```bash
tar -xvzf riak-cs-2.0.0-OSX-x86_64.tar.gz
```

At this point, you can move on to [configuring Riak CS]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/riak-cs/).

### Installing Riak CS on Debian or Ubuntu

On Debian and Ubuntu, Riak CS packages are hosted on
[packagecloud.io](https://packagecloud.io/basho/riak-cs). Instructions
for installing via shell scripts, manual installation, Chef, and Puppet
can be found in packagecloud's [installation docs](https://packagecloud.io/basho/riak/install).

Platform-specific pages are linked below:

* [Lucid](https://files.tiot.jp/riak/cs/2.0/2.0.0/ubuntu/lucid/riak-cs_2.0.0-1_amd64.deb)
* [Precise](https://files.tiot.jp/riak/cs/2.0/2.0.0/ubuntu/precise/riak-cs_2.0.0-1_amd64.deb)
* [Squeeze](https://files.tiot.jp/riak/cs/2.0/2.0.0/debian/6/riak-cs_2.0.0-1_amd64.deb)
* [Trusty](https://files.tiot.jp/riak/cs/2.0/2.0.0/ubuntu/trusty/riak-cs_2.0.0-1_amd64.deb)
* [Wheezy](https://files.tiot.jp/riak/cs/2.0/2.0.0/debian/7/riak-cs_2.0.0-1_amd64.deb)


### Installing Riak CS on RHEL or CentOS

On RHEL or CentOS, Riak CS packages are hosted on
[files.tiot.jp](https://files.tiot.jp/riak/cs/).

Platform-specific pages are linked below:

* [el5](https://files.tiot.jp/riak/cs/2.0/2.0.0/rhel/5/riak-cs-2.0.0-1.el5.x86_64.rpm)
* [el6](https://packagecloud.io/basho/riak-cs/packages/el/6/riak-cs-2.0.0-1.el6.x86_64.rpm)
* [Fedora 19](https://files.tiot.jp/riak/cs/2.0/2.0.0/fedora/19/riak-cs-2.0.0-1.fc19.x86_64.rpm)

## Installing Stanchion on a Node

Stanchion is an application that manages globally unique entities within
a Riak CS cluster. It performs actions such as ensuring unique user
accounts and bucket names across the whole system. **Riak CS cannot be
used without Stanchion**.

All Riak CS nodes must be configured to communicate with a single
Stanchion node. Although multiple Stanchion instances may be installed
and running within a cluster, even one on each node, only one may be
actively used by the cluster. Running multiple instances of Stanchion
simultaneously can produce a variety of problems such as the inability
to create user accounts and buckets or the inability to enforce their
uniqueness.

Because only one Stanchion instance can be used at any given time, it's
not uncommon for a load balancer to be used to handle Stanchion failover
in the event that the primary Stanchion node becomes unavailable. You
can achieve this by specifying a load balancer IP as the Stanchion IP
in each Riak CS node's `riak-cs.conf`. This load balancer must be
configured to send all requests to a single Stanchion node, failing over
to a secondary Stanchion node if the primary is unavailable. More
details can be found in [Specifying the Stanchion Node]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/#specifying-the-stanchion-node).

### Installing Stanchion on Mac OS X

First, download the appropriate package from the [downloads]({{<baseurl>}}riak/cs/2.0.0/downloads/#stanchion-1-4-3) page.

```bash
curl -O http://s3.amazonaws.com/downloads.basho.com/stanchion/1.4/1.4.3/osx/10.8/stanchion-2.0.0-OSX-x86_64.tar.gz
```

Then, unpack the downloaded tarball:

```bash
stanchion-2.0.0-OSX-x86_64.tar.gz
```

At this point, you can move on to [configuring Riak CS]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/riak-cs).

### Installing Stanchion on Debian or Ubuntu

Stanchion files are available from [files.tiot.jp](https://files.tiot.jp/riak/stanchion/).

#### Installing the `.deb` Package Manually (not recommended)

```bash
sudo dpkg -i <stanchion-package.deb>
```

Replace `<riak-cs-package.deb>` with the actual filename for the package
you are installing.

At this point, you can move on to [configuring Riak CS]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/riak-cs).

### Installing Stanchion on RHEL or CentOS

On RHEL or CentOS, you can either use `yum` or install the `.rpm`
package manually.


#### Installing the `.rpm` Package Manually (not recommended)

```bash
sudo rpm -Uvh <stanchion-package.rpm>
```

Replace `<stanchion-package.rpm>` with the actual filename for the
package you are installing.

At this point, you can move on to [configuring Riak CS]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/riak-cs).

> **Note on SELinux**
>
> CentOS enables Security-Enhanced Linux (SELinux) by default. If you
encounter errors during installation, try disabling SELinux.

## What's Next?

Once you've completed installation of Riak CS and Riak, you're ready to
learn more about [configuring Riak CS]({{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/riak-cs).
