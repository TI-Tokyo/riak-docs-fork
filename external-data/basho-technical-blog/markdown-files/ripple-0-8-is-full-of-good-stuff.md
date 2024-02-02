---
title: "Ripple 0.8 is Full of Good Stuff"
description: "August 31, 2010 This is a repost from the blog of Sean Cribbs, our Developer Advocate.  It's been a while since I've blogged about a release of Ripple, in fact, it's been a long time since I've released Ripple. So this post is going to dig into Ripple 0.8 (released today, August 31) and catch"
project: community
lastmod: 2015-05-28T19:24:16+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2010-08-31T04:42:05+00:00
---
August 31, 2010
This is a repost from the blog of Sean Cribbs, our Developer Advocate. 
It’s been a while since I’ve blogged about a release of Ripple, in fact, it’s been a long time since I’ve released Ripple. So this post is going to dig into Ripple 0.8 (released today, August 31) and catch you up on what has happened since 0.7.1 (and 0.5 if you don’t follow the Github project).
The major features, which I’ll describe in more detail below, are:

Supports Riak 0.12 features
Runs on Rails 3 (non-prerelease)
Adds Linked associations
Adds session stores for Rack and Rails 3 apps

Riak 0.12 Features
The biggest changes here were some bucket-related features. First of all, you can define default quorum parameters for requests on a per-bucket basis, exposed as bucket properties. Riak 0.12 also allows you to specify “symbolic” quorums, that is, “all” (N replies), “quorum” (N/2 + 1 replies), or “one” (1 reply). Riak::Bucket has support for these new properties and exposes them as attr\_accessor-like methods. This is a big time saver if you need to tune your quorums for different use-cases or N-values.
Second, keys are not listed by default. There used to be a big flashing warning sign on Riak::Client#bucket that encouraged you to pass :keys => false. In Ripple 0.8 that’s the default, but it’s also explicit so that if you use the latest gem on Riak 0.11 or earlier, you should get the same behavior.
Runs on Rails 3
I’ve been pushing for Rails 3 ever since Ripple was conceived, but now that the actual release of Rails 3 is out, it’s an easier sell. Thanks to all the contributors who helped me keep Ripple up-to-date with the latest prereleases.
Linked associations
These are HOT, and were the missing features that held me back from saying “Yes, you should use Ripple in your app.” The underlying concepts take some time to understand (the upcoming link-walking page to the Fast Track will help), but you actually have a lot more freedom than foreign keys. Here’s some examples (with a little detail of how they work):
You’ll notice only one and many in the above examples. From the beginning, I’ve eschewed creating the belongs\_to macro because I think it has the wrong semantics for how linked associations work (links are all on the origin side). It’s more like you “point to one of” or “point to many of”. Minor point, but often it’s the language you choose that frames how you think about things.
Session stores
Outside the Ruby-sphere, web session storage is one of Riak’s most popular use-cases. Both Mochi and Wikia are using it for this. Now, it’s really easy to do the same for your Rails or Sinatra app.
For Sinatra, Padrino and other pure Rack apps, use Riak::SessionStore:
For Rails 3, use Ripple::SessionStore.
