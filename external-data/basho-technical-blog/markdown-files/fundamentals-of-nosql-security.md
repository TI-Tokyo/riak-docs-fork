---
title: "Fundamentals of NoSQL Security"
description: "February 23, 2015 Over the last week, for a variety of reasons, the topic of security in the NoSQL space has become a prominent news item. Chief among these reasons was the announcement of a popular NoSQL database having multiple instances exposed to the public internet. From the headlines you migh"
project: community
lastmod: 2015-05-28T19:23:31+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Tyler Hannan"
pub_date: 2015-02-23T16:02:03+00:00
---
February 23, 2015
Over the last week, for a variety of reasons, the topic of security in the NoSQL space has become a prominent news item. Chief among these reasons was the announcement of a popular NoSQL database having multiple instances exposed to the public internet. From the headlines you might think that NoSQL solutions have inherent security problems. In fact, in some cases, the discussion is positioned intentionally as a relational vs. NoSQL issue. The reality is that NoSQL is not more or less secure than a traditional RDBMS.
The Security of any component of the technology stack is both the responsibility of the vendor providing the technology and those that are deploying it. How many routers are running with the default administrative password still set? Similarly, exposing any database, regardless of type, to the public internet without taking appropriate security precautions, including user authentication and authorization, is a “bad idea.” A base level of network security is an absolute requirement when deploying any data persistence utility. For Riak this can include:

Appropriate physical security (including policies about root access)
Securing the epmd listener port, handoff\_port listener port, and the range ports specified in the riak.conf
Defining users and optionally, groups (using Riak Security in Riak 2.0)
Defining an authentication source for each user
Granting necessary permissions to each user (and/or group)
Checking Erlang MapReduce code for invocations of Riak modules other than riak\_kv\_mapreduce
Ensuring your client software passes authentication information with each request, supports HTTPS or encrypted Protocol Buffers traffic

If you enable Riak security without having an established functioning SSL connection, all request to Riak will fail because Riak security (when enabled) requires a secure SSL connection. You will need to generate SSL certificates, enable SSL, and establish a certification configuration on each node.
The security discussion does not, however, end at the network. In fact, for those who are familiar with the Open Systems Interconnection model (OSI), a 7 layer conceptual model that characterizes and standardizes the internal functions of a communication system by partitioning it into abstraction layers, (ISO 7498-1) there is a corresponding security architecture reference (ISO 7498-2)…and that is just for the network. It is necessary to take adopt a comprehensive approach to security at every layer of the application stack…including the database.
The process of securing a database, which is only a component of the application stack, requires striking a fine balance. Riak has worked with large enterprise customers to ensure that Riak’s security architecture meets the needs of their application deployments and balances the effort required with the security, or compliance, requirements demanded by some of the worlds largest deployments.
NoSQL vs. Relational Security
As enterprises continue to adopt NoSQL more broadly, the question of security will continue to be raised. The reality is simple, it is necessary to evaluate the security of the database you are exploring in the same way that you would evaluate its scalability or availability characteristics. There is nothing inherent to the NoSQL market that makes it less, or more, secure that relational databases. It is true that some relational database, by aegis of their age and maturation, have more expansive security tooling available. However, when adopting a holistic, risk-based approach to security NoSQL solutions — like Riak — are as secure as required.
Security and Compliance
A compliance checklist (be it HIPAA or PCI) details, in varying specificity, the security requirements to achieve compliance. This checklist is subsequently verified through an audit by an independent entity…as well as ongoing internal audits.
So can I use NoSQL in compliant environments?
Without question, Yes. The difficulty of achieving compliance will depend on how the database is configured, what controls it provides for authentication and authorization, and many other elements of your application stack (including physical security of the datacenter, etc). Riak customers have deployed Riak in highly regulated environments and achieved their compliance requirements.
I would encourage you, however, to realize that compliance is an event. The process of securing your application, database, datacenter, etc. is an ongoing exercise. Many, particularly those in the payments industry, refer to this as a “risk-based” approach to security vs. a “compliance-based” approach.
Security and Riak
In nearly all commercial deployments of Riak, Riak is deployed on a trusted network and unauthorized access is restricted by firewall routing rules. This is expected, this is necessary and is sufficient for many use cases (when included as part of a holistic security posture including locking down ports, reasonable policies regarding root access, etc.). Some applications need an additional layer of security to meet business or regulatory compliance requirements.
To that end, in Riak 2.0, the security store changed substantially. While you should — without question — apply network layer security on top of Riak and the systems that Riak runs upon, there are now security features built into Riak that protect Riak itself, not just its network. This includes authentication (the process of identifying a user) and authorization (verifying whether the authenticated user has access to perform the requested operation). Riak’s new security features were explicitly modeled after user- and role-based systems like PostgreSQL. This means that the basic architecture of Riak Security should be familiar to most.
In Riak, administrators can selectively control access to a wide variety of Riak functionality. Riak Security allows you to both authorize users to perform specific tasks (from standard read/write/delete operations to search queries to managing bucket types and more) and to authenticate users and clients using a variety of security mechanisms. In other words, Riak operators can now verify who a connecting client is and determine what that client is allowed to do (if anything). In addition, Riak Security in 2.0 provides four options for security sources:

trust — Any user accessing Riak from a specified IP may perform the permitted operations
password — Authenticate with username and password (works essentially like basic auth)
pam — Authenticate using a pluggable authentication module (PAM)
certificate – Authenticate using client-side certificates

More detail on the Riak 2.0 Security capabilities are presented in the Security section of the documentation, in particular the section entitled Authentication and Authorization.
With a NoSQL system that provides authentication and authorization, and a properly secured network, you have progressed a long way in reducing the risk profile of your system. The application layer, of course, must still be considered.
Learn More
Relational databases are still a part of the technology stack for many companies; others are innovating and incorporating NoSQL solutions either as a replacement for or alongside existing relational databases. As a result they have simplified their deployments, enhanced their availability, and reduced their costs.
Join us for this webinar where we will look at the differences between relational databases and NoSQL databases like Riak. We will look at why companies choose Riak over a relational database. We will analyze the decision points you should consider when choosing between relational and NoSQL databases and we will look at specific use cases, review data modeling and query options.
This Webinar is being held in two time slots:

Wednesday, March 4, 2015 8:00-9:00 AM PST (4:00-5:00 PM GMT)
Wednesday, March 4, 2015 12:00-1:00 PM PST (3:00-4:00 PM EST)

Tyler Hannan
