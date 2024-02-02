---
title: "Protobuffs in Riak 1.2"
description: "July 18, 2012 You might remember that back in April, we sent around a survey to get input about what features developers use and want in Riak clients. All in all, we had about 87 developers respond to the survey. One of the questions in that survey — and the one that was the most interesting t"
project: community
lastmod: 2015-05-28T19:24:11+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2012-07-18T00:00:00+00:00
---
July 18, 2012
You might remember that back in April, we sent around a survey to get input about what features developers use and want in Riak clients. All in all, we had about 87 developers respond to the survey.
One of the questions in that survey — and the one that was the most interesting to me — asked the respondent to rank some potential features for the roadmap. At the top of that list in the results was to support Secondary Index (2I) and Riak Search queries natively from the Protocol Buffers (Protobuffs) interface. You could already query them by sending a MapReduce request, but the additional step was confusing for some, and slow for others. I set out to make these features happen for the Riak 1.2 release.
Coupling challenges
Originally, the Protobuffs interface was created in an effort to satisfy a customer’s specific performance issue with HTTP, back around Riak version 0.10 or so. It seemed to work well for others, too, and so it got merged into the mainline. From that point until 1.0, not much was done with it. In Riak 1.0, it got a slew of new options — especially enhancments to Key-Value operations like get, put, and delete — that brought it closer to feature-parity with the HTTP interface.
Now, simply adding 2I queries to the existing system would have been straightforward, but search queries would not have been so. Why?

While the HTTP interface of Riak has always been built atop Webmachine, making it easy to
add new resources as needed, the Protobuffs components were part of riak\_kv. In fact, the Protobuffs interface was created while riak\_search was still in its infancy, and when we had little idea what its interface would look like. Adding a coupling back the other direction (from riak\_kv to riak\_search) might just make the problem worse.
The riak-erlang-client was a dependency of riak\_kv so that they could share the riakclient.proto file that contained all of the protocol message definitions. This made the Riak codebase potentially brittle to changes in the client library and made it necessary to copy the riakclient.proto file to our other clients that generate code from it.
We were using an antiquated version of the erlang\_protobuffs library that we had forked and not kept up-to-date. The new maintainer had added features like extensions that we would like to use in the future. If I recall correctly, our version didn’t even properly support enumerations.

Refactoring
With those problems in mind and with the help of a few of my fellow Engineers, I set out to refactor the entire thing. Here’s what we came up with.
First, we separated the connection management from the message processing. This is a bit like how Webmachine works, where the accepting (mochiweb) and dispatching (webmachine) of an incoming HTTP message is separate from processing the message (your resource module and the decision graph). The result of our refactoring is the new riak\_api OTP
application. It consists of a TCP listener, server processes that are spawned for each connection, and a registration utility for defining your own message handlers which are called “services”. Here’s how riak\_kv registers its services:
erlang
riak\_api\_pb\_service:register([{riak\_kv\_pb\_object, 3, 6}, %% ClientID stuff
{riak\_kv\_pb\_object, 9, 14}, %% Object requests
{riak\_kv\_pb\_bucket, 15, 22}, %% Bucket requests
{riak\_kv\_pb\_mapred, 23, 24}, %% MapReduce requests
{riak\_kv\_pb\_index, 25, 26} %% Secondary index requests
])
Each service, represented as a module that implements the riak\_api\_pb\_service behaviour, specifies a range of message codes it can handle. When an incoming message with a registered message code is received, it is dispatched to the corresponding service module, which can then do some processing and decide what messages to send back to the client.
Second, we separated the Protobuffs message definitions from the Erlang client library. We put the .proto file in a new library application called riak\_pb, and actually split it out into several files, grouped by the component of the server they represent; this means there’s a riak.proto, riak\_kv.proto, and riak\_search.proto. In addition to removing the coupling between the Erlang client and the server, we now have a project whose only responsibility is to describe the messages of the protocol. It’s like the equivalent of an RFC, but in code! In the near future we will have build targets in the project that let us generate Java or Python shims from the included messages and that we can distribute as standalone .jar and .egg files.
Third, we merged upstream changes from the new erlang\_protobuffs maintainer and made some updates of our own. In addition to the features like extensions, the newer version has a more complete test suite. Our own updates fixed some bugs and edge cases in the library so that we could improve the overall experience for users. For example, when encountering an unknown message field, the TCP connection will no longer close because of a decoding error; instead, the unknown field will just be ignored.
New features
Whew, that was a lot of work just to get to good stuff! With the updated code structure and a plan with how to move forward, we added two new services, one in riak\_kv (supporting native 2I) and one in riak\_search (supporting native search-index queries), and four new messages to riak\_pb to support those services. We decided not to expose the “add to index” or “delete from index” features in riak\_search because we want to take it in a direction that focuses on indexing KV data rather maintaining a separate index-management interface. If you’re already using the “search KV hook” to index your data, you’ll be fine.
Client-side support for these new requests and responses has already landed in the Ruby client and will soon be landing in Java, Erlang, and Python. You can track support for the new features on our updated Client Libraries wiki page.
Roadmap
Those two new client-facing features are great, but the survey showed us a lot more about what you want and need from Riak’s interfaces. For future releases we’ll be investigating how to improve Protobuff’s error messages and support for bucket properties, how to expose bulk or asynchronous operations, and much more.
Keep using Riak and sending us great feedback!
Sean
