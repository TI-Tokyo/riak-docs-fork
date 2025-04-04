---
title: "PBC Auth Request"
description: ""
project: "riak_kv"
project_version: "3.2.4"
<<<<<<< HEAD
lastmod: 2025-01-26T00:00:00-00:00

sitemap:
  priority: 0.2
=======
lastmod: 2022-12-30T00:00:00-00:00
sitemap:
  priority: 0.9
>>>>>>> master
menu:
  riak_kv-3.2.4:
    name: "Auth Request"
    identifier: "pbc_auth_request"
    weight: 125
    parent: "apis_pbc"
toc: true
aliases:
  - /riak/3.2.4/dev/references/protocol-buffers/auth-req
  - /riak/kv/3.2.4/dev/references/protocol-buffers/auth-req
---

Sends a username (`user`) and password (`password`) to Riak as part of
an authentication request. Both values are sent as binaries.

## Request

```protobuf
message RpbAuthReq {
    required bytes user = 1;
    required bytes password = 2;
}
```

For more on authentication, see our documentation on [Authentication and Authorization]({{<baseurl>}}riak/kv/3.2.4/using/security/basics).

