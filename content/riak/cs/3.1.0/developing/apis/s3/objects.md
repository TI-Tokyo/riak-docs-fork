---
title_supertext: "APIs > S3"
title: "Manage Objects"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Objects"
    identifier: "develop_apis_s3_objects"
    weight: 201
    parent: "develop_apis_s3"
project: "riak_cs"
project_version: "3.1.0"
toc: true
aliases:
since: 3.1.0
---

Create, Update, Retrieve, Delete Objects, and Manage their ACLs.

## Create or Update

- [Create or Write (PUT) Object](./put-object)
- [Duplicate an Object (PUT with Copy)](./put-object-copy)
- [Upload large Objects (Multi-Part Uploads)](./multipart-uploads)
  - [Initiate Multi-Part Upload](./multipart-uploads/initiate-multipart-upload)
  - [Upload Parts](./multipart-uploads/upload-part)
  - [Complete a Multi-Part Upload](./multipart-uploads/complete-multipart-upload)
  - [Abort a Multi-Part Upload](./multipart-uploads/abort-multipart-upload)
  - [List the parts already uploaded](./multipart-uploads/list-parts)
  - [List Multi-Part Uploads that are still in-progress](./multipart-uploads/list-multipart-uploads)

## Retrieve

- [Get an Object](./get-object)
- [Get only the HEAD of an Object](./head-object)

## Delete

- [Delete an Object](./delete-object)
- [Delete multiple Objects](./delete-multiple)

## Manage ACLs

- [Create or Update ACL for an Object (PUT Object ACL)](./put-object-acl)
- [Get the ACL of an Object](./get-object-acl)
