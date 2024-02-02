---
title: "Webmachine in the Data Center"
description: "May 19, 2010 While Riak is Riak's most-heavily developed and widely distributed piece of open source software, we also hack on a whole host of other projects that are components of Riak but also have myriad standalone uses. Webmachine is one of those projects. We recently heard from Caleb Tenni"
project: community
lastmod: 2015-05-28T19:24:17+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Caleb Tennis"
pub_date: 2010-05-19T01:31:31+00:00
---
May 19, 2010
While Riak is Riak’s most-heavily developed and widely distributed piece of open source software, we also hack on a whole host of other projects that are components of Riak but also have myriad standalone uses. Webmachine is one of those projects.
We recently heard from Caleb Tennis, a member of The Data Cave team who was tasked with building out and simplifying operations in their 80,000 square foot data center. Caleb filled us in on how he and his team are using Webmachine in their day to day operations to iron out the complexities that come with running such a massive facility. After some urging on our part, he was gracious enough to put together this illustrative blog post.
Enjoy,
Mark
Community Manager
A Data Center from the Ground Up
Building a new data center from the ground up is a daunting task. While most of us are familiar with the intricacies of the end product (servers, networking gear, and cabling), there’s a whole backside supporting infrastructure that also must be carefully thought out, planned, and maintained. Needless to say, the facilities side of a data center can be extremely complex.
Having built and maintained complex facilities before, I already had both experience and a library of software in my tool belt that I had written to help manage the infrastructure of these facilities. However, I recognized that if I was to use the legacy software, some of which was over 10 years old, it would require considerable work to fit it to my current needs. And, during that period, many other software projects and methodologies had matured to a state that it made sense to at least consider a completely different approach.
The main crux of such a project is that it involves communications with many different pieces of equipment throughout the facility, each of which has its own protocols and specifications for communication. Thus, the overall goal of this project is to abstract the communications behind the scenes and present a consistent and clear interface to the user so that the entire process is easier.
Take for example the act of turning on a pump. There are a number of pumps located throughout the facility that need to be turned on and off dynamically. To the end user, a simple “on/off” style control is what they are looking for. However, the actual process of turning that pump on is more complicated. The manufacturer for the pump controller has a specific way of receiving commands. Sometimes this is a proprietary serial protocol, but other times this uses open standard protocols like Modbus, Fieldnet, or Devicenet.
In the past, we had achieved this goal using a combination of open source libraries, commercial software, and in-house software. (Think along the lines of something like Facebook’s Thrift, where you define the interface and let the backend implementation be handled behind the scenes;in our case, the majority of the backend was written in C++.)
“This is what led us to examine Erlang…”
But as we were looking at the re-implementation of these ideas for our data center, we took a moment to re-examine them. The implementation we had, for the most part, was stateless, meaning that as systems would GET and SET information throughout the facility, they did so without prior knowledge and without attempting to cache the state of any of the infrastructure. This is a good thing, conceptually, but is also difficult in that congestion on the communication networks can occur if too many things need access to the same data frequently. It also suffered from the same flaws as many other projects: it was big and monolithic; changes to the API were not always easy; and, most of all, upgrading the code meant stops and restarts, so upgrading was done infrequently. As we thought about the same type of implementation in our data center, it became clear that stops and restarts in general were not acceptable at all.
This is what led us to examine Erlang, with its promise of hot code upgrades, distributed goodness, and fault -tolerance. In addition, I had been wanting to learn Erlang for a while now but never really had an excuse to sit down and focus on it. This was my excuse.
While thinking about how this type of system would be implemented in Erlang, I began by writing a Modbus driver for Erlang, as a large portion of the equipment we interact with uses Modbus as part of its communications protocol. I published the fruits of these labors to GitHub (http://github.com/ctennis/erlang-modbus), in the hopes that it might inspire others to follow the same path. The library itself is a little rough (it was my first Erlang project) but it served as the catalyst for thinking about not only how to design this system in Erlang, but also how to write Erlang code in general.
Finding Webmachine
While working on this library, I kept thinking about the overall stateless design, and thought that perhaps a RESTful interface may be appropriate. Using REST (and HTTP) as the way to interface with the backend would simplify the frontend design greatly, as there are myriad tools already available for client side REST handling. This would eliminate the need to write a complicated API and have a complicated client interface for it. This is also when I found Webmachine.
Of course there are a number of different ways this implementation could have been achieved, Erlang or not. But the initial appeal of Webmachine was that it used all of the baked in aspects of HTTP, like the error and status codes, and made it easy to use URLs to disseminate data in an application. It is also very lightweight, and the dispatching is easy to configure.
Like all code, the end result was the product of several iterations and design changes, and may still be refactored or rewritten as we learn more about how we use the code and how it fits into the overall infrastructure picture.
Webmachine in Action
Let’s look at how we ultimately ended up using Webmachine to communicate with devices in our data center…
For the devices in the facility that communicate via modbus, we created a modbus\_register\_resource in Webmachine that handles that interfacing. For our chilled water pumps (First Floor, West, or “1w”), the URL dispatching looks like this:
{["cw\_pump","1w",parameter],modbus\_register\_resource,[cw\_pump, {cw\_pump\_1w, tcp, "10.1.31.202", 502, 1}]}.
This correlates to the url: http://address:8000/cw\_pump/1w/PARAMETE
So we can formulate URIs something like this:
http://address:8000/cw\_pump/1w/motor\_speed or http://address:8000/cw\_pump/1w/is\_active
And by virtue of the fact that our content type is text:
 content\_types\_provided(RD,Ctx) ->
{[{"text/plain", to\_text}], RD, Ctx}.

We use HTTP GETs to retrieve the desired result, as text. The process is diagrammed below:

This is what is looks like from the command line:
user@host:~# curl http://localhost:8000/cw\_pump/1w/motor\_speed
47.5
It even goes further. If the underlying piece of equipment is not available (maybe it’s powered off), we use Webmachine to send back HTTP error codes to the requesting client. This whole process is much easier than writing it in C++, compiling, distributing, and doing all of the requisite exception handling, especially across the network. Essentially, what had been developed and refined over the past 10 years as our in-house communications system was basically redone from scratch with Erlang and Webmachine in a matter of weeks.
For updating or changing values, we use HTTP POSTs to change values that are writable. For example, we can change the motor speed like this:
user@host:~# curl -X POST http://localhost:8000/cw\_pump/1w/motor\_speed?value=50
user@host:~# curl http://localhost:8000/cw\_pump/1w/motor\_speed
50.0

But we don’t stop here. While using Webmachine to communicate directly with devices is nice, there also needs to exist an infrastructure that is more user friendly and accessible for viewing and changing these values. In the past, we did this with client software, written in Windows, that communicated with the backend processes and presented a pretty overview of what was happening. Aside from the issue of having to write the software in Windows, we also had to maintain multiple copies of it actively running as a failover in case one of them went down. Additionally, we had to support people running the software remotely, from home for example, via a VPN, but still had to be able to communicate with all of the backend systems. We felt a different approach was needed.
What was this new approach? Webmachine is its own integrated webserver, so this gave us just about everything we needed to host the client side of the software within the same infrastructure. By integrating jQuery and jQTouch into some static webpages, we built an entire web based control system directly within Webmachine, making it completely controllable via mobile phone. Here is a screenshot:

What’s Next?
Our program is still a work in progress, as we are still learning all about the various ways the infrastructure works as well as how we can best interact with it from both Webmachine and the user perspective. We are very happy with the progress made thus far, and feel quite confident about the capabilities that we will be able to achieve with Webmachine as the front end to our very complex infrastructure.
Caleb
Caleb Tennis is President of The Data Cave, a full service, Tier IV compliant data center located in Columbus, Indiana.
