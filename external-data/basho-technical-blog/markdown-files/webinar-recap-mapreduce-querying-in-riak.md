---
title: "Webinar Recap - MapReduce Querying in Riak"
description: "July 7, 2010 Thank you to all who attended the webinar last Thursday, it was a great turnout with awesome engagement. Like before, we're recapping the questions below for everyone's sake (in no particular order). Q: Say I want to perform two-fold link walking but don't want to keep the "walk-t"
project: community
lastmod: 2016-10-20T07:47:32+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2010-07-07T04:03:47+00:00
---
July 7, 2010
Thank you to all who attended the webinar last Thursday, it was a great turnout with awesome engagement. Like before, we’re recapping the questions below for everyone’s sake (in no particular order).
Q: Say I want to perform two-fold link walking but don’t want to keep the “walk-through” results, including the initial one. Can I do something to keep only the last result?
In a MapReduce query, you can specify any number of phases to keep or ignore using the “keep” parameter on the phase. Usually you only want to keep the final phase. If you’re using the link-walker resource, it’ll return results from any phases whose specs end in “1”. See the REST API wiki page for more information on link-walking.
Q: Will Riak Search work along with MapReduce, for example, to avoid queries over entire bucket?Will there be a webinar about Riak Search?
Yes, we intend to have this feature in the Generally Available release of Riak Search. We will definitely have a webinar about Riak Search close to its public release.
Q: Are there still problems with executing “qfun” functions from Erlang during MapReduce?
“qfun” phases (that use anonymous Erlang functions) will work on a one-node cluster, but not across a multi-node cluster. You can use them in development but it’s best to switch to a compiled module function or Javascript function when moving to production.
Q: Although streams weren’t mentioned, do you have any recommendations on when to use streaming map/reduce versus normal map/reduce?
Streaming MapReduce sends results back as they get produced from the last phase, in a multipart/mixed format. To invoke this, add ?chunked=true to the URL when you submit the job. Streaming might be appropriate when you expect the result set to be very large and have constructed your application such that incomplete results are useful to it. For example, in an AJAX web application, it might make sense to send some results to the browser before the entire query is complete.
Q: Which way is faster: storing a lot of links or storing the target keys in the value as a list? Are there any limits to the maximum number of links on a key?
How the links are stored will likely not have a huge impact on performance. If you choose to store a key list in a document, both methods would work. There are two relevant operations that would be performed with the key list document (updating and traversal).
The update process would involve retrieving the list, adding a value, and saving the list. If you are using the REST interface you will need to be aware of limitations in the number of allowed headers and the allowed header length. Mochiweb restricts the number of allowed headers to 1000. Header length is limited to 8192 characters. This imposes an upper limit for the number of Links that can be set through the REST interface.
The best method for updating a key list would be to write a post commit hook that performed the update. This avoids the need to access the key list using the REST interface so header limitations are no longer a concern. However, the post-commit hook could become a bottleneck in your update path if number of links grows large.
Traversal involves retrieving the key list document, collecting the related keys, and outputting a bucket/key list to be used in proceeding map phases. A built-in function is provided to process links. If you were to store keys in the value you would need to write a custom function to parse the keys and generate a bucket/key list.
Q: What’s the benefit of passing an arg to a map or reduce phase? Couldn’t you just send the function body with the arg value filled in? Can I pass in a list of args or an arbitrary number of args?
When you have a lot of queries that are similar but with minor differences, you might be able to generalize a map or reduce function so that it can vary based on the ‘arg’ parameter. Then you could store that function in a built-ins library (see the question below) so it’s preloaded rather than evaluated at query-time. The arg parameter can be any valid JSON value.
Q: What’s the behavior if the map function is missing from one or more nodes but present on others?
The entire query will fail. It’s best to make sure, perhaps via automated deployment, that all of your functions are available on all nodes. Alternatively, you can store Javascript functions directly in Riak and use them in a phase with “bucket” and “key” instead of “source” or “name”.
Q: If there are 2 map phases, for example, then does that mean that both phases will be run back to back on each individual node and \*then\* it’s all sent back for reduce? Or is there some back and forth between phases?
It’s more like a pipeline, one phase feeds the next. All results from one phase are sent back to the coordinating node, which then initiates the subsequent phase once all participating nodes have replied.
Q: Would it be possible to send a function which acts as both a map predicate and an updater?
In general we don’t recommend modifying objects as part of a MapReduce job because it can add latency to the request. However, you may be able to implement this with a map function in Erlang. Erlang MapReduce functions have full access to Riak including being able to read and write data.
“`erlang
%% Inside your own Erlang module
map\_predicate\_with\_update(Value,\_KeyData,\_Arg) ->
case predicate(Value) of
true -> [update\_passed\_value(Value)];
\_ -> []
end.
update\_passed\_value(Value) ->
{ok, C} = riak:local\_client(),
%% modify your object here, store with C:put
ModifiedValue.
“`
This could come in handy for large updates instead of having to pull each object, update it and store it.
Q: Are Erlang named functions or JS named functions more performant? Which are faster — JS or Erlang functions?
There is a slight overhead when encoding the Riak object to JSON but otherwise the performance is comparable.
Q: Is there a way to use namespacing to define named Javascript functions? In other words, if I had a bunch of app-specific functions, what’s the best way to handle that?
Yes, checkout the built-in Javascript MapReduce functions for an example.
Q: Can you specify how data is distributed among the cluster?
In short, no. Riak consistently hashes keys to determine where in the cluster data is located. This article explains how data is replicated and distributed throughout the cluster. In most production situations, your data will be evenly distributed.
Q: What is the reason for the nested list of inputs to a MapReduce query?
The nested list lets you specify multiple keys as inputs to your query, rather than a single bucket name or key. From the Erlang client, inputs are expressed as lists of tuples (fixed-length arrays) which have length of 2 (for bucket/key) or 3 (bucket/key/key-specific-data). Since JSON has no tuple type, we have to express the inputs as arrays of length 2 or 3 within an array.
Q: Is there a syntax requirement of JSON for Riak?
JSON is only required for the MapReduce query when submitted via HTTP, the objects you store can be in any format that your application will understand. JSON also happens to be a convenient format for MapReduce processing because it is accessible to both Erlang and Javascript. However, it is fairly common for Erlang-native applications to store data in Riak as serialized Erlang datatypes.
Q: Is there any significance to the name of file for how Riak finds the saved functions? I assume you can leave other languages in the same folder and it would be ignored as long as language is set to javascript? Additionally, is it possible/does it make sense to combine all your languages into a single folder?
Riak only looks for “\*.js” files in the js\_source\_dir folder (see Configuration Files on the wiki). Erlang modules that contain map and reduce functions need to be on the code path, which could be completely separate from where the Javascript files are located.
Q: Would you point us to any best practices around matrix computations in Riak? I don’t see any references to matrix in the riak wiki…
We don’t have any specific support for matrix computations. We encourage you to find an appropriate Javascript or Erlang library to support your application.
— Dan and Sean
