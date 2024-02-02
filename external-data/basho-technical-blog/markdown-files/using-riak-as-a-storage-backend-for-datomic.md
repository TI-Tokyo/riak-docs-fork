---
title: "Using Riak as a Storage Backend for Datomic"
description: "How to enable Riak as a storage service for Datomic."
project: community
lastmod: 2016-10-20T07:50:33+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Hector Castro"
pub_date: 2014-02-12T16:14:57+00:00
---
February 12, 2014
Datomic is a distributed database system that supports queries, joins, and ACID transactions. Through its pluggable persistence layer, you can wire Datomic up to a horizontally scalable key/value store that strives for operational simplicity, like Riak.
(Want to hear more about Datomic? Check out the Riak Community Hangout with Stuart Halloway of Cognitect.)
Below, we’ll explore the specifics around getting Riak enabled as a storage service for Datomic. We will also provide you with a Vagrant project that automates many of these steps, so you can have a local development environment with a Riak-backed Datomic running within minutes.
ZooKeeper
Datomic stores indexes and a log of known transactions in its storage backend. You can think of the indexes as sorted sets of datoms, and the data log as a recording of all transaction data in historic order.
Both of these pieces of data are stored as trees with blocks that are roughly 64K in size. The blocks themselves are immutable and cater very well to the strengths of eventual consistency. Other bits of data, like the root pointers (for the trees) for indexes and the data log, require the ability to compare-and-swap (CAS). They need to be stored in a strongly consistent backend.
Enter ZooKeeper.
We won’t go through the details of standing up a ZooKeeper ensemble here, but once you have, make sure you have a list of IP:PORT pairs for each instance (at least three recommended for production usage).
Note: Strong consistency is coming in Riak 2.0 and will make ZooKeeper unnecessary for this use case.
Riak
Riak is a distributed key/value store with an emphasis on high availability. To learn more, download the free eBook, A Little Riak Book.
To get started with Riak, head over to the Quick Start Guide and walk through the setup of a five-node cluster.
Transactor
In Datomic, the Transactor component is responsible for coordinating write requests and is a critical single point of failure. Think of the Transactor the same way you think about a relational database. You need one, but you may also want another ready to go if the primary fails.
The Transactor needs to know a few things about Riak:

riak-host
riak-port
riak-interface (valid options are protobuf or http – use protobuf)
riak-bucket (can just set this to datomic)

Note: The Transactor passes the Riak host and port to the riak-java-client. You’ll want to round-robin requests against all of the nodes in your cluster evenly (usually accomplished with a load balancer). If you setup a load balancer to front your Riak cluster, provide its host and port to the Transactor via riak-host and riak-port.
The Forbidden Dance
At this point it’s assumed that you have a ZooKeeper ensemble, Transactor instance, and Riak cluster ready to go. Now, fetch your list of ZooKeeper nodes and supply it (comma delimited) as the payload of an HTTP PUT request to Riak like so:

Now all of the components can talk to each other!

Vagrant
For those who aren’t familiar, Vagrant simplifies the process of creating and configuring virtual development environments. By combining it with a few Chef cookbooks for Datomic, ZooKeeper, and Riak, we can automate all of the steps described above (for a local development environment).
Simply clone the vagrant-datomic-riak repository and execute the following:

Within a few minutes, you should have a functioning local development environment. To test, install the Datomic Peer Library and walk through the tutorial.
Hector Castro
