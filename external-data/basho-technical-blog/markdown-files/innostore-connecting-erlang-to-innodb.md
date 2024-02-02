---
title: "Innostore -- connecting Erlang to InnoDB"
description: "January 26, 2010 Riak has pluggable storage engines, and so we're always on the lookout for better ways that users can store their data locally. Recent experiences with some Riak customers managing some large datasets led us to believe that InnoDB might work out very well for them. To answer"
project: community
lastmod: 2015-05-28T19:24:18+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Justin Sheehy"
pub_date: 2010-01-26T21:17:13+00:00
---
January 26, 2010
Riak has pluggable storage engines, and so we’re always on the lookout for better ways that users can store their data locally. Recent experiences with some Riak customers managing some large datasets led us to believe that InnoDB might work out very well for them.
To answer that question and fill that need, Innostore was written. It is a standalone Erlang application that provides a simple interface to Embedded InnoDB. So far its performance has been quite good, though InnoDB (with or without the Innostore API) is highly dependent on tuning the local configuration to match the local hardware. Luckily, Dizzy — the author of Innostore — has some heavy-duty experience doing that kind of tuning and as a result we’ve been able to help people meet their performance goals using Innostore.
-Justin
