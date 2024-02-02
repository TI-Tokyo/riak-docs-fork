---
title: "Creating a Local Riak Cluster with Vagrant and Chef"
description: "The Riak Fast Track has been around for at least nine months now, and lots of developers have gotten to know Riak that way, building their own local clusters from the Riak source. But there’s always been something that has bothered me about that process, namely, that the developer has to build Riak"
project: community
lastmod: 2015-05-28T19:24:15+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2011-02-04T20:01:20+00:00
---
February 4, 2011
The “Riak Fast Track” has been around for at least nine months now, and lots of developers have gotten to know Riak that way, building their own local clusters from the Riak source. But there’s always been something that has bothered me about that process, namely, that the developer has to build Riak herself. Riak provides pre-built packages on downloads.riak.com for several Linux distributions, Solaris, and Mac OS/X, but these have the limitation of only letting you run one node on a machine.
I’ve been a long-time fan of Chef the systems and configuration management tool by Opscode, especially for the wealth of community recipes and vibrant participation. It’s also incredibly easy to get started with small Chef deployments with Opscode’s Platform, which is free for up to 5 managed machines.
Anyway, as part of updating Riak’s Chef recipe last month to work with the 0.14.0 release, I discovered the easiest way to test the recipe — without incurring the costs of Amazon EC2 — was to deploy local virtual machines with Vagrant. So this blog post will be a tutorial on how to create your own local 3-node Riak cluster with Chef and Vagrant, suitable for doing the rest of the Fast Track.
Before we start, I’d like to thank Joshua Timberman and Seth Chisamore from Opscode who helped me immensely in preparing this.
Step 1: Install VirtualBox
Under the covers, Vagrant uses VirtualBox, which is a free virtualization product, originally created at Sun. Go ahead and download and install the version appropriate for your platform:
Step 2: Install Vagrant and Chef
Now that we have VirtualBox installed, let’s get Vagrant and Chef. You’ll need Ruby and Rubygems installed for this. Mac OS/X comes with these pre-installed, but they’re easy to get on most platforms.
Now that you’ve got them both installed, you need to get a virtual machine image to run Riak from. Luckily, Opscode “has provided some images for us that have the 0.9.12 Chef gems preinstalled. Download the Ubuntu 10.04 image and add it to your local collection:
Step 3: Configure Local Chef
Head on over to Opscode and sign up for a free Platform account if you haven’t already. This gives you access to the cookbooks site as well as the Chef admin UI. Make sure to collect your “knife config” and “validation key” from the “Organizations” page of the admin UI, and your personal “private key” from your profile page. These help you connect your local working space to the server.
Now let’s get our Chef workspace set up. You need a directory that has specific files and subdirectories in it, also known as a “Chef repository”. Again Opscode has made this easy on us, we can just clone their skeleton repository:

Now let’s put the canonical Opscode cookbooks (including the Riak one) in our repository:
Finally, put the Platform credentials we downloaded above inside the repository (the .pem files will be named differently for you):
Step 4: Configure Chef Server
Now we’re going to prep the Chef Server (provided by Opscode Platform) to serve out the recipes needed by our local cluster nodes. The first step is to upload the two cookbooks we need using the \*knife\* command-line tool, shown in the snippet below the next paragraph. I’ve left out the output since it can get long.
Then we’ll create a “role” — essentially a collection of recipes and attributes — that will represent our local cluster nodes, and call it “riak-vagrant”. Using knife role create will open your configured EDITOR (mine happens to be emacs) with the JSON representation of the role. The role will be posted to the Chef server when you save and close your editor.
The key things to note about what we’re editing in the role below are the “run list” and the “override attributes” sections. The “run list” tells what recipes to execute on a machine that receives the role. We configure iptables to run with Riak, and of course the relevant Riak recipes. The “override attributes” change default settings that come with the cookbooks. I’ve put explanations inline, but to summarize, we want to bind Riak to all network interfaces, and put it in a cluster named “vagrant” which will be used by the “riak::autoconf” recipe to automatically join our nodes together.
Step 5: Setup Vagrant VM
Now that we’re ready on the Chef side of things, let’s get Vagrant going. Make three directories inside your Chef repository called dev1, dev2, and dev3, just like from the Fast Track. Change directory inside dev and run vagrant init. This will create a Vagrantfile which you should edit to look like this one (explanations inline again):
Remember: change any place where it says ORGNAME to match your Opscode Platform organization.
Step 6: Start up dev1 Now we’re ready to see if all our preparation has paid off:
If you see lines at the end of the output like the ones above, it worked! If it doesn’t work the first time, try running vagrant provision from the command line to invoke Chef again. Let’s see if our Riak node is functional:

Awesome!
Step 7: Repeat with dev2, dev3
Now let’s get the other nodes set up. Since we’ve done the hard parts already, we just need to copy the Vagrantfile from dev1/ into the other two directories and modify them slightly.
The easiest way to describe the modifications is in a table:
| Line | dev2 | dev3 | Explanation |
| 7 | “33.33.33.12” | “33.33.33.13” | Unique IP addresses |
| 11 (last number) | 8092 | 8093 | HTTP port forwarding |
| 12 (last number) | 8082 | 8083 | PBC port forwarding |
| 40 | “riak-fast-track-2” | “riak-fast-track-3” | Unique chef node name |
| 48 | “riak@33.33.33.12” | “riak@33.33.33.13” | Unique Riak node name |
With those modified, start up dev2 (run vagrant up inside dev2/) and watch it connect to the cluster automatically. Then repeat with dev3 and enjoy your local Riak cluster!
Conclusions
Beyond just being a demonstration of cool technology like Chef and Vagrant, you’ve now got a developer setup that is isolated and reproducible. If one of the VMs gets too messed up, you can easily recreate the whole cluster. It’s also easy to get new developers in your organization started using Riak since all they have to do is boot up some virtual machines that automatically configure themselves. This Chef configuration, slightly modified, could later be used to launch staging and production clusters on other hardware (including cloud providers). All in all, it’s a great tool to have in your toolbelt.
— Sean
