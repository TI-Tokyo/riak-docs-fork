---
title: "Riak CS: Proxy vs. Direct Configuration"
description: "When to choose Proxy versus Direct configuration in Riak CS."
project: community
lastmod: 2015-05-28T19:23:36+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Hector Castro"
pub_date: 2013-09-18T09:00:21+00:00
---
September 18, 2013
The other day you heard about a cool new object storage solution, Riak CS, with an Amazon S3-compatible API. You starred the repository on GitHub so that you could easily find it on another day when there’s more time to play.
That day is today.
(If you haven’t heard about the cool new object storage solution called Riak CS, today is your lucky day.)
You download and install the Riak and Riak CS packages for your operating system and dig into the configuration files. For Riak CS, the configuration files live in a file named app.config within /etc/riak-cs.
As you skim through the default settings and the comments that surround them, something stands out. The default for cs\_root\_host is set to s3.amazonaws.com. Before reading the comments, your mind begins to speculate, “Does Riak CS talk to S3? I thought this was meant to replace Amazon S3!”
Good news: Riak CS doesn’t talk to S3.
Instead, this configuration item makes it possible to direct Amazon S3 clients to your Riak CS installation, even if they weren’t designed to support an S3-compatible alternative.
Proxy Configuration for S3 Clients
Ideally, your client does support alternatives to S3. If so, skip to the “Direct Configuration for S3 Clients” section below. However, if you’re not so lucky, read on.
A proxy configuration allows S3 clients to communicate with Riak CS as if it were Amazon S3. When configuring these clients, you’ll need:

The hostname and port of your Riak CS cluster, configured under your client’s proxy settings
The Riak CS user credentials (access\_key and secret\_key)

When requests from this client hit Riak CS, they are processed and returned to the client as if they were serviced by S3.
Note that in this scenario, URLs returned from Riak CS will contain s3.amazonaws.com. Also, several S3 clients only allow you to set one proxy per client. Both of these issues make things difficult if you’re trying to link users to objects stored in Riak CS, or if you want to interact with Riak CS and S3 simultaneously from the same client.
Direct Configuration for S3 Clients
A direct configuration requires that the client has support for interacting with an S3-compatible service. This boils down to a client that allows you to alter the endpoint of the storage service you want to use.
Examples of clients that allow you to do this:

Transmit
s3cmd
DragonDisk

There is no S3 trickery in this scenario. The client connects directly to Riak CS without any proxies. To make this work, the value for cs\_root\_host needs to change to the fully qualified domain name (FQDN) of your Riak CS cluster.
Also, since S3 uses a subdomain to identify buckets created within it, in the spirit of S3-compatibility, Riak CS does too. In order to make this work in your environment, you will need a wildcard DNS entry. This is typically hosted beneath a Riak CS-specific subdomain. If you use storage.example.com as your cluster name, you’ll need \*.storage.example.com defined as a DNS entry with the appropriate IP address so the S3 buckets will resolve properly.
Conclusion
There are pros and cons to each approach. Proxy is easier to setup initially and works with a wider variety of clients. Direct requires a bit more technical expertise and works with a smaller number of clients, but allows you to rid your application of references to s3.amazonaws.com.
Choose the one that makes the most sense for your use case. We’re just glad you chose Riak CS.
Resources

Riak CS docs
Riak CS code on GitHub
Riak CS download and installation
Riak CS configuration
Riak mailing list for questions

Hector Castro
