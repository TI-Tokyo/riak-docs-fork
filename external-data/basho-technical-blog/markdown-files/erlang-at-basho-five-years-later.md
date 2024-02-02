---
title: "Erlang at Riak, Five Years Later"
description: "Justin Sheehy discusses whether or not Riak would choose Erlang today."
project: community
lastmod: 2015-05-28T19:23:38+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Justin Sheehy"
pub_date: 2013-07-02T17:30:26+00:00
---
July 2, 2013
We use the Erlang/OTP programming language in building our products here at Riak. We made that choice consciously, believing that it would be a tradeoff – significant benefits balanced by a handful of costs. I am often asked if we would make the same choice all over again. To answer that question I need to address the tradeoff we thought we were making.
The single most compelling reason to choose Erlang was the attribute for which it is best known: extremely high availability. The original design goal for Erlang was to enable rapid development of highly robust concurrent systems that “run forever.” The poster child of its success (outside Riak, of course) is the AXD 301 ATM switch, which reportedly delivers at or better than “nine nines” (99.9999999%) of uptime to customers. Since when we set out to build a database for applications requiring extremely high availability, Erlang was a natural fit.
We knew that Erlang’s supervisor concept, enabling a “let it crash” program designed for resilience, would be a big help for making systems that handle unforeseen errors gracefully. We knew that lightweight processes and a “many-small-heaps” approach to garbage collection would make it easier to build systems not suffering from unpredictable pauses in production. Those features paid off exactly as expected, and helped us a great deal. Many other features that we didn’t understand the full importance of at the time (such as the ability to inspect and modify a live system at run-time with almost no planning or cost) have also helped us greatly in making systems that our users and customers trust with their most critical data.
It turns out that our assessment of the key trade-off — a more limited pool of talented engineers — is, in practice, not a problem for a company like Riak. We need to hire great software developers, and we tend to look for ones with particular skills in areas like databases and/or distributed systems. If someone is a skilled programmer in relatively arcane disciplines like those, then the ability to learn a new programming language will not be daunting. While it’s theoretically a nice bonus for someone to bring knowledge of all the tools we use, we’ve hired a significant number of engineers that had no prior Erlang experience and they’ve worked out well.
This same purported drawback is a benefit in some ways. By not just looking for “X Engineers” (where X is Java, Erlang, or anything else), we make a statement both about our own technology decision-making process and the expected levels of interesting work at Riak. To help me work on my house, I’d rather have someone who self-identifies as an “expert carpenter” or “expert plumber,” not “expert hammer wielder,” even in the cases where most of the job might involve that tool. We expect developers at Riak to exercise deep, broad interests and expertise, and for them to do highly creative work. When we mention Erlang and the other thoughtful decisions we made in building our products, they value the roadmap and leadership.
I had an entertaining and ironic conversation about this recently with a manager at a large database company. He explained to me that we had clearly made the wrong choice, and that we should have chosen Java (like his team) in order to expand the recruiting pool. Then, without breaking a stride, he asked if I could send any candidates his way, to fill his gaps in finding talented people.
We continue to grow and to bring on great new engineers.
That’s not to say that there are no downsides. Any language, runtime, and community will bring with it different constraints and freedoms, making some tasks easier and others less so. We’ve done some work over the years to participate in the highly supportive Erlang community. But the big organizational weakness that so many people thought would come with the choice? It’s simply not a problem.
That lesson, combined with the ongoing technical advantages we enjoy because of Erlang, makes it easy to answer the question:
Yes, we would absolutely choose Erlang today.
Justin Sheehy
