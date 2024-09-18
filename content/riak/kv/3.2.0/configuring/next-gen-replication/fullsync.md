---
title_supertext: "Configuring: Next Gen Replication"
title: "FullSync"
description: ""
project: "riak_kv"
project_version: "3.2.0"
lastmod: 2024-09-16T00:00:00-00:00
sitemap:
  priority: 0.9
menu:
  riak_kv-3.2.0:
    name: "FullSync"
    identifier: "nextgen_rep_fullsync"
    weight: 103
    parent: "nextgen_rep"
version_history:
  in: "2.9.1+"
toc: true
commercial_offering: false
aliases:
---

[configure tictacaae]: ../../active-anti-entropy/tictac-aae/
[configure nextgenrepl fullsync]: ../fullsync/
[configure nextgenrepl realtime]: ../realtime/
[configure nextgenrepl queuing]: ../queuing/

NextGenRepl's FullSync feature provides a considerable improvement over the legacy fullsync engines. It is faster, more efficient, and more reliable. NextGenRepl is the recommended replication engine to use.

FullSync will ensure that the data in the source cluster is also in sink cluster.

{{% note %}}
NextGenRepl relies on [TicTac AAE](../../active-anti-entropy/tictac-aae/), so this must be enabled.
{{% /note %}}

## Overview

