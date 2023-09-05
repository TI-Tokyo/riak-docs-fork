---
title_supertext: "APIs > S3 > Buckets:"
title: "Delete a Bucket policy"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Delete Policy"
    identifier: "develop_apis_s3_buckets_delete_policy"
    weight: 402
    parent: "develop_apis_s3_buckets"
project: "riak_cs"
project_version: "3.1.0"
toc: true
aliases:
  - /riakcs/3.1.0/references/apis/storage/s3/RiakCS-DELETE-Bucket-policy
  - /riak/cs/3.1.0/references/apis/storage/s3/RiakCS-DELETE-Bucket-policy
  - /riakcs/3.1.0/references/apis/storage/s3/delete-bucket-policy
  - /riak/cs/3.1.0/references/apis/storage/s3/delete-bucket-policy
---

The `DELETE Bucket policy` operation deletes the `policy` subresource of an existing bucket. To perform this operation, you must be the bucket owner.

## Requests

### Request Syntax

```curl
DELETE /?policy HTTP/1.1
Host: bucketname.data.basho.com
Date: date
Authorization: signatureValue

```

### Request Parameters

This operation does not use request parameters.

### Request Headers

This operation uses only request headers that are common to all operations. For more information, see [Common Riak CS Request Headers]({{<baseurl>}}riak/cs/3.0.1/references/apis/storage/s3/common-request-headers).

### Request Elements

No body should be appended.

## Response

### Response Headers

This implementation of the operation uses only response headers that are common to most responses. For more information, see [Common Riak CS Response Headers]({{<baseurl>}}riak/cs/3.0.1/references/apis/storage/s3/common-response-headers).

### Response Elements

`DELETE` response elements return whether the operation succeeded or not.

## Examples

### Sample Request

The following request shows the DELETE individual policy request for the bucket.

```curl
DELETE /?policy HTTP/1.1
Host: bucketname.data.basho.com
Date: Tue, 04 Apr 2010 20:34:56 GMT
Authorization: AWS AKIAIOSFODNN7EXAMPLE:xQE0diMbLRepdf3YB+FIEXAMPLE=

```

### Sample Response

```curl
HTTP/1.1 204 No Content
Date: Tue, 04 Apr 2010 12:00:01 GMT
Connection: keep-alive
Server: Riak CS
```
