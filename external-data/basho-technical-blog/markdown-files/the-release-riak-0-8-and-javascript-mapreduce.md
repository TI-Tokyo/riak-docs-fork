---
title: "The Release Riak 0.8 and JavaScript Map/Reduce"
description: "February 3, 2010 We are happy to announce the release of Riak 0.8 available for download immediately. Riak 0.8 features a number of enhancements to the core map/reduce machinery that will make Riak more accessible to a wider audience. The biggest enhancement is the ability to write map/reduce queri"
project: community
lastmod: 2015-05-28T19:24:18+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2010-02-03T21:54:40+00:00
---
February 3, 2010
We are happy to announce the release of Riak 0.8 available for download immediately. Riak 0.8 features a number of enhancements to the core map/reduce machinery that will make Riak more accessible to a wider audience. The biggest enhancement is the ability to write map/reduce queries in JavaScript. We’re using our erlang\_js project to integrate Mozilla’s Spidermonkey engine directly into Riak to keep overhead to a minimum.
We’ve also built a spiffy REST API for submitting map/reduce queries. Queries are described in JSON and POST-ed to the Riak server. Results are sent back as JSON for your processing pleasure. And, the REST interface supports streaming results for large result sets, too.
To kick it all off, we’ve put together a short screencast demonstrating how to use Riak’s flashy new features. You can watch it below, or view it on Vimeo. There’s also a slew of bug fixes and optimizations included in Riak 0.8. See the release notes for all the juicy details.
Download and enjoy!

View on Vimeo
