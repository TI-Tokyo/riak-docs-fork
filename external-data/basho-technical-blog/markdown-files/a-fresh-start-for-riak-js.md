---
title: "A Fresh Start For riak-js"
description: "Today I'm happy to announce a new release of riak-js, the Riak client for Node.js."
project: community
lastmod: 2016-09-14T15:21:14+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mathias Meyer"
pub_date: 2012-11-13T00:00:00+00:00
---
November 13, 2012
Today I’m happy to announce a new release of riak-js, the Riak client for Node.js. This release breathes new life back into riak-js. For various reasons, development on riak-js had been dormant for quite a while, but that has changed for the better and we’re committed to making it a viable client library for production applications.
Ancient History
riak-js has been around for a quite a while. The first commit by its original author, Francisco Treacy, dates back to March 2010. The original implementation was written in JavaScript and running on Node 0.1. Subsequent versions were rewritten in CoffeeScript, until Francisco wanted a clean start. CoffeeScript is a great language, but shipping compiled code to the user meant increased headaches during debugging and a slightly increased entrance barrier for the JavaScript community.
So he started a complete rewrite in JavaScript more than a year ago. It progressed quite nicely, but hit a wall when Frank was looking for a new maintainer for the library. Thankfully the guys from mostlyserious picked up the torch, and eventually I joined them in the effort to finalize the library into a new release.
During the transition, riak-js got a new home as well, and can now be found on mostlyserious/riak-js. Please make sure to send all issues an pull requests to this repository.
The Present
Version 0.9.0 of riak-js was shipped today and can be installed from npmjs.org:

While the new release is mostly backwards-compatible, it does come with some changes that I deemed worthwhile to make before hitting the big 1.0.
Most notably, the functions for using MapReduce and Riak Search have moved into their own namespace. That brings riak-js more on par with other libraries in making both functionalities a bit more separate from normal client operations.
To run MapReduce, you now use something like the following example:

The same goes for Riak Search, which is now fully supported, including adding, removing and querying documents directly from a search index:

There are other minor changes, e.g. accessing bucket properties:

To get a good overview of the current API of riak-js, check out the documentation.
Back To The Future
Development on future versions has already begun, and we still have a good list of things to work on for future releases.
Most notable, Protocol Buffer support is going to return. For simplicity reasons, it was removed from the JavaScript implementation. It will be back!
I’ve also started adding instrumentation to the HTTP API operations for easier tracking of metrics and logging. But more on that in another blog post.
If there’s anything you’d like to see in future version, open an issue, send a pull request or start a discussion on the Riak mailing list!
Mathias
