---
title: "Supporting Riak on *BSD"
description: "June 11, 2012 Q: What platforms are supported by Riak? This question comes up quite frequently and there has always been two answers to that question. The first answer really answers the question of "what platforms can Riak run on?" The second one answers the question of "what platforms are pa"
project: community
lastmod: 2016-10-20T08:35:34+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Jared Morrow"
pub_date: 2012-06-11T00:00:00+00:00
---
June 11, 2012
Q: What platforms are supported by Riak?
This question comes up quite frequently and there has always been two answers to that question. The first answer really answers the question of “what platforms can Riak run on?” The second one answers the question of “what platforms are packaged for and tested by Riak?”
\*BSD and the Answer to the First Question
Riak has been built and run on FreeBSD by a handful of users for quite some time from what we can tell. Those dedicated users had to jump through hoops involving modifying many of Riak’s Makefile‘s as well as #ifdef‘s scattered throughout our storage backends. From a great-user-experience point of view, this is not ideal.
Early last year work was started by both the community and Riak to improve our codebase to support \*BSD properly. Unfortunately with our limited resources at the time, finishing the testing on \*BSD got set aside in favor of new Riak features. This left a single missing piece from easy use of Riak on \*BSD, LevelDB.
In December Riak engineer Andrew Thompson submitted a patch to Google to add DragonFlyBSD/NetBSD/OpenBSD support to LevelDB so we can use those fixes in eLevelDB. In March 2012, the patch was merged into LevelDB. That patch, along with the eventual merge of the “BSD Support” pull-request, finally added BSD to the answer of the first question, “what platforms can Riak run on?”
While great, this still leaves Riak \*BSD users alone when it comes to the second question “what platforms are packaged for and tested by Riak?”
\*BSD and the Answer to the Second Question
Whenever asked what platforms are officially supported by Riak the answer has always been to look at what packages were available for Riak. After the release of Riak 1.1, we decided we had the capacity to support another major platform with our next major release. Due to interest from the community and customers, FreeBSD was chosen. Now “Free” doesn’t satisfy all \* in \*BSD, but it is a good place to start!
Luckily the work to actually package FreeBSD was made easier by all the work mentioned above to have it build cleanly. As of this commit, packages for FreeBSD have been building with every commit on riak/master. So, stay tuned for the next major Riak release, when we’ll ship FreeBSD 9 packages as well as update the installation documentation. Now I’m on the hook for writing documentation I suppose!
Until then if you want to play around with the FreeBSD packages and send me feedback, clone Riak and
$ gmake package RELEASE=1
from your FreeBSD machine. Then you should be able to pkg\_add and pkg\_delete Riak.
Thanks,
Jared
