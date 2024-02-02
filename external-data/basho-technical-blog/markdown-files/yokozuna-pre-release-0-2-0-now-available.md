---
title: "Yokozuna Pre-release 0.2.0 Now Available"
description: "Ryan Zezeski and others have been hard at work on Yokozuna, the next generation of Riak Search that marries Riak with Apache Solr."
project: community
lastmod: 2015-05-28T19:24:09+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2012-12-31T00:00:00+00:00
---
December 31, 2012
Happy Holidays from all of us here at Riak. We’ve got some new code to help you ring in the new year. Ryan Zezeski and others have been hard at work on Yokozuna, the next generation of Riak Search that marries Riak with Apache Solr.
The latest pre-release, 0.2.0, was just tagged, and there’s plenty to be excited about for those of you who are interested in test-driving the code. In addition to various bug fixes, some of the new features include:

Active Anti Entropy Support – Automatic background processing that seeks out and rectifies divergences between data stored in Riak and indexes stored in Yokozuna.
Benchmark Scripts – A pre-built collection of benchmarking scripts for automating performance testing.
Sibling Support – When enabled, Yokozuna will now index all object versions. It will also handle index cleanup upon sibling resolution.

The full release notes are up on the GitHub repo.
Contributors
Commits in this release came from Ryan Zezeski, Eric Redmond, and Dan Reverri. Mark Steele also reported a few issues that were fixed in this release.
Use Yokozuna
Remember that this is alpha software, and won’t be officially supported by Riak until a future release. That said, Ryan and the team are actively looking for beta testers with use cases that might be appropriate for Yokozuna. If you’re in the market for scalable, distributed full-text search, join the Riak Mailing List and start asking questions.
There’s a pre-built Yokozuna AWS AMI (ami-8b8d03e2) with the latest changes that’ll make it easy to take Yokozuna for a test drive.
And if you’re looking for a high-level introduction to Yokozuna, take 30 minutes to watch Ryan’s RICON2012 talk or browse the matching slide deck.
Enjoy.
The Riak Team
