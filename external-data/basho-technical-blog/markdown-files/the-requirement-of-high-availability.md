---
title: "The Requirement of High Availability"
description: "February 17, 2015 According to TechTarget, a common definition of “High Availability” is: "In information technology, high availability refers to a system or component that is continuously operational for a desirably long length of time. Availability can be measured relative to "100% operational" o"
project: community
lastmod: 2015-05-28T19:23:31+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Tyler Hannan"
pub_date: 2015-02-17T07:57:14+00:00
---
February 17, 2015
According to TechTarget, a common definition of “High Availability” is:
The reality is that this phrase has become semantically overloaded by its inclusion in marketing copy across a disparate set of technologies. Much like “Big Data”, perspectives on availability vary based on industry and customer expectation.
For many of today’s applications and platforms, high availability has a direct impact on revenue. A few examples include: cloud services, online retail, shopping carts, gaming and betting, and advertising. Further, lack of availability can damage user trust and result in a poor user experience for many social media and chat applications, websites, and mobile applications. Riak provides the high availability needed for your critical applications.
Availability – By the Numbers
As we highlighted in an infographic entitled Down with Downtime, more than 95% of businesses with 1,000+ employees estimate that they lose more than $100,000 for every 1 hour of downtime. For more than 1 in 2 large businesses, the cost of downtime amounts to more than $300,000 per hour. At the lower end of this scale, this is $83 dollars per minute. At the upper end of the spectrum (in financial services) it can amount to $1,800 a second of downtime.
This fiscal impact has resulted in availability being measured as a percentage calculation of uptime in a given year. This percentage is often referred to as the “number of 9s” of availability. For example, “one nine” of availability equates to 90% uptime in a year. Similarly, “five nines” (the standard that was set by consulting firms on enterprise projects) equates to 99.999% availability in a year. While that percentage is often referenced, the practical reality is that it means there can be no more than 6.05 seconds of unplanned downtime per week.
Availability – A Feature or A Benefit?
Often, when describing Riak, I begin by explaining the benefits of Riak (availability, scalability, fault tolerance, operational simplicity) and then discuss, in detail, the properties that these benefits are derived from. Availability is not something that can be added to a system (be it a distributed database or otherwise), rather it is an outcome of the core architectural decisions that were made in the development of the product.
Consider, for example, the AXD 301 ATM switch. It, reportedly, delivers at or better than “nine nines” (99.9999999%) of availability to customers. This is a staggering number that requires NO MORE than 6.048 milliseconds of downtime per week. Interestingly, it shares a common architectural component with Riak also being developed in Erlang.
“How does Riak achieve high availability?” Or, perhaps better stated as, “What are the architectural decisions made in Riak that enable high availability?”
Availability – An Architectural Decision
Riak is a masterless system designed for high availability, even in the event of hardware failures or network partitions. Any server (termed a “node” in Riak) can serve any incoming request and all data is replicated across multiple nodes. If a node experiences an outage, other nodes will continue to service read and write requests. Further, if a node becomes unavailable to the rest of the cluster, a neighboring node will take over the responsibilities of the missing node. The neighboring node will pass new or updated data (termed “objects”) back to the original node once it rejoins the cluster. This process is called “hinted handoff” and it ensures that read and write availability is maintained automatically to minimize your operational burden when nodes fail or comes back on-line.
More information about the architectural decisions involved in Riak’s design are available in our documentation. In particular, the Concepts – Clusters section is deeply illustrative.
Availability – A Use Case
Consider, for example the implementation of Riak at Temetra. Temetra has thousands of users and millions of meters that create billions of data points. The massive influx of data that was being generated quickly became difficult to manage with the company’s legacy SQL database. When considering how this structured database could be overhauled, Temetra conducted evaluations with Cassandra and Hadoop but ultimately chose Riak due to its high availability, relatively self-maintaining and easy to deploy infrastructure. It is essential that the data collected from the meters is always available as it is relied on to determine correct billing for Temetra’s customers.
Availability – A Summary
The reality is that a database, even a distributed, masterless, multi-model platform like Riak, is only one component of the application stack. Understanding your availability requirements requires deep knowledge of the entirety of the deployment environment. “High Availability” cannot be retrofit into a system. Rather it requires conscious effort in the early stages to ensure that customer requirements are met and that downtime does not result in lost customers and lost revenue.
Tyler Hannan
