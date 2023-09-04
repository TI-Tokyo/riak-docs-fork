---
title_supertext: "APIs > S3 > Objects"
title: "Get only the HEAD of an Object"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Get Object HEAD"
    identifier: "develop_apis_s3_objects_get_head"
    weight: 302
    parent: "develop_apis_s3_objects"
project: "riak_cs"
project_version: "3.1.0"
toc: true
aliases:
  - /riakcs/3.1.0/references/apis/storage/s3/RiakCS-HEAD-Object
  - /riak/cs/3.1.0/references/apis/storage/s3/RiakCS-HEAD-Object
  - /riakcs/3.1.0/references/apis/storage/s3/head-object
  - /riak/cs/3.1.0/references/apis/storage/s3/head-object
---

The `HEAD Object` operation retrieves metadata from an object without returning the object.

*Note:* You must have READ access to the object to use this operation.

A HEAD request has the same options as a GET operation on an object, and the HEAD response is identical to the GET response, except that there is no response body.

## Requests

### Request Syntax

```curl
HEAD /ObjectName HTTP/1.1
Host: bucketname.data.basho.com
Date: date
Authorization: signature_value
```

## Examples

### Sample Request

The following request returns the metadata of an object.

```curl
HEAD /basho-process.jpg HTTP/1.1
Host: bucket.data.basho.com
Date: Wed, 06 Jun 2012 20:47:15 +0000
Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```

### Sample Response

```curl
HTTP/1.1 200 OK
Date: Wed, 06 Jun 2012 20:48:15 GMT
Last-Modified: Wed, 06 Jun 2012 13:39:25 GMT
ETag: "3327731c971645a398fba9dede5f2768"
Content-Length: 611892
Content-Type: text/plain
Connection: close
Server: MochiWeb/1.1 WebMachine/1.9.0 (someone had painted it blue)
```
