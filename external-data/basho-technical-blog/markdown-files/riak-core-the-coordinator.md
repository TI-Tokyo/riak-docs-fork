---
title: "Riak Core - The Coordinator"
description: "Logically speaking, a coordinator is just what it sounds like. It's job is to coordinate incoming requests. It enforces the consistency semantics of N, R and W and performs anti-entropy services like read repair ..."
project: community
lastmod: 2015-05-28T19:24:15+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Ryan Zezeski"
pub_date: 2011-04-19T22:39:30+00:00
---
April 19, 2011
This was originally posted on Ryan Zezeski’s working blog Try Try Try.
At the end of my vnode post I asked the question Where’s the redundancy? There is none in RTS, thus far. Riak Core isn’t magic but rather a suite of tools for building distributed, highly available systems. You have to build your own redundancy. In this post I’ll talk about the coordinator and show how to implement one.
What is a Coordinator?
Logically speaking, a coordinator is just what it sounds like. It’s job is to coordinate incoming requests. It enforces the consistency semantics of N, R and W and performs anti-entropy services like read repair. In simpler terms, it’s responsible for distributing data across the cluster and re-syncing data when it finds conflicts. You could think of vnodes as the things that Get Shit Done (TM) and the coordinators as the other things telling them what to do and overseeing the work. They work in tandem to make sure your request is being handled as best as it can.
To be more concrete a coordinator is a gen\_fsm. Each request is handled in it’s own Erlang process. A coordinator communicates with the vnode instances to fulfill requests.
To wrap up, a coordinator

coordinates requests
enforces the consistency requirements
performs anti-entropy
is an Erlang process that implements the gen\_fsm behavior
and communicates with the vnode instances to execute the request

Implementing a Coordinator
Unlike the vnode, Riak Core doesn’t define a coordinator behavior. You have to roll your own each time. I used Riak’s get and put coordinators for guidance. You’ll notice they both have a similar structure. I’m going to propose a general structure here that you can use as your guide, but remember that there’s nothing set in stone on how to write a coordinator.
Before moving forward it’s worth mentioning that you’ll want to instantiate these coordinators under a simple\_one\_for\_one supervisor. If you’ve never heard of simple\_one\_for\_one before then think of it as a factory for Erlang processes of the same type. An incoming request will at some point call supervisor:start\_child/2 to instantiate a new FSM dedicated to handling this specific request.
init(Args) -> {ok, InitialState, SD, Timeout}
erlang
Args = term()
InitialState = atom()
SD = term()
Timeout = integer()
This is actually part of the gen\_fsm behavior, i.e. it’s a callback you must implement. It’s job is to specify the InitialState name and it’s data (SD). In this case you’ll also want to specify a Timeout value of 0 in order to immediately go to the InitialState, prepare.
A get coordinator for RTS is passed four arguments.

ReqId: A unique id for this request.
From: Who to send the reply to.
Client: The name of the client entity — the entity that is writing log events to RTS.
StatName: The name of the statistic the requester is interested in.

All this data will be passed as a list to init and the only work that needs to be done is to build the initial state record and tell the FSM to proceed to the prepare state.
erlang
init([ReqId, From, Client, StatName]) ->
SD = #state{req\_id=ReqId,
from=From,
client=Client,
stat\_name=StatName},
{ok, prepare, SD, 0}.
The write coordinator for RTS is very similar but has two additional arguments.

Op: The operation to be performed, one of set, append, incr,
incrby or sadd.
Val: The value of the operation. For the incr op this is undefined.

Here is the code.
erlang
init([ReqID, From, Client, StatName, Op, Val]) ->
SD = #state{req\_id=ReqID,
from=From,
client=Client,
stat\_name=StatName,
op=Op,
val=Val},
{ok, prepare, SD, 0}.
prepare(timeout, SD0) -> {next\_state, NextState, SD, Timeout}
erlang
SD0 = SD = term()
NextState = atom()
Timeout = integer()
The job of prepare is to build the preference list. The preference list is the preferred set of vnodes that should participate in this request. Most of the work is actually done by riak\_core\_util:chash\_key/1 and riak\_core\_apl:get\_apl/3. Both the get and write coordinators do the same thing here.

