---
title: "QuickChecking Poolboy for Fun and Profit"
description: "This week, the Riak engineering team flew out to Denver and spent a week at the Oxford Hotel. Also attending was John Hughes, the CEO of QuviQ, who spent the week teaching a bunch of us how to use his property-based software testing tool, QuickCheck."
project: community
lastmod: 2015-05-28T19:24:13+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Andrew Thompson"
pub_date: 2012-01-27T02:43:48+00:00
---
This post appeared originally on Andrew Thompson’s Blog.
January 27, 2012
TL;DR

Unit tests are great, but they can’t test everything
Code always has bugs
QuickCheck helps you generate testcases at a volume where writing unit tests would be impractical
Negative testing is as important as positive testing (test the invalid inputs)
Automatically shrinking test cases to the minimal case is immensely helpful
If you write erlang commercially, you should really consider looking at property-based testing because it will find bugs you’ll never be able to replicate otherwise

This week, the Riak engineering team flew out to Denver and spent a week at the Oxford Hotel. Also attending was John Hughes, the CEO of QuviQ, who spent the week teaching a bunch of us how to use his property-based software testing tool, Quickcheck.
Property-based testing, for those unfamiliar with the term, is where you define some ‘properties’ about your software and then QuickCheck tries to come up with some combination of steps/inputs that will break your software. Beyond that it will shrink the typically massive failing cases it finds down to the minimal combination needed to provoke the failure (typically a handful of steps). However, I’m not going to go into details on how QuickCheck works, just on the results it provided.
After two days of working through the QuickCheck training material and the exercises, we were ready to start writing our own QuickCheck tests against some of Riak’s code. I chose to start out with testing poolboy, the erlang worker pool library Riak uses internally for some tasks.
Poolboy was actually third party code written by devinus from #erlang on Freenode. I needed a worker pool implementation for implementing worker pools in riak\_core, specifically for doing asynchronous folds in riak\_kv (but it’s a general feature in riak\_core). I didn’t feel like writing my own, so I looked around and settled on poolboy, I added a bunch of tests, fixed a couple bugs, added a way to check out workers without blocking if none were available and started using it.
Now, poolboy had 85% test coverage (and most of the remaining 15% was irrelevant boilerplate) when I started QuickChecking it, and I felt pretty happy with its solidity, so I didn’t expect to find many bugs, if any. I was very wrong.
So, my first step was to write a simple QuickCheck model for poolboy using eqc\_statem, the quickcheck helper for testing stateful code. The abstract model for poolboy’s internals is pretty simple, all we really need to keep track of is the pid of the pool, the minimum size of the pool and by how much it can ‘overflow’ with ephemeral workers and the list of workers currently checked out. From those bits of data, we can model how poolboy should behave, and those become the ‘property’ we test.
Initially, I only tested starting, stopping, doing a non-blocking checkout and checking a worker back in. I omitted testing blocking checkouts since they’re a little harder to do. This initial property checked out fine, no bugs found (except in the property).
Next I added blocking checkouts, and suddenly the property failed. The output is a little hard to read, but the steps are;

Start poolboy with a size of 0 and an overflow of 1
Do a non-blocking checkout, which succeeds
Do a blocking checkout that fails (with a timeout)
Check the worker obtained in step 2 back in
Do another non-blocking checkout

The result of step 5 should be a worker, but we get full instead.


Turns out non-blocking checkouts have a bug if the timeout on the block happens and then a worker becomes available. This happens because the caller is blocked by the FSM storing the ‘From’ argument in a queue and popping that queue whenever a worker becomes available. However, if the caller times out during the checkout the ‘From’ is left in the queue, the next worker checked in will be sent to a process no longer expecting it (which might not even be alive). This means poolboy leaks workers in this case. I fix this by keeping track when the checkout request is made, and what the timeout on it was and discarding elements from the waiting queue who have expired.

After making this change, the counterexample quickcheck found now passes. The next thing I decided to check was if workers dying while they’re checked out is handled correctly. I added a ‘kill\_worker’ command which randomly kills a checked out worker. I run this test with a lot of iterations and I find a second counterexample. This is what happens this time:

Start a pool with a size of 1 and overflow of 1
Do 3 non-blocking checkouts, first 2 succeed, the third rightfully fails
Check both of the workers we successfully checked out back in
Check a worker back out
Kill it while its checked out
Do 2 more checkouts, both should succeed but instead the second one reports the pool is ‘full’

