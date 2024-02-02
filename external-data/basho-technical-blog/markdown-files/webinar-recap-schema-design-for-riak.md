---
title: "Webinar Recap - Schema Design for Riak"
description: "July 7, 2010 Thank you to all who attended the webinar yesterday. The turnout was great, and the questions at the end were also very thoughtful. Since I didn't get to answer very many, I've reviewed the questions below, in no particular order. If you want to review the slides from yesterday's pre"
project: community
lastmod: 2015-05-28T19:24:17+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2010-07-07T03:41:37+00:00
---
July 7, 2010
Thank you to all who attended the webinar yesterday. The turnout was great, and the questions at the end were also very thoughtful. Since I didn’t get to answer very many, I’ve reviewed the questions below, in no particular order. If you want to review the slides from yesterday’s presentation, they’re on Slideshare.
Q: You say listing keys is expensive. How are Map phases affected? Does the number of keys in a bucket have an effect on the expense of the operation? (paraphrased)
Listing keys (for a single bucket, there is no analog for the entire system) requires traversing the entire keyspace, even examining keys that don’t belong to the requested bucket. If your Map/Reduce query uses a whole bucket as its inputs, it will be nearly as expensive as listing keys back to the client; however, Map phases are executed in parallel on the nodes where the data lives, so you get the full benefits of parallelism and data-locality when it executes. The expense of listing keys is taken before any Map phase begins.
It bears reiterating that the expense of listing keys is proportional to the total number of keys stored (regardless of bucket). If your bucket has only 10 keys and you know what they are, it will probably be more efficient to list them as the inputs to your Map/Reduce query than to use the whole bucket as an input.
Q: How do you recommend modeling relationships that require a large number of associations (thousands or millions)?
This is difficult to do, and I won’t say there’s an easy or best answer. One idea that came up in the IRC
 room after the webinar was building a B-tree-like data-structure that could be grown to fit the number of associations. This solves the one-to-many relationship, but will require extra handling and care on the part of your application. In some cases, where you only need to know membership in the relationship, a bloom filter might be appropriate. If you must model lots of highly-connected data, consider throwing a graph database in the mix. Riak is not going to fit all use-cases, some models will be awkward.
Q: My company provides a Java web application and analytics solution that uses JDO to persist to and query from a relational database. Where would I start in integrating with Riak?
Since I haven’t done Java in a serious way for a long time, I can’t speak to the specifics of JDO, or how you might work on migrating away from it. However, I have found that most ORMs hide things from the
developer that he/she should really be aware of — how the mapping is performed, what queries are executed, etc. You’ll likely have to look into the guts of how JDO persists and retrieves objects from the database, then step back and reevaluate what your top queries are and how Riak can help improve or simplify those operations. This is all in the theme of the webinar: Know your data!
Q: Is the source code for the example application and schema design available? (paraphrased)
No, there isn’t any sample code yet. You can play with the existing application (Lowdown) at lowdownapp.com. The other authors and I are seeking a few people to take over its development, and the initial group we contacted have indicated it will be open-sourced.
Q: Is there an way to get notified on changes in a bucket?
That’s not built-in to Riak. However, you could write a post-commit hook in Erlang that pushes a notification to RabbitMQ, for example, then have the interested parties consume messages from that queue.
Q: What mechanism does Riak have to deal with the unique user issue?
Riak has neither write locks nor transactions. There is no way to absolutely guarantee uniqueness without introducing an intermediary that acts as a single-arbiter (and point-of-failure). However, in cases when you aren’t experiencing high write-concurrency on the data in question there are a few things you can do to simulate the uniqueness constraint:

Check for existence of the key before writing. In HTTP, this is as simple as a HEAD request. If the response is 404 Not Found, the object probably doesn’t exist.
Use a conditional PUT (in HTTP) when creating the object. The If-None-Match: \* header should prevent you from blindly overwriting an existing key.

Neither of these solutions are bullet-proof because all operations happen in Riak asynchronously. Remember that it’s eventually consistent, meaning that not all parts of the system may agree at all times, but they will converge on a single state over time. There will be corner-cases where a key doesn’t exist when you check for it, the write via the conditional request succeeds, and you still end up creating an object in conflict. Caveat emptor.
Q: Are the intermediate results of Link and Map phases cached?
Yes, the results of both map and link phases are cached in a pretty naive LRU. The development team has plans to improve its behavior in future versions of Riak.
Q: Could you comment on commit hooks and what place they have, if any, in riak schema design? Would it make sense to use hooks to build an index e.g. keys in a bucket?
Yes, commit hooks are very useful in schema design. For example, you could use a pre-commit hook to validate the format of data before it’s stored. You could use post-commit hooks to send the data to external services (see above) or, as you suggest, build an index in another bucket. Building a secondary index reliably is complicated though, and it’s something I want to work on over the next few months.
Q: So if you have allow\_mult=false are there cases where riak will return a conflict 409? Is the default that last write wins?
Riak never returns a 409 Conflict status from the HTTP interface on writes. If you supply a conditional header (If-Match, for example) you might get a 412 Precondition Failed response if the ETag of the object to be modified doesn’t match the header. In general, it is Riak’s policy to accept writes regardless of the internal state of the object.
The “last write wins” behavior comes in two flavors: “clobbering” writes, and softer “show me the latest one” reads. The latter is the default behavior, in which siblings might occur internally (and the vector clock grown) but not exposed to the client; instead it returns the sibling with the latest timestamp at read/GET time and “throws away” new writes that are based on older (ancestor) vclocks. The former actually ignores vector clocks for the specified bucket, providing no guarantees of causal ordering of writes. To turn this behavior on, set the last\_write\_wins bucket property to true. Except in the most extreme cases where you don’t mind clobbering things that were written since the last time you read, we recommend using the default behavior. If you set allow\_mult=true, conflicting writes (objects with divergent vector clocks, not traceable descendents) will be exposed to the client with a 300 Multiple response.
Again, thanks for attending! Look for our next webinar in about two weeks.
— Sean
