---
title: "Riak Search"
description: "May 21, 2010 This post is going to start by explaining how in-the-trenches experience with key/value stores, like Riak, led to the creation of Riak Search. Then it will tell you why you care, what you'll get out of Riak Search, and why it's worth waiting for. A bit of history Few people know that"
project: community
lastmod: 2015-05-28T19:24:17+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2010-05-21T01:50:01+00:00
---
May 21, 2010
This post is going to start by explaining how in-the-trenches experience with key/value stores, like Riak, led to the creation of Riak Search. Then it will tell you why you care, what you’ll get out of Riak Search, and why it’s worth waiting for.
A bit of history
Few people know that Riak used to develop applications for deployment on Salesforce.com. We had big goals, and were thinking big to fill them, and part of that was choosing a data storage system that would give us what we needed not only to succeed and grow, but to survive — a confluence of pragmatism and ideal that embodied a bulletproof operations story, a path upward — resilience, reliability, and scalability, through the use of proven science.
So, that’s what we did: we developed and used what has grown to be, and what you know today, as Riak.
Idealism can’t get you everywhere, though. While we answered hard questions with link-walking and map/reduce, there was still the desire in the back of all of our heads: sometimes you just want to ask, “What emails were sent on May 21 that included the word ‘strategy’?” without having to figure out how to walk links from an organizational chart to mailboxes to mails, and then filter over the data there. It was a pragmatic desire: we just wanted a quick answer in order to decide whether or not to spend more time chasing a path. “Less yak-shaving, please.”
The Operations Story
Then we stopped making Salesforce.com apps, and started selling Riak. We quickly found the same set of desires. Operationally, Riak is a huge win. Pragmatically, something that does indexing and search in a similar operational manner is even bigger. Thus, Riak Search was born.
The operational story is, in a nutshell, this: when you add another node to your cluster, you add capacity and compute power. That’s it, you just add another box and “it just works.” Purposefully or not, eventually a node leaves the cluster, hardware fails, whatever: Riak deals with it. If the node comes back, it’s absorbed like it never left.
We insisted on these qualities for Riak, and have continued that insistence in Riak Search. We did it with all the familiar bits: consistent hashing, hinted handoff, replication, etc.
Why Riak Search?
Now, we’ll be the first to tell you that with Riak you can get pretty far using link-walking and map/reduce, with the understanding that you know what you are going to want ahead of time, and/or are willing to wait for it.
Riak Search answers questions that pop into your head; “find me all the blue dresses that are between $20 and $30 dollars,” “find me the document Bob referred to last week at the TPS procedures meeting,” “how can I delete all these emails from my aunt that have those stupid attachments?” “find me that comic strip with Bob,” etc.
It’s about making members of the sea of data in your key-value store findable. At a higher level, it’s about agility. The ability to answer questions you have about your business and your customers without having to consult a developer or dig through reference manuals and without your application developers having to reinvent the wheel with a very real possibility of doing it just right enough to assure you nothing will go wrong. It’s about a common indexing language.
Okay, now you know — thanks for bearing with us — let’s get to the technical bits.
Riak Search …
The system we have built …

is an easily-scalable, fault-tolerant search and indexing system, adhering to the operational story you just read
supports full-text indexing and search
allows querying via the Lucene query syntax
has Solr-compatible /select and /update web-services
supports date and numeric indexing
supports faceting
automatically distributes indexes
has an intermediate query language and integrated query planner
supports scoring
has integrated tokenizing, filtering and analysis (yes, you can
use StandardAnalyzer!)

… and much more. Sounds pretty great, right?
If you want to know more about the internals and technical nitty gritty, check out the Riak Search presentation one of our own, Riak Search engineer John Muellerleile, gave at the San Francisco Erlang Factory this year.
So, why don’t you have it yet? The easy part.
There are still some stubs and hard-coded things in what we have. For instance, the full-text analyzer in use is just whitespace, case-normalization, and stop-word filtering. We intend to fully support the ability to specify other Lucene analyzers, including custom modules, but the code isn’t there yet.
There is also very little documentation. Without a little bit of handholding, even the brightest and most ambitious user could be forgiven for staring blankly, lost for even the first question to ask. We’re spreading the knowledge among our own team right now; that process will generate the artifacts needed for the next round of users to step in.
There are also many fiddly, finicky bits. These are largely relics of early iterations. Rather than having the interwebs be flooded with, “How do you stop this thing?” (as it was with Riak), we’re going to make things friendlier.
So, why don’t you have it yet? The not-so-easy part.
You’ve probably asked yourself, “What of integration of Riak and Riak Search?” We have many notes from discussions about how it could or should be done, as well as code showing how it can be done. But, we’re not completely satisfied with any of our implementations so far.
There are certainly no shortage of designs and ideas on how this could or should work, so we’re going to make a final pass at refining all of our ideas, given our current Riak Search system to play with, so that we can provide a solid, extensible system, instead of one that with many rough edges that would almost certainly be replaced immediately.
Furthering this sentiment is that we think that our existing map/reduce framework and the functionality and features provided by Riak Search are a true power combo when used together intelligently, than simply as alternatives, or at worse, at odds. As a result, we’re defining exactly how Riak Search indexing and querying should be threaded into Riak map/reduce processing to bring you a combination that is undoubtedly more than the sum of its parts.
We could tease you with specifics, like generating the set of bucket/key inputs to a map phase by performing a Riak Search query, or parameterizing Search phases with map results; though, for now, amidst protest both internally — we’re chomping at the bit to get this out into the world and into your hands  – and externally, as our favorite people continually request this exact set of technology and features, we’re going to implement the few extra details from our refined notes before forcing it on you all.
Hold on just a little longer. :)
-the Riak Search Team
