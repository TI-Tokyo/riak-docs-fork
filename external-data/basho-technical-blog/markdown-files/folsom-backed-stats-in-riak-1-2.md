---
title: "Folsom-backed Stats In Riak 1.2"
description: "July 2, 2012 Part of the Riak 1.2 release is a change to how we gather metrics. In the past we used some home-grown code for storing and calculating statistics, but with this release we're moving to the Folsom metrics library from those smart hackers at Boundary. Why Change? In previous version"
project: community
lastmod: 2015-05-28T19:24:11+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Russell Brown"
pub_date: 2012-07-02T00:00:00+00:00
---
July 2, 2012
Part of the Riak 1.2 release is a change to how we gather metrics. In the past we used some home-grown code for storing and calculating statistics, but with this release we’re moving to the Folsom metrics library from those smart hackers at Boundary.
Why Change?
In previous versions of Riak the same process gathered metrics and calculated statistics. In certain situations, under load, reading statistics would slow down, or even timeout altogether. The call to read stats would block that same process that updates stats, leading to large message queue backlogs.
A further issue is that the sliding window histograms used for get and put FSM stats are written to (and read from) disk. In an I/O bound system like Riak it is better not to add extraneous load to the disk. Folsom stores stats in memory (in ets tables, for the detail orientated). This also helps with GC since there is less copying of large amounts of state between updates.
Another advantage in using Folsom is that it is a proven, standard library so we can focus our efforts on writing Riak. We’re contributing to Folsom, and we’re helping to maintain it, but a focused library that does one thing well means Riak can spend more time on Riak and less on stats. Boundary have battle-tested Folsom in production and we’ve benchmarked Riak extensively with Folsom in preparation for the switch. We’re confident it performs better than stats in previous Riak releases.
With a cache for stat reads, and separate processes for updating and calculating stats, Riak stats are faster and more stable than before.
What Does It Mean For Me, The User?
Hopefully, very little to nothing. We’ve benchmarked Riak with the new stats code and it performs better than ever, with the added benefit of no longer having the possibility that reading stats will timeout or cause messages to back up. The same stats are exposed by riak-admin status and the HTTP /stats endpoint. Hopefully all you notice is an improvement in performance reading stats under heavy load.
However, there are some changes to the number of readings stored for sliding window histogram calculations (more on this below) so there maybe a slight drop in resolution for some metrics, but we provide configuration parameters to up the sample size, should you need to.
What Else Is New?
As mentioned above, Riak’s sliding window histogram readings are stored on disk. And it stored all readings for the window size. So if your server is handling 10k gets a second, then your sliding window has 600k readings to read from disk and calculate when you call /stats. Folsom didn’t have a sliding window sample for histograms, so we added one. This stores your readings in memory, rather than disk, but has the same issue in that its size is unbounded. Sometimes you want full resolution like that, but then your calculations will take a while, and you’re using RAM to hold all the readings. As a trade off, we added another sample type for histograms: slide\_uniform. What this means is that you can trade off a little resolution for a bounded number of readings. Each second in the sliding window is limited to a set number of readings. When that many readings have been taken, a random sample of incoming readings is stored. The main benefit is that the amount of memory needed to hold the readings is bounded, as is the amount of time it takes to calculate the statistics from the readings.
By default Riak’s histograms use this bounded sample, with a sliding window of 60 seconds and a sample size of 1,028 readings per second. You can change the sample type (and even use Folsom’s other histogram sample types), by setting a variable in your the riak\_kv and/or riak\_search sections of your app.config file. The relevant parameter to tweak is stat\_sample\_type and it’ll be part of app.config when Riak 1.2 is official.
For example, you could change the size, in seconds, to 60 for the sliding window:
{stat\_sample\_type, {slide, 60}}
Or add the number of readings to save per second configured (with 1028 being that readings number):
{stat\_sample\_type, {slide\_uniform, {60, 1028}}
The metrics are registered with Folsom and stored in memory. If you wish to change the sample type you must do so before you start Riak, or must restart the node for the new type to take effect.
Is That It?
Yes and no. There is more you can do now, and we’ll be sharing more details after the release. A few examples:

If you attach to a node using riak-attach, you can inspect individual metrics. Type folsom\_metrics:get\_metrics() to see a list of all registered metrics. You’ll notice that all of Riak’s metrics are name-spaced by app, e.g. {riak\_kv, node\_get\_fsm\_time}, so if you have your own application running on top of Riak, or riak\_core, you can register your stats with Folsom, too.
Say you’re interested only in the histogram of put times for this Riak node, type folsom\_metrics:get\_histogram\_value({riak\_kv, put\_fsm\_time}), and you only calculate the statistics for that metric.

It’s also worth noting that we plan to expose individual metrics for queries like this, both through the command line tools and the REST interface. Until then, you can attach and query Folsom directly.
Enjoy. If you have any questions or concerns, feel free to let us know in the comments or on the Riak Mailing List.
Russell
