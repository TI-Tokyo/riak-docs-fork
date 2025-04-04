---
title_supertext: "Installing on"
title: "Oracle Linux"
description: ""
project: "riak_kv"
<<<<<<<< HEAD:content/riak/kv/3.2.5/setup/installing/oracle-linux.md
project_version: "3.2.5"
========
project_version: "3.2.3"
>>>>>>>> master:content/riak/kv/3.2.3/setup/installing/oracle-linux.md
lastmod: 2023-12-08T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
<<<<<<<< HEAD:content/riak/kv/3.2.5/setup/installing/oracle-linux.md
  riak_kv-3.2.5:
========
  riak_kv-3.2.3:
>>>>>>>> master:content/riak/kv/3.2.3/setup/installing/oracle-linux.md
    name: "Oracle Linux"
    identifier: "installing_oraclelinux"
    weight: 306
    parent: "installing"
toc: true
version_history:
  in: "3.0.3+"
aliases:
<<<<<<<< HEAD:content/riak/kv/3.2.5/setup/installing/oracle-linux.md
  - /riak/3.2.5/ops/building/installing/Installing-on-Oracle-Linux
  - /riak/kv/3.2.5/ops/building/installing/Installing-on-Oracle-Linux
  - /riak/3.2.5/installing/Oracle-Linux/
  - /riak/kv/3.2.5/installing/Oracle-Linux/
---

[install source index]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/source
[install source erlang]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.2.5/setup/installing/verify
========
  - /riak/3.2.3/ops/building/installing/Installing-on-Oracle-Linux
  - /riak/kv/3.2.3/ops/building/installing/Installing-on-Oracle-Linux
  - /riak/3.2.3/installing/Oracle-Linux/
  - /riak/kv/3.2.3/installing/Oracle-Linux/
---

[install source index]: {{<baseurl>}}riak/kv/3.2.3/setup/installing/source
[install source erlang]: {{<baseurl>}}riak/kv/3.2.3/setup/installing/source/erlang
[install verify]: {{<baseurl>}}riak/kv/3.2.3/setup/installing/verify
>>>>>>>> master:content/riak/kv/3.2.3/setup/installing/oracle-linux.md

## Installing From Package

If you wish to install the Oracle Linux package by hand, follow these
instructions.

### For Oracle Linux 8

**Note** There are various Riak packages available for different OTP versions, please ensure that you are using the correct package for your OTP version.

Before installing Riak on Oracle Linux 9, we need to satisfy some Erlang dependencies
from EPEL first by installing the EPEL repository:

```bash
sudo yum install -y epel-release
```

Once the EPEL has been installed, you can install Riak on Oracle Linux 8 using yum, which we recommend::

```bash
<<<<<<<< HEAD:content/riak/kv/3.2.5/setup/installing/oracle-linux.md
wget https://files.tiot.jp/riak/kv/3.2/3.2.5/oracle/9/riak-3.2.5.OTP25-1.el9.x86_64.rpm
sudo yum install -y riak-3.2.5.OTP25-1.el9.x86_64.rpm
========
wget https://files.tiot.jp/riak/kv/3.2/3.2.3/oracle/9/riak-3.2.3.OTP25-1.el9.x86_64.rpm
sudo yum install -y riak-3.2.3.OTP25-1.el9.x86_64.rpm
>>>>>>>> master:content/riak/kv/3.2.3/setup/installing/oracle-linux.md
```

## Next Steps

Now that Riak is installed, check out [Verifying a Riak Installation][install verify].

