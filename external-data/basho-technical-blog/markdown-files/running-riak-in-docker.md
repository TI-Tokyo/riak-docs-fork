---
title: "Running Riak in Docker"
description: "Unless you've been living under a rock for the last couple of years (and believe me, given what's happening in the world today, I ain't gonna judge) you know that Docker is building an Empire in the World of Containers. It's permeating DevOps and infrastructure, microservices, financial services, he"
project: community
lastmod: 2016-09-29T06:44:47+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Jon Brisbin"
pub_date: 2016-09-29T06:00:28+00:00
---
Unless you’ve been living under a rock for the last couple of years (and believe me, given what’s happening in the world today, I ain’t gonna judge) you know that Docker is building an Empire in the World of Containers. It’s permeating DevOps and infrastructure, microservices, financial services, healthcare, and just about anywhere that containerized applications make sense. Although it might one day power a smart IoT application that helps beat cancer, by itself it is no operational panacea. It can do more harm than good if wielded irresponsibly. In this blog post, I’ll lay out some groundwork for running a Riak cluster in Docker. Expect to see:

How to run single nodes as well as multiple node clusters.
How to test the container.
How to run applications that connect to Riak.
How to build your own Docker container with Riak installed.

Getting Started
To run Riak in Docker you need a relatively recent version of the daemon. Everything should probably work on 1.11, though for the purposes of this article I’ll assume you’re using a recent version of 1.12. Swarm mode is outside the scope of this post but it’s there if you want to experiment on your own.
You don’t need anything special to run Riak in Docker beyond just Docker. It seems a little anti-climactic, but here’s all you need to run a single node of Riak KV using a Ubuntu Trusty base image:
docker run --name=riak -d -p 8087:8087 -p 8098:8098 basho/riak-kv

Docker will download the image from Docker Hub (KV, TS) and start a single node.
You should then be able to start any client in your preferred language and connect to either localhost:8087 or the IP address of your box (or VM, if you happen to be running the above inside a virtual machine).
Similarly for Riak TS (Time Series):
docker run --name=riak -d -p 8087:8087 -p 8098:8098 basho/riak-ts

NOTE: Since both these example commands use the container name “riak” and the standard ports, you’ll have to stop the first container before starting the second. docker rm -f riak should do the trick.
In this example, we’re mapping the ports to their standard values using port mapping. If you want to use randomly-assigned ports that you can discover later (because you want to run multiple containers on the same host), just remove the -p options and replace them with a single -P:
docker run --name=riak -d -P basho/riak-ts

Connecting to a Riak Node
NOTE: This entire section assumes the use of Docker’s bridge networking. Using host networking will involve additional considerations and will be the topic of a different post.
In order to connect to a Riak node running in Docker, you need to know what IP address to use. That will vary depending on the network settings you’re using for that container. In the default bridge configuration, you can access Riak via the internal Docker IP address (probably 172.17. or similar) and use the default port of 8087, \*regardless\* of what you have set in the port mappings. If you access Riak via localhost, however, you can \*only\* use the mapped ports (in our example: 8087 and 8098).
TIP: If you’re running an application in another Docker container and that container has access to the Docker subnet your Riak container is running in, you should have no problems. Before confusing everything too much with custom configurations and multiple subnets, try running your Riak nodes using the Docker defaults–at least until you’re comfortable with the peculiarities of running a clustered database with complex networking needs inside Docker.
To discover the IP address and port combinations needed to connect to Riak, use docker inspect. In general, you only need to discover one of the two HOST:PORT values: either HOST or PORT. If you use the Docker internal IPs, then you can use the standard Riak ports of 8087 and 8098 for Protobuf and HTTP, respectively. If you use any other IP (like the IP address of your box or VM), then you’ll need to discover the PORT values and use a pre-determined IP address value. The two variations can be supported by using docker inspect and specifying a Go template string in the -f flag to filter the JSON output to only show the values we care about.
To discover the port
Assuming all your containers are running on a single host and you’ll reuse the IP address (for the purposes of this example: localhost), you only need to discover what the port mappings are. The following Go template expression should spit out the protobuf ports (replace 8087 with 8098 to get the HTTP ports):
docker inspect -f 'localhost:{{(index (index .NetworkSettings.Ports "8087/tcp") 0).HostPort}}' riak

This will print a list of the mapped ports, one host:port line per container. In this example, we’re only specifying the riak container which we started earlier. If we wanted to inspect an entire cluster, we’d have to list all the containers of the cluster.
docker inspect -f 'localhost:{{(index (index .NetworkSettings.Ports "8087/tcp") 0).HostPort}}' riak-1 riak-2 riak-3 riak-4 riak-5

