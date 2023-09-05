---
title_supertext: "APIs > S3 > Buckets:"
title: "Delete a Bucket"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Delete"
    identifier: "develop_apis_s3_buckets_delete"
    weight: 301
    parent: "develop_apis_s3_buckets"
project: "riak_cs"
project_version: "3.1.0"
toc: true
aliases:
  - /riakcs/3.1.0/references/apis/storage/s3/RiakCS-DELETE-Bucket
  - /riak/cs/3.1.0/references/apis/storage/s3/RiakCS-DELETE-Bucket
  - /riakcs/3.1.0/references/apis/storage/s3/delete-bucket
  - /riak/cs/3.1.0/references/apis/storage/s3/delete-bucket
---

The `DELETE Bucket` operation deletes the bucket specified in the URI.

{{% note title="Note" %}}
All objects in the bucket must be deleted before you can delete the bucket.
{{% /note %}}

## Requests

### Request Syntax

```curl
DELETE / HTTP/1.1
Host: bucketname.data.basho.com
Date: date
Authorization: signature_value
```

## Responses

DELETE Bucket uses only common response headers and doesn't return any response elements.

## Examples

### Sample Request

The DELETE Bucket operation deletes the bucket name "projects".

```curl
DELETE / HTTP/1.1
Host: projects.data.basho.com
Date: Wed, 06 Jun 2012 20:47:15 +0000
Authorization: AWS QMUG3D7KP5OQZRDSQWB6:4Pb+A0YT4FhZYeqMdDhYls9f9AM=
```

### Sample Response

```curl
HTTP/1.1 204 No Content
Date: Wed, 06 Jun 2012 20:47:15 +0000
Connection: close
Server: MochiWeb/1.1 WebMachine/1.9.0 (someone had painted it blue)
```