Calculate the index in the ring that this request falls on.
From this index determine the N preferred partitions that should handle the request.

Here is the code.
erlang
prepare(timeout, SD0=#state{client=Client,
stat\_name=StatName}) ->
DocIdx = riak\_core\_util:chash\_key({list\_to\_binary(Client),
list\_to\_binary(StatName)}),
Prelist = riak\_core\_apl:get\_apl(DocIdx, ?N, rts\_stat),
SD = SD0#state{preflist=Prelist},
{next\_state, execute, SD, 0}.
The fact that the key is a two-tuple is simply a consequence of the fact that Riak Core was extracted from Riak and some of it’s key-value semantics crossed during the extraction. In the future things like this may change.
execute(timeout, SD0) -> {next\_state, NextState, SD}
erlang
SD0 = SD = term()
NextState = atom()
The execute state executes the request by sending commands to the vnodes in the preflist and then putting the coordinator into a waiting state. The code to do this in RTS is really simple; call the vnode command passing it the preference list. Under the covers the vnode has been changed to use riak\_core\_vnode\_master:command/4 which will distribute the commands across the Preflist for you. I’ll talk about this later in the post.
Here’s the code for the get coordinator.
erlang
execute(timeout, SD0=#state{req\_id=ReqId,
stat\_name=StatName,
preflist=Prelist}) ->
rts\_stat\_vnode:get(Prelist, ReqId, StatName),
{next\_state, waiting, SD0}.
The code for the write coordinator is almost identical except it’s parameterized on Op.
erlang
execute(timeout, SD0=#state{req\_id=ReqID,
stat\_name=StatName,
op=Op,
val=undefined,
preflist=Preflist}) ->
rts\_stat\_vnode:Op(Preflist, ReqID, StatName),
{next\_state, waiting, SD0}.
waiting(Reply, SD0) -> Result
erlang
Reply = {ok, ReqID}
Result = {next\_state, NextState, SD}
| {stop, normal, SD}
NextState = atom()
SD0 = SD = term()
This is probably the most interesting state in the coordinator as it’s job is to enforce the consistency requirements and possibly perform anti-entropy in the case of a get. The coordinator waits for replies from the various vnode instances it called in execute and stops once it’s requirements have been met. The typical shape of this function is to pattern match on the Reply, check the state data SD0, and then either continue waiting or stop depending on the current state data.
The get coordinator waits for replies with the correct ReqId, increments the reply count and adds the Val to the list of Replies. If the quorum R has been met then return the Val to the requester and stop the coordinator. If the vnodes didn’t agree on the value then return all observed values. In this post I am punting on the conflict resolution and anti-entropy part of the coordinator and exposing the inconsistent state to the client application. I’ll implement them in my next post. If the quorum hasn’t been met then continue waiting for more replies.
erlang
waiting({ok, ReqID, Val}, SD0=#state{from=From, num\_r=NumR0, replies=Replies0}) ->
NumR = NumR0 + 1,
Replies = [Val|Replies0],
SD = SD0#state{num\_r=NumR,replies=Replies},
if
NumR =:= ?R ->
Reply =
case lists:any(different(Val), Replies) of
true ->
Replies;
false ->
Val
end,
From ! {ReqID, ok, Reply},
{stop, normal, SD};
true -> {next\_state, waiting, SD}
end.
The write coordinator has things a little easier here cause all it cares about is knowing that W vnodes executed it’s write request.
erlang
waiting({ok, ReqID}, SD0=#state{from=From, num\_w=NumW0}) ->
NumW = NumW0 + 1,
SD = SD0#state{num\_w=NumW},
if
NumW =:= ?W ->
From ! {ReqID, ok},
{stop, normal, SD};
true -> {next\_state, waiting, SD}
end.
What About the Entry Coordinator?
Some of you may be wondering why I didn’t write a coordinator for the entry vnode? If you don’t remember this is responsible for matching an incoming log entry and then executing it’s trigger function. For example, any incoming log entry from an access log in combined logging format will cause the total\_reqs stat to be incremented by one. I only want this action to occur at maximum once per entry. There is no notion of N. I could write a coordinator that tries to make some guarentees about it’s execution but for now I’m ok with possibly dropping data occasionally.
Changes to rts.erl and rts\_stat\_vnode
Now that we’ve written coordinators to handle requests to RTS we need to refactor the old rts.erl and rts\_stat\_vnode. The model has changed from rts calling the vnode directly to delegating the work to rts\_get\_fsm which will call the various vnodes and collect responses.
“`text
rts:get —-> rts\_stat\_vnode:get (local)
 /--> stat\_vnode@rts1

rts:get —-> rts\_get\_fsm:get —-> riak\_stat\_vnode:get –|—> stat\_vnode@rts2
–> stat\_vnode@rts3
“`
Instead of performing a synchronous request the rts:get/2 function now calls the get coordinator and then waits for a response.
erlang
get(Calient, StatName) ->
{ok, ReqID} = rts\_get\_fsm:get(Client, StatName),
wait\_for\_reqid(ReqID, ?TIMEOUT).
The write requests underwent a similar refactoring.
“`erlang
do\_write(Client, StatName, Op) ->
{ok, ReqID} = rts\_write\_fsm:write(Client, StatName, Op),
wait\_for\_reqid(ReqID, ?TIMEOUT).
do\_write(Client, StatName, Op, Val) ->
{ok, ReqID} = rts\_write\_fsm:write(Client, StatName, Op, Val),
wait\_for\_reqid(ReqID, ?TIMEOUT).
“`
The rts\_stat\_vnode was refactored to use riak\_core\_vnode\_master:command/4 which takes a Preflist, Msg, Sender and VMaster as argument.
Preflist: The list of vnodes to send the command to.
Msg: The command to send.
Sender: A value describing who sent the request, in this case the coordinator. This is used by the vnode to correctly address the reply message.
VMaster: The name of the vnode master for the vnode type to send this command to.
erlang
get(Preflist, ReqID, StatName) ->
riak\_core\_vnode\_master:command(Preflist,
{get, ReqID, StatName},
{fsm, undefined, self()},
?MASTER).
Coordinators in Action
Talk is cheap, let’s see it in action. Towards the end of the vnode post I made the following statement:
“If you start taking down nodes you’ll find that stats start to disappear.”
One of the main objectives of the coordinator is to fix this problem. Lets see if it worked.
Build the devrel
bash
make
make devrel
Start the Cluster
bash
for d in dev/dev\*; do $d/bin/rts start; done
for d in dev/dev{2,3}; do $d/bin/rts-admin join rts1@127.0.0.1; done
Feed in Some Data
bash
gunzip -c progski.access.log.gz | head -100 | ./replay --devrel progski
Get Some Stats
text
./dev/dev1/bin/rts attach
(rts1@127.0.0.1)1> rts:get("progski", "total\_reqs").
{ok,97}
(rts1@127.0.0.1)2> rts:get("progski", "GET").
{ok,91}
(rts1@127.0.0.1)3> rts:get("progski", "total\_sent").
{ok,445972}
(rts1@127.0.0.1)4> rts:get("progski", "HEAD").
{ok,6}
(rts1@127.0.0.1)5> rts:get("progski", "PUT").
{ok,not\_found}
(rts1@127.0.0.1)6> rts:get\_dbg\_preflist("progski", "total\_reqs").
[{730750818665451459101842416358141509827966271488,
'rts3@127.0.0.1'},
{753586781748746817198774991869333432010090217472,
'rts1@127.0.0.1'},
{776422744832042175295707567380525354192214163456,
'rts2@127.0.0.1'}]
(rts1@127.0.0.1)7> rts:get\_dbg\_preflist("progski", "GET").
[{274031556999544297163190906134303066185487351808,
'rts1@127.0.0.1'},
{296867520082839655260123481645494988367611297792,
'rts2@127.0.0.1'},
{319703483166135013357056057156686910549735243776,
'rts3@127.0.0.1'}]
Don’t worry about what I did on lines 6 and 7 yet, I’ll explain in a second.
Kill a Node
text
(rts1@127.0.0.1)8> os:getpid().
"91461"
Ctrl^D
kill -9 91461
Verify it’s Down
bash
$ ./dev/dev1/bin/rts ping
Node 'rts1@127.0.0.1' not responding to pings.
Get Stats on rts2
You’re results my not exactly match mine as it depends on which vnode instances responded first. The coordinator only cares about getting R responses.
text
./dev/dev2/bin/rts attach
(rts2@127.0.0.1)1> rts:get("progski", "total\_reqs").
{ok,97}
(rts2@127.0.0.1)2> rts:get("progski", "GET").
{ok,[not\_found,91]}
(rts2@127.0.0.1)3> rts:get("progski", "total\_sent").
{ok,445972}
(rts2@127.0.0.1)4> rts:get("progski", "HEAD").
{ok,[not\_found,6]}
(rts2@127.0.0.1)5> rts:get("progski", "PUT").
{ok,not\_found}
Let’s Compare the Before and After Preflist
Notice that some gets on rts2 return a single value as before whereas others return a list of values. The reason for this is because the Preflist calculation is now including fallback vnodes. A fallback vnode is one that is not on it’s appropriate physical node. Since we killed rts1 it’s vnode requests must be routed somewhere else. That somewhere else is a fallback vnode. Since the request-reply model between the coordinator and vnode is asynchronous our reply value will depend on which vnode instances reply first. If the instances with values reply first then you get a single value, otherwise you get a list of values. My next post will improve this behavior slightly to take advantage of the fact that we know there are still two nodes with the data and there should be no reason to return conflicting values.
text
(rts2@127.0.0.1)6> rts:get\_dbg\_preflist("progski", "total\_reqs").
[{730750818665451459101842416358141509827966271488,
'rts3@127.0.0.1'},
{776422744832042175295707567380525354192214163456,
'rts2@127.0.0.1'},
{753586781748746817198774991869333432010090217472,
'rts3@127.0.0.1'}]
(rts2@127.0.0.1)7> rts:get\_dbg\_preflist("progski", "GET").
[{296867520082839655260123481645494988367611297792,
'rts2@127.0.0.1'},
{319703483166135013357056057156686910549735243776,
'rts3@127.0.0.1'},
{274031556999544297163190906134303066185487351808,
'rts2@127.0.0.1'}]
In both cases either rts2 or rts3 stepped in for the missing rts1. Also, in each case, one of these vnodes is going to return not\_found since it’s a fallback. I added another debug function to determine which one.
text
(rts2@127.0.0.1)8> rts:get\_dbg\_preflist("progski", "total\_reqs", 1).
[{730750818665451459101842416358141509827966271488,
'rts3@127.0.0.1'},
97]
(rts2@127.0.0.1)9> rts:get\_dbg\_preflist("progski", "total\_reqs", 2).
[{776422744832042175295707567380525354192214163456,
'rts2@127.0.0.1'},
97]
(rts2@127.0.0.1)10> rts:get\_dbg\_preflist("progski", "total\_reqs", 3).
[{753586781748746817198774991869333432010090217472,
'rts3@127.0.0.1'},
not\_found]
(rts2@127.0.0.1)11> rts:get\_dbg\_preflist("progski", "GET", 1).
[{296867520082839655260123481645494988367611297792,
'rts2@127.0.0.1'},
91]
(rts2@127.0.0.1)12> rts:get\_dbg\_preflist("progski", "GET", 2).
[{319703483166135013357056057156686910549735243776,
'rts3@127.0.0.1'},
91]
(rts2@127.0.0.1)13> rts:get\_dbg\_preflist("progski", "GET", 3).
[{274031556999544297163190906134303066185487351808,
'rts2@127.0.0.1'},
not\_found]
Notice the fallbacks are at the end of each list. Also notice that since we’re on rts2 that total\_reqs will almost always return a single value because it’s fallback is on another node whereas GET has a local fallback and will be more likely to return first.
Conflict Resolution & Read Repair
In the next post I’ll be making several enhancements to the get coordinator by performing basic conflict resolution and implementing read repair.
Ryan