If we pipe this output through tr, we can create a comma-separated list of HOST:PORT pairs suitable for passing to the various Riak client libraries. They each have their own way of specifying a list of nodes to connect to, so YMMV. At a minimum, you’ll want to translate the newlines to commas and maybe set an environment variable.
export RIAK\_HOSTS=$(docker inspect -f 'localhost:{{(index (index .NetworkSettings.Ports "8087/tcp") 0).HostPort}}' riak | tr '\\n' , | sed 's/,$//')

NOTE: The tr is to pull the separate lines into a single line, separating them by commas, and the sed is to strip the final comma off the end since it might be a little more awkward for some logic to deal with an empty string when it’s expecting HOST:PORT.
To discover the IP
This will work on just about any Linux distribution or Mac OS X using docker-machine with an appropriate route set up. This will \*not\* work on the Mac OS X native Docker beta since it’s not currently possible to route traffic from your Mac across the internal xhyve VM running the Docker daemon to the 172.17. addresses that Docker uses. For more information, read the GitHub issue on this topic.
export RIAK\_HOSTS=$(docker inspect -f '{{.NetworkSettings.IPAddress}}:8087' riak | tr '\\n' , | sed 's/,$//')

This will give you something like 172.17.0.3:8087. Pass this value to the client library of your choice (assuming where you’re running this client has access to the 172.17. subnet, as just discussed). That’s easiest to do if your client code is \_also\_ running inside a Docker container. If you’re using the native Mac beta of Docker, this is also the only way to access those 172.17. addresses reported by docker inspect.
Clustering
If you already have the infrastructure for creating Riak clusters, then you could likely reuse it with Docker by replacing direct calls to riak and riak-admin with docker exec $CONTAINER riak|riak-admin. Another option for taking advantage of Riak’s clustering is to create the cluster manually using docker exec and riak-admin. Just follow the excellent documentation on creating a cluster and prepend all the riak-admin commands with docker exec in the appropriate container. Since this is a manual process and will be blown away when you restart the node, you should really only consider this approach appropriate for ad hoc testing and custom automation; it’s also beyond the scope of this post.
Unless you’re building your own Docker image and intentionally excluding Riak Explorer for a specific reason, you can take advantage of the simple cluster bootstrapping functionality that’s baked into the Docker image. Riak Explorer is used for this because its clustering operation combines the node add and cluster commit operations into a single REST call. This bootstrapping is activated when the value of the COORDINATOR\_NODE environment variable passed to docker run is the IP address of the first node in a cluster.
Starting a Dockerized Cluster
In order to start a Dockerized cluster, you must first start a coordinator node. This is the first node in a cluster and the one which subsequent nodes will join to in order to create the cluster. In these examples, we’ll start the nodes manually with docker run to illustrate the steps. Afterward, we’ll create a docker-compose.yml file to encapsulate this functionality into an easily-digestible form.
The following starts a coordinator node, mapping the ports to their default settings. This will be the primary node we interact with and the one we pass the IP address to when we start other nodes.
docker run --name=riak -d -p 8087:8087 -p 8098:8098 --label cluster.name=adhoc basho/riak-kv

NOTE: The ability to tag containers with arbitrary labels is a very powerful–and sometimes overlooked–feature of Docker. Whenever you start a container for a Riak cluster, it will make your life easier to tag that container with a label to make them easy to find and manipulate later.
We can now discover the IP address we’ll need to use as the value of COORDINATOR\_NODE by using docker inspect:
$ docker inspect -f '{{.NetworkSettings.IPAddress}}' riak
172.17.0.3

Whenever we start the other containers in this cluster, we’ll just pass -e COORDINATOR\_NODE=172.17.0.3 and the cluster will be auto-created.
docker run -d -P -e COORDINATOR\_NODE=172.17.0.3 --label cluster.name=adhoc basho/riak-kv

TIP: Instead of using a hard-coded IP address, you can replace it with a shell expression like $(docker inspect -f '{{.NetworkSettings.IPAddress}}' riak) to facilitate automating these steps. Don’t forget to parameterize the name of the container being passed to the coordinator node (the value of --name in the docker run command).
Some notable differences between this command and the one we used to start the coordinator node are:

No --name specified. We likely won’t be referring to this individual node itself, but by finding the container ID using docker ps and filtering on the label.
No specific port mapping. Running multiple nodes on the same host means each container will have to have the standard ports mapped to available ones. It’s easiest to let Docker handle that and randomly assign the mappings. We’ll look these values up later with docker inspect anyway.
Addition of the COORDINATOR\_NODE environment variable. The bootstrapping code will use this IP address to join to when starting the container.

