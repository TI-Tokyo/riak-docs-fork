---
title: "Why MapReduce is Easy"
description: "MapReduce has this incredible Big Data aura around it, but it's actually not that hard, when used in the small."
project: community
lastmod: 2015-05-28T19:24:15+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Mathias Meyer"
pub_date: 2011-03-30T21:21:53+00:00
---
March 30, 2011
There’s something about MapReduce that makes it seem rather scary. It almost has this Big Data aura surrounding it, making it seem like it should only be used to analyze a large amount of data in a distributed fashion. It’s one of the pieces that makes Riak a pretty versatile key-value store. Feed a bunch of keys into it, and do some analytics on the objects, quite handy.
But when you narrow it down to just the basics, MapReduce is pretty simple. I’m almost 100% certain even that you’ve used it in one way or another in an application you’ve written. So before we go all distributed, let’s break MapReduce down into something small that you can use every day. That certainly has helped me understand it much better.
For our webinar on Riak and Node.js we built a little application with Node.js and Riak Search to store and search syslog messages. It’s called Riaktant and handily converts and stores syslog messages in a way that’s friendlier for both Riak Search and MapReduce. We’ll base this on examples we used in building the application.
MapReduce is easy because it works on simple data
MapReduce loves simple data structures. Why? Because when there are no deep, nested relationships between say, objects, distributing data for parallel processing is a breeze. But I’m getting a little ahead of myself.
Let’s take the data Riaktant stores in Riak and see how easy it is to sift through it without even having to go distributed. It uses a JavaScript library called glossy to parse a syslog message and turn it into this nice JSON data structure.
javascript
message = {
"originalMessage": "<35>1 2011-02-14T11:10:25.137+01:00 lb1.riak.com ftpd 7003 - Client disconnected",
"time": "2011-02-14T10:10:25.137Z",
"severityID": 3,
"facility": "auth",
"version": 1,
"prival": 35,
"host": "lb1.riak.com",
"facilityID": 4,
"message": "7003 - Client disconnected",
"severity": "err"
}
MapReduce is easy because you use it every day
I’m almost 100% certain you use MapReduce every day. If not daily, then at least once a week. Whenever you have a list of items that you loop or iterate over and transform into something else one by one, if only to extract a single attribute, there’s your map function.
Keeping with JavaScript, here’s how you’d extract the host from the above JSON, for a whole list:
“`javascript
messages = [message];
messages.map(function(message) {
return message.host
}))
“`
Or, if you insist, here’s the Ruby equivalent:
ruby
messages.map do |message|
message[:host]
end
If you must ask, here’s Python, using a list comprehension, for added functional programming sugar:
python
[message['hello'] for message in messages]
There, so simple, right? Halfway there to some full-fledged MapReduce action.
MapReduce is easy because it’s just code
Before we continue, let’s add another syslog message.
javascript
message2 = {
"originalMessage": "<35>1 2011-02-14T11:10:25.137+01:00 web2.riak.com ftpd 7003 - Client disconnected",
"time": "2011-02-14T10:12:37.137Z",
"severityID": 3,
"facility": "http",
"version": 1,
"prival": 35,
"host": "web2.riak.com",
"facilityID": 4,
"message": "7003 - Client disconnected",
"severity": "warn"
}
messages.push(message2)
We can take the above example even further (still using JavaScript), and perform some additional operations like result sorting, for example.
javascript
messages.map(function(message) {
return message.host
}).sort()
This gives us a nice sorted list of hosts. Coincidentally, sorting happens to be the second step in traditional MapReduce. Isn’t it nice how easily this is coming together?
The third and last step involves, you guessed it, more code. I don’t know about you, but I love things that involve code. Let’s reduce the list of hosts and count the occurrences of each host, (and if this reminds you of an SQL query that involves GROUP BY, you’re right on track).
“`
var reduce = function(total, host) {
if (host in total) {
total[host] += 1
} else {
total[host] = 1
}
return total
}
messages.map(function(message) {
return message.host
}).sort().reduce(reduce, {})
“`
There’s one tiny bit missing for this to be as close to MapReduce as we can get without going distributed. We need to slice up the list before we hand it to the map function. As JavaScript doesn’t have a built-in function to partition a list we’ll whip up our own real quick. After all, we’ve come this far.
function chunk(list, chunkSize) {
for(var position, i = 0, chunk = -1, chunks = []; i < list.length; i++) {
if (position = i % chunkSize) {
chunks[chunk][position] = list[i]
} else {
chunk++;
chunks[chunk] = [list[i]]
}
}
return chunks;
}
It loops through the list, splitting it up into equally sized chunks, returning them neatly wrapped in a list.
Now we can chunk the initial list of messages, and boom, we have our own little MapReduce going, without magic, just code. Let’s put the new chunk function to good use.
javascript
var mapResults = [];
chunk(messages, 2).forEach(function(chunk) {
var messages = chunk.map(function(message) {
return message.host
})
mapResults = mapResults.concat(messages)
})
mapResults.sort().reduce(reduce, {})
We split up the messages into two chunks, run the map function for each chunk, collecting the results as we go. Then we sort the results and feed them into the reduce function. That’s MapReduce in eight lines of JavaScript code. Easy, right?
That’s all there’s to MapReduce. You use it every day, whether you’re aware of it or not. It works nicely with simple data structures, and it’s just code.
Unfortunately, things get complicated as soon as you go distributed, for example in a Riak cluster. But we’ll save that for the next post, where we’ll examine why MapReduce is hard.
— Mathias
