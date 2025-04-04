---
title: "PBC Set Client ID"
description: ""
project: "riak_kv"
project_version: "3.2.3"
lastmod: 2024-12-09T00:00:00-00:00
sitemap:
  priority: 0.2
menu:
  riak_kv-3.2.3:
    name: "Set Client ID"
    identifier: "pbc_set_client_id"
    weight: 126
    parent: "apis_pbc"
toc: true
aliases:
  - /riak/3.2.3/dev/references/protocol-buffers/set-client-id
  - /riak/kv/3.2.3/dev/references/protocol-buffers/set-client-id
---

{{% note title="Deprecation notice" %}}
The use of client IDs in conflict resolution is now deprecated in Riak. If you
are building or maintaining a Riak client that is intended to be compatible
with Riak 1.4 or later, you can safely ignore client IDs.
{{% /note %}}

Set the client ID for this connection. A library may want to set the
client ID if it has a good way to uniquely identify actors across
reconnects. This will reduce vector clock bloat.

## Request

```protobuf
message RpbSetClientIdReq {
    required bytes client_id = 1; // Client id to use for this connection
}
```

## Response

Just the `RpbSetClientIdResp` message code.

## Example

Request

```
Hex      00 00 00 07 05 0A 04 01 65 01 B6
Erlang <<0,0,0,7,5,10,4,1,101,1,182>>

RpbSetClientIdReq protoc decode:
client_id: "001e001266"

```

Response

```
Hex      00 00 00 01 06
Erlang <<0,0,0,1,6>>

RpbSetClientIdResp - only message code defined
```

