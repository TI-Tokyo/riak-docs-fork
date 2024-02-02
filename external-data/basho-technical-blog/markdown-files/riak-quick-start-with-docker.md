---
title: "Riak Quick Start with Docker"
description: "A technical guide to running Riak with Docker."
project: community
lastmod: 2016-09-30T10:32:08+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Hector Castro"
pub_date: 2014-04-21T13:00:51+00:00
---

This post is now outdated. Please refer to this more recent post: /posts/technical/running-riak-in-docker/

April 21, 2014
In the Riak documentation, one of the first sections contains a quick start. The goal of the quick start is to get Riak on your workstation and then establish a five-node cluster (in under five minutes).
The quick start itself is based on the source build of Riak. The source contains a Makefile with a target labeled devrel. The devrel (or development release) automates the creation of 5 separate copies of Riak. After the devrel process is complete, you can start each copy of Riak and join each instance into a cluster.
In a world with Linux Containers (LXC) and Docker, is there a way we can leverage these technologies to make the Riak quick start process more streamlined for developers? At the same time, can the isolation and portability of containers ease the transition of clusters between environments for operators?
Below is a first pass at it.
Prerequisites
Docker and LXC are key prerequisites for a container-based quick start. Luckily, Docker’s website has installation instructions for almost every flavor of Linux, Windows, and Mac OS. Furthermore, those instructions also include the installation of LXC.
Note: Before executing any of the commands below, ensure that your DOCKER\_HOST environmental variable is set correctly. This is the host that is running Docker’s server component:
$ export DOCKER\_HOST="tcp://127.0.0.1:4243" 
Building a Riak Image
Since we’re working with Docker’s API instead of LXC directly, the process of building a Riak container begins with a Dockerfile. A Dockerfile contains the steps required to build a Docker image. From that image, we can spawn container instances.
To get the Dockerfile, simply clone the docker-riak repository. From there, use the build Makefile target to build the container:

After the image building process is complete, you should see something like this from the output of docker images:

(See the entire Dockerfile contents here.)
Bring Up a Cluster
After the Docker image for Riak is created, the next step is to create a container out of it. But, since Riak is a distributed database, we don’t want to spin up just one Riak container – we want to spin up at least 5.
We also want the Riak containers to communicate with each other. Within Docker, this can be accomplished by linking containers. Linking connects one container to others by populating the target container’s environment with variables containing IP and port information of exposed endpoints associated with the source container.
Instead of establishing all of the links between containers manually, you can automated each step in another Makefile target labeled start-cluster. The start-cluster target is aware of three environmental variables that can alter its behavior:

DOCKER\_RIAK\_CLUSTER\_SIZE – The number of nodes in your Riak cluster (default: 5)
DOCKER\_RIAK\_AUTOMATIC\_CLUSTERING – A flag to automatically cluster Riak (default: false)
DOCKER\_RIAK\_DEBUG – A flag to set -x on the cluster management scripts (default: false)

To start a 5 node cluster, you can invoke the start-cluster target like this:

Now, not only do we have 5 Docker containers running Riak, but those containers are also joined into a cluster!
Testing a Cluster
From outside the container, we can interact with Riak’s HTTP or Protocol Buffers interfaces. For testing, we’re going to use the HTTP interface.
/stats
The HTTP interface has an endpoint called /stats that emits Riak statistics. The test-cluster Makefile target hits a random container’s /stats endpoint and pretty-prints its output to the console.
The most interesting attributes for testing cluster membership are ring\_members:

And ring\_ownership:

Together, these attributes let us know that this particular Riak node knows about all of the other Riak instances.
GETs and PUTs
We can also test GETs and PUTs to show that the nodes can accept reads and writes. At the same time, we can issue a PUT to one container and a GET from another to demonstrate that the nodes can communicate with each other.
Using docker ps, we can get a list of the running Riak containers. Note that the PORTS column includes multiple pairings for each container. We’re interested in the pairing associated with 8098 because that’s where Riak’s HTTP API endpoint is listening:

For riak04 (or container ID fba2c4d85aac), port 8098 is mapped to 49160. Let’s issue a PUT to riak04 with an arbitrary key and some data:

Now, let’s read the same value from riak02 (or container ID 9cac9ef525a5). In this case, port 8098 is mapped to 49156:

Looks like we have an operational Riak cluster!
Why?
Docker is an increasingly popular tool used to package applications (and their dependencies) into a virtual container that can run on any Linux server. Containers are almost as lightweight as a process, but with the isolation and portability of a virtual machine.
Running Riak within a Linux Container has advantages over a devrel in that it includes the Erlang distribution we test with and avoids the need to alter port bindings to prevent clashes. At the same time, it’s attractive to operators because a developer’s Riak cluster can be moved to another environment by simply saving and loading containers.
If you’re already playing around with Docker (or want to), give it a shot with Riak.
Hector Castro
