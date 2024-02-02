---
title: "Peter Alvaro – Outwards from the Middle of the Maze"
description: "Thousands have watched and enjoyed Peter Alvaro’s engaging and informative RICON 2014 Keynote presentation. Alvaro is a PhD candidate at the University of California Berkeley. His research interests lie at the intersection of databases, distributed systems, and programming languages. Alvaro’s style"
project: community
lastmod: 2015-05-28T19:23:31+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Dorothy Pults"
pub_date: 2015-02-09T18:45:30+00:00
---
Thousands have watched and enjoyed Peter Alvaro’s engaging and informative RICON 2014 Keynote presentation. Alvaro is a PhD candidate at the University of California Berkeley. His research interests lie at the intersection of databases, distributed systems, and programming languages. Alvaro’s style of delivery blends humor with deep technical detail and is especially informative for those interested in distributed systems.
In his presentation, Alvaro discusses 4 key ideas:

Mourning the death of transactions
What is so hard about distributed systems?
Distributed consistency: managing asynchrony
Fault-tolerance: progress despite failures

Alvaro starts his presentation by introducing us to Jim Gray and transactional systems. Many of you may know Gray’s work, and, sadly, that he was lost at sea in January 2007. His spirit and legacy are missed.
Alvaro provides insights into transactional systems and the top-down approach these systems traditionally used. He also points out that Eric Brewer, in his RICON 2012 keynote address, suggested that a bottoms-up approach might be needed for today’s distributed systems.
Alvaro dives into why anyone would implement distributed systems and why developing distributed systems is hard, really hard. In a distributed system, it is necessary to manage two fundamental uncertainties or failure modes — asynchrony and partial failure. Alvaro uses a humorous metaphor of two clowns to demonstrate how, in the real world, asynchrony and partial failure can’t be dealt with separately, but must be looked at together.

From his humorous metaphor come some definitions:
Alvaro then provides details on distributed consistency and when data is distributed, how consistency is handled. First, start with object-level consistency. Alvaro introduces and defines CRDTs and how these replicated data types help solve the distributed consistency challenge at the object level.
But what happens as objects are in flight? There must also be flow-level consistency for data in motion. Language-level consistency can help with this problem. Alvaro makes the following key points:
Consistency is tolerance to asynchrony
Tip: Focus on data in motion, not at rest
Alvaro then moves from distributed consistency to fault tolerance. He discusses his most recent research “lineage-driven fault injection.” He reminds us that we build systems of components and we verify these components to be fault tolerant.
However, when we put these components together it doesn’t guarantee end-to-end fault tolerance.

Alvaro talks about the challenges of the top-down approach to testing all components in a system and outlines the goal of lineage-driven fault injection (LDFI).

Alvaro then introduces us to Molly, a top-down fault injector.

He describes Molly like starting from the middle of a maze and moving to the outside as a method to arrive at a solution.
Alvaro provides detailed examples to show modeling programs using lineage so that fault tolerance can be analyzed. He then shows how the role of the adversary can be automated. He describes Molly in more detail as a prototype LDFI. Molly finds fault-tolerance violations quickly or guarantees that none exist. Alvaro provides some output using Molly and shows how lineage allows you to reason backwards from good outcomes.
Alvaro closes with a recap and explanation describing composition as the hardest problem of distributed systems.
Don’t miss this interesting and informative presentation.
Peter Alvaro – Outwards from the Middle of the Maze
Also, KDnuggets did a follow-up interview with Alvaro in which he expanded on some points made in his RICON 2014 Keynote speech. Here are links to the 2-part article:
KDnuggets: Interview with Peter Alvaro (Part 1) on Consistency Challenge in Distributed Systems
KDnuggets: Interview with Peter Alvaro (Part 2) on Managing Asynchrony and Partial Failure
By Dorothy Pults