Clearly something is wrong. I actually re-ran this a bunch of times and found a bunch of similar counterexamples. I had a really hard time debugging this until John suggested looking at the pool’s internal state to see what it thought was going on. So, I added a ‘status’ call to poolboy that would report its internal state (ready, overflow or full) and the number of the permanent and overflow workers. John also suggested I use a dynamic precondition, which allowed me to cross-check the model and pool’s state before each step and exit() on any discrepancy. This led to me finding lots of places where poolboy’s internal state was wrong, mainly around when it changed between the 3 possible states.
With those issues fixed, I moved on to checking what happened if a worker died while it was checked in. I wrote a command that would check out a worker, check it back in and then kill it. QuickCheck didn’t find any bugs initially, but then I remembered an issue poolboy had where poolboy was using tons of ram because it was keeping track of way too many process monitors. Whenever you check a worker out of poolboy, poolboy monitors the pid holding the worker so if it dies, poolboy can also kill the worker and create some free space in the pool. So, I decided to add the number of monitors as one of the things crosschecked between what the model expected and what poolboy actually had.
The latest counterexample went like this:

Pool size 2, no overflow
Checkout a worker
Kill an idle worker (check it out, check it back in and then kill it)
Checkout a worker

The crosscheck actually blew up right before step 4, saying poolboy wasn’t monitoring any processes, when clearly it should have been monitoring who had done the checkout in step 2. I looked at the code and found when it got an EXIT message from a worker that wasn’t currently checked out, it set the list of monitors to the empty list, blowing away all tracking of who had what worker checked out. This was pretty serious, but not that hard to fix; I just didn’t change the list of monitors in that case, instead of zeroing it out.
However, seeing that serious flaw made me wonder more about how poolboy handled unexpected EXITs in other cases, like an EXIT from a process that wasn’t a worker. This could happen if you linked to the poolboy process for some reason and then that process exited. You might even want to do this to make sure your code knew if the pool exited, but in erlang links are both ways. So, I went ahead and wrote a command to generate some spurious exit messages for the pool. As was becoming normal, QuickCheck quickly found a counterexample:

Pool size 1, no overflow
Checkout a worker
Send a spurious EXIT message
Kill the worker we checked out
Stop the pool

Right before step 5, the crosscheck failed telling me poolboy thought it had 2 workers available, not one. Clearly this was another bug, and sure enough poolboy was assuming any EXIT messages were from workers and it’d start a new worker to replace the dead one, actually growing the size of the pool beyond the configured limits. So, I changed the code to ignore EXIT messages from non-worker pids, but to handle the death of checked in workers correctly.
After all the bugs around EXIT messages, I decided to randomly checkin non-worker pids 10% of the time and see what happened. Again, poolboy wasn’t checking for this condition and strange things would happen to the internal state. The fix was very similar to the one for spurious EXIT messages.
Now, I was beginning to run out of ways to break poolboy. I looked at the test coverage and saw that certain code around blocking checkouts was being hit by the unit tests but not by QuickCheck. Now, QuickCheck can run commands serially or parallel, and I had only been running commands serially so far. So, I added a parallel property and tried to run it. It blew up telling me dynamic preconditions weren’t allowed. John told me this was actually the case, and so I just commented it out. We’d lose our cool crosschecking but it could always be uncommented if needed.
With the parallel tests running, I started to get counterexamples like this:
Common prefix

Start pool with size of 1, no overflow

Process 1

Check out a worker

Process 2

Check out a worker

Now, problem was, both checkouts would succeed. This is clearly wrong, until you understand that process 1 might exit before process 2 does the checkout, in which case poolboy notices and frees up space in the pool, at which point process 2 can successfully and validly check out a worker. John again suggested a neat trick where we’d add a final command to each branch that’d call erlang:self() (which returns the current pid). I then modified the tracking of checked out workers to include which worker had done the checkout, so we knew which workers would be destroyed (and their slots in the pool freed) when one of the parallel branches exited. This worked great and I was able to hit the code paths that were unreachable from a purely serial test.
However, no matter how many iterations I ran, I couldn’t get another valid counterexample (I ran into some races in the erlang process registry, but those are well known and harmless). At this point, finally, we knew that barring flaws in the model, poolboy was pretty sound and this adventure came to an end.
Interestingly, at no point did any of the original unit tests fail. However, I omitted describing the many bugs I found in my own model and how I was using QuickCheck, since I can’t really remember any of them, and they don’t matter in the long run.
Finally, I’d like to thank John Hughes for the great instruction and for being patient and helpful in the face of the crazy things I ran into developing and testing the QuickCheck property, Riak for being so dedicated to software quality that they provide all of their engineers with this great tool and the training to use it correctly and all the people that helped proof-read this post.
If you have any feedback, you can email me at andrew AT hijacked.us. Also, if you think doing stuff like this is interesting, we’re hiring!
Andrew
