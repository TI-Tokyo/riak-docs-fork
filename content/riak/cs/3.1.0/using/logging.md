---
title_supertext: "Using:"
title: "Logging"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Logging"
    identifier: "using_log"
    weight: 400
    parent: "using"
project: "riak_cs"
project_version: "3.1.0"
aliases:
  - /riak/cs/3.1.0/cookbooks/logging/
  - /riakcs/3.1.0/cookbooks/logging/
---

In versions 1.5.0 and later, you can use Riak CS in conjunction with
[Lager](https://github.com/basho/lager), the logging framework used for
Riak. By default, all Riak CS logs can be found in the `/log` directory
of each node.

You can configure Lager for Riak CS in the `advanced.config` configuration
file in each Riak CS node, in the section of that file named `lager`.
That section looks something like this:

```advancedconfig
{lager, [
    {handlers, [
    ...

    %% Other configs
]}
```

```appconfig
{lager, [
    {handlers, [
    ...

    %% Other configs
]}
```

A full description of all available parameters can be found in the
[configuration files]({{<baseurl>}}riak/kv/3.0.9/configuring/reference#logging) document for Riak KV.
