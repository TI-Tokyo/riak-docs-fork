---
tile_supertext: "Configuring:"
title: "Rack Awareness"
description: ""
project: "riak_kv"
project_version: "3.0.6"
menu:
  riak_kv-3.0.6:
    name: "Rack Awareness"
    identifier: "nextgen_rep"
    weight: 200
    parent: "configuring"
version_history:
  in: "2.9.1+"
toc: true
commercial_offering: true
aliases:
---

The configuration for Rack Awarenesss can be set  in two ways:

Via riak admin:
`riak admin cluster location rack_a` on the node where the command is executed. 
`riak admin cluster location rack_a --node=dev2@127.0.0.1` to specify a specific nodes rack location.

Via riak attach:

To define a location for a node:
```riak attach
`riak_core_claimant:set_node_location(node(), "location_a"),`
```
To commit review and commit these changes:

```
riak_core_claimant:plan(),
riak_core_claimant:comit().
```