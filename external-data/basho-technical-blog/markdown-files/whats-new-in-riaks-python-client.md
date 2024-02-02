---
title: "What's New In Riak's Python Client?"
description: "A summary of new and noteworthy features in the 1.3.0 release of the Riak client for Python."
project: community
lastmod: 2015-05-28T19:24:14+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mathias Meyer"
pub_date: 2011-08-04T00:00:00+00:00
---
August 4, 2011
There’s a whole slew of new and noteworthy features in today’s release of the Python client for Riak, and I thought it’d be a good idea for us to sit down and look at a bunch of them so I can add more detail to what is already in the release notes.
Test Server
Ripple has had an in-memory test server for a while now, and I thought the Python client should have something similar too. By the way, a lot of the features here draw heavy inspiration from Ripple in general, so credit where credit is due.
The basic idea is that instead of using a locally installed Riak instance with file system storage you use one that stores data in memory instead. This is not only faster than storing everything on disk, it makes it much easier to just wipe all the data and start from scratch, without having to restart the service. In short, this is a neat way to integrate Riak into your test suite.
All the test server requires is a local installation to use the libraries from and to steal some files to build a second Riak installation in a temporary directory. Let’s look at an example:
“`python
from riak.test\_server import TestServer
server = TestServer()
server.prepare()
server.start()
“`
This will start a Riak instance in the background, with the Python part interacting with it through the Erlang console. That allows you to do things like wiping all data to have a minty fresh and empty Riak installation for the next test run:
python
server.recycle()
The TestServer class has a default of where to look for a Riak installation, but the path could be anywhere you put a Riak build you made from an official release archive. Just point it to that Riak installation’s bin directory, and you’re good to go:
python
server = TestServer(bin\_dir="/usr/local/riak/0.14.2/bin")
You can also overwrite the default settings used to generate the app.config file for the in-memory Riak instance. Just specify a keyword pointing to a dictionary for every section in the app.config like so:
python
server = TestServer(riak\_core={"web\_port": 8080})
By default the test server listens on ports 9000 (HTTP) and 9001 (Protocol buffers), so make sure you adapt your test code accordingly.
Using Riak Search’s Solr-compatible HTTP Interface
One of the nice things about Riak Search is its Solr-compatible HTTP interface. So far, you were only able to use Riak Search through MapReduce. New in release 1.3 of the Python client is support to directly index and query documents using Riak Search’s HTTP interface.
The upside is that you can use Riak Search with a Python app as a scalable full-text search without having to store data in Riak KV for them to be indexed.
The interface is as simple as it is straight forward, we’ve added a new method to the RiakClient class called solr() that returns a small façade object. That in turn allows you to interact with the Solr interface, e.g. to add documents to the index:
python
client.solr().add("superheroes",
{"id": "hulk",
"name": "hulk"
"skill": "Hulksmash!"})
You just specify an index and a document, which must contain a key-value pair for the id, and that’s it.
The beauty about using the Solr interface is that you can use all the available parameters for sorting, limiting result sets and setting default fields to query on, without having to do that with a set of reduce functions.
python
client.solr().query("superheroes", "hulk", df="name")
Be sure to check our documentation for the full set of supported parameters. Just pass in a set of keyword arguments for all valid parameters.
Something else that’s new on the search integration front is the ability to programmatically enable and disable indexing on a bucket by installing or removing the relevant pre-commit hook.
python
bucket = client.bucket("superheroes")
if not bucket.search\_enabled():
bucket.enable\_search()
Storing Large Files With Luwak
When building Riagi, the application showcased in the recent webinar, I missed Luwak support in the Python client. Luwak is Riak’s way of storing large files, chunked into smaller bits and stored across your Riak cluster. So we added it. The API consists of three simple functions, store\_file, get\_file, and delete\_file.
“`python
client.store\_file(“hulk”, “hulk.jpg”)
client.get\_file(“hulk”)
client.delete\_file(“hulk”)
“`
Connection Caching for Protocol Buffers and HTTP
Thanks to the fine folks at Formspring the Python client now sports easier ways to reuse protocol buffer and even HTTP connections, and to make their use more efficient. All of them are useful if you’re doing lots of requests or want to reuse connections across several requests, e.g. in the context of a single web request.
Here’s a summary of the new transports added in the new release, all of them accept the same parameters as the original transport classes for HTTP and PBC:

riak.transports.pbc.RiakPbcCachedTransport
A cache that reuses a set of protocol buffer connections. You can set a boundary of connections kept in the cache by specifying a maxsize attribute when creating the object.
riak.transports.http.RiakHttpReuseTransport
This transport is more efficient when reusing HTTP connections by setting SO\_REUSEADDR on the underlying TCP socket. That allows the TCP stack to reuse connections before the TIME\_WAIT state has passed.
riak.transports.http.RiakHttpPoolTransport
Use the urllib3 connection pool to pool connections to the same host.

We’re always looking for contributors to the Python client, so keep those pull requests coming!
— Mathias
