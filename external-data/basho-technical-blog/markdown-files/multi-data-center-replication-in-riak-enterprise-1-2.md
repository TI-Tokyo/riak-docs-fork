---
title: "Multi-Data Center Replication in Riak Enterprise 1.2"
description: "August 8, 2012 The Replication team @Riak has been hard at work implementing new features for Multi-Data Center (MDC) Replication. These new features are the direct result of customer feedback, and are included in the release of Riak Enterprise 1.2. Riak Enterprise documentation is also now publ"
project: community
lastmod: 2015-05-28T19:24:11+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2012-08-08T00:00:00+00:00
---
August 8, 2012
The Replication team @Riak has been hard at work implementing new features for Multi-Data Center (MDC) Replication. These new features are the direct result of customer feedback, and are included in the release of Riak Enterprise 1.2. Riak Enterprise documentation is also now publicly available for the first time.
What is MDC Replication?
Replication is a tool available in Riak Enterprise that allows data to be copied between Riak clusters. Data can be copied on initial connection to a remote cluster, in realtime as a bucket is updated, or as a periodic full-synchronization. Although replication is uni-directional, remote clusters can be setup to replicate data back to a primary cluster, thus synchronizing bi-directionally.
These settings are all configurable along side other Riak settings in app.config, and by using the Riak Enterprise command line tool riak-repl (in your Riak Enterprise ./bin directory).
What’s new?
SSL
As replicating sensitive data over the internet isn’t safe, we now provide encryption via OpenSSL out of the box. Certificates signed by a standard Certificate Authority (CA) such as Verisign are supported, as well as self-signed certs.
Certificate chains can be validated down to the CA, but both certificates must resolve to the same root CA. Additionally, you can configure the number of intermediate CA’s allowed. Certificate common name whitelisting is also supported.
An example of enabling SSL is as easy as specifying these 4 parameters to the riak\_repl section of app.config:
bash
{ssl\_enabled, true},
{certfile, "/full/path/to/site1-cert.pem"},
{keyfile, "/full/path/to/site1-key.pem"},
{cacertdir, "/full/path/to/cacertsdir"}
Additional SSL configuration parameters are documented in the forthcoming Riak Enterprise Replication Operations Guide.
Per-bucket replication settings
Per-bucket replication allows for more granular control of exactly what and how things get replicated. Using this feature is as easy as setting a bucket property. Supported per-bucket replication schemes are: realtime only, full-sync only, both realtime + full-sync, and no replication.
For example, to entirely disable replication on a bucket titled “my\_bucket”:
bash
curl -v -X PUT -H "Content-Type: application/json" -d '{"props":{"repl":false}}' http://127.0.0.1:8091/riak/my\_bucket
The following example only replicates data during a full-sync (skipping real-time replication) on a bucket titled “my\_bucket”:
bash
curl -v -X PUT -H "Content-Type: application/json" -d '{"props":{"repl":"fullsync"}}' http://127.0.0.1:8091/riak/my\_bucket
These parameters are documented in the forthcoming Riak Enterprise Replication Operations Guide.
Extensive documentation updates
We are excited to be releasing new and improved Riak Enterprise documentation in v1.2. This documentation is now available publicly on the Riak wiki. Additional settings have been documented which allow for greater control of replication behavior.
Support for replication over NAT
It’s typical to see Network Address Translation (NAT) in an enterprise environment, so support has been added to make this easier for our customers to use. Combining SSL + replication over NAT should take care of securely copying data over the internet.
The new command:
bash
riak-repl add-nat-listener     
will allow the primary cluster (aka “listener”) to replicate data on both an internal IP/port and public IP/port.
Replication over NAT Example
Server A is the primary source of replicated data.
Server B and Server C would like to be clients of replicated data.
To configure this scenario:
Server A is setup with static NAT, configured for IP addresses:
192.168.1.10 (internal) and 50.16.238.123 (public)
Server A replication will listen on:

the internal IP address 192.168.1.10, port 9010
the public IP address 50.16.238.123, port 9011

Server B is setup with a single public IP address: 50.16.238.200
Server B replication will connect as a client to the public IP address 50.16.238.123, port 9011
Server C is setup with a single internal IP address: 192.168.1.20
Server C replication will connect as a client to the internal IP address of 192.168.1.10, port 9010
Configure a listener (replication server) on Server A:
bash
riak-repl add-nat-listener riak@192.168.1.10 192.168.1.10 9010 50.16.238.123 9011
Configure a site (replication client) on Server B
bash
riak-repl add-site 50.16.238.123 9011 server\_a\_to\_b
Configure a site (replication client) on Server C
bash
riak-repl add-site 192.168.1.10 9010 server\_a\_to\_c

To summarize, we hope that SSL, replication over NAT, per-bucket replication settings and updated documentation will allow for better control of Riak MDC Replication in your enterprise installation.
Thanks for reading!
Dave, Andrew, and Chris
