---
title: "Reviving the Zombie App for OSCON"
description: "Returning to the Fight Zombies have been all around us for 2 years now and we’re starting to lose the battle.  Riak KV is the natural choice to fight off the zombie hordes. It scales as the war rages on and stays online even as our administrators are consumed (by other work). My dear colle"
project: community
lastmod: 2015-07-20T14:44:13+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Stephen Condon"
pub_date: 2015-07-20T14:42:26+00:00
---
Returning to the Fight
Zombies have been all around us for 2 years now and we’re starting to lose the battle.

Riak KV is the natural choice to fight off the zombie hordes. It scales as the war rages on and stays online even as our administrators are consumed (by other work).
My dear colleagues, the Brothers Kerrigan, taught us how to hunt them using inverted indexes in Riak. They took the next step to empower us all to win the war by creating an application to visualize their location.
It hasn’t been enough.
With the inevitable degradation of the Internet, we’ve needed a way to fight the oncoming horde as individuals. Download the Vagrantfile and join the fight.
:: Breaking character :: 
The inverted-indexes implementation, appreciatively nicknamed The Zombie Riak demo, has been a great way to highlight using Riak in a production application architecture. There are great ways to extend upon this project that I’ll list below.
Vagrant, started by Mitchell Hashimoto and part of the amazing suite of work from Hashicorp, is a standard for spinning up a local environment. Thanks to its simplicity and a little scripting, you can run The Zombie Riak demo right on your own laptop: https://github.com/basho-labs/vagrant-zombie-riak

How’s it Work?
The Vagrantfile is familiar in syntax:

The magic happens in two calls: provision.sh and zombie.sh run through the shell provisioner (more on the shell provisioner in Vagrant here). The first installs Riak KV and configures everything from Riak Search to leveldb as the storage backend. This file is an extension of how Engineers at Riak, like Bryce Kerley, test the client libraries using vagrant. The second file gets an environment ready to install the inverted-indexes repository:

Walking through the file, you find a great deal of environmental configuration, validation and lastly the running of our web server using Ruby’s unicorn gem. This all takes place with a simple vagrant up now.

Join the War Against Zombies
Get started with this example application and learn how to hunt zombies in Riak KV from the comfort of your laptop. Take the time to explore some of the nuances:

How data is loaded into Riak KV in load\_data.rb
Explore how the app discovers Riak KV instances in riak\_hosts.rb
Understand how inverted indexes are generated in index/inverted\_index.rb

There is a range of tools we could add to our toolset to fight off our brain-hungry foes. Here are a few that are now in issues on the repo:

Improve any of the visualizations, features or documentation are more than welcome: they’re appreciated
Design a manual upload server that can push new zombies into the database using a client or just using cURL
Create a Zombie Sighting Report System so the concentration of live zombies in an area can quickly be determined based on the count and last report date
Add a correlation feature, utilizing Graph CRDTs, so we can find our way back to Patient Zero
Prepare for demand of our system by configuring Nginx as a caching and load-balancing system
Add a crowdsourced Inanimate Zombie Reporting System so that members of the non-zombie population can report inanimate zombies. Incorporate Baysian filtering to prevent false reporting by zombies. They kind of just mash on the keyboard so this shouldn’t be too difficult


