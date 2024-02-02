---
title: "Baseball Batting Average, Using Riak Map/Reduce"
description: "A few days ago, I announced a tool that I assembled last weekend, called luwak_mr. That tool extends Riak's map/reduce functionality to Luwak files."
project: community
lastmod: 2015-05-28T19:24:15+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Bryan Fink"
pub_date: 2011-01-20T06:47:04+00:00
---
January 20, 2011
A few days ago, I announced a tool that I assembled last weekend, called luwak\_mr. That tool extends Riak’s map/reduce functionality to “Luwak” files.
But what does that mean? What can it do?
Luwak is a tree-based block-storage library for Riak. Basically, you feed Luwak a large binary, and it splits the binary into chunks, and creates a tree representing how those chunks fit together. Each chunk (or “block”) is stored as a separate value in Riak, and the tree structure is stored under whatever “filename” you give it. Among other things, this allows for much more effecient access to ranges of the binary (in comparison to storing the entire binary as one value in Riak, forcing it to be read and written in its entirety).
The luwak\_mr tool allows you to easily feed a chunked Luwak file into Riak’s map/reduce system. It will do this in such a way as to provide each chunk for map processing, individually. For example, if you had a Luwak file named “foo” made of ten blocks, the following map/reduce request would evaluate the “BarFun“ function ten times (once for each block):
“`erlang
C:mapred({modfun, luwak\_mr, file, <<“foo”>>},
[{map, BarFun, none, true}]).
“`
So what’s that good for?
Partitioning distributed work is the boon of Luwak+luwak\_mr. If you’re using a multi-node Riak cluster, Luwak has done the work of spreading pieces of your large binary across all of your nodes. The luwak\_mr tool allows you to capitalize on that distribution by using Riak’s map/reduce system to analyze those pieces, in parallel, on the nodes where the pieces are stored.
How about a more concrete example? The common one is distributed grep, but I find that a little boring and contrived. How about something more fun … like baseball statistics.
[1]
[2]
I’ll use Retrosheet’s Play-by-Play Event Files as input. Specifically, I’ll use the regular season, by decade, 1950-1959. If you’d like to follow along download “1950seve.zip” and unzip to a directory called “1950s”
If you look at one of those files, say “1950BOS.EVA”, you’ll see that each event is a line of comma-separated values. I’m interested in the “play” records for this computation. The first one in that file is on line 52:
“`text
play,1,0,rizzp101,??,,K
“`
This says that in the first inning (1), the away (0) player “Phil Rizzuto” (rizzp101), struck out (K). For the purposes of the batting average calculation, this is one at-bat, no hit.
Using grep [3], I can find all of Phil’s “plays” in the 1950s like so:
“`bash
$ grep -e play,.,.,rizzp101 \*.EV\*
1950BOS.EVA:play,1,0,rizzp101,??,,K
1950BOS.EVA:play,3,0,rizzp101,??,,53
1950BOS.EVA:play,5,0,rizzp101,??,,6
…snip (3224 lines total)…
“`
What I need to do is pile these plays into two categories: those that designate an “at bat,” and those that designate a “hit.” That’s easily done with some extra regular expression, and a little counting:
“`bash
$ grep -E “play,.,.,rizzp101,.\*,.\*,(S[0-9]|D[0-9]|T[0-9]|H([^P]|$))” \*.EV\* | wc -l
562
$ grep -E “play,.,.,rizzp101,.\*,.\*,(NP|BK|CS|DI|OA|PB|WP|PO|SB|I?W|HP|SH)” \*.EV\* | wc -l
728
“`
The result of the first grep is the number of hits (singles, doubles, triples, home runs) found (562). The result of the second grep is the number of non-at-bat plays (substitutions, base
steals, walks, etc.; 728); if I subtract it from the total number of plays (3224), I get the number of at-bats (2496). Phil’s batting average is 562(hits)/2456(at-bats) (x1000), or 225.
Great, so now let’s parallelize. The first thing I’ll do is get the data stored in Riak. That’s as simple as attaching to any node’s console and running this function:
“`erlang
load\_events(Directory) ->
true = filelib:is\_dir(Directory),
Name = iolist\_to\_binary(filename:basename(Directory)),
{ok, Client} = riak:local\_client(),
{ok, LuwakFile} = luwak\_file:create(Client, Name, dict:new()),
LuwakStream = luwak\_put\_stream:start\_link(Client, LuwakFile, 0, 5000),
filelib:fold\_files(Directory,
“.\*.EV?”, %% only events files
false, %% non-recursive
fun load\_events\_fold/2,
LuwakStream),
luwak\_put\_stream:close(LuwakStream),
ok.
load\_events\_fold(File, LuwakStream) ->
{ok, FileData} = file:read\_file(File),
luwak\_put\_stream:send(LuwakStream, FileData),
LuwakStream.
“`
I’ve put this code in a module named “baseball”, so running it is as simple as:
“`text
(riak@10.0.0.1) 1> baseball:load\_events(“/home/bryan/baseball/1950s”).
“`
This will create one large Luwak file (approximately 48MB) named “1950s” by concatenating all 160 event files. Default Luwak settings are for 1MB blocks, so I’ll have 48 of them linked from my tree.
Mapping those blocks is quite simple. All I have to do is count the hits and at-bats for each block. The code to do so looks like this:
“`erlang
ba\_map(LuwakBlock, \_, PlayerId) ->
Data = luwak\_block:data(LuwakBlock),
[count\_at\_bats(Data, PlayerId)].
count\_at\_bats(Data, PlayerId) ->
Re = [<<“^play,.,.,”>>,PlayerId,<<“,.\*,.\*,(.\*)$”>>], %”>>],
case re:run(Data, iolist\_to\_binary(Re),
[{capture, all\_but\_first, binary},
global, multiline, {newline, crlf}]) of
{match, Plays} ->
lists:foldl(fun count\_at\_bats\_fold/2, {0,0}, Plays);
nomatch ->
{0, 0}
end.
count\_at\_bats\_fold([Event], {Hits, AtBats}) ->
{case is\_hit(Event) of
true -> Hits+1;
false -> Hits
end,
case is\_at\_bat(Event) of
true -> AtBats+1;
false -> AtBats
end}.
is\_hit(Event) ->
match == re:run(Event,
“^(”
“S[0-9]” % single
“|D[0-9]” % double
“|T[0-9]” % triple
“|H([^P]|$)” % home run
“)”,
[{capture, none}]).
is\_at\_bat(Event) ->
nomatch == re:run(Event,
“^(”
“NP” % no-play
“|BK” % balk
“|CS” % caught stealing
“|DI” % defensive interference
“|OA” % base runner advance
“|PB” % passed ball
“|WP” % wild pitch
“|PO” % picked off
“|SB” % stole base
“|I?W” % walk
“|HP” % hit by pitch
“|SH” % sacrifice (but)
“)”,
[{capture, none}]).
“`
When the ba\_map/3 function runs on a block, it produces a 2-element tuple. The first element of that tuple is the number of hits in the block, and the second is the number of at-bats. Combining them is even easier:
“`erlang
ba\_reduce(Counts, \_) ->
{HitList, AtBatList} = lists:unzip(Counts),
[{lists:sum(HitList), lists:sum(AtBatList)}].
“`
The ba\_reduce/2 function expects a list of tuples produced by map function evaluations. It produces a single 2-element tuple whose first element is the sum of the first elements of all of the inputs (the total hits), and whose second; the second elements (the total at-bats).
These functions live in the same baseball module, so using them is simple:
“`erlang
Client:mapred({modfun, luwak\_mr, file, Filename},
[{map, {modfun, baseball, ba\_map}, PlayerID, false},
{reduce, {modfun, baseball, ba\_reduce}, none, true}]),
“`
I’ve exposed that call as batting\_average/2 function, so finding Phil Rizzuto’s batting average in the 1950s is as simple as typing at the Riak console:
“`text
(riak@10.0.0.1) 2> baseball:batting\_average(<<“1950s”>>, <<“rizzp101”>>).
225
“`
Tada! Parallel processing power! But, you couldn’t possibly let me get away without a micro-benchmark, could you? Here’s what I saw:



