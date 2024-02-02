---
title: "Schema Design in Riak - Introduction"
description: "March 19, 2010 One of the challenges of switching from a relational database (Oracle, MySQL, etc.) to a "NoSQL" database like Riak is understanding how to represent your data within the database. This post is the beginning of a series of entries on how to structure your data within Riak in useful"
project: community
lastmod: 2015-05-28T19:24:18+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2010-03-19T22:29:23+00:00
---
March 19, 2010
One of the challenges of switching from a relational database (Oracle, MySQL, etc.) to a “NoSQL” database like Riak is understanding how to represent your data within the database. This post is the beginning of a series of entries on how to structure your data within Riak in useful ways.
Choices have consequences
There are many reasons why you might choose Riak for your database, and I’m going to explain how a few of those reasons will affect the way your data is structured and manipulated.
One oft-cited reason for choosing Riak, and other alternative databases, is the need to manage huge amounts of data, collectively called “Big Data”. If you’re storing lots of data, you’re less likely to be doing online queries across large swaths of the data. You might be doing real-time aggregation in addition to calculating longer-term information in the background or offline. You might have one system collecting the data and another processing it. You might be storing loosely-structured information like log data or ad impressions. All of these use-cases call for low ceremony, high availability for writes, and little need for robust ways of finding data — perfect for a key/value-style scheme.
Another reason one might pick Riak is for flexibility in modeling your data. Riak will store any data you tell it to in a content-agnostic way — it does not enforce tables, columns, or referential integrity. This means you can store binary files right alongside more programmer-transparent formats like JSON or XML. Using Riak as a sort of “document database” (semi-structured, mostly de-normalized data) and “attachment storage” will have different needs than the key/value-style scheme — namely, the need for efficient online-queries, conflict resolution, increased internal semantics, and robust expressions of relationships.
The third reason for choosing Riak I want to discuss is related to CAP – in that Riak prefers A (Availability) over C (Consistency). In contrast to a traditional relational database system, in which transactional semantics ensure that a datum will always be in a consistent state, Riak chooses to accept writes even if the state of the object has been changed by another client (in the case of a race-condition), or if the cluster was partitioned and the state of the object diverges. These architecture choices bring to the fore something we should have been considering all along — how should our applications deal with inconsistency? Riak lets you choose whether to let the “last one win” or to resolve the conflict in your application by automated or human-assisted means.
More mindful domain modeling
What’s the moral of these three stories? When modeling your data in Riak, you need to understand better the shape of your data. You can no longer rely on normalization, foreign key constraints, secondary indexes and transactions to make decisions for you.
Questions you might ask yourself when designing your schema:

Will my access pattern be read-heavy, write-heavy, or balanced?
Which datasets churn the most? Which ones require more sophisticated conflict resolution?
How will I find this particular type of data? Which method is most efficient?
How independent/interrelated is this type of data with this other type of data? Do they belong together?
What is an appropriate key-scheme for this data? Should I choose my own or let Riak choose?
How much will I need to do online queries on this data? How quickly do I need them to return results?
What internal structure, if any, best suits this data?
Does the structure of this data promote future design modifications?
How resilient will the structure of the data be if requirements change? How can the change be effected without serious interruption of service?

I like to draw up my domain concepts on a pad of unlined paper or a whiteboard with boxes and arrows, then figure out how they map onto the database. Ultimately, the concepts define your application, so get those solid before you even worry about Riak.
Thinking non-relationally
Once you’ve thought carefully about the questions described above, it’s time think about how your data will map to Riak. We’ll start from the small-scale in this post (single domain concepts) and work our way out in future installments.
Internal structure
For a single class of objects in your domain, let’s consider the structure of that data. Here’s where you’re going to decide two interrelated issues — how this class of data will be queried and how opaque its internal structure will be to Riak.
The first issue, how the data will be queried, depends partly on how easy it is to intuit the key of a desired object. For example, if your data is user profiles that are mostly private, perhaps the user’s email or login name would be appropriate for the key, which would be easy to establish when the user logs in. However, if the key is not so easy to determine, or is arbitrary, you will need map-reduce or link-walking to find it.
The second issue, how opaque the data is to Riak, is affected by how you query but also by the nature of the data you’re storing. If you need to do intricate map-reduce queries to find or manipulate the data, you’ll likely want it in a form like JSON (or an Erlang term) so your map and reduce functions can reason about the data. On the other hand, if your data is something like an image or PDF, you don’t want to shoehorn that into JSON. If you’re in the situation where you need both a form that’s opaque to Riak, and to be able to reason about it with map-reduce, have your application add relevant metadata to the object. These are created using X-Riak-Meta-\* headers in HTTP or riak\_object:update\_metadata/2 in Erlang.
Rule of thumb: if it’s an abstract datatype, use a map-reduce-friendly format like JSON; if it’s a concrete form, use its original representation. Of course, there are exceptions to every rule, so think carefully about your modeling problem.
Consistency, replication, conflict resolution
The second issue I would consider for each type of data is the access pattern and desired level of consistency. This is related to the questions above of read/write loads, churn, and conflicts.
Riak provides a few knobs you can turn at schema-design time and at request-time that relate to these issues. The first is allow\_mult, or whether to allow recording of divergent versions of objects. In a write-heavy load or where clients are updating the same objects frequently, possibly at the same time, you probably want this on (true), which you can change by setting the bucket properties. The tradeoffs are that the vector clock may grow quickly and your application will need to decide how to resolve conflicts.
The second knob you can turn is the n\_val, or how many replicas of each object to store, also a per-bucket setting. The default value is 3, which will work for many applications. If you need more assurance that your data is going to withstand failures, you might increase the value. If your data is non-critical or in large chunks, you might decrease the value to get greater performance. Knowing what to choose for this value will depend on an honest assessment of both the value of your data and operational concerns.
The third knob you can turn is per-request quorums. For reads, this is the R request parameter: how many replicas need to agree on the value for the read to succeed (the default is 2). For writes, there are two parameters, W and DW. W is how many replicas need to acknowledge the write request before it succeeds (default is 2). DW (durable writes) is how many replica backends need to confirm that the write finished before the entire write succeeds (default is 0). If you need greater consistency when reading or writing your data, you’ll want to increase these numbers. If you need greater performance and can sacrifice some consistency, decrease them. In any case, your R, W, and DW values must be smaller than n\_val if you want the request to succeed.
What do these have to do with your data model? Fundamentally understanding the structure and purpose of your data will help you determine how you should turn these knobs. Some examples:

Log data: You’ll probably want low R and W values so that writes are accepted quickly. Because these are fire-and-forget writes, you won’t need allow\_mult turned on. You might also want a low n\_val, depending on how critical your data is.
Binary files: Your n\_val is probably the most significant issue here, mostly depending on how large your files are and how many replicas of them you can tolerate (storage consumption).
JSON documents (abstract types): The defaults will work in most cases. Depending on how frequently the data is updated, and how many you update within a single conceptual operation with the application, you may want to enable allow\_mult to prevent blind overwrites.

Sean Cribbs
 
