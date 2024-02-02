---
title: "Built on Riak: Dynamiq by Tapjoy"
description: "It’s a pleasure to see when Riak users find new and effective ways to build innovative applications on top of the distributed, open source system that is Riak. The team at Tapjoy leveraged Riak KV as a basis for a message queue and chose the Go programming language (aka Golang) to do so. They call i"
project: community
lastmod: 2015-08-11T09:17:33+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Stephen Condon"
pub_date: 2015-08-11T06:46:51+00:00
---
It’s a pleasure to see when Riak users find new and effective ways to build innovative applications on top of the distributed, open source system that is Riak. The team at Tapjoy leveraged Riak KV as a basis for a message queue and chose the Go programming language (aka Golang) to do so. They call it Dynamiq.
Why Message Queues? 
Message queues are a powerful, and necessary, requirement of modern application architectures because of the simple fact that they allow for asynchronous processes. 
Need a visual aid? I highly recommend watching the first 10 minutes of Martin Kleppmann’s talk from Craft Conf titled Using logs to build a solid data infrastructure. At the culmination of his architectural diagram, he gets to this view of the stack:

You can see a message queue on the side of this insanity.
Why the Mess?
What can be tricky about all this? 
Nearly all infrastructures require more than one type of data index. Each data source provides a query pattern that’s unique and, in some beneficial way, required by part of the application. The intuitive solution, to have the application write to multiple data sources, leads to a race condition (visualized below). Martin covers this practice in the talk above, which I borrow from (with his permission) in this introduction to message queues talk.

Knowing that writing to multiple sources isn’t reliable, how do we manage data indexing across these platforms?
Cue message queues.
An asynchronous workflow allows applications to process data reliably without slowing down the end user experience. Whether you want to write to 4 or 400 different data services, a message queues gives you an asynchronous method to decouple applications by separating the actions of sending and receiving data.
What’s Dynamiq?
Dynamiq is an at-least-once queue that benefits from the scalability and fault-tolerance of Riak KV. Messages are tagged with a 64-bit integer and partitioned across the cluster. This, in turn, is partitioned across Riak KV, which inherits its low-latency, high-performing distribution of data. This message ID is later queried through the 2i secondary search index.
Data is retrieved through the REST API, a ruby client, and there is a scala client in the works. You can unpack its implementation, API endpoints, and recommended configuration in the detailed README.
But Why  Dynamiq?

There are a number of message queues in the wild, from fellow Erlang-ers at RabbitMQ to the easy-to-use SNS and SQS services from Amazon, so you might wonder why Tapjoy decided to build their own. There are four specific business drivers that lead to this new project. 
1. High Availability at Scale
Just like databases, not all queues are created with the same system architectures in mind. We know how complicated distributed systems can be to implement. Designing a system that provides for high availability with low latency while keeping it easily scalable is a challenge for most. Leveraging Riak KV as a platform for Dynamiq ensured each of these requirements from the start given the masterless, AP-architecture of Riak KV. As Sean Kelly, one of the creators of Dyamiq, put it: “we’re leveraging a known product [in Riak KV], that is a rock solid distributed system.”
2. Cost
SNS and SQS are well loved inside Tapjoy and are still leveraged for other services. The use case that Dynamiq was designed for hit a level where these services were cost prohibitive. The README states Dyamiq’s goal as a “drop-in replacement for the Amazon’s SNS / SQS services, which can become expensive at scale, both in terms of price as well as latency.”
Hosting an open source software, on-premises, was a money saver and resulted in an even faster (i.e. lower latency) service.
3. Expertise
Tapjoy has expertise with Riak KV from years of running it in production. This in-house familiarity gives them the good fortune of being comfortable spinning up further Riak clusters and scaling them, up and down, based on demand. “It’s a thing we just know how to do,” Sean says when he discusses using Riak KV for Dynamiq.
The dev team at Tapjoy continue to find new and novel ways to build Riak KV into their services. There is a benefit to standardization, especially when it comes to how the Ops team monitors these clusters like any other in the production environment. 
4. Always be Learning
The team at Tapjoy are proud polyglots, continuing to pick up new programming languages on a regular basis. Part of the inspiration to design this solution was for that team to build a system — from start to finish — in Golang. 
Golang has increased in popularity in the Riak community to the point where we have engineers who have an open Beta of the Golang client.

 
Message Queues: Go Pick Something
The engineers at Tapjoy have created a powerful distributed message queue that will feel simple and familiar to any Riak KV user today. As Sean states in his presentation on message queues, we should start by becoming familiar with the use cases.
Here are some follow up posts for you:

Introduction to Amazon SQS (at Sean’s recommendation)
Kafka or RabbitMQ for durability (great quora thread)
Exploring message brokers (prototypes from ActiveMQ to Kafka w/specific needs)

Some key takeaways:

Distributed systems are hard – make sure your message queue knows how to scale
It’s worth the extra effort to wrap your Publisher code to prevent tight coupling with a specific message broker / queuing platform
It’s NOT worth doing the same on the Consumer side
Pay attention to client implementations (and its buffers)
If you have moderate scale, use SQS from Amazon
If you have a more challenging scale, don’t be shy about using Dynamiq from Tapjoy ( built on top of Riak KV)

Whether you need a message queue in your infrastructure or are inspired to open source your next project on Riak KV, be sure to share with our broader community.  We curate code using the Riak Labs organization on GitHub. Share your latest work with us here.
