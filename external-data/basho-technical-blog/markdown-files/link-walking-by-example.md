---
title: "Link Walking By Example"
description: "February 24, 2010 Riak has a notion of “links” as part of the metadata of its objects. We talk about traversing, or “walking”, links, but what do the queries for doing so actually look like? Let's put four objects in riak:   	hb/first will link to hb/second and hb/third 	hb/second will"
project: community
lastmod: 2015-05-28T19:24:18+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Bryan Fink"
pub_date: 2010-02-24T22:13:25+00:00
---
February 24, 2010
Riak has a notion of “links” as part of the metadata of its objects. We talk about traversing, or “walking”, links, but what do the queries for doing so actually look like?
Let’s put four objects in riak:





hb/first will link to hb/second and hb/third
hb/second will link to hb/fourth
hb/third will also link to hb/fourth
hb/fouth doesn’t link anywhere






$ curl -X PUT -H "content-type:text/plain"
 -H "Link: ; riaktag="foo", ; riaktag="bar""
 http://localhost:8098/riak/hb/first --data "hello"

$ curl -X PUT -H "content-type: text/plain"
 -H "Link:; riaktag="foo""
 http://localhost:8098/riak/hb/second --data "the second"

$ curl -X PUT -H "content-type: text/plain"
 -H "Link:; riaktag="foo""
 http://localhost:8098/riak/hb/third --data "the third"

$ curl -X PUT -H "content-type: text/plain"
 http://localhost:8098/riak/hb/fourth --data "the fourth"
Now, say we wanted to start at hb/first, and follow all of its outbound links. The easiest way to do this is with the link-walker URL syntax:
$ curl http://localhost:8098/riak/hb/first/\_,\_,\_
The response will be a multipart/mixed body with two parts: the hb/second object in one, and the hb/third object in the other:
--N2gzGP3AY8wpwdQY0jio62L9nJm
Content-Type: multipart/mixed; boundary=3ai6VRl4aLli3dKw8tG9unUeznT

--3ai6VRl4aLli3dKw8tG9unUeznT
X-Riak-Vclock: a85hYGBgzGDKBVIsTKLLozOYEhnzWBn+H/h5hC8LAA==
Location: /riak/hb/third
Content-Type: text/plain
Link: ; rel="up", ; riaktag="foo"
Etag: 5Fs0VskZWx7Y25tf1oQsvS
Last-Modified: Wed, 24 Feb 2010 15:25:51 GMT

the third
--3ai6VRl4aLli3dKw8tG9unUeznT
X-Riak-Vclock: a85hYGBgzGDKBVIsLEHbN2YwJTLmsTLMPvDzCF8WAA==
Location: /riak/hb/second
Content-Type: text/plain
Link: ; rel="up", ; riaktag="foo"
Etag: 2ZKEJ2gaT57NT7xhLDPCQz
Last-Modified: Wed, 24 Feb 2010 15:24:11 GMT

the second
--3ai6VRl4aLli3dKw8tG9unUeznT--

--N2gzGP3AY8wpwdQY0jio62L9nJm--
It’s also possible to express the same query in map-reduce, directly:
$ curl -X POST -H "content-type:application/json"
 http://localhost:8098/mapred --data @-
{"inputs":[["hb","first"]],"query":[{"link":{}},{"map":{"language":"javascript","source":"function(v)
{ return [v]; }"}}]}
^D
That’s the exact same query. The content type of the response is different. It’s now a JSON array with two elements: a JSON encoding of the hb/second object, and a JSON encoding of the hb/third object. (Pretty-printed here, for clarity.)
[
 {
 "bucket": "hb",
 "key": "second",
 "vclock": "a85hYGBgzGDKBVIsLEHbN2YwJTLmsTLMPvDzCF8WAA==",
 "values": [
 {
 "metadata": {
 "Links": [
 ["hb","fourth","foo"]
 ],
 "X-Riak-VTag": "2ZKEJ2gaT57NT7xhLDPCQz",
 "content-type": "text/plain",
 "X-Riak-Last-Modified": "Wed, 24 Feb 2010 15:24:11 GMT",
 "X-Riak-Meta": []
 },
 "data": "the second"
 }
 ]
 },
 {
 "bucket": "hb",
 "key": "third",
 "vclock": "a85hYGBgzGDKBVIsTKLLozOYEhnzWBn+H/h5hC8LAA==",
 "values": [
 {
 "metadata": {
 "Links": [
 ["hb","fourth","foo"]
 ],
 "X-Riak-VTag": "5Fs0VskZWx7Y25tf1oQsvS",
 "content-type": "text/plain",
 "X-Riak-Last-Modified": "Wed, 24 Feb 2010 15:25:51 GMT",
 "X-Riak-Meta": []
 },
 "data": "the third"
 }
 ]
 }
]
Another interesting query is “follow only links that are tagged foo.” For that, just add a tag field to the link phase spec:
$ curl -X POST -H "content-type:application/json"
 http://localhost:8098/mapred --data @-
{"inputs":[["hb","first"]],"query":[{"link":{"tag":"foo"}},{"map":{"language":"javascript","source":"function(v)
{ return [v]; }"}}]}
^D
Here you should get a JSON array with one element: a JSON encoding of the hb/second object. The link to the hb/third object was tagged bar, so that link was not followed. The equivalent URL syntax is:
$ curl http://localhost:8098/riak/hb/first/\_,foo,\_
It’s also possible to filter links by bucket by adding a bucket field to the link phase spec, or by replacing the first underscore with a bucket name in the URL format. But, all of our example links point to the same bucket, so hb is the only interesting setting here.
Link phases may also be chained together (or put after other phases if those phases produce bucket/key lists). For example, we could follow the links all the way from hb/first to hb/fourth with:
$ curl -X POST -H "content-type:application/json"
 http://localhost:8098/mapred --data @-
{"inputs":[["hb","first"]],"query":[{"link":{}},{"link":{}},{"map":{"language":"javascript","source":"function(v)
{ return [v]; }"}}]}
^D
(Notice the added link phase.) If you run that, you’ll find that you get two copies of the hb/fourth object in the response. This is because we didn’t bother uniquifying the results of the link extraction, and both hb/second and hb/third link to hb/fourth. A reduce phase is fairly easy to add:
$ curl -X POST -H "content-type:application/json"
 http://localhost:8098/mapred --data @-
{"inputs":[["hb","first"]],"query":[{"link":{}},{"link":{}},{"reduce":{"language":"erlang","module":"riak\_mapreduce","function":"reduce\_set\_union"}},{"map":{"language":"javascript","source":"function(v)
{ return [v]; }"}}]}
^D
The resource handling the URL link-walking format does just this:
$ curl http://localhost:8098/riak/hb/first/\_,\_,\_/\_,\_,\_
That should get you just one copy of the hb/fourth object.
So why choose either map/reduce or URL-syntax? The advantage of URL syntax is that if you’re starting from just one object, and just want to get the objects at the ends of the links, and you can handle multipart/mixed encoding, then URL syntax is much simpler and more compact. Map/reduce with link phases should be your choice if you want to start from multiple objects at once, or you want to get some processed or aggregated form of the objects, or you want the result to be JSON-encoded.
Riak version 0.8 note: In Riak 0.8, the format of the result of ‘link’ map/reduce phases was not able to be transformed into JSON. This meant both that it was not possible to put a Javascript reduce phase right after a link phase, and also that it was not possible to end an HTTP map/reduce query with a link phase. Those issues have been resolved in the tip of the source repository, and will be part of the 0.9 release.
-Bryan
