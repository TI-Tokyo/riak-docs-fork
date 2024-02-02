---
title: "Strong Consistency in Riak 2.0"
description: "November 10, 2014 Many data needs are better served by data stores that are optimized for maximum availability and scalability -- rather than optimized for consistency. For certain use cases, there are elements to the data that require strong consistency. With Riak 2.0, in addition to eventual co"
project: community
lastmod: 2016-10-20T08:33:31+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2014-11-10T15:42:45+00:00
---
November 10, 2014
Many data needs are better served by data stores that are optimized for maximum availability and scalability — rather than optimized for consistency. For certain use cases, there are elements to the data that require strong consistency. With Riak 2.0, in addition to eventual consistency, there is now a way to enforce strong consistency when needed.
NOTE: Riak’s strong consistency feature is currently an open-source-only feature and is not yet commercially supported.
Behavioral Changes with Strong Consistency
Strongly consistent operations in Riak function much like eventually consistent operations at the application level. The core difference lies in the types of errors Riak will report to the client.
Each request to update an object (except for the initial creation) must include a context value reflecting the last time the application read it. This is the same behavior that Riak clients have always followed with version vectors and strong consistency also mandates its use. Similarly, reading data from a strongly consistent Riak bucket functions exactly like eventually consistent reads.
If that value is not provided for an update operation to an existing object, Riak will reject it. This is because the database assumes that you have not seen the current value and may not know what you’re doing.
Similarly, if that context value is out of date, Riak will also reject update operations. The client must re-read the latest value and supply an update based on that new value, with the new context.
If Riak cannot contact a majority of the servers responsible for the key, the request will fail. Ordinarily, Riak is happy to accept all operations in the interest of high availability and never dropping a write – even in the extreme case of only one server surviving data center outages.
Strong consistency also eliminates object siblings, as it is effectively impossible for the cluster to disagree on the value of an object.
Use Case(s)
When considering consistency models in an application, it is easy for the logic to quickly become daunting. This is especially true when designing a workflow that leverages both eventually and strongly consistent models. It is, therefore, easiest to begin with a simple use case.
Consider the workflow involved in storing and updating username and password data. In the case of a password update, it is necessary that — at any given time — there be exactly ONE result for a user’s password. Relatedly, it is important to ensure that an update of this value is fully atomic or user experience is substantially degraded. It would be possible to leverage Riak for all the eventually consistent elements of the application and leverage strong consistency for the username and password.
To see how eventual and strong consistency can be combined to solve business problems, let’s take a not-so-hypothetical example from the energy industry.
Imagine you’re collecting massive amounts of geological data for analysis. Each batch of data must be processed by a single instance of your application. Since this processing can take hours, days, or even weeks to complete, it’s expensive if two applications handle the same batch.
Let’s walk through the sequence of events.

Batch of data arrives for processing.
The batch is stored in a large object store (like, Riak CS) under a batch ID.
The batch ID is added to a pending job list in Riak and stored as a set (one of the new Riak Data Types).

This is a classic example of eventual consistency and an illustration of the value of the new Riak Data Types introduced with Riak 2.0. Storing a new batch ID in your database should never fail, even if servers are offline. If multiple applications are adding batch IDs to the pending list at the same time, it’s perfectly reasonable for those lists to temporarily diverge, as long as they can be trivially merged later.
Let’s continue to see where strong consistency comes into play.

A compute node becomes available to process the data.
The compute node retrieves the pending job list and picks a batch ID.
The compute node attempts to create a lock for that batch ID.

This is where strong consistency is required. This lock object should be created in a bucket that is managed by the new strong consistency subsystem in Riak 2.0. If someone else also grabs that batch ID and tries to create another lock object, Riak’s strong consistency logic will reject this second attempt. This compute node will just start over by grabbing a new ID.
To detect crashed jobs, the lock object should be created with basic job data, such as which compute node owns the processing job and what time it was started.

The compute node asks Riak to add the batch ID to a different set, a running job list.
The compute node asks Riak to remove the batch ID from the pending list.
The job runs.
When completed, the compute node asks Riak to add the batch ID to a completed job list.
Riak is asked to remove the batch ID from the running list.
The compute node deletes the lock object (or updates it to reflect the completion of the processing job).

Tradeoffs When Using Strong Consistency

Blind updates will be rejected, so the client must read the existing value before supplying a new one (except in the case of entirely new keys).
Write requests may be slightly slower due to coordination overhead.
If a majority of the servers responsible for a piece of data are unavailable, write requests will fail. Read operations may fail depending on the freshness of the data that is still accessible.
Secondary indexes (2i) are not yet supported.
Multi-datacenter replication in Riak Enterprise is not yet supported.

Resources

Using Strong Consistency (for developers)
Managing Strong Consistency (for operators)
Strong Consistency (theory & concepts)

Downloads
Strong Consistency is now available with Riak 2.0. Download Riak 2.0 on our Docs Page.
Riak Team