Environment
Time


grep [4]
0.060 + 0.081 + 0.074 = 0.215s (0.002\*3 = 0.006s cached)


Riak, 1 node [5]
0.307s (0.012s cached)


Riak, 4 nodes
0.163s (0.024s cached)



All of the disclaimers about micro-benchmarks apply: disc caches play games, opening and closing files takes time, this isn’t a large enough dataset to highlight the really interesting cases, etc. But, I’m fairly certain that these numbers show two things. The first is that since the Riak times aren’t orders of magnitude off of the grep times, the Riak approach is not fundamentally flawed. The second is that since the amount of time decreases with added nodes, some parallelism is being exploited.
There is, of course, at least one flaw in this specific implementation, though, and also several ways to improve it. Anyone want to pontificate?
–Bryan
[1] Astute readers
will note that this is really distrubted grep, entangled with some
processing, but at least it gives more interesting input and
output data.
[2] Forgive me if I botch these calculations. I stopped playing baseball when the coach was still pitching. The closest I’ve come since is intramural softball. But, it’s all numbers, and I can handle numbers. Wikipedia has been my guide, for better or worse.
[3] Okay, maybe not so much astuteness is necessary.
[4] My method for aquiring the grep stat was, admitedly, lame. Specifically, I ran the grep commands as given above, using the “time” utility (i.e. “time grep -E …”). I added the time for the three runs together, and estimated the time for the final sum and division at 0.
[5] Timing the map/reduce was simple, by using timer:tc(baseball, batting\_average, [<<"1950s">>, <<"rizzp101">>])..
