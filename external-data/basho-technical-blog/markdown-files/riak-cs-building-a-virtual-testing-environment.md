---
title: "Riak CS: Building a Virtual Testing Environment"
description: "This blog post excerpts content from the Riak CS Fast Track. It walks through how to build a Riak CS environment using Vagrant and Chef."
project: community
lastmod: 2015-05-28T19:23:40+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-04-05T16:46:14+00:00
---
This blog post excerpts content from the Riak CS Fast Track. It walks through how to build a Riak CS environment using Vagrant and Chef. 
This option for building a test environment uses a Vagrant project powered by Chef to bring up a local Riak CS cluster. Each node can run either Ubuntu 12.04 or CentOS 6.3 32-bit with 1536MB of RAM by default. If you want to tune the OS or node/memory count, you’ll have to edit the Vagrantfile directly.
Install Prerequisites
Download and install VirtualBox via the VirtualBox Downloads.
Download and install Vagrant via the Vagrant Installer.
NOTE: Please make sure to install Vagrant 1.1.0 and above.
Clone the Repository
In order to begin, it is necessary to clone a GitHub repository to your local machine and change directories into the cloned folder.

$ git clone https://github.com/basho/vagrant-riak-cs-cluster
$ cd vagrant-riak-cs-cluster

Launch Cluster
With VirtualBox and Vagrant installed, it’s time to actually launch our virtual environment. The command below will initiate the Vagrant project:

$ RIAK\_CS\_CREATE\_ADMIN\_USER=1 vagrant up

If you haven’t already downloaded the Ubuntu or CentOS Vagrant box, this step will download it.
Recording Admin User Credentials
In the Chef provisioning output you will see entries that look like:

[2013-03-27T11:59:12+00:00] INFO: Riak CS Key: 5N2STDSXNV-US8BWF1TH
[2013-03-27T11:59:12+00:00] INFO: Riak CS Secret: RF7WD0b3RjfMK2cTaPfLkpZGbPDaeALDtqHeMw==

Take note of these keys as they will be required in the testing step. In this case, those keys are:

Access key: 5N2STDSXNV-US8BWF1TH
Secret key: RF7WD0b3RjfMK2cTaPfLkpZGbPDaeALDtqHeMw==

Next Steps
Congratulations, you have deployed a virtualized environment of Riak CS. You are ready to progress to Testing the Riak CS Installation in the Riak CS Fast Track.
Stopping Your Virtual Environment
When you are done testing, or just want to start again from scratch, you can end the current virtualized environment by typing:

vagrant destroy

NOTE: Executing this command will reset the environment to a clean state removing any/all changes that you have done.
Congratulations on setting up your first Riak CS testing environment! Make sure to try the entire Riak CS Fast Track. Full documentation is available here.
