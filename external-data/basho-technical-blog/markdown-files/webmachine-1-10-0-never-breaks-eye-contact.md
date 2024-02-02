---
title: "Webmachine 1.10.0: never breaks eye contact"
description: "May 3, 2013 We recently tagged version 1.10.0 of Webmachine and, in addition to a slew of bug fixes, it includes some notable new features. Those features are the subject of today's post; but first a bit of background on the driving force for these additions. The development of Riak CS is grea"
project: community
lastmod: 2015-05-28T19:23:39+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Kelly Mclaughlin"
pub_date: 2013-05-03T16:19:52+00:00
---
May 3, 2013
We recently tagged version 1.10.0 of Webmachine and, in addition to a slew of bug fixes, it includes some notable new features. Those features are the subject of today’s post; but first a bit of background on the driving force for these additions.
The development of Riak CS is great for dogfooding and bringing home some of the pain points in application development using Riak. The same is also true for Webmachine.
Webmachine has not received a great deal of attention recently because it had what Riak needed and, for the most part, Webmachine has just worked. With Riak CS we needed things from Webmachine that either were not possible or did not work in a way that suited our needs. Besides there was more pressing work to be done making Riak more awesome. With Riak CS that was not always the case. So we have been adding new features we needed and we believe these features will be of use and interest to the larger Webmachine community. Dogfooding FTW again!
We have now also created a 1.11.0 tag that includes an updated tag of mochiweb so that Webmachine can be built and used with Erlang R16.
New features for 1.10.0

Run multiple dispatch groups within a single application
Users can now specify multiple groups of dispatch rules that listen on different IP addresses and ports within the same Erlang application. Read about how to configure this here.
Event-based logging system
The server modules that previously handled Webmachine logging have been replaced with an event-based system. Log event handlers can be added and removed dynamically and custom log modules can be easily added and run in concert with any existing log handlers. More details about the new logging system are here.
Ablity to specify a URL rewrite module
This feature is very similar to the mod\_rewrite module for Apache httpd. A rewrite module specifies a set of rules for rewriting the URL and the rewritten URL is what is processed by the dispatch rules of Webmachine. Docs are here. The module used by Riak CS to rewrite S3 API requests can be found here.
Stream large response objects without buffering the entire object in memory
Streaming content has long been possible with Webmachine, but it was not suitable for use with large objects when not using multipart/mixed because Webmachine buffered all of the content in memory to determine the size in order to properly set the Content-Length header. This was important for Riak CS because it needed to stream back very large objects and the S3 API does not use multipart responses for this operation. Now streaming large content where the size can be determined in advance can be accomplished without having to pay the price of buffering everything in memory. More info on using this feature is here.
Ability to override the resource\_module value in the request metadata
The impetus for this feature is more esoteric than the other features so an example is probably the best description. Take the case where the Webmachine resource modules duplicate a lot of code in implementing the required callbacks to service requests. One way to address this is to move much of that common code to a single module and use that common module as the resource in all dispatch rules. The ModOpts for each dispatch rule are used to specify a smaller set of callbacks for resource specialization so that logging data reflects the specialized resource module and not the common module. We will provide further details about the motivations this in a subsequent blog post focused on Riak CS. Documentation on how to configure this option can be found here.
Kelly Mclaughlin

