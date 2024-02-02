---
title: "Choosing the Right Tool"
description: "August 10, 2012 We have a poorly defined term in our industry: “NoSQL.” [Does your toaster run SQL? No? Then you own a NoSQL toaster.] Be that as it may, Riak falls under the umbrella of software that carries this label. In our attempt to own the label, we reinterpret it to mean that we now have"
project: community
lastmod: 2015-05-28T19:24:11+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2012-08-10T00:00:00+00:00
---
August 10, 2012
We have a poorly defined term in our industry: “NoSQL.” [Does your toaster run SQL? No? Then you own a NoSQL toaster.] Be that as it may, Riak falls under the umbrella of software that carries this label. In our attempt to own the label, we reinterpret it to mean that we now have more choices as developers. For too long, our only meaningful options for data storage were SQL relational databases and the file system.
In the past few years, that has changed. We now have many production-ready tools available for storing and retrieving data, and many of those fall within the sphere of NoSQL. With all of these new options, how do we as developers choose which database to use?
On the Professional Services Team, this is the first question we ask ourselves: What is the best storage option for this application? At Riak, Professional Services goes on-site to assist clients with training, application development, operational planning – anything to help get the most out of Riak. In order to know how to do that, we also have to know quite a bit about other NoSQL databases and storage options, and when it might be a better option to go with something other than Riak. Below we outline some of our reasoning when we evaluate Riak for our clients and their applications.
A Simple Key-Value Store
When our clients simply need a key-value store, our job as consultants couldn’t get any easier. Riak is a great key-value database with an excellent performance profile, fantastic high availability and scaling properties, and the best deployment/operations story that we know. We are very proud of our place in the industry when it comes to these features.
But when the business logic for the application requires an access pattern more sophisticated than a simple key lookup, we have to dig deeper to figure out whether Riak is the right tool for the job. We have evolved the following distinguishing criteria:
If there is a usage scenario requiring ad-hoc, dynamic querying, then we might consider alternative solutions.

Ad-hoc: by this we mean that queries run at unpredictable times, possibly triggered by end-users of the application.
Dynamic: by this we mean that queries are constructed at the time they are being run.

If the usage scenario requires neither ad-hoc nor dynamic queries, then we can usually construct the application in such a way that even complex analysis works well with Riak’s key-value nature. If the scenario requires ad-hoc but not dynamic queries, then we have look at options to tune performance of the known access patterns. If the scenario requires dynamic queries run on a regular basis, then we might investigate running the dynamic queries on an ‘offline’ cluster replica so that we don’t interfere with the availability of the ‘online’ production clusters.
These criteria began to take form in our evaluations of Riak for data analytics. We often see Riak deployed as a Big Data solution because of its exceptional fault-tolerance and scaling properties, and running analytics on Big Data is a common use case. MapReduce gives us the ability to run sophisticated analytics on Riak, but other solutions exist that are optimized for analytics in ways that Riak is not. It is generally not a good idea to run MapReduce on a production Riak cluster for data analysis purposes. MapReduce exists in Riak primarily for data maintenance, data migrations, or offline analysis of a cluster replicate. All three of these are good use cases for Riak’s MapReduce implementation.[1]
Key-Value State of Mind
Does that mean that data analysis applications are off the table? Absolutely not! In our training sessions and workshops, we emphasize that key-value databases requite a different mindset than relational databases when you are planning your application.
In traditional SQL applications, we as engineers start defining the data model, normalizing the data, and structuring models in such a way that relations can be fetched efficiently with appropriate indexing. If we do a good job modeling the data, then we can proceed with reasonable certainty that the application built on top if it will unfold naturally. The developers of the application layer will take advantage of well-known patterns and practices to construct their queries and get what they want out of the data model. It’s no surprise that SQL is pretty good for this kind of thing.
In a key-value store, we approach the software architecture from the opposite side and proceed in the other direction. Instead of asking what the data model should look like and working up to the application view, we begin by asking what the resulting view will look like and then work ‘backwards’ to define the data model. We start with the question: What do you want the data to look like when you fetch it from the database?
If we can answer the above question, and if we can define the structure of the result that we want in advance, then we probably have a good case for pre-processing the results. We pre-process the data in the application layer before it enters Riak, and then we just save the answer that we want as the value of a new key-value pair. In these cases, we can often get better performance when fetching the result than a relational approach because we don’t have to perform
the computation of compiling and executing the SQL query.
A rolling average is a simple example: Imagine that we want to have the average of some value within data objects that get added to the system throughout the day. In a SQL database, we can just call average() on that column, and it will compute the answer at query time. In a key-value store, we can add logic in the application layer to catch the object before it enters Riak, fetch the average value and number of included elements from Riak, compute the new rolling average, and save that answer back in Riak. The logic in the application layer is now slightly more complicated, but we weigh this trade-off against the simplicity of administering the key-value database instead of a relational one. Now, when you go to fetch the average, it doesn’t have to compute it for you. It just returns the answer.
With the right approach, we can build applications in such a way that they work well with a key-value database and preserve the highly available, horizontally scaling, fault-tolerant, easy-as-pie administration that we have worked so hard to provide in Riak. We look forward to continuing to help you get the most out of Riak, and choosing the best tool for the job.
Casey
Related
See Sean’s excellent post on Schema Design in Riak
Footnotes
[1]: In some situations, using MapReduce to facilitate a bulk fetch provides better performance than requesting each object individually because of the connection overhead. If you go that route, be sure to use the native Erlang MapReduce functions like ‘reduce\_identity’ already available in Riak. As always, test your solution before putting it into production.
