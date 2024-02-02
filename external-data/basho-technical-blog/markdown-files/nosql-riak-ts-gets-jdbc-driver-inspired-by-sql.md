---
title: "NoSQL Riak TS Gets JDBC Driver Inspired by SQL"
description: "When Riak's engineering team released Riak TS 1.0 back in December 2015 one of the features that I found most exciting was its use of standard SQL. I know that there aren't a lot of people who get excited by SQL in this era of NoSQL databases but SQL isn't dead just yet. In the 30+ years that SQL h"
project: community
lastmod: 2016-08-30T17:30:47+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Craig Vitter"
pub_date: 2016-08-30T17:25:07+00:00
---
When Riak’s engineering team released Riak TS 1.0 back in December 2015 one of the features that I found most exciting was its use of standard SQL. I know that there aren’t a lot of people who get excited by SQL in this era of NoSQL databases but SQL isn’t dead just yet. In the 30+ years that SQL has been in use, it has had the opportunity to find itself integrated into the vast majority of databases and reporting tools used by enterprises. Essentially SQL has become the lingua franca of data analysis and by making SQL the query language of Riak TS, Riak made the database accessible to a wider range of potential users.
As cool as that is, as a developer, I realized that the use of SQL also made it possible to build a JDBC (Java Database Connectivity) driver for Riak TS. If you aren’t already familiar with the JDBC API, it provides Java applications standardized methods to connect to, query, and update data in any database (almost exclusively relational databases) that provides a JDBC driver. As an official part of the Java language since 1997, JDBC has been widely adopted by developers. If you use a reporting tool like those available from Cognos, Microstrategy, Business Objects, or Jaspersoft, then you can connect to any data source that provides a JDBC driver.
Once I realized how important a JDBC driver would be for Riak TS, I was compelled to write one. When I started down the path of writing a JDBC driver for Riak TS my goal was simply to use it as a learning opportunity, I wasn’t really convinced that I would have the time or ability to produce something that would be generally useful. As I started working on the driver the learning exercise became a viable project and so now I’ve decided to open source the project and share my work with the community:
https://github.com/basho-labs/Riak-TS-JDBC-Driver
There are two main reasons why you would want to use the JDBC Driver:

You are a Java application developer familiar with the JDBC API and want to integrate Riak TS into an application;
You use reporting tools like BusinessObjects, Cognos, or Jaspersoft that allow you to connect to databases using JDBC drivers.

If you have one or more of the proceeding uses for a JDBC driver for Riak TS check out the ReadMe at https://github.com/basho-labs/Riak-TS-JDBC-Driver/tree/master/riakts.jdbc.driver for full details on the driver’s capabilities and how to get started using it. And if you do use the driver please leave feedback, submit issues, or submit pull requests.
You can learn more about Riak TS at these links:

riak.com/riak-ts
Riak TS Getting Started Guide
Download Riak TS

Please reach out via twitter with any feedback.
Craig Vitter
@craigvitter
