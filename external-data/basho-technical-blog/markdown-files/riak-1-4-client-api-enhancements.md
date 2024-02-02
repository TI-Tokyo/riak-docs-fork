---
title: "Riak 1.4: Client API Enhancements"
description: "A look at the new Client API features and updates from Riak 1.4."
project: community
lastmod: 2015-05-28T19:23:38+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Adron Hall"
pub_date: 2013-07-22T09:00:03+00:00
---
July 22, 2013
With the release of Riak 1.4, we have made some important additions and changes to the Client API features, with a goal of strengthening the real-time, streaming, and timeout behaviors for clients. To take a deeper look at all of the changes in Riak 1.4, check out the release notes.
Protocol Buffers & Multiple Interface Binding
In previous versions of Riak, the interface binding for protocol buffers was set to a default 127.0.0.1 with a port of 8087 and the endpoint was limited to a single IP address and port. With Riak 1.4, the list of endpoints can be configured. This feature dramatically extends the options around setting up firewall security and other options at the network level, which will provide security more choice in which port ranges to close off or IP ranges to shift.
Clients can also bind to these new ranges. This gives clients the ability to use protocol buffers on more web-friendly port ranges, even utilizing protocol buffers in parallel with HTTP on port 80 if necessary. With this update, Riak now has closer feature parity between HTTP and Protocol Buffers.
Client-Specified Timeouts
Milliseconds can now be assigned to a timeout value for clients. Client-specified timeouts can be used for object manipulation around fetch, store and delete, listing buckets, or keys. This addition will be useful for asynchronous requests and pivotal for use with synchronous requests. For more on the client-specified timeouts, take a look at the relevant GitHub issue.
To explore response times and where timeout conditions can occur, check out the Riak Bench docs. There are examples for testing various scenarios and identifying bottlenecks that may need custom timeouts or performance improvements.
Bucket Properties for Protocol Buffers
Bucket properties can now be reset to default values and all built-in properties can be configured via the protocol buffers API. This helps client usage of protocol buffers in a dramatic way and, again, steps closer to feature parity with HTTP. For more information on setting and using these bucket properties, check out the bucket properties documentation.
List-Buckets Streaming: Real-time
Listing keys or buckets via a streaming request will now send bucket names to the client as received. This prevents the need to wait for all nodes to respond to the request. This update helps with response time and timeouts from the client point of view. It also allows for the use of streaming features with Node.js, C#, Java, and other languages and frameworks that support real-time streaming data feeds.
To get started, download Riak 1.4 on our docs site. Also, be sure and grab a ticket to RICON West before they sell out.
Adron
