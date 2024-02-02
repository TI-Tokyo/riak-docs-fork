---
title: "The Evolution of Riak's Chef Cookbook"
description: "The story of Chef and Riak."
project: community
lastmod: 2016-10-20T08:14:42+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Seth Thomas"
pub_date: 2013-09-09T22:17:59+00:00
---
September 9, 2013
Chef is a configuration management system that is widely deployed by Operations teams around the world. Tools like Chef can bring sanity and uniformity when deploying a massive Riak cluster; however, as with any tool, it needs to be reliably tested, as any misconfiguration could bring down systems. Here is the story of Chef and Riak.
History
The first public Chef Cookbook was pushed to Github on July 18, 2011, back when Riak 0.14.2 was the latest and greatest. We started by making the basic updates for releases but, as both the Riak and Chef user base grew, so did the number of issues and pull requests. Even with some automation, testing was still time consuming, error prone, and problematic. Too much time was being spent catching bugs manually, a familiar story for anyone who has had to test any software.
Our initial reaction was to only keep what we knew users (primarily customers) were using. As testing the build from source was so time consuming, it was removed until we could later ensure that it be properly tested. We knew that we had to start automating this testing pipeline to not only maintain quality, but sanity. Fortunately, Chef’s testing frameworks were beginning to come into their own and a free continuous integration service for GitHub repositories called TravisCI was starting to take off. However, before talking about the testing frameworks, we need to cover two tools that help make this robust testing possible.
Vagrant
Vagrant is a tool that leverages virtualization and cloud providers, so users don’t have to maintain custom static virtual machines. Vagrant was on the rise when we started the cookbooks and was indispensable for early testing. While it didn’t offer us a completely automated solution, it was far ahead of anything else at the time and serves as a great building block for our testing today.
There are also a variety of useful plugins that we use in conjunction with it, including vagrant-berkshelf and vagrant-omnibus. The former integrates Vagrant and Berkshelf so each Vagrant “box” has its own self-contained set of cookbooks that it uses and the latter allows for easy testing of any version of Chef.
Berkshelf
Berkshelf manages dependencies for Chef cookbooks – like a Bundler Gemfile for cookbooks. It allows users to identify and pin a known good version, bypassing the potential headaches of trying to keep multiple cookbooks in sync.
Now, back to testing frameworks.
Enter Foodcritic
Foodcritic is a Ruby gem used to lint cookbooks. This not only checks for basic syntax errors that would prevent a recipe from converging, but also for style inconsistencies and best practices. Foodcritic has a set of rules that it checks cookbooks against. While most of them are highly recommended, there may be a few that don’t apply to all cookbooks and can be ignored on execution. Combine this with TravisCI, and each commit or pull request to GitHub is automatically tested.
While this is helpful, it still didn’t actually help us test that the cookbooks worked. Luckily, we weren’t the only ones with this issue, which is why Fletcher Nichol wrote test-kitchen.
Test-Kitchen
Test-kitchen is another Ruby gem that helps to integrate test cookbooks using a variety of drivers (we use the Vagrant driver). For products like Riak and Riak CS, there are a number of supported platforms that we need to run the cookbook against, and that’s exactly what this tool accomplishes.
In the configuration file for test-kitchen, we define the permutation of Vagrant box, run list, and attributes for testing as many cases for the cookbook as needed. With this, we are able to execute simple Minitests against multiple platforms and we can also test both our enterprise and open source builds at any version by configuring attributes appropriately.
Granted, if you need to spin up a ton of virtual machines in parallel, you’ll want a beefy machine, but the upside is that you’ll have a nice status report to know which permutation of platform/build/version failed.
Why is This Important?
With these tools, we are able to make sure our builds pass all tests across platforms. Since we have many customers deploying the latest Riak and Riak CS version with Chef, we need to ensure that everything works as expected. These tools allowed us to move from testing every cookbook change manually to automatically testing the permutations of operating system, Chef, and Riak versions.
Now everyone gets a higher quality cookbook and there are fewer surprises for those maintaining it. Testing has shifted from a chore to a breeze. This benefits not only our users, but ourselves included as these cookbooks are used to maintain our Riak CS demo service.
Check out our Docs Site for more information about installing Riak with Chef.
Special thanks to Joshua Timberman (advice), Fletcher Nichol (test-kitchen), Hector Castro (reviews and PRs), Mitchell Hashimoto (Vagrant), and Jamie Winsor (Berkshelf).
Seth Thomas
