---
title: Forward Incompatibility of app.config
project: riak
lastmod: 2015-04-15T00:00:00-00:00
sitemap:
  priority: 0.5
version: 1.0.0+
versions: false
document: reference
---

Info | Value
:----|:-----
Date issued | April 15, 2015
Product | Riak
Affected Riak versions | 2.0.0+, 2.1.0+

## Overview

A forward incompatibility of app.config between 1.x and 2.x has been
identified.

## Description

There is a forward compatibility issue when upgrading from the 1.x series to the
2.x series and continuing to use an app.config instead of migrating to the new
riak.conf introduced in 2.0. This issue results in different default bucket
properties being applied to un-typed buckets in 2.x, specifically for the
`allow_mult` and `dvv_enabled` parameters. In 1.x `allow_mult` defaulted to
`false` but defaults to `true` in 2.x. The `dvv_enabled` parameter did not exist
in 1.x, and defaults to `true` in 2.x.

## Impact

A switch from `&#123;allow_mult, false&#125;` to `&#123;allow_mult, true&#125;` can cause
unbounded growth of objects as siblings are added each time the object is updated
unless the client application is explicitly resolving siblings.

## Mitigation Strategy

This issue can be mitigated by adding `&#123;allow_mult, false&#125;` to
`default_bucket_props` in the `riak_core` section of the app.config.
`&#123;dvv_enabled, false&#125;` can also be added, however enabling Dotted Version Vectors
should have no negative impact on the cluster.

```app.config
  &#123;riak_core, [
    . . .
    {default_bucket_props, [
      &#123;allow_mult, false&#125;,
      &#123;dvv_enabled, false&#125;]},
    . . .
```
