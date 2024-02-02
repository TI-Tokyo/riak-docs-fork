---
title: "Two new Erlang/OTP applications added to Riak products - 'riak_err' and 'cluster_info'"
description: "November 17, 2010 The next release of Riak will include two new Erlang/OTP applications: riak_err and cluster_info. The riak_err application will improve Riak's runtime robustness by strictly limiting the amount of RAM that is used while processing event log messages. The cluster_info application w"
project: community
lastmod: 2015-05-28T19:24:16+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Scott Fritchie"
pub_date: 2010-11-17T05:41:35+00:00
---
November 17, 2010
The next release of Riak will include two new Erlang/OTP applications: riak\_err and cluster\_info. The riak\_err application will improve Riak’s runtime robustness by strictly limiting the amount of RAM that is used while processing event log messages. The cluster\_info application will assist troubleshooting by automatically gathering lots of environment, configuration, and runtime statistics data into a single file.
Wait a second, what are OTP applications?
The Erlang virtual machine provides most of the services that an operating system like Linux or Windows provides: memory management, file system management, TCP/IP services, event management, and the ability to run multiple applications. Most modern operating systems allow you to run a Web browser, word processor, spreadsheet, instant messaging app, and many others. And if your email GUI app crashes, your other applications continue running without interference.
Likewise, the Erlang virtual machine supports running multiple applications. Here’s the list of applications that are running within a single Riak node — we ask the Erlang CLI to list them for us.
text
(riak@127.0.0.1)6> application:which\_applications().
[{cluster\_info,"Cluster info/postmortem app","0.01"},
{luwak,"luwak","1.0"},
{skerl,"Skein hash function NIF","0.1"},
{riak\_kv,"Riak Key/Value Store","0.13.0"},
{riak\_core,"Riak Core","0.13.0"},
{bitcask,[],"1.1.4"},
{luke,"Map/Reduce Framework","0.2.2"},
{webmachine,"webmachine","1.7.3"},
{mochiweb,"MochiMedia Web Server","1.7.1"},
{erlang\_js,"Interface between BEAM and JS","0.4.1"},
{runtime\_tools,"RUNTIME\_TOOLS version 1","1.8.3"},
{crypto,"CRYPTO version 1","1.6.4"},
{os\_mon,"CPO CXC 138 46","2.2.5"},
{riak\_err,"Custom error handler","0.1.0"},
{sasl,"SASL CXC 138 11","2.1.9"},
{stdlib,"ERTS CXC 138 10","1.16.5"},
{kernel,"ERTS CXC 138 10","2.13.5"}]
Yes, that’s 17 different applications running inside a single node. For each item in the list, we’re told the application’s name, a human-readable name, and that application’s version number. Some of the names like ERTS CXC 138 10 are names assigned by Ericsson.
Each application is, in turn, a collection of one or more processes that provide some kind of computation service. Most of these processes are arranged in a “supervisor tree”, which makes the task of managing faults (e.g., if a worker process crashes, what do you do?) extremely easy. Here is the process tree for the kernel application.

And here is the process tree for the riak\_kv application.

The riak\_err application
See the GitHub README for riak\_err for more details.
The Erlang/OTP runtime provides a useful mechanism for managing all of the info, error, and warning events that an application might generate. However, the default handler uses some not-so-smart methods for making human-friendly message strings.
The big problem is that the representation used internally by the virtual machine is a linked list, one list element per character, to store the string. On a 64-bit machine, that’s 16 bytes of RAM per character. Furthermore, if the message contains non-printable data (i.e., not ASCII or Latin-1 characters), the data will be formatted into numeric representation. The string “Bummer” would be formatted just like that, Bummer. But if each character in that string had the constant 140 added to it, the 6-byte string would be formatted as the 23-byte string 206,257,249,249,241,254 instead (an increase of about 4x). And, in rare but annoying cases, there’s some additional expansion on top of all of that.
The default error handler can take a one megabyte error message and use over 1 megabyte \* 16 \* 4 = 32 megabytes of RAM. Why should error messages be so large? Depending on the nature of a user’s input (e.g. a large block of data from a Web client), the process’s internal state, and other factors, error messages can be much, much bigger than 1MB. And it’s really not helpful to consume half a gigabyte of RAM (or more) just to format one such message. When a system is under very heavy load and tries to format dozens of such messages, the entire virtual machine can run out of RAM and crash.
The riak\_err OTP application replaces about 90% of the default Erlang/OTP info/error/warning event handling mechanism. The replacement handler places strict limits on the maximum size of a formatted message. So, if you want to limit the maximum length of an error message to 64 kilobytes, you can. The result is that it’s now much more difficult to get Riak to crash due to error message handling. It makes us happy, and we believe you’ll be happier, too.
Licensing for the riak\_err application
The riak\_err application was written by Riak Technologies, Inc. and is licensed under the Apache Public License version 2.0. We’re very interested in bug reports and fixes: our mailbox is open 24 hours per day for GitHub comments and pull requests.
The cluster\_info application
The cluster\_info application is included in the packaging for the Hibari key-value store, which is also written in Erlang. It provides a flexible and easily-extendible way to dump the state of a cluster of Erlang nodes.
Some of the information that the application gathers includes:

Date & time
Statistics on all Erlang processes on the node
Network connection details to all other Erlang nodes
Top CPU- and memory-hogging processes
Processes with large mailboxes
Internal memory allocator statistics
ETS table information
The names & versions of each code module loaded into the node

The app can also automatically gather all of this data from all nodes and write it into a single file. It’s about as easy as can be to take a snapshot of all nodes in a cluster. It will be a valuable tool for Riak’s support and development teams to diagnose problems in a cluster, as a tool to aid capacity planning, and merely to answer a curious question like, “What’s really going on in there?”
Over time, Riak will be adding more Riak-specific info-gathering functions. If you’ve got feature suggestions, (e.g., dump stats on how many times you woke up last night, click here to send all this data to Riak’s support team via HTTP or SMTP), we’d like to hear them. Or, if you’re writing the next game-changing app in Erlang, just go ahead and hack the code to fit your needs.
Licensing for the cluster\_info application
The cluster\_info application was written by Gemini Mobile Technologies, Inc. and is licensed under the Apache Public License version 2.0.
Scott
