---
title: "Client Libraries"
description: ""
project: "riak_kv"
project_version: 2.9.8
menu:
  riak_kv-2.9.8:
    name: "Client Libraries"
    identifier: "developing_client_libraries"
    weight: 106
    parent: "developing"
toc: true
aliases:
  - /riak/2.9.8/dev/using/libraries
  - /riak/kv/2.9.8/dev/using/libraries
---

## Basho-Supported Libraries

Basho officially supports a number of open-source client libraries for a
variety of programming languages and environments.

Language | Source | Documentation | Download
:--------|:-------|:--------------|:--------
Java | [riak-java-client](https://github.com/basho/riak-java-client) | [javadoc](http://basho.github.com/riak-java-client), [wiki](https://github.com/basho/riak-java-client/wiki) | [Maven Central](http://search.maven.org/?#search%7Cgav%7C1%7Cg%3A%22com.basho.riak%22%20AND%20a%3A%22riak-client%22) |
Ruby | [riak-ruby-client](https://github.com/basho/riak-ruby-client) | [GitHub Pages](http://basho.github.io/riak-ruby-client/) | [RubyGems](https://rubygems.org/gems/riak-client)
Python | [riak-python-client](https://github.com/basho/riak-python-client) | [sphinx](http://basho.github.com/riak-python-client) | [PyPI](http://pypi.python.org/pypi?:action=display&name=riak#downloads)
C# | [riak-dotnet-client](https://github.com/basho/riak-dotnet-client) | [api docs](http://basho.github.io/riak-dotnet-client-api/), [wiki](https://github.com/basho/riak-dotnet-client/wiki) | [NuGet package](http://www.nuget.org/List/Packages/RiakClient), [GitHub Releases](https://github.com/basho/riak-dotnet-client/releases)
Node.js | [riak-nodejs-client](https://github.com/basho/riak-nodejs-client) | [api docs](http://basho.github.com/riak-nodejs-client/), [wiki](https://github.com/basho/riak-nodejs-client/wiki) | [NPM](https://www.npmjs.com/package/basho-riak-client), [GitHub Releases](https://github.com/basho/riak-nodejs-client/releases)
PHP | [riak-php-client](https://github.com/basho/riak-php-client) | [apigen](http://basho.github.io/riak-php-client)
Erlang | [riak-erlang-client (riakc)](https://github.com/basho/riak-erlang-client) | [edoc](http://basho.github.com/riak-erlang-client/) | [GitHub](https://github.com/basho/riak-erlang-client)
Go | [riak-go-client](https://github.com/basho/riak-go-client) | [GoDoc](https://godoc.org/github.com/basho/riak-go-client) | [GitHub](https://github.com/basho/riak-go-client)

**Note**: All official clients use the integrated issue tracker on
GitHub for bug reporting.

In addition to the official clients, Basho provides some unofficial
client libraries, listed below. There are also many client libraries and
related [community projects]({{<baseurl>}}community/projects/).


## Community Libraries

The Riak Community is developing at a break-neck pace, and the number of
community-contributed libraries and drivers is growing right along side
it. Here is a list of projects that may suit your programming needs or
curiosities. If you know of something that needs to be added or are
developing something that you wish to see added to this list, please
fork the [Riak Docs repo on GitHub](https://github.com/basho/basho_docs)
and send us a pull request.

{{% note title="Note on community-produced libraries" %}}
All of these projects and libraries are at various stages of completeness and
may not suit your application's needs based on their level of maturity and
activity.
{{% /note %}}

### Client Libraries and Frameworks

#### C/C++

* [riak-cpp](https://github.com/ajtack/riak-cpp)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A C++ Riak client
  library for use with C++11 compilers
* [Riak C Driver](https://github.com/fenek/riak-c-driver)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A library
  to communicate with Riak using cURL and Protocol Buffers
* [Riack](https://github.com/trifork/riack)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A simple C client
  library
* [Riack++](https://github.com/TriKaspar/riack_cpp)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A C++ wrapper
  around riack

#### Clojure

* [knockbox](https://github.com/reiddraper/knockbox)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
An eventual
  consistency toolbox for Clojure
* [Welle](http://clojureriak.info)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
An expressive Clojure client with
  batteries included
* [clj-riak](http://github.com/mmcgrana/clj-riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Clojure bindings
  to the Riak Protocol Buffers API
* [sumo](https://github.com/reiddraper/sumo)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Protocol
  Buffer-specific client for Riak with KV, 2i, and MapReduce support
* [kria](https://github.com/bluemont/kria)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak 2.0 Asynchronous
  (NIO.2) Clojure client. Callback driven, low level, Protocol Buffer
  API, Java 7.

#### ColdFusion

* [Riak-Cache-Extension](https://github.com/getrailo/Riak-Cache-Extension)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak-backed cache extension for Railo/ColdFusion

#### Common Lisp

* [cl-riak (1)](https://github.com/whee/cl-riak)
* [cl-riak (2)](https://github.com/eriknomitch/cl-riak)

#### Dart

* [riak-dart](https://github.com/agilord/riak_dart_client)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
HTTP
  client for Riak written in Dart

#### Django (Python)

* [django-riak-sessions](https://github.com/flashingpumpkin/django-riak-sessions)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak-based Session Backend for Django
* [Django Riak Engine](https://github.com/oubiwann/django-riak-engine)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak backend for Django

#### Erlang

* [Uriak Pool](https://github.com/unisontech/uriak_pool)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Erlang
  connection pool library from the team at
  [Unison](http://www.unison.com)
* [Riak PBC Pool](https://github.com/snoopaloop/Riak-PBC-Pool)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak
  Protocol Buffer Client pool application
* [Pooly](https://github.com/aberman/pooly)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak Process Pool
* [riakpool](https://github.com/dweldon/riakpool)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Application for
  maintaining a dynamic pool of Protocol Buffer client connections to a
  Riak database
* [pooler](https://github.com/seth/pooler)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
An OTP Process Pool
  Application
* [krc](https://github.com/klarna/krc)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A simple wrapper around the
  official Riak client for Erlang
* [riakc_pool](https://github.com/brb/riakc_pool)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A really simple
  Riak client process pool based on poolboy

#### Go

* [riaken](https://github.com/riaken)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A fast and extendable Riak
  Protocol Buffer Client
* [goriakpbc](https://github.com/tpjg/goriakpbc)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Golang Riak
  client inspired by the Ruby riak-client from Basho and riakpbc from mrb
* [riakpbc](https://github.com/mrb/riakpbc)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak Protocol Buffer
  client in Go
* [goriak](https://github.com/zegl/goriak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Go language driver for Riak KV

#### Grails

* [Grails ORM for Riak](http://www.grails.org/plugin/riak)

#### Griffon

* [Riak Plugin for
  Griffon](http://docs.codehaus.org/display/GRIFFON/Riak+Plugin)

#### Groovy

* [spring-riak](https://github.com/jbrisbin/spring-riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak
  support from Groovy and/or Java

#### Haskell

* [Riak Haskell Client](https://github.com/markhibberd/riak-haskell-client)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A fast Haskell client library from the team at MailRank.

#### Java

* [Riak-Java-PB-Client](http://github.com/krestenkrab/riak-java-pb-client)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Java Client Library for Riak based on the Protocol Buffers API
* [Asynchronous Riak Java Client](https://github.com/jbrisbin/riak-async-java-client)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Asynchronous, NIO-based Protocol Buffers client for Riak
* [Riak Module for the Play
  Framework](http://www.playframework.org/modules/riak-head/home)

#### Lisp-flavored Erlang

* [Gutenberg](https://github.com/dysinger/gutenberg/)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak MapReduce
  examples written in LFE

#### Node.js

* [zukai](https://github.com/natural/zukai)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak ODM for Node.js
  from Troy Melhase
* [riak-pb](https://github.com/CrowdProcess/riak-pb)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak Protocol
  Buffers client for Node.js from the team at
  [CrowdProcess](http://crowdprocess.com)
* [node_riak](https://github.com/mranney/node_riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Voxer's
  production Node.js client for Riak.
* [riakpbc](https://github.com/nlf/riakpbc)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A simple Riak Protocol
  Buffer client library for Node.js
* [nodiak](https://npmjs.org/package/nodiak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Supports bulk
  get/save/delete, sibling auto-resolution, MapReduce chaining, Search,
  and 2i's
* [resourceful-riak](https://github.com/admazely/resourceful-riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A
  Riak engine to the
  [resourceful](https://github.com/flatiron/resourceful/) model
  framework from [flatiron](https://github.com/flatiron/)
* [Connect-Riak](https://github.com/frank06/connect-riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak
  session store for Connect backed by [Riak-js](http://riakjs.org/)
* [Riak-js](http://riakjs.com)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Node.js client for Riak with support
  for HTTP and Protocol Buffers
* [Riakjs-model](https://github.com/dandean/riakjs-model)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
a model
  abstraction around riak-js
* [Node-Riak](http://github.com/orlandov/node-riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A wrapper around
  Node's HTTP facilities for communicating with Riak
* [riak-dc](https://github.com/janearc/riak-dc)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A very thin, very small
  http-based interface to Riak using promises intended to be used for small
  tools like command-line applications; aims to have the "most-synchronous-
  like" interface.
* [Nori](https://github.com/sgonyea/nori)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Experimental Riak HTTP
  library for Node.js modeled after Ripple
* [OrionNodeRiak](http://github.com/mauritslamers/OrionNodeRiak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
  Node-based server and database-frontend for Sproutcore
* [Chinood](https://npmjs.org/package/chinood)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Object data mapper
  for Riak built on Nodiak
* [SimpleRiak](https://npmjs.org/package/simpleriak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A very simple
  Riak HTTP client

#### OCaml

* [Riak OCaml Client](http://metadave.github.com/riak-ocaml-client/)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
  Riak OCaml client
* [OCaml Riakc](https://github.com/orbitz/ocaml-riakc)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Protocol
  Buffers client for Riak

#### Perl

* [Net::Riak](http://search.cpan.org/~franckc/Net-Riak/)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Perl
  interface to Riak
* [AnyEvent-Riak adapter](http://github.com/franckcuny/anyevent-riak)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Non-blocking Riak adapter using anyevent
* [riak-tiny](https://github.com/tempire/riak-tiny)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Perl interface
  to Riak without Moose
* [Riak::Light](https://metacpan.org/module/Riak::Light)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Fast and
  lightweight Perl client for Riak (PBC only)

#### PHP

* [riak-client](https://github.com/php-riak/riak-client)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak
  2.0-compliant PHP client with support for Protocol Buffers by [Fabio
  Silva](https://github.com/FabioBatSilva)
* [Ripple-PHP](https://github.com/KevBurnsJr/ripple-php)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A port of
  Ripple to PHP
* [riiak](https://bitbucket.org/intel352/riiak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak PHP client
  library for the [Yii Framework](http://www.yiiframework.com/)
* [riak-php](https://github.com/marksteele/riak-php)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak PHP
  client with support for Protocol Buffers
* [RiakBundle](https://github.com/remialvado/RiakBundle)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
  [Symfony](http://symfony.com) Bundle designed to ease interaction
  with Riak
* [php_riak](https://github.com/TriKaspar/php_riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A PHP extension
  written in C, Both Riak client and PHP session module

#### Python

* [Aioriak](https://github.com/rambler-digital-solutions/aioriak) 
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Asyncio PBC Riak 2.0+ client library. (Based on official Basho 
  python client)
* [Riakasaurus](https://github.com/calston/riakasaurus)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak
  client library for Twisted (based on txriak)
* [RiakKit](http://shuhaowu.com/riakkit)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A small Python ORM that
  sits on top of riak-python-client, similar to mongokit and couchdbkit
* [riakalchemy](https://github.com/Linux2Go/riakalchemy)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Object
  mapper for Riak written in Python
* [riak_crdt](https://github.com/ericmoritz/riak_crdt)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A CRDT
  (Conflict-Free Replicated Data Type) loader for Riak using the [CRDT
  API](https://github.com/ericmoritz/crdt)
* [txriak](https://launchpad.net/txriak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Twisted module for
  communicating with Riak via the HTTP interface
* [txriakidx](https://github.com/williamsjj/txriakidx)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak client
  for Twisted Python that implements transparent indexes

#### Racket

* [riak.rkt](https://github.com/shofetim/riak.rkt)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Racket API to
  Riak
* [Racket Riak](https://github.com/dkvasnicka/racket-riak)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Racket
  1.3.x API to Riak

#### Ruby

* [Risky](https://github.com/aphyr/risky)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A lightweight Ruby ORM for
  Riak
* [riak_sessions](http://github.com/igorgue/riak_sessions)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
  Riak-backed session storage for Rack
* [Riaktor](http://github.com/benmyles/riaktor)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Ruby client and
  object mapper for Riak
* [dm-riak-adapter](http://github.com/mikeric/dm-riak-adapter)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
  DataMapper adapter for Riak
* [Riak PB Client](https://github.com/sgonyea/riak-pbclient)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Riak
  Protocol Buffer Client in Ruby
* [Devise-Ripple](http://github.com/frank06/devise-ripple)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
An ORM
  strategy to use Devise with Riak
* [ripple-anaf](http://github.com/bkaney/ripple-anaf)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Accepts nested
  attributes support for Ripple
* [Pabst](https://github.com/sgonyea/pabst)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Cross-platform Ruby
  extension for Protocol Buffers written in both Objective-C and
  Objective-C++

#### Scala

* [Riakka](http://github.com/timperrett/riakka)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
Scala library for
  talking to Riak
* [Ryu](http://github.com/softprops/ryu)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Tornado Whirlwind Kick
  Scala client for the Riak raw HTTP interface
* [Raiku](https://github.com/gideondk/Raiku)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
An Akka IO- and
  Sentinel-driven Riak Scala client

#### Smalltalk

* [Phriak](http://www.squeaksource.com/Phriak/)
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Riak client for
  Pharo Smalltalk based on Runar Jordahl's EpigentRiakInterface
* [EpigentRiakInterface](http://www.squeaksource.com/EpigentRiakInterface/)
 
  - /riak/latest/developing/client-libraries/
  - /riak/kv/latest/developing/client-libraries/
  - /riakkv/latest/developing/client-libraries/
---
A Pharo Smalltalk interface to Riak. There is also a blog post
  with some additional info about the client
  [here](http://blog.epigent.com/2011/03/riak-interface-for-pharo-smalltalk.html).




