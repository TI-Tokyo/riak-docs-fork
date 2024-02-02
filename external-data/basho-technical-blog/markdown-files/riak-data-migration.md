---
title: "Riak Data Migration"
description: "A few considerations to keep in mind when migrating data to Riak."
project: community
lastmod: 2015-05-28T19:23:34+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-11-19T18:04:35+00:00
---
November 19, 2013
Implementing a database for a new project is a relatively straightforward process. However, when challenges of scalability are encountered in existing applications or workflows, it may be necessary to migrate data from an existing database solution to Riak. Our Professional Services team specializes in this type of engagement (Contact Us if you need help) and has put together a general set of considerations and guidelines when migrating to Riak.
When migrating data to Riak, we recommend a staged approach – migrating specific areas to Riak while continuing to run any existing data storage architecture. For each stage, pick a standalone logical unit of data, convert it to a storage format appropriate to Riak, consider how the data will be accessed, and write the migration scripts.
You should start with areas of data that have a one-to-one relationship, which makes them easier to model as a pair of keys and values (such as sessions, user preferences or profiles, logs, or straight content). This type of data can be easy to identify, as it usually will have a readily available key, such as a user id or session id.
Once you have isolated this data, you need to plan how it will be stored in Riak. In most cases, the keys will be dictated by the existing application data (the format of the session id or user id will be already be defined) and these objects can be reused as Riak object keys. The format of your object payload will also help dictate how it’s stored in Riak. Small binaries (PDFs or small images) can be stored as binary blobs, structured tables or other data can be stored as JSON or XML, and accompanying metadata can be stored as custom Riak headers.
Once the data model is defined, the act of migration is straightforward. Extract the relevant data from the existing system, create appropriate Riak objects, and upload the data. It’s hard to get much simpler than writing keys and values.
As you continue to migrate more difficult relational data, or need help during any step of the way, we have extensive documentation at docs.riak.com, the Riak users mailing list, and the Professional Services team is always available to answer questions or even help manage your transition.
Riak
