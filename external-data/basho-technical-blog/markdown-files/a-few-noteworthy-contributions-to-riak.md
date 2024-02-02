---
title: "A Few Noteworthy Contributions to Riak"
description: "The community contributions to Riak have been increasing at an exciting rate. The pull requests are starting to roll in, and I wanted to take a moment and recognize several of the many contributions we've received over the past months and weeks (and days)."
project: community
lastmod: 2016-08-31T18:03:19+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mark Phillips"
pub_date: 2010-12-16T06:19:08+00:00
---
December 16, 2010
The community contributions to Riak have been increasing at an exciting rate. The pull requests are starting to roll in, and I wanted to take a moment and recognize several of the many contributions we’ve received over the past months and weeks (and days).
Ripple
Anyone who uses Riak with Ruby knows about Ripple. This is Riak’s canonical Ruby driver and its development has been spearheaded by Riak hacker Sean Cribbs. Not long after Sean started developing this code, he saw a significant influx in Rubyists who were interested in using Riak with Ruby and wanted to lend a hand in the driver’s development. Sean was happy to oblige and, as a result, there are now 15 developers in addition to Sean who have contributed to Ripple in a significant way. Special recognition should also be given to Duff Omelia and Adam Hunter who have made significant contributions to the code and use it in production.
Riak-js
Francisco Treacy and the team at Widescript made it known many months ago that they were looking into Riak to power part of their application. They, along with several other community members, were experimenting with Riak and Node.js. There were a few Node clients for Riak, but they were primarily experimental and immature. Riak had plans to write one but development time was stretched and a node client was several months off.
So, they rolled their own. Francisco, along with Alexander Sicular, James Sadler, Jakub Stastny, and Rick Olson developed and released riak-js. Since its release, it has picked up a ton of users and is being used in applications all over the place. (We liked it so much we even decided to build an app on it… more on this later.).
Thanks, guys, for the node client and helping to kickstart the Riak+Node.js community.
Riak Support in Spring Data
VMware’s Spring Data project is an ambitious one, and it has huge implications for the proliferation of new database technologies in application stacks everywhere. VMware made it known that Riak was slated for integration, needing only someone to take the time to write the code to connect the two. Jon Brisbin took up the task and never looked back.
Jon’s twitter stream is essentially a running narrative of how his work on Riak developed and, as you can see, it took about a month to build support for Riak into the Grails framework, the culmination of which was the 1.0.0.M1 release of the Riak Support in Spring Data.
So, if you’re using Riak with Spring Data, you have Jon Brisbin to thank for the code that made it possible. Thanks, Jon.
Python Docs
I met Daniel Lindsley at StrangeLoop in October. Rusty Klophaus and I were helping him debug a somewhat punishing benchmarking test he was running against a three node Riak cluster on his laptop (during a Cassandra talk) using Riak’s Python client. About a month later Daniel wrote a fantastic blog post called Getting Started With Riak & Python. Though his impressions of Riak were positive on the whole, one of the main points of pain for Daniel was that the Python library had poor documentation. At the time, this was true. Though the library was quite mature as far as functionality goes, the docs had been neglected. I got in touch with Daniel, thanked him for the post, and let him know we were working on the docs. He mentioned he would take a stab at updating the docs if he had a free moment. Shortly thereafter Daniel sent over a huge pull request. He rewrote all of the Python documentation! And it’s beautiful. Check them out here.
Thanks to Daniel and the rest of the team at Pragmatic Badger, we have robust Python documentation. Thanks for the contribution.
Want to contribute to Riak? There is still much code to be written and the Riak community is a great place to work and play. Download the code, join us on IRC, or take a look at GitHub repos to get started.
Mark
