---
title: "Fixing the Count"
description: "Many thanks to commenter Mike for taking up the challenge I offered in my last post. The flaw I was referring to was, indeed, the possibility that Luwak would split one of my records across two blocks."
project: community
lastmod: 2015-05-28T19:24:15+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Bryan Fink"
pub_date: 2011-01-26T19:45:01+00:00
---
January 26, 2011
Many thanks to commenter Mike for taking up the challenge I offered in my last post. The flaw I was referring to was, indeed, the possibility that Luwak would split one of my records across two blocks.
I can check to see if Luwak has split any records with another simple map function:
“`text
(riak@127.0.0.1)2> Fun = fun(L,O,\_) ->
(riak@127.0.0.1)2> D = luwak\_block:data(L),
(riak@127.0.0.1)2> S = re:run(D, “^([^r]\*)”,
(riak@127.0.0.1)2> [{capture, all\_but\_first, binary}]),
(riak@127.0.0.1)2> P = re:run(D, “n([^r]\*)$”,
(riak@127.0.0.1)2> [{capture, all\_but\_first, binary}]),
(riak@127.0.0.1)2> [{O, S, P}]
(riak@127.0.0.1)2> end.
“`
This one will return a 3-element tuple consisting of the block offset, anything before the first carriage return, and anything after the last linefeed. Running that function via map/reduce on my data, I see that it’s not only possible for Luwak to split a record across a block boundary, it’s also extremely likely:
“`text
(riak@127.0.0.1)3> {ok, R} = C:mapred({modfun, luwak\_mr, file, <<“1950s”>>},
(riak@127.0.0.1)3> [{map, {qfun, Fun}, none, true}]).
(riak@127.0.0.1)4> lists:keysort(1, R).
[{0,
{match,[<<“BAL,A,Baltimore,Orioles”>>]},
{match,[<<“play,4,0,pignj101”>>]}},
{1000000,
{match,[<<“,00,,NP”>>]},
{match,[<<“play,3,1,math”>>]}},
{2000000,
{match,[<<“e101,??,,S7/G”>>]},
{match,[<<“play,7,1,kue”>>]}},
{3000000,
{match,[<<“nh101,??,,4/L”>>]},
{match,[<<“start,walll101,”Lee Walls”,1,7,”>>]}},
…snip…
“`
There are play records at the ends of the first, second, and third blocks (as well as others that I cut off above). This means that Joe Pignatano, Eddie Mathews, and Harvey Kuenn are each missing a play in their batting average calculation, since my map function only gets to operate on the data in one block at a time.
Luckily, there are pretty well-known ways to fix this trouble. The rest of this post will describe two: chunk merging and fixed-length records.
Chunk Merging
If you’ve watched Guy Steel’s recent talk about parallel programming, or read through the example luwak\_mr file luwak\_mr\_words.erl, you already know how chunk-merging works.
The basic idea behind chunk-merging is that a map function should return information about data that it didn’t know how to handle, as well as an answer for what it did know how to handle. A second processing step (a subsequent reduce function in this case) can then match up those bits of unhandled data from all of the different map evaluations, and get answers for them as well.
I’ve updated baseball.erl to do just this. The map function now uses regexes much like those earlier in this post to produce “suffix” and “prefix” results for unhandled data at the start and end of the block. The reduce function then combines these chunks and produces additional hit:at-bat results that can be summed with the normal map output.
For example, instead of the simple count tuple a map used to produce:
“`erlang
[{5, 50}]
“`
The function will now produce something like:
“`erlang
[{5, 50},
{suffix, 2000000, <<“e101,??,,S7/G”>>},
{prefix, 3000000, <<“play,7,1,kue”>>}]
“`
Fixed-length Records
Another way to deal with boundary-crossing records is to avoid them entirely. If every record is exactly the same length, then it’s possible to specify a block size that is an even multiple of the record length, such that record boundaries will align with block boundaries.
I’ve added baseball\_flr.erl to the baseball project to demonstrate using fixed-length records. The two records needed from the “play” record for the batting average calculation are the player’s Retrosheet ID (the third field in the CSV format) and the play description (the sixth CSV field). The player ID is easy to handle: it’s already a fixed length of eight characters. The play description is, unfortunately, variable in length.
I’ve elected to solve the variable-length field problem with the time-honored solution of choosing a fixed length larger than the largest variation I have on record, and padding all smaller values out to that length. In this case, 50 bytes will handle the play descriptions for the 1950s. Another option would have been to truncate all play descriptions to the first two bytes, since that’s all the batting average calculation needs.
So, the file contents are no longer:
“`text
play,3,1,mathe101,??,,S7/G
play,7,1,kuenh101,??,,4/L
“`
but are now:
“`text
mathe101S7/G………………………………..
kuenh1014/L…………………………………
“`
(though a zero is used instead of a ‘.’ in the actual format, and there are also no line breaks).
Setting up the block size is done at load time in baseball\_flr:load\_events/1. The map function to calculate the batting average on this format has to change the way in which it extracts each record from the block, but the analysis of the play data remains the same, and there is no need to worry about partial records. The reduce
 function is exactly the same as it was before learning about chunks (though the chunk-handling version would also work; it just wouldn’t find any chunks to merge).
