---
title: "Understanding Riak's Configurable Behaviors: Part 3"
description: "May 9, 2013 This is the penultimate blog post in our look at Riak configuration parameters and associated behaviors, particularly the less obvious implications thereof. I would advise at least skimming Part 1 and Part 2 before tackling this. Of success and failure It is important to understa"
project: community
lastmod: 2016-09-19T15:35:08+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "John Daily"
pub_date: 2013-05-09T18:13:21+00:00
---
May 9, 2013
This is the penultimate blog post in our look at Riak configuration parameters and associated behaviors, particularly the less obvious implications thereof.
I would advise at least skimming Part 1 and Part 2 before tackling this.
Of success and failure
It is important to understand that failure can still result in success, success can result in failure, and, well, distributed systems are hard.
A successful failure
Imagine that a primary vnode is unavailable and a write request with PW=3 is dispatched.
Even though the client will be informed of a write failure, the reachable primary vnodes received and will still write the new data. When the missing primary vnode returns to service, it will also receive the new data via read repair or active anti-entropy.
Thus, reads will see the “failed” write.
A failed success
As mentioned earlier, Riak will attempt to oblige a request even when vnodes which ordinarily would manage that data are unavailable. This leads to a situation called a sloppy quorum, in which other vnodes will handle reads or writes to comply with the specified R or W value.
This can lead to unexpected behavior.
Imagine this sequence of events:

One of the primary vnodes for a given key becomes unavailable
The key/value pair is copied to a secondary vnode
The primary vnode comes back online
A request arrives to delete the key; all primary vnodes acknowledge
the request
The tombstones (see below) are removed
The same primary vnode fails again
A request arrives for the deleted key
Because the secondary vnode for that data doesn’t know about the
deletion, it replies with the old data
Read repair causes the old data to be distributed to the primary vnodes

Voilà, deleted data is resurrected.
Tombstones, tombstones, and delete\_mode
In addition to the “failed success” scenario above, it is possible to see deleted objects resurrected when synchronizing between multiple datacenters, especially when using older versions of Riak Enterprise and multi datacenter replication (MDC) in environments where connectivity between the datacenters can be spotty.
These cases of resurrected deleted data can be avoided by retaining the tombstones (and the all-important vector clocks) via the delete\_mode configuration parameter.
Deleting an object in a distributed data store is distinctly non-trivial, and in Riak it requires several steps. If you don’t need to delete objects, you should consider refraining from doing so.
Here is the sequence of events that take place when a key is deleted.

A client requests that the object be deleted

Note: all R/W/etc parameters must be met to allow a deletion request to succeed


On each vnode with the key to be deleted, Riak creates a new, empty data object to replace the old data (a tombstone)

The existing vector clock is updated and stored with the tombstone
X-Riak-Deleted=true metadata is added to the object for both internal record-keeping and external requests


If delete\_mode is set to keep, no further action is taken. The tombstone will remain in the database, although it cannot be retrieved with a simple GET operation
If delete\_mode is set to an integer value (in milliseconds) the backend will be instructed after that period of time to delete the object

This is the standard path; the configuration value is 3000 (hence 3 seconds) by default


If delete\_mode is set to immediate or the time interval has passed, and all of these criteria are met, the backend is asked to delete the object

No client has written to the same key in the interim
All primary vnodes are available
All primary vnodes report the same tombstone


The backend flags the object as a tombstone (Bitcask or LevelDB) or wipes it immediately (in-memory)

This is not the same as a Riak tombstone


Compaction will eventually remove the backend tombstone; in any event, it is invisible to Riak

Important: Riak tombstones will appear in MapReduce, Riak Pipe, and key list operations; even if you do not set delete\_mode to keep, you should be aware of these occasional interlopers (check for the X-Riak-Deleted metadata in the object).
Caveat: There is currently a bug when requesting tombstoned objects via HTTP. The response will be a 404 status code with a vector clock header, but no X-Riak-Deleted header. A patch is available but has not yet been applied.
Deleting and replacing
If you delete a key and wish to write to it again, it is best to retrieve any existing vector clock for that key to use for the new write, else you may end up with tombstone siblings (if allow\_mult=true) or even see tombstones replace the new value.
Since you may never be fully aware of what other clients are doing to your database, if you can afford the performance impact it is advisable to always request a key and attach the vector clock before writing data.
When using protocol buffers, make certain that deletedvclock in your object request is set to true in order to receive any tombstone vector clock.
notfound tuning
As I’ve discussed, the act of deleting objects and their corresponding vector clocks leads to challenges with eventual consistency. Additionally, there are performance implications when reading non-existent keys, and corresponding configuration toggles to help manage the impact.
Waiting for every vnode with responsibility for a given key to respond with notfound (thus indicating that the key does not exist on that vnode) can add undesirable latency. If your environment is optimized for fast reads over consistency by using R=1, waiting for all 3 vnodes to reply is not what you signed up for.
The first toggle is an optimization Riak incorporated into early Riak and later converted to a configuration parameter named basic\_quorum. This setting has a very narrow scope: if set to true, R=1 read requests will report a notfound back to the client after only 2 vnodes reply with notfound values instead of waiting for the 3rd vnode.
The default value is false.
Later, the notfound\_ok configuration value was added. It has a much more profound impact on Riak’s behavior when keys are not present.
If notfound\_ok=true (the default) then a notfound response from a vnode is treated as a positive assertion that the key does not exist, rather than as a failure condition.
This means that when R=1 and notfound\_ok=true (regardless of the basic\_quorum value) if the first vnode replies with a notfound message the client will also receive a notfound message.
If notfound\_ok=false, then the coordinating node will wait for enough vnodes to reply with notfound messages to know that it cannot fulfill the requested R value. So, if R=2 and N=3, then 2 negative responses are enough to report notfound back to the client.
Note: This in no way impacts read repair. If it turns out that one of the other vnodes has a value for that key, read repair will handle the distribution of that data appropriately for future reads.
In the worst case, where only the last vnode to reply has a value for a given key, the table below indicates the number of consecutive vnode notfound messages that will be returned before the coordinating node will reply with notfound to the client.



basic\_quorum
notfound\_ok
R=1
R=2
R=3




true
true
1
2
3


false
true
1
2
3


true
false
2
2
1


false
false
3
2
1



Any cell with 3 indicates that the client will receive the value from that 3rd vnode; any other scenario results in a notfound.
Broadly speaking, if you forget everything you’ve just read and trust Riak’s defaults, you should get the behavior you expect along with reasonable performance. With the introduction of active anti-entropy, there should not be many situations (other than during recovery from hardware/network failure) where multiple vnodes do not know about a valid key.
What’s next?
In our final post, I’ll take what we’ve learned and create configuration bundles to emphasize performance or data consistency.
I’ll also mention a couple of ways to perform your own stress tests to see how Riak behaves under normal (or abnormal) conditions.
John R. Daily
