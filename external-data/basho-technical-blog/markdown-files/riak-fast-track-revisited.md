---
title: "Riak Fast Track Revisited"
description: "May 27, 2010 You may remember a few weeks back we posted a blog about a new feature on the Riak Wiki called The Riak Fast Track. To refresh your memory, "The Fast Track is a 30-45 minute interactive tutorial that introduces the basics of using and understanding Riak, from building a three node clus"
project: community
lastmod: 2015-05-28T19:24:17+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mark Phillips"
pub_date: 2010-05-27T02:02:40+00:00
---
May 27, 2010
You may remember a few weeks back we posted a blog about a new feature on the Riak Wiki called The Riak Fast Track. To refresh your memory, “The Fast Track is a 30-45 minute interactive tutorial that introduces the basics of using and understanding Riak, from building a three node cluster up through MapReduce.”
This post is intended to offer some insight into what we learned from the launch and what we are aiming to do moving forward to build out the Fast Track and other similar resources.
The Numbers
The Fast Track and accompanying blog post were published on Tuesday, May 5th. After that there was a full week to send in thoughts, comments, and reviews. In that time period:

I received 24 responses (my hope was for >15)
Of those 24, 10 had never touched Riak before
Of those 24, 13 said they were already planning on using Riak in production or after going through the
Fast Track were now intending to use Riak in production in some capacity

The Reviews
Most of the reviews seemed to follow a loose template: “Hey. Thanks for this! It’s a great tool and I learned a lot. That said, here is where I think you can improve…”
Putting aside the small flaws (grammar, spelling, content flow, etc.), there emerged numerous recurring topics:

Siblings, Vector Clocks, Conflict Resolution, Concurrent Updates…More details please. How do they work in Riak and what implications do they have?
Source can be a pain. Can we get a tutorial that uses the binaries?
Curl is great, but can we get an Erlang/Protocol Buffers/language specific tutorial?
I’ve heard about Links in Riak but there is nothing in the Fast Track about it. What gives!?
Pictures, Graphics and Diagrams would be awesome. There is all this talk of Rings, Clusters, Nodes, Vnodes, Partitions, Vector Clocks, Consistent Hashing, etc. Some basic diagrams would go along way in helping me grasp the Riak architecture.
Short, concise screencasts are awesome. More, please!
The Basic API page is great but it seems a bit…crowded. I know they are all necessary but do we really need all this info about query parameters, headers and the like in the tutorial?

Another observation about the nature of the reviews: they were very long and very detailed. It would appear that a lot of you spent considerable time crafting thoughtful responses and, while I was expecting this to some extent, I was still impressed and pleasantly surprised.
This led me to draw two conclusions:

People were excited by the idea of bettering the Fast Track for future Riak users to come
Swag is a powerful motivator

Now, I’m going to be a naïve Community Manager and let myself believe that the Riak Developer Community maintains a high level of programmer altruism. The swag was just an afterthought, right?
So What Did We Change?
We have been doing the majority of the editing and enhancing on the fly. This process is still ongoing and I don’t doubt that some of you will notice elements still present that you thought needed changing. We’ll get there. I promise.
Here is a partial list of what was revised:

The majority of changes were small and incremental, fixing a phrase here, tweaking a sentence there. Many small fixes and tweaks go a long way!
The most-noticeable alterations are on the MapReduce page, where we worked a lot to make it flow better and more interactive. This continues to be improved.
The Basic API Operations page got some love in the form of simplification. After reading your comments, we went back and realized that we were probably throwing too much information at you too fast.
There are now several graphics relating to the Riak Ring and Consistent Hashing. There will be more.

And, as I said, this is still ongoing.
Thank You!
I’ve added a Thank You page to the end of the Fast Track to serve as a permanent shout-out to those who help revise and refine the Fast Track. (I hope to see this list grow, too.) Future newcomers to Riak will surely benefit from your time, effort, and input.
What is Next?
Since its release, the Fast Track tutorial has become the second most-visited page on the Riak Wiki, second only to the wiki.riak.com itself. This tells us here at Riak that there is a need for more tools and tutorials like this. So our intention is to expand this as far as time permits.
In the short term, we plan to add a link-walking page. This was scheduled for the original iteration of the Fast Track but was scrapped because we didn’t have time to assemble all the components. The MapReduce section is going to get more interactive, too.
Another addition will be content and graphics that demonstrate Riak’s fault-tolerance and ability to withstand node outages.
We also want to get more specific with languages. Right now, it uses curl over HTTP. This is great but language-specific makes tremendous sense, and the only preventing us from doing this is time. The ultimate vision is to expand transform the Fast Track into a sort of “choose your own adventure” module, such that if a Ruby dev who prefers Debian shows up at wiki.riak.com without having ever heard of Riak, they can click a few links and arrive at a tutorial that shows them how to spin up three nodes of Riak on Debian and query it through Ripple. Erlang, Ruby, Javascript and Java are at the top of the list.
But, we have a long way to go before we get there, so stay tuned for continuous enhancements and improvements. And if you’re at all at interested in helping develop and expand the Fast Track (say, perhaps, outlining an up-and-running tutorial for for Riak+JavaScript) don’t hesitate to shoot an email to mark@riak.com.
Mark
Community Manager
