---
title: "Riak in Production - A Distributed Event Registration System Written in Erlang"
description: "March 20, 2010 Riak, at its core, is an open source project. So, we love the opportunity to hear from our users and find out where and how they are using Riak in their applications. It is for that reason that we were excited to hear from Chris Villalobos. He recently put a Distributed Event Registr"
project: community
lastmod: 2015-05-28T19:24:18+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Chris Villalobos"
pub_date: 2010-03-20T00:26:29+00:00
---
March 20, 2010
Riak, at its core, is an open source project. So, we love the opportunity to hear from our users and find out where and how they are using Riak in their applications. It is for that reason that we were excited to hear from Chris Villalobos. He recently put a Distributed Event Registration application into production at his church in Gainesville, Florida, and after hearing a bit about it, we asked him to write a short piece about it for the Riak Blog.
Use Case and Prototyping
As a way of going paperless at our church, I was tasked with creating an event registration system that was accessible via touchscreen kiosk, SMS, and our website, to be used by members to sign up for various events. As I was wanting to learn a new language and had dabbled in Erlang (specifically Mochiweb) for another small application, I decided that I was going to try and do the whole thing in Erlang. But how to do it, and on a two month time line, was quite the challenge.
The initial idea was to have each kiosk independently hold pieces of the database, so that in the event something happened to a server or a kiosk, the data would still be available. Also, I wanted to use the fault-tolerance and distributed processing of Erlang to help make sure that the various frontends would be constantly running and online. And, as I wanted to stay as close to pure Erlang as possible, I decided early against a SQL database. I tried Mnesia but I wasn’t happy with the results. Using QLC as an interface, interesting issues arose when I took down a master node. (I was also facing a time issue so playing with it extensively wasn’t really an option.)
It just so happened that Riak released Riak 0.8 the morning I got fed up with it. So I thought about how I could use a key/value store. I liked how the Riak API made it simple to get data in and out of the database, how I could use map-reduce functionality to create any reports I needed and how the distribution of data worked out. Most importantly, no matter what nodes I knocked out while the cluster was running, everything just continued to click. I found my datastore.
During the initial protoyping stages for the kiosk, I envisioned a simple key/value store using a data model that looked something like this:
“`erlang
[
{key1, {Title, Icon, Background Image, Description, [signup\_options]}},
{key2, {…}}
]
“`
This design would enable me to present the user with a list of options when the kiosk was started up. I found that by using Riak, this was simple to implement. I also enjoyed that Riak was great at getting out of the way. I didn’t have to think about how it was going to work, I just knew that it would. ( The primary issue I kept running into when I thought about future problems was sibling entries. If two users on two kiosks submit information at the same time for the same entry, (potentially an issue as the number of kiosks grow), then that would result in sibling entries because of the way user data is stored:
“`erlang
<>, <>, [user data]
“`
But, by checking for siblings when the reports are generated, this problem became a non-issue.)
High Level Architecture
The kiosk is live and running now with very few kinks (mostly hardware) and everything is in pure Erlang. At a high level, the application architecture looks like this:
Each Touchscreen Kiosk:

wxErlang
Riak node

Web-Based Management/SMS Processing Layer:

Nitrogen Framework speaking to Riak for Kiosk Configuration/Reporting
Nitrogen/Mochiweb processing SMS messages from SMS aggregator

Periodic Email Sender:

Vagabond’s gen\_smtp client on a eternal receive after 24 hours send email-loop.

In Production
Currently, we are running four Riak nodes (writing out to the Filesystem backend) outside of the three Kiosks themselves. I also have various Riak nodes on my random linux servers because I can use the CPU cycles on my other nodes to distribute MapReduce functions and store information in a redundant fashion.
By using Riak, I was able to keep the database lean and mean with creative uses of keys. Every asset for the kiosk is stored within Riak, including images. These are pulled only whenever a kiosk is started up or whenever an asset is created, updated, or removed (using message passing). If an image isn’t present on a local kiosk, it is pulled from the database and then stored locally. Also, all images and panels (such as the on-screen keyboard) are stored in memory to make things faster.
All SMS messages are stored within an SMS bucket. Every 24 hours all the buckets are checked with a “mapred\_bucket” to see if there are any new messages since the last time the function ran. These results are formatted within the MapReduce function and emailed out using the gen\_smtp client. As assets are removed from the system, the current data is stored within a serialized text file and then removed the database.
As I bring more kiosks into operation, the distributed map-reduce feature is becoming more valuable. Since I typically run reports during off hours, the kiosks aren’t overloaded by the extra processing power. So far I have been able to roll out a new kiosk within 2 hours of receiving the hardware. Most of this time is spent doing the installation and configuration of the touchscreen. Also, the system is becoming more and more vital to how we are interfacing with people, giving members multiple ways of contacting us at their convenience. I am planning on expanding how I use the system, especially with code-distribution. For example, with the Innostore interface, I might store the beam files inside and send them to the kiosks using Erlang commands. (Version Control inside Riak, anyone?)
What’s Next?
I have ambitious plans for the system, especially on the kiosk side. As this is a very beta version of the software, it is only currently in production in our little community. That said, I hope to open source it and put it on github/bitbucket/etc. as soon as I pretty up all the interfaces.
I’d say probably the best thing about this whole project is getting to know the people inside the Erlang community, especially the Riak people and the #erlang regulars on IRC. Anytime I had a problem, someone was there willing to work through it with me. Since I am essentially new to Erlang, it really helped to have a strong sense of community. Thank you to all the folks at Riak for giving me a platform to show what Erlang can do in everyday, out of the way places.
Chris Villalobos
