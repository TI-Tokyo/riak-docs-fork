---
title_supertext: "Configuring:"
title: "Legacy Active Anti-Entropy"
description: ""
project: "riak_kv"
project_version: 2.9.0p5
menu:
  riak_kv-2.9.0p5:
    name: "Legacy AAE"
    identifier: "configuring_legacy_aae"
    weight: 103
    parent: "configuring-active-anti-entropy"
toc: true
version_history:
  in: "2.9.0p5+"
since: 2.9.0p5
aliases:
---

The configuration for the legacy AAE is kept in
 the `riak.conf` configuration file. 

## Validate Settings

Once your configuration is set, you can verify its correctness by
running the `riak` command-line tool:

```bash
riak chkconfig
```

## riak.conf Settings

Setting | Options | Default | Description
:-------|:--------|:--------|:-----------
