---
title: "statebox, an eventually consistent data model for Erlang (and Riak)"
description: "When you choose an eventually consistent data store you're prioritizing availability and partition tolerance over consistency, but this doesn't mean your application has to be inconsistent. What it does mean is that you have to move your conflict resolution from writes to reads ..."
project: community
lastmod: 2016-10-20T08:40:32+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2011-05-13T23:25:26+00:00
---
May 13, 2011
This was originally posted by Bob Ippolito on May 9th on the Mochi Media Labs Blog. If you’re planning to comment, please do so on the original post.

A few weeks ago when I was on call at work I was chasing down a bug in friendwad [1] and I realized that we had made a big mistake. The data model was broken, it could only work with transactions but we were using Riak. The original prototype was built with Mnesia, which would’ve been able to satisfy this constraint, but when it was refactored for an eventually consistent data model it just wasn’t correct anymore. Given just a little bit of concurrency, such as a popular user, it would produce inconsistent data. Soon after this discovery, I found another service built with the same invalid premise and I also realized that a general solution to this problem would allow us to migrate several applications from Mnesia to Riak.
When you choose an eventually consistent data store you’re prioritizing availability and partition tolerance over consistency, but this doesn’t mean your application has to be inconsistent. What it does mean is that you have to move your conflict resolution from writes to reads. Riak does almost all of the hard work for you [2], but if it’s not acceptable to discard some writes then you will have to set allow\_mult to true on your bucket(s) and handle siblings [3] from your application. In some cases, this might be trivial. For example, if you have a set and only support adding to that set, then a merge operation is just the union of those two sets.
statebox is my solution to this problem. It bundles the value with repeatable operations [4] and provides a means to automatically resolve conflicts. Usage of statebox feels much more declarative than imperative. Instead of modifying the values yourself, you provide statebox with a list of operations and it will apply them to create a new statebox. This is necessary because it may apply this operation again at a later time when resolving a conflict between siblings on read.
Design goals (and non-goals):

The intended use case is for data structures such as dictionaries and sets
Direct support for counters is not required
Applications must be able to control the growth of a statebox so that it does not grow indefinitely over time
The implementation need not support platforms other than Erlang and the data does not need to be portable to nodes that do not share code
It should be easy to use with Riak, but not be dependent on it (clear separation of concerns)
Must be comprehensively tested, mistakes at this level are very expensive
It is ok to require that the servers’ clocks are in sync with NTP (but it should be aware that timestamps can be in the future or past)

Here’s what typical statebox usage looks like for a trivial application (note: Riak metadata is not merged [5]). In this case we are storing an orddict in our statebox, and this orddict has the keys following and followers.
“`erlang
-module(friends).
-export([add\_friend/2, get\_friends/1]).
-define(BUCKET, <<“friends”>>).
-define(STATEBOX\_MAX\_QUEUE, 16). %% Cap on max event queue of statebox
-define(STATEBOX\_EXPIRE\_MS, 300000). %% Expire events older than 5 minutes
-define(RIAK\_HOST, “127.0.0.1”).
-define(RIAK\_PORT, 8087).
-type user\_id() :: atom().
-type orddict(T) :: [T].
-type ordsets(T) :: [T].
-type friend\_pair() :: {followers, ordsets(user\_id())} |
{following, ordsets(user\_id())}.
-spec add\_friend(user\_id(), user\_id()) -> ok.
add\_friend(FollowerId, FolloweeId) ->
statebox\_riak:apply\_bucket\_ops(
?BUCKET,
[{[friend\_id\_to\_key(FollowerId)],
statebox\_orddict:f\_union(following, [FolloweeId])},
{[friend\_id\_to\_key(FolloweeId)],
statebox\_orddict:f\_union(followers, [FollowerId])}],
connect()).
-spec get\_friends(user\_id()) -> [] | orddict(friend\_pair()).
get\_friends(Id) ->
statebox\_riak:get\_value(?BUCKET, friend\_id\_to\_key(Id), connect()).
%% Internal API
connect() ->
{ok, Pid} = riakc\_pb\_client:start\_link(?RIAK\_HOST, ?RIAK\_PORT),
connect(Pid).
connect(Pid) ->
statebox\_riak:new([{riakc\_pb\_client, Pid},
{max\_queue, ?STATEBOX\_MAX\_QUEUE},
{expire\_ms, ?STATEBOX\_EXPIRE\_MS},
{from\_values, fun statebox\_orddict:from\_values/1}]).
friend\_id\_to\_key(FriendId) when is\_atom(FriendId) ->
%% NOTE: You shouldn’t use atoms for this purpose, but it makes the
%% example easier to read!
atom\_to\_binary(FriendId, utf8).
“`
To show how this works a bit more clearly, we’ll use the following sequence of operations:
“`erlang
add\_friend(alice, bob), %% AB
add\_friend(bob, alice), %% BA
add\_friend(alice, charlie). %% AC
“`
Each of these add\_friend calls can be broken up into four separate atomic operations, demonstrated in this pseudocode:
“`erlang
%% add\_friend(alice, bob)
Alice = get(alice),
put(update(Alice, following, [bob])),
Bob = get(bob),
put(update(Bob, followers, [alice])).
“`
Realistically, these operations may happen with some concurrency and cause conflict. For demonstration purposes we will have AB happen concurrently with BA and the conflict will be resolved during AC. For simplicity, I’ll only show the operations that modify the key for
alice.
“`erlang
AB = get(alice), %% AB (Timestamp: 1)
BA = get(alice), %% BA (Timestamp: 2)
put(update(AB, following, [bob])), %% AB (Timestamp: 3)
put(update(BA, followers, [bob])), %% BA (Timestamp: 4)
AC = get(alice), %% AC (Timestamp: 5)
put(update(AC, following, [charlie])). %% AC (Timestamp: 6)
“`
Timestamp 1:

