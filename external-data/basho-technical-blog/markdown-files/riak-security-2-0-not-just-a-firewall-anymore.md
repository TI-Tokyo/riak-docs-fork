---
title: "Riak Security 2.0: Not Just a Firewall Anymore"
description: "October 9, 2014 Security is on everyone’s mind these days. Security in previous versions of Riak was typically applied at the network level, using a firewall or similar protective layer. While this is sufficient for many use cases, the issue with this approach is that in the unlikely event that s"
project: community
lastmod: 2016-10-20T07:09:20+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Luc Perkins"
pub_date: 2014-10-09T13:40:39+00:00
---
October 9, 2014
Security is on everyone’s mind these days. Security in previous versions of Riak was typically applied at the network level, using a firewall or similar protective layer. While this is sufficient for many use cases, the issue with this approach is that in the unlikely event that someone manages to penetrate the firewall, they may have unrestricted access to your Riak cluster, from performing basic read/write/delete operations, to running MapReduce jobs, and more. For some use cases, particularly where regulatory compliance is demanded, this is undesirable.
With the release of Riak 2.0, the security story has been significantly enhanced. While you should still apply a network security layer on top of Riak, there are now security features built into Riak that protect Riak itself, not just its network. Riak Security allows you to both authorize users to perform specific tasks (from standard read/write/delete operations to search queries to managing bucket types and more) and to authenticate users and clients using a variety of security mechanisms. In other words, Riak operators can now verify who a connecting client is and determine what that client is allowed to do (if anything).
Riak’s new security features were explicitly modeled after user- and role-based systems like PostgreSQL. This means that the basic architecture of Riak Security should be familiar to most.
Turning Riak Into a Fortress
This section will walk you through how to take a fresh Riak installation with no security whatsoever to creating users, managing roles, applying security sources, the whole deal.
This walkthrough assumes that you’ve installed Riak 2.0 and have a cluster up and running. If you don’t have a cluster running yet, you can download Riak, install it, and stand up a cluster in your local development environment.
Once your Riak cluster is up and running, cd into the installation’s directory. In the /bin directory for each node, you’ll see a riak-admin command. You can run that command via the /bin directory (in any running node) or alias the command to use it anywhere on your system. All Riak Security-related functionality is available through the riak-admin security command.
At first, by default, all the gates to your Riak cluster are completely open. Anyone can run any Riak operation on this cluster as long as they know the host/port and know how to use HTTP and/or Protocol Buffers. But with one simple command, we can change that:

If you run that command and get no response, then security has been successfully enabled on your cluster. Now, nobody can do anything with this cluster; no one can read or write objects, no one can run MapReduce jobs, no one can change bucket properties, etc. Riak is now an absolute fortress.
User Management
While turning Riak into a fortress might relieve some anxiety, you can’t really do anything with it in that state. It’s time to selectively enable access to certain parts of Riak.
First, we’ll create an admin user that enables anyone who can authenticate as it to have complete access to Riak (please note that admin is being used for the sake of example and is not a reserved term). We use the add-user command for that:

This creates the admin account, but we still need to grant permissions before the admin can do anything. Let’s enable the admin user to perform basic reads and writes on any bucket of any bucket type by granting the admin user the riak\_kv.get and riak\_kv.put permissions:

Now, anyone who can authenticate as the admin user is authorized to make read and write requests.
At this point, however, we’ve only specified what that account is authorized to do, but we still need to specify how it will be authenticated to perform those operations by designating our cluster’s security sources.
Source Management
Riak Security in 2.0 provides four options for security sources:

trust — Any user accessing Riak from a specified IP may perform the permitted operations
password — Authenticate with username and password (works essentially like basic auth)
pam — Authenticate using a pluggable authentication module (PAM)
certificate – Authenticate using client-side certificates

In this tutorial, we’ll use username/password as a security source for the sake of simplicity. This riak-admin command indicates that a connection from localhost that includes the admin password will be allowed:

When we created our admin account, we didn’t specify a password. We can change that using the alter-user command. Below, we assign the password riakmaster to the admin user:

Now we’ve created an admin user, granted permissions, and specified a security source (password). We can access Riak using the HTTPS interface. We have to use SSL because Riak does not accept credentials of any kind over HTTP, and we’ll run this from one of the Riak servers because we defined our source to apply to localhost only.
We’ll run a simple GET request on the key test\_key in the bucket test\_bucket, which will simply bear the default type, passing in the username and password:

If Riak security has been appropriately set up, this should return a simple not found, as there’s not yet any data in our empty yet secure Riak cluster. Let’s fix that:

That will return a response like this:

And there it is: a secure message from Riak.
Just the Beginning
More extensive information on Riak’s security features is available in our Authentication and Authorization and Managing Security Sources tutorials. With these features, you can require that all clients connecting to Riak authenticate themselves using certificates, provide access to specific buckets or bucket types only to some clients and not others, create users that are only allowed to run MapReduce jobs, and much more.
In the world of data storage, security is no longer optional. The security capabilities introduced in Riak 2.0 are just the beginning and provide a strong base for more security features in the future.
Resources

Riak Security: Locking the Distributed Chicken Coop (talk by Andrew Thompson at RICON)
Authentication and Authorization tutorial from the Riak docs
Security Sources tutorial from the Riak docs

We also have documentation on using the security features with Riak’s official client libraries:

Introduction to Client-side Security
Java
Ruby
Python
Erlang

Downloads
Riak Security is now available with Riak 2.0. Download Riak 2.0 on our Docs Page.
Luc Perkins
