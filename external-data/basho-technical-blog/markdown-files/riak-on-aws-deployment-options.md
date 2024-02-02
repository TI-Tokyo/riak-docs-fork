---
title: "Riak on AWS - Deployment Options"
description: "High-level overview of deployment options for using Riak on Amazon."
project: community
lastmod: 2017-02-13T11:49:58+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-01-30T22:02:48+00:00
---
January 30, 2013
Many teams run Riak in public cloud environments, either as a part of their infrastructure or as the foundation of it. Increasingly, we see enterprises and startups use a hybrid implementation that leverages both private infrastructure and public cloud services. This hybrid model is often used to address burst capacity issues, tenancy/location concerns, and simple proof-of-concept implementations prior to hardware acquisition.
Over the past few years, we have seen substantive adoption of Riak on Amazon Web Services. To that end, we are pleased that Riak has been approved as an Amazon Web Services Technology Partner. We look forward to highlighting interesting use cases, publishing detailed case studies of usage, and continuing to improve the usability and deployment speed of Riak on the AWS platform.
This post provides a high-level overview of your deployment options for using Riak on Amazon.
How Many Nodes?
Before we discuss the mechanics of implementation, it is important to consider the size of your deployment. One of the most frequent questions Riak is asked is, “How many nodes should I start with?” For production deployments, we recommend that your cluster has a minimum of five nodes. For more details on how this minimum ensures the performance and availability of your implementation, please read the post entitled: Why Your Riak Cluster Should Have At Least Five Nodes.
So, you have a minimum of five nodes and you’ve decided that leveraging a cloud provider is appropriate for your current business needs. Now, how do you get started?
Amazon Machine Image
At its simplest, an Amazon Machine Image (AMI) is a pre-built machine image and configuration of Riak for Amazon EC2 users.
Obtaining and configuring the image is a relatively straightforward process. However, since Riak needs the nodes in the cluster to communicate with each other, there is some manual setup involved.
First, provision the Riak KV AMI or  Riak TS AMI onto the server of your choice via the AWS marketplace.
Once the virtual machine is created, manually configure the EC2 security group to allow the Riak nodes to speak to each other. The details of this step can be found on our docs portal under Installing on AWS Marketplace. However, this is generally as simple as opening a few inbound ports and defining a “Custom TCP rule.”
At this point, the machines can be clustered together. When the individual virtual machines are provisioned and the security group is configured, simply SSH into each machine and use internal riak-admin tool to join the nodes to the cluster.
Amazon CloudFormation
But what if you want to automate some of the configuration of your cluster? Or, what if you want the ability to set up a VPC-based stack that includes:

a front-end load balancer,
a cluster of application servers,
a Riak powered demo application,
a back-end load balancer,
and a cluster of Riak servers.

In that case, the Riak team has made available scripts that leverage AWS CloudFormation to build out your cluster in a scripted fashion.
Since this is a much different process than the previous method, it is well worth watching the introductory video (embedded below). In addition, the scripts in the cloudformation-riak repo can be thought of as “known good” templates. We accept Pull Requests and happy forking!

Manual Installation
As always, there is a manual option.
If you need to control the system configuration or are most comfortable with software that you have built and deployed yourself, there is always the option to install from package or source.
For a full list of supported operating systems, check out the Installing and Upgrading page of the doc portal. In addition, we have recently launched a new download page that includes the source for the OSS versions of Riak.
Final Thoughts
Highly available.
Fault-tolerant.
Low latency.
And easier to deploy than ever before. If you have feedback on present Riak AWS deployments or recommendations on ways to make Riak support for cloud infrastructure easier, please drop us a note in the mailing list.
The options above describe an OSS deployment of Riak. If you need multi-cluster replication and support, contact us to discuss Riak Enterprise.
@basho
