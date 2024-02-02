---
title: "Spotlight: When “Zero Data Loss” is Imperative - Guess What's the Most Important Technology in the Sirv Stack?"
description: "At Riak, we love hearing about exciting uses of our technology. Today, we look at how one company used Riak® KV to solve the problem of delivering ever-growing image libraries to consumers using a multitude of channels and devices. It goes beyond digital asset management to deliver the right image,"
project: community
lastmod: 2016-04-29T07:16:30+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Toni Vicars"
pub_date: 2016-04-13T08:55:43+00:00
---
At Riak, we love hearing about exciting uses of our technology. Today, we look at how one company used Riak® KV to solve the problem of delivering ever-growing image libraries to consumers using a multitude of channels and devices. It goes beyond digital asset management to deliver the right image, in the right format, to as many consumers as possible as quickly as possible.

Sirv is an advanced image hosting platform that processes images on-the-fly. It’s used by e-commerce teams and marketing departments to serve either static images or images with visual effects such as spin and zoom.
A big time saver for web developers, images can be scaled or cropped to any size, with any style, watermark or text overlay. Images are automatically optimised for the web and responsive to fit any device. Retailers use Sirv to streamline their workflow, relieve pressure on development teams and deliver their assets not only to their website but also their web apps and social media sites.
Riak KV is at the heart of the Sirv application, making it the single most important technology choice in their stack. Because the ability to scale fast, with 100% availability and data integrity were prerequisites, Riak was the product of choice. Using Riak KV 2.1 in an eventually consistent architecture, it stores not only all customer images but other Sirv data such as account info, user info, billing, etc. Other parts of their technology stack might be changed in the future but not Riak. It is core to their entire business.

And they are growing fast. Here are some recent stats:
3,000+ users
9,000+ Riak buckets of different bucket types
38,000,000 files and 8TB of data stored
1,000+ writes per minute
100,000+ reads per minute
9 nodes
They expect user growth and multi-region replication to grow stored data beyond 1PB (petabyte) by 2018.
We sat down with Jake Brumby, Co-Founder of Sirv, to get a bit of insight into how they are using and experiencing Riak.
According to Jake, “Riak was literally the only technology which we tested that had zero data loss. This was imperative for us. It gives us immense confidence in our platform and after 18 months we have a flawless track record. Even with 5 hard drive failures and 3 lost servers, not a single byte of data has been lost.”
Riak has also allowed Jake and his team to scale easily and swap from one cluster to another, without any downtime. This saves them valuable time and gives them flexibility to try new hardware configurations.
The open nature of Riak allowed them to get creative. All Sirv client files are stored using their own filesystem, which they call Riak® FS . Sirv provides an Amazon S3 compatible connector and FTP connector, all via Riak FS.
Jake says it’s a unique approach. “Riak FS makes a distributed, replicated file system abstraction, which has many properties that are even better or equal to some Fault-Tolerant Cloud Storage Systems. Performance is good while stability when losing nodes is just unbeatable, thanks to Riak.”
Sirv also developed their own Riak client library for Node.js which suited their needs better than the official Riak Node client. It’s faster and supports Riak authentication, connection pooling and balancing across multiple servers according to their weight.
They currently use Riak map/reduce for some Sirv administrative operations.
The team is enhancing their technology stack and have, as they describe it, “discovered the beauty of Erlang Virtual Machine (BEAM) and are going to adopt it.” They will also be adding Yokozuna for site search features.
Jakes key advice to getting the most from Riak is to think in terms of eventual consistency and use simple application models. He recommends learning and understanding the Riak CAP options (n, r, w) to squeeze the most from Riak for your needs.