Using this method does require reloading the data to get it in the proper format in Riak, but this format can have benefits beyond alleviating the boundary problem. Most notably, analyzing fixed-length records is usually much faster than analyzing variable-length, comma-separated records, since the record-splitter doesn’t have to search for the end of a record — it knows exactly where to find each one in advance.
“Fixed”
Now that I have solutions to the boundary problems, I can correctly award Harvey Kuenn’s 1950s batting average as:
“`text
(riak@127.0.0.1)8> baseball:batting\_average(<<“1950s”>>, <<“kuenh101”>>).
284
(riak@127.0.0.1)9> baseball\_flr:batting\_average(<<“1950s\_flr”>>, <<“kuenh101”>>).
284
“`
instead of the incorrect value given by the old, boundary-confused code:
“`text
(riak@127.0.0.1)7> baseball:batting\_average(<<“1950s”>>, <<“kuenh101”>>).
284
“`
… wait. Did I forget to reload something? Maybe I better check the counts before division. New code:
“`text
(riak@127.0.0.1)20> C:mapred({modfun, luwak\_mr, file, <<“1950s\_flr”>>},
(riak@127.0.0.1)20> [{map, {modfun, baseball\_flr, ba\_map},
(riak@127.0.0.1)20> <<“kuenh101”>>, false},
(riak@127.0.0.1)20> {reduce, {modfun, baseball\_flr, ba\_reduce},
(riak@127.0.0.1)20> none, true}]).
{ok,[{1231,4322}]}
“`
old code:
“`text
(riak@127.0.0.1)19> C:mapred({modfun, luwak\_mr, file, <<“1950s”>>},
(riak@127.0.0.1)19> [{map, {modfun, baseball, ba\_map},
(riak@127.0.0.1)19> <<“kuenh101”>>, false},
(riak@127.0.0.1)19> {reduce, {modfun, baseball, ba\_reduce},
(riak@127.0.0.1)19> none, true}]).
{ok,[{1231,4321}]}
“`
Aha: 1231 hits from both, but the new code found an extra at-bat — 4322 instead of 4321. The division says 0.28482 instead of 0.28488. I introduced more error by coding bad math (truncating instead of rounding) than I did by missing a record!
This result highlights a third method of dealing with record splits: ignore them. If the data you are combing through is statistically large, a single missing record will not change your answer significantly. If completely ignoring them makes you too squeemish, consider adding a simple “unknowns” counter to your calculation, so you can compute later how far off your answer might have been.
For example, instead of returning “suffix” and “prefix” information, I might have returned a simpler “unknown” count every time a block had a broken record at one of its ends (instead of a hit:at-bat tuple, a hit:at-bat:unknowns tuple). Summing these would have given me 47, if every boundary in my 48-block file broke a record. With that, I can say that if every one of those broken records was a hit for Harvey, then his batting average might have been as high as (1231+47)/(4321+47)=0.2926. Similarly, if every one of those broken records was a non-hit at-bat for Harvey, then his batting average might have been as low as 1231/(4321+47)=0.2818.
So, three options for you: recombine split records, avoid split records, or ignore split records. Do what your data needs. Happy map/reducing!
–Bryan