Simply repeat the above command once for each node you want to start.
Using Riak Explorer
Riak Explorer comes bundled with Riak in the standard Docker image. It provides a comprehensive HTTP API that adds functionality not available in the standard Riak HTTP API. If you started the coordinator node using a command similar to the one shown above, you should be able to open the Riak Explorer UI in a web browser by navigating to http://localhost:8098/admin/. If you’re using Linux, you can alternatively use the Docker IP passed as the COORDINATOR\_NODE value (from the example above: http://172.17.0.3:8098/admin/).

Operational Info
Besides providing a nice web GUI for interacting with data in Riak, Explorer provides some nice graphs on resource usage for the cluster, as well as providing information about the nodes in the cluster. Pull up the Ops tab, where you can select “Individual Node Details” to see the list of nodes in the cluster and have links provided to view statistics, log files, and current configuration values for each node.

I’ll let you two get acquainted. Spend as much time as you like.
Using docker-compose
It’s fairly easy to encapsulate the necessary parameters to docker run to create a Riak cluster by using docker-compose. First, download one (or both) of the following example docker-compose.yml files and save them to your local disk.

NOTE: If you name them anything other than docker-compose.yml, remember that you’ll have to add the -f myfile.yml option to docker-compose every time you run the command.
There are two services defined in this docker-compose.yml file: a coordinator node and a member node. The intent is to use the coordinator as the value for the COORDINATOR\_NODE in subsequent member nodes to create the cluster. You will scale the service to 1 for coordinator and N for members (N = $CLUSTER\_SIZE – 1).
To start a 5-node cluster using docker-compose, use the command scale:
docker-compose scale coordinator=1 member=4

The containers will start in the background. You can monitor their progress with the logs command.
docker-compose logs

When all the member containers have started, you should be able to execute commands like riak-admin cluster status on the coordinator and see that the member nodes have successfully joined the cluster.
$ docker-compose exec coordinator riak-admin cluster status

---- Cluster Status ----
Ring ready: true

+---------------------+------+-------+-----+-------+
| node |status| avail |ring |pending|
+---------------------+------+-------+-----+-------+
| (C) riak@172.17.0.2 |valid | up | 87.5| 50.0 |
| riak@172.17.0.4 |valid | up | 0.0| 0.0 |
| riak@172.17.0.5 |valid | up | 0.0| 0.0 |
| riak@172.17.0.6 |valid | up | 0.0| 0.0 |
| riak@172.17.0.7 |valid | up | 12.5| 50.0 |
+---------------------+------+-------+-----+-------+

When you’re ready to take down the cluster, just use docker-compose down.
A Word on Volumes
You can use volumes in docker-compose for /var/lib/data and /var/log/riak–as well as for the schemas in /etc/riak/schemas. Compose seems to encourage using --volumes-from a specific container (which you’re certainly free to do). In order to use, say, a local directory, you’ll need to declare the volume as external in the YAML config.
Advanced Configuration
In order to customize the container bootstrapping process with custom configuration, you have several options:

Mount the needed configuration items into the container as a volume. e.g. Add -v $(pwd)/riak.conf:/etc/riak/riak.conf to the docker run command.
Augment the default riak.conf using sed like the built-in bootstrapping script.
Derive a new container from the standard container and copy in the config using COPY in a Dockerfile.
Log into the container interactively using docker exec -it, change the config, and manually restart the node (not really recommended except for one-off, ad hoc situations where you’re just experimenting since everything will be lost when the container stops).

Bucket Type Bootstrapping
The Riak Docker images contain specialized bootstrapping code to find files in the /etc/riak/schemas directory that end in .dt or .sql and use the contents of those files to create bucket types or time series tables, respectively.
Create Schema Files
If you want to bootstrap a KV datatype, create a file in the /etc/riak/schemas directory named bucket\_name.dt. Replace bucket\_name with the name you want to use for the bucket. Inside the file, include a single line that contains the datatype you want to use for this bucket.
For example, to create a bucket named “counter” for the CRDT datatype counter, create a file named counter.dt and put the text counter as the only content in the file.
echo "counter" >schemas/counter.dt

Mount the schemas into the container as a volume when running the container:
docker run -d -P -v $(pwd)/schemas:/etc/riak/schemas basho/riak-ts

If you pull up Riak Explorer, as described above, you should see a bucket type of “counter” listed in the DATA tab.
Create TS Tables
The process for creating time series tables is identical to that for bucket types, except the content of the file will be a CREATE TABLE command.
cat 
@j\_brisbin
