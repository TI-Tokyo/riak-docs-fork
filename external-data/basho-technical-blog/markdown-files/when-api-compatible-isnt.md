---
title: "When API Compatible Isn't"
description: "June 18, 2012 There's no quicker way to learn the unspoken, de facto standard components of an API than to write a compatible replacement for an old implementation. I was reminded of this recently while tracking down an issue with Riak's MapReduce APIs. It has now been over a year since Riak P"
project: community
lastmod: 2015-05-28T19:24:12+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Bryan Fink"
pub_date: 2012-06-18T00:00:00+00:00
---
June 18, 2012
There’s no quicker way to learn the unspoken, de facto standard components of an API than to write a compatible replacement for an old implementation. I was reminded of this recently while tracking down an issue with Riak’s MapReduce APIs.
It has now been over a year since Riak Pipe was announced, and nearly nine months since it was released with Riak to take over MapReduce processing. The compatibility layer we wrote to run Riak MapReduce jobs on Riak Pipe accepts the same query specification, provides the same data to the same processing functions, and produces output formatted in the same manner as the previous system … unless you consider the number of results in each message to be part of the format.
The format of each message in the external streaming format is a list of one or more results, marked by which phase produced them. Riak Pipe took full advantage of this for its naive first pass at compatibility: it put one result in each message. The system it replaced, though, chose to always deliver the entirety of a reduce result in one message, and also chose to batch map results into chunks of 100.
Consuming these two correctly is no different: just accumulate all the results into bins for each phase. When time and space are concerned, however, the manner in which that binning is done makes all the difference in the world.
To wit: in a world where all reduce results are delivered in one go and map results are delivered in batches of 100, binning with orddict:append\_list/3, an O(MN)\* operation (M = messages, N = results), is not horrible, because M is usually small. But, in a world where each map or reduce result is delivered as its own message, M is equal to N, meaning we’re dealing with an O(N^2) operation, 100 to N times more expensive. Growing in time and space (garbage production) at the square of the size of your input is not a solution fit for large amounts of data.
A couple of quick fixes in the 1.2 release have shrunk the growth factor back down to O(N) for the Erlang Protocol Buffers client library. Similar fixes have improved the same situation in the HTTP interface as well. Or, in simpler terms, Riak’s 1.2 release speeds up non-streamed MapReduce results by leaps and bounds.
We’ve been quite happy with the Riak Pipe implementation of Riak’s MapReduce system from usability and debugging standpoints. With all of the improvements we’ve made in the last year, and the few that we have planned for the next major release after 1.2 (delivery of multiple results per message, for example), we feel confident in deprecating the previous, “legacy” system in 1.2, in preparation for removing it entirely in the following release. Transitioning to exclusively Riak Pipe for MapReduce will clean up the codebase, simplifying maintenance and make way for future growth.
–Bryan