There is no data for alice in Riak yet, so
statebox\_riak:from\_values([]) is called and we get a statebox
with an empty orddict.

“`erlang
Value = [],
Queue = [].
“`
Timestamp 2:

There is no data for alice in Riak yet, so
statebox\_riak:from\_values([]) is called and we get a statebox
with an empty orddict.

“`erlang
Value = [],
Queue = [].
“`
Timestamp 3:

Put the updated AB statebox to Riak with the updated value.

“`erlang
Value = [{following, [bob]}],
Queue = [{3, {fun op\_union/2, following, [bob]}}].
“`
Timestamp 4:

Put the updated BA statebox to Riak with the updated value. Note
that this will be a sibling of the value stored by AB.

“`erlang
Value = [{followers, [bob]}],
Queue = [{4, {fun op\_union/2, followers, [bob]}}].
“`
Timestamp 5:

Uh oh, there are two stateboxes in Riak now… so
statebox\_riak:from\_values([AB, BA]) is called. This will apply
all of the operations from both of the event queues to one of the
current values and we will get a single statebox as a result.

“`erlang
Value = [{followers, [bob]},
{following, [bob]}],
Queue = [{3, {fun op\_union/2, following, [bob]}},
{4, {fun op\_union/2, followers, [bob]}}].
“`
Timestamp 6:

Put the updated AC statebox to Riak. This will resolve siblings
created at Timestamp 3 by BA.

“`erlang
Value = [{followers, [bob]},
{following, [bob, charlie]}],
Queue = [{3, {fun op\_union/2, following, [bob]}},
{4, {fun op\_union/2, followers, [bob]}},
{6, {fun op\_union/2, following, [charlie]}}].
“`
Well, that’s about it! alice is following both bob and charlie despite the concurrency. No locks were harmed during this experiment, and we’ve arrived at eventual consistency by using statebox\_riak, statebox, and Riak without having to write any conflict resolution code of our own.
Bob
And if you’re at all interested in getting paid to do stuff like this, Mochi is hiring.
References






[1]
friendwad manages our social graph for Mochi Social and MochiGames.
It is also evidence that naming things is a hard problem in
computer science.









[2]
See Riak’s articles on Why Vector Clocks are Easy and
Why Vector Clocks are Hard.









[3]
When multiple writes happen to the same place and they have
branching history, you’ll get multiple values back on read.
These are called siblings in Riak.









[4]
An operation F is repeatable if and only if F(V) = F(F(V)).
You could also call this an idempotent unary operation.









[5]
The default conflict resolution algorithm in statebox\_riak
chooses metadata from one sibling arbitrarily. If you use
metadata, you’ll need to come up with a clever way to merge it
(such as putting it in the statebox and specifying a custom
resolve\_metadatas in your call to statebox\_riak:new/1).




