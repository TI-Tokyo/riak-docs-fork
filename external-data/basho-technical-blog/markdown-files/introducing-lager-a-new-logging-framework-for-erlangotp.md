---
title: "Introducing Lager - A New Logging Framework for Erlang/OTP"
description: "My name is Andrew Thompson and I've been working on Riak at Riak since March. I've been focused primary on various technical debt issues within Riak since starting. The largest project I've undertaken is Lager, a new logging framework for Erlang/OTP ..."
project: community
lastmod: 2015-05-28T19:24:14+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Andrew Thompson"
pub_date: 2011-07-20T00:43:33+00:00
---
July 20, 2011
Hi. My name is Andrew Thompson and I’ve been working on Riak at Riak since March. I’ve been focused primary on various technical debt issues within Riak since starting. The largest project I’ve undertaken is Lager, a new logging framework for Erlang/OTP. Lager is actually my second logging framework for Erlang, and I’ve used my previous experience to make this one even better. I think Lager has some compelling features to offer not seen in other erlang logging frameworks and I’ll go over the highlights in this post.
So, why write another log framework for Erlang? There’s already several; error\_logger itself, SASL, log4erl and riak\_err to name a few. One of the key goals was to make logging friendlier; both to the end-user and to the sysadmin. Lager tries very hard to hide the traditional “giant error tuple of doom” from the user (unless they go looking for it). For example, here’s what happens when a gen\_server dies with a badmatch error (with SASL running):
“`text
=ERROR REPORT==== 19-Jul-2011::17:50:10 ===
\*\* Generic server crash terminating
\*\* Last message in was badmatch
\*\* When Server state == {}
\*\* Reason for termination ==
\*\* {{badmatch,{}},
[{crash,handle\_call,3},
{gen\_server,handle\_msg,5},
{proc\_lib,init\_p\_do\_apply,3}]}
=CRASH REPORT==== 19-Jul-2011::17:50:10 ===
crasher:
initial call: crash:init/1
pid:
registered\_name: crash
exception exit: {{badmatch,{}},
[{crash,handle\_call,3},
{gen\_server,handle\_msg,5},
{proc\_lib,init\_p\_do\_apply,3}]}
in function gen\_server:terminate/6
ancestors: []
messages: []
links: []
dictionary: []
trap\_exit: false
status: running
heap\_size: 377
stack\_size: 24
reductions: 127
neighbours:
“`
A little scary, no? Conversely, here’s what Lager displays when that happens:
“`text
2011-07-19 17:51:21 [error] gen\_server crash terminated with reason: no match of right hand value {} in crash:handle\_call/3
2011-07-19 17:51:22 [error] CRASH REPORT Process crash with 0 neighbours crashed with reason: no match of right hand value {} in crash:handle\_call/3<
“`
A little more readable, eh? Now, there’s times when all that extra information is useful, and Lager doesn’t throw it away. Instead, it has a “crash log” where those messages go, and you’re free to dig through this file for any additional information you might need. Lager also borrows heavily from riak\_err, such that printing large crash messages are safe. (I actually found a bug in riak\_err, so Lager is even safer).
Now, those were messages coming out of error\_logger, which is fine for legacy or library code, but Lager also has its own logging API that you can use. It’s actually implemented via a parse\_transform so that Lager can capture the current module, function, line number and pid for inclusion in the log message. All this is done automatically, and the logging call in the code looks like this:
“`erlang
lager:error(“oh no!”)
lager:warning(“~s, ~s and ~s, oh my!”, [lions, tigers, bears])
“`
Which will be displayed like:
“`text
2011-07-19 18:02:02 [error] @test2:start:8 oh no!
2011-07-19 18:02:02 [warning] @test2:start:9 lions, tigers and bears, oh my!
“`
Note that you can easily see where the error occurred just by glancing at the line. Also notice that you don’t need to stick a newline on the end of the log message. Lager automatically (and happily) does that for you.
Why did I use a parse transform? I was originally going to use the traditional macro approach, capturing ?MODULE and ?LINE but I had a talk with Steve Vinoski, who also has some prior experience with Erlang logging, and he suggested a parse transform. A parse transform is handy in a couple different ways; we can do some compile time calculations, we can capture the current function name and in some ways its more flexible than a macro. Of course, Lager could also easily be extended to provide more traditional logging macros as well.
Now, Lager is architected much like error\_logger in that its a gen\_event with multiple handlers installed. Right now, there are only two provided: a console handler and a file handler. The file version supports multiple files at different levels, so you can have a log of only errors and above, and a log with informational messages as well. The loglevels are adjustable at runtime so you can turn the logging up/down for a particular backend:
“`erlang
lager:set\_loglevel(lager\_console\_backend, debug)
lager:set\_loglevel(lager\_file\_backend, “error.log”, warning)
“`
The first call would tell the console to display all messages at the debug level and above, and the second tells the file backend to set the “error.log” file to log any messages warning and above. You can of course set the defaults in Riak’s app.config.
Lager keeps track of which backends are consuming which levels and will very efficiently discard messages that would not be logged anywhere (they aren’t even sent to the gen\_event). This means that if you have no backends consuming debug messages, you can log a million debug messages in less than half a second; they’re effectively free. Therefore you can add lots of debug messages to your code and not have to worry they’re slowing things down if you’re not looking at them.
Lager also plays well with log rotation. You simply move or delete the log file and Lager will notice and reopen the file or recreate it. This means it will work out of the box with tools like logrotate or newsyslog. It also handles situations like out of disk space or permission errors gracefully and when the situation is resolved it will resume logging.
Some further enhancements I plan to make are:

Internal log rotation by size and time
Syslog (remote and local) backends
Ability to filter messages by module/pid (think enabling debug messages for just a single module instead of globally)

Needless to say, I’m pretty excited about releasing this code. Lager should be merged mainline in Riak sometime this week once the integration work has been reviewed. That means that it will be part of the next major release, as well. Please let me know what you think. As usual, patches or suggestions are welcomed and encouraged.
Andrew
