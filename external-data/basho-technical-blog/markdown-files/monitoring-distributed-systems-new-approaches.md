---
title: "Monitoring Distributed Systems (New Approaches)"
description: "A roundup of videos from RICON 2012 focusing on the challenges of (and solutions to) monitoring distributed systems."
project: community
lastmod: 2015-05-28T19:24:09+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2012-11-13T00:00:00+00:00
---
November 13, 2012
Legacy RDBMS systems offered mature monitoring capabilities that usually gave operators a clear view of how their databases were (or weren’t) performing. Emerging distributed systems introduce new levels of complexity, presenting new problems in monitoring and diagnosis. In this blog we highlight two talks given at last month’s RICON which shed light on this problem and offer some interesting solutions.
Next Generation Monitoring of Large Scale Riak Applications
In this talk, Theo Schlossnage, founder of OmniTI, talks about moving beyond standard monitoring metrics (average, mean, 95th percentile, 99th percentile, etc.), and advocates for more sophisticated methods, namely histograms and new visualization techniques. He illustrates this with some interesting real world examples in which metrics such as average response time have little meaning in the face of real world distributions which are often multi-modal and rapidly evolving.

Modern Radiology for Distributed Systems
In this talk, Boundary engineer Dietrich Featherston uses radiological imaging as a metaphor to explore the challenges of monitoring distributed systems –Boundary uses Riak to store high-resolution network data for its analysis engine. In this metaphor, if we just look at metrics pulled from individual hosts (CPU usage, memory usage, etc.), we can see diseased “cells”, but ignore the whole organism. We react to problems, instead of preventing them. To illustrate, Dietrich walks through a series of case studies highlighting new, “context aware”, non-invasive monitoring techniques.

For more RICON videos on a range of distributed systems topics, visit our RICON aftermath site.
