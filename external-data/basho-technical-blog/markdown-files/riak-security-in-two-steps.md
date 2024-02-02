---
title: "Riak Security in Two Steps"
description: "In recent weeks, security compromises have been reported against MongoDB, ElasticSearch, CouchDB and Hadoop installations. These attacks are costly to businesses and present real risks to user data. A similar scenario prompted us to publish a blog post detailing the Fundamentals of NoSQL Security ba"
project: community
lastmod: 2017-01-25T15:48:30+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Justin Pease"
pub_date: 2017-01-25T15:48:30+00:00
---
In recent weeks, security compromises have been reported against MongoDB, ElasticSearch, CouchDB and Hadoop installations. These attacks are costly to businesses and present real risks to user data. A similar scenario prompted us to publish a blog post detailing the Fundamentals of NoSQL Security back in February 2015.
System attacks are an ever-present threat in today’s business world. We urge all Riak operators to take the time to implement these best practices to secure their Riak clusters. Here is a quick checklist of the two areas to review:

Secure your network

SSL

Generate SSL certificates
Enable SSL
Establish a certificate configuration


Firewall

Configure appropriate ports for Riak usage




Setup Riak authentication and authorization

Define users and, optionally, groups
Define an authentication source for each user
Grant the necessary permissions to each user (and/or group)



The above checklist is just that: a checklist. It is not intended to provide complete coverage on the important and expansive topic of security. For detailed information on the specifics of Riak security, we highly recommend you review our relevant documentation:


Riak KV

Security Basics
Managing Security Sources


Security & Firewalls 


Riak TS

Security Checklist
Enable & Disable
User Management
Sources Management
Notifying Riak


Security Overview 


Riak CS

Account Management
Access Control Lists
Authentication


Accounts and Administration 


Riak Clients

Java
Python
Ruby
Erlang
PHP


Client Security



As always, we welcome you to reach out if you have any questions. Customers may do so by opening a support ticket via our online help desk. The community is invited to do so via our mailing list.
Justin Pease
VP, Services
@jpease
