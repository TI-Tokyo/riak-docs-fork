---
title: "Understanding riak_core: VisitFun()"
description: "At Erlang Factory 2015, I presented a talk entitled “How to build applications on top of riak_core.” I wanted to do this talk because there is a serious lack of “one-stop” documentation around riak_core.  In particular, implementing handoffs has been under documented and not well disseminated. To he"
project: community
lastmod: 2015-08-28T14:54:36+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mark Allen"
pub_date: 2015-07-06T19:50:04+00:00
---
At Erlang Factory 2015, I presented a talk entitled “How to build applications on top of riak\_core.” I wanted to do this talk because there is a serious lack of “one-stop” documentation around riak\_core.  In particular, implementing handoffs has been under documented and not well disseminated. To help, I have a few blog posts to share.
In my first post, Understanding riak\_core: Handoff, I explored some background, defined a handoff and answer the question of “should I use riak\_core?” In the second post, Understanding riak\_core: Building Handoff, we walked you through building an application that uses riak\_core as its foundation. In this third post, the last in the series, let’s unmask the ?FOLD\_REQ macro and the code behind VisitFun. 
The Mysterious VisitFun
First, the macro is defined here, and below are the relevant bits of the header file:

Aha! ?FOLD\_REQ is a record, and it has four fields. The two we’re most interested in are the “foldfun” field and the “acc0” field. We already knew that the foldfun field was an actual function riak\_core\_handoff\_sender:visit\_item/3. The contents of the “acc0” field are  another record, #ho\_acc, which does all of the bookkeeping about the state of a handoff for a particular vnode.
If you read the code carefully, you’ll see that the fold is advancing the state of the accumulator record as it serializes and transmits a key and its associated data value to the receiving end of the handoff.  And it continues to do this as it iterates over the list of objects supplied as input.
Break it down, function by function
Let’s start with the largest function first, handle\_handoff\_command/3.  This is what the implementation in udon looks like:

It looks a bit intimidating but let’s go through it line by line. Line 1 is the function head with our old friends VisitFun() and Acc0.  On line 2, I wrote a function to find all of the keys for the vnode from disk.  (The code for that function can be found here.) On line 5, I start writing the fold function – in my example above, this function is called “magic/2”.  Here it has name “Do”.
Every key in the vnode will be passed to this function, along with the current state of the handoff accumulator (AccIn).  The function retrieves the metadata for the key and the associated data blob (lines 8-12).  Then in line 17, we make the call to VisitFun with the parameters of {<<”udon”>>, ObjectDiskPath}, {Meta, Latest}, AccIn and the value we get back is an updated handoff state in AccOut.  VisitFun, remember, takes care of serializing our data using encode\_handoff\_item/2 and sending it to the receiving vnode.  So, we fold over the list of objects until we’re done, and get back a final handoff state which is the reply of this callback on line 22.
Let’s look at encode\_handoff\_data/2 next, since we’ve mentioned it a few times already. It looks like the below:

The function head here breaks out the parameters explicitly (\_Key in this case would be a tuple of {Bucket, Key} but we’re not using it anyway).  Data is the tuple from VisitFun, {Meta, Latest} – I just call them out in the function head as a reminder.  To serialize this data, I’m using good old term\_to\_binary/1.  If you have application specific serialization libraries or logic this is the place to handle it.  For example, it might be a good idea to compress the data before it goes over the wire (or not – only benchmarking can really answer that question well).
Next, let’s take a look at handle\_handoff\_data/2. This is the function that receives handoff data from the sender. An example implementation looks like the below:

On line 2, we deserialize the data into its components.  On line 3, we check data integrity using Adler32.  If the file data checksum matches the metadata, we store the data to disk on the receiver (line 5). If not, we return an error tuple (line 9). We then reply to the call on line 11.
Finally, let’s take a look at is\_empty/1. This callback is supposed to tell riak\_core whether a vnode is populated with any data.  If a vnode doesn’t have any data to move, riak\_core just updates its own internal state bookkeeping and doesn’t actually move anything across the network.  An example implementation looks like the below:

On line 2, a function checks whether any keys are in the vnode directory. Its return value gives us the boolean we need to use for this callback.  If is\_empty/1 returns true, then riak\_core considers the vnode empty and it marks the vnode as “moved” and no data transfer takes place.  If is\_empty/1 returns false, then riak\_core follows the whole handoff code path.
Some optional callbacks
Just like gen\_server has some callbacks which may or may not be useful to your application, so does the handoff code.
You’ve seen them before, from the demoapp code above. Let’s talk about them quickly.

handoff\_starting/2 is called on the sending vnode.  This callback allows you to implement application specific behavior before a callback begins – for example, perhaps you need to quiesce a resource before you start moving the data.  Please note the boolean return value of this function. If the function returns true, the handoff will proceed through the normal path. If it returns false, the handoff will be cancelled.
handoff\_cancelled/2 is called on the sending vnode and is usually triggered explicitly through the command line admin tool that riak\_core generates for your application.  If there are cleanup steps required to cancel a handoff, this is where they should be implemented.
Finally, handoff\_finished/2 is called on the sending vnode when a handoff has been completed successfully.  Maybe a resource that has been quiesced needs to be allowed to accept reads/writes again – that can be accomplished with this handoff.
Final thoughts
riak\_core is a tremendously useful library for writing distributed applications. One of the greatest strengths is that it handles vnode plumbing for you, “out of the box.”  
Hopefully this blog series shed a lot of light on how to implement your own application specific handoff code.  If you’re looking for further information about building riak\_core applications, please review the following resources:

Mariano Guerra’s Erlang Factory Lite talk from December 2014. (video slides code)
Mark Allen’s Erlang Factory talk from March 2015. (video slides code)

Also feel free to use the riak\_core user’s mailing list or the freenode #riak channel to ask questions or seek help.
Mark Allen
@bytemeorg
Speakerdeck
GitHub
