---
title: "Syslog and Riak 1.3"
description: "An overview of Lager for Syslog support."
project: community
lastmod: 2016-10-20T08:25:40+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-04-08T18:59:25+00:00
---
April 8, 2013
Logs are an important part of understanding the health of any system. They provide historic records of events with information that may not be seen when running ad-hoc commands like riak-admin or reviewing the data gathered and trended from riak-admin status or http://$riakNode/stats.
When used correctly, logs can forewarn of an issue as well as provide forensic evidence to use after the event.
By default, Riak’s logging is split sensibly into different files for different types of log data: errors, crash and console. Take a look in /var/log/riak or /var/log/riak-cs to explore further.
Since you may have five to 50 nodes in your Riak cluster (we recommend that you have at least five nodes), reviewing those logs by hand would be a timely and repetitive task. This makes it unlikely that you’d catch any events as they’re happening.
Gathering logs centrally for analysis is the best way to resolve this and make this easier. Riak has Syslog support through its logging framework – Lager.
Lager
Lager is another great open source project from Riak. We recommend checking out the project page for full details, but a summary of its features are:

Custom formatting
Runtime changes
Internal log rotation (yes, sysadmin, we already have you covered)
AMQP support
SMTP support
Syslog Support
Loggly Support
Tracing

Configuring Lager to use Syslog is as easy as opening your /etc/riak/app.config then navigating down to Lager’s configuration section. Here, you can replace the existing handlers section with

{handlers, [
{lager\_syslog\_backend, ["riak", local1, info]},
] },

Then, in your local syslog.conf you may add the following: (Please remember Syslog likes tabs between sections, not spaces. If you copy and paste, you will need to edit the line accordingly.)

local1.info             /var/log/riak\_info.log

Both Riak and Syslog will require restarting to apply the changes.
You can add multiple handlers for the various levels you wish to configure. You can also use a hybrid approach, leaving Lager managing some levels while Syslog manages others. Check out the Lager Syslog project page for more information.
The next step in your logging solution would be to redirect the logs to your central Syslog server. Doing so is outside the scope of this post, but there are many great guides available that describe the process.
Analysis Tools
There are some excellent tools out there for log file anaylsis. These include: Splunk and Logstash, which can both be run locally, and Loggly and Papertrail, which are Software as a Service.
What am I seeing?
When using one of these tools, we recommend that you set up alerts for common, frequent, and important messages. Check our online documentation for descriptions of common messages to get started.
Further Help
If you need further assistance, please don’t hesitate to contact the team and community:
IRC – irc.freenode.net #riak
Email – http://lists.riak.com/mailman/listinfo/riak-users\_lists.riak.com
Or, if you’re an enterprise customer, contact us through your support account.
Team Client Services
