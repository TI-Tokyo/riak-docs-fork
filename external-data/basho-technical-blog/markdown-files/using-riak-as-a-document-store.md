---
title: "Using Riak as a Document Store"
description: "January 6, 2015 If you have read about Riak, or seen a member of the Riak team present, you have probably heard the phrase “Your data is opaque to Riak.” While this is not, strictly, true with the inclusion of distributed Data Types in Riak 2.0, it was a phrase that hinted at the core structure of"
project: community
lastmod: 2015-05-28T19:23:32+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Tyler Hannan"
pub_date: 2015-01-06T13:58:01+00:00
---
January 6, 2015
If you have read about Riak, or seen a member of the Riak team present, you have probably heard the phrase “Your data is opaque to Riak.” While this is not, strictly, true with the inclusion of distributed Data Types in Riak 2.0, it was a phrase that hinted at the core structure of Riak itself.
Riak is a Key Value data store.
In a relational database, data is organized by tables that are separate and unique structures. Within these tables exist rows of data organized into columns. As such, interaction with the database is by retrieving or updating entire tables, individual rows, or a group of columns within a set of rows.
In contrast, Riak has a much simpler data model. An Object is both the largest and smallest element of data. As such, interaction with the database is by retrieving or modifying the entire object. There is no partial fetch or update of the data.
Keys in Riak are simply a binary value (or a string) that are used to identify Objects. The Key/Value pair (or Object) is stored in a higher level namespace called a Bucket. And, with Riak 2.0, there is an extra layer of abstraction known as Bucket Types.
This Key/Value/Bucket model enables broad flexibility in modeling the applications data domain with Riak as the data store for persistence.
Another NoSQL model that many are familiar with is the document store. Unlike the Key/Value model the data store is aware of the structure of the objects stored. These objects, or documents, are grouped into “collections” — which is analogous to a relational “table” — and the datastore provides a query mechanism to search collections for objects with particular attributes. When the data that is being persisted is easily rendered as a JSON document, a document store can seem a natural fit. Some common use cases include product catalog data and content management systems.
The Riak Docs have a lengthy tutorial entitled Using Riak as a Document Store that walks you through the process of leveraging Riak as a document store for a CMS. There are many approaches to modeling, but the tutorial demonstrates the power of Riak 2.0 features by combining the maps data type and indexing that data with Riak Search.
When the data you are persisting can be represented as JSON, and you require the ability to query the data, Riak 2.0 is an excellent solution for persisting and modeling document data. The flexibility of the Key/Value model, combined with the power of Riak Search and Riak Data Types, provide you with a highly scalable, highly available document store with rich, full-text query capabilities. In addition, the inclusion of the maps data type means that you don’t have to write complex client side resolution logic when faced with network partitions. Riak Data Types handle that conflict resolution automatically.
A scalable, available document store that is operationally simple may seem compelling enough to use Riak. But when you combine the characteristics of Riak with the multi-datacenter replication capabilities of Riak Enterprise, now you have a solution that enables you to bring your data operations closer to the end user.
Scalable, available, operationally simple, and replicated. That’s the power of using Riak as a document store.
Tyler Hannan
