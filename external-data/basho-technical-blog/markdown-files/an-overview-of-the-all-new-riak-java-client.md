---
title: "An Overview Of The All New Riak Java Client"
description: "Hi. My name is Russell Brown and since March, I've been working on the Riak Java Client. This past week I merged a large, backwards-compatible branch with some enhancements and long-awaited fixes and refinements ..."
project: community
lastmod: 2016-09-14T15:14:51+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Russell Brown"
pub_date: 2011-07-14T00:26:34+00:00
---
July 14, 2011

Hi. My name is Russell Brown and since March, I’ve been working on the Riak Java Client (making me the lone Java developer in an Erlang shop). This past week I merged a large, backwards-compatible branch with some enhancements and long-awaited fixes and refinements. In this post I want to introduce the changes I’ve made and the motivations behind them. At Riak we firmly believe that Riak’s Java interface is on track to be the among the best there is for Java developers who need a rock solid, production-ready database, so it’s time you get to know it if you don’t already.


First, Some History

When Riak was first released, it was only equipped with an HTTP API, so it followed that the Java client was a REST client. Later a Protocol Buffers Interface was added to Riak and Kresten Krab-Thorup and the team at Trifork contributed a Protocol Buffer’s interface for the Java library. Later still, around version 0.14, the Trifork PB Client was merged into the official Riak Riak Java Client. With this added interface, however, came a problem: both clients work well but they don’t share any interfaces or types. I started working for Riak in March 2011, my first task was to fix any issues with the existing clients and refactor them to a common, idiomatic interface. Some way into that task I was exposed to the rather brilliant Riak and Scala at Yammer talk given by Coda Hale and Ryan Kennedy at a Riak Meetup in San Francisco. This opened my eyes, and I’m very thankful to Coda and Ryan for sharing their expert understandings so freely. If you meet either of these two gentlemen, I urge you to buy them drinks.



A Common Interface

Having a common interface should be a no-brainer. Developers shouldn’t have to chose upfront about a low-level transport and then have all their subsequent code shaped by that choice. To that end, I added a RawClient interface to the library that describes the set of operations you can perform with Riak. I also adapted each of the original clients to this interface. If all you want to do is pump data in, or pull raw data out of Riak, the PB RawClient adapter is for you. There are some figures on the Riak Wiki that show it’s quite snappy. If you need to write a non-blocking client, or simply have to use the Jetty HTTP library, implementing this interface is the way to go.
There is some bad news here: I had to deprecate a lot of the original client and move that code to new packages. This will look a tad ugly in your IDE for a release or two, but it is better to make the changes than be stuck with odd packages for ever. There will be a code cull of the deprecated classes before the client goes v1.0.
The next task on the list for this raw package is to move the interfaces into a separate core project/package to avoid any circular dependency issues that will arise if you create your own RawClient implementation.The RawClient solves the common/idiomatic interface problem, but it doesn’t solve the main new challenge that an eventually consistent, fault-tolerant datastore brings to the client: siblings.



Sibling Values

Before we move on, if you have the time please take a moment to read the excellent Vector Clocks page on the Riak wiki (but make sure you come back). Thanks to Vector Clocks Riak does all that it can to save you from dealing with conflicting values, but this doesn’t guarantee they won’t occur. The RawClient presents you with a Vector Clock and an array of sibling values, and you need to create a single, correct value to work with (and even write back to Riak as the one true value.) The new, higher-level client API in the Java Client makes this easier.



Conflict Resolution

Conflict resolution is going to depend on your domain. Your data is opaque to Riak, which is why conflict resolution is a read time problem for the client. The canonical example (from the Dynamo Paper) is a shopping cart. If you have sibling shopping carts you can merge them (with a set-union operation, for example) to get a single cart with the values from all carts present. (Yes, you can re-instate a removed item, but that is far better than losing items. Ask Amazon.) Keep the idea of a shopping cart fresh in your mind for the remainder of this post as it figures in some of the examples I’ve used.


A Few Words On Domain Conversion

You use a Bucket to get key/values pairs from Riak.
Bucket b = client.createBucket(bucketName)
 .nVal(1)
 .allowSiblings(true)
 .execute();

IRiakObject fetched = b.fetch("k").execute();
b.store("k", "my new value").execute();
b.delete("k").execute();
The Bucket is a factory for RiakOperations, and a Riak Operation is a fluent builder that, when executed, calls out out to Riak. “Fetch” and “Store” Riak Operations accept a Converter and ConflictResolution implementation from you so that the data Riak returns can be deserialised into a domain object and any siblings can be resolved. The library provides a Jackson-based JSONConverter that will convert the JSON payload of a Riak data item into an instance of some domain class; think of it as a bit like an ORM (but maybe without the “R”).
final Bucket carts = client.createBucket(bucketName).allowSiblings(true).execute();

final ShoppingCart cart = new ShoppingCart(userId);

cart.addItem("fixie");
cart.addItem("moleskine");

carts.store(cart).returnBody(false).retrier(DefaultRetrier.attempts(2)).execute();
Adding your own converters is trivial and I plan to provide a Jackson XML based one soon. Look at this test for a complete example.



Conflict Resolver

Once the data is marshalled into domain instances, your logic is run to resolve any conflicts. A trivial shopping cart example is provided in the tests here. The ConflictResolver interface has a single method that takes an array of domain instances and returns a single, resolved value.
T resolve(final Collection siblings) throws UnresolvedConflictException;
It throws the checked UnresolvedConflictException if you need to bail out. Your code can catch this and make the siblings available to a user (for example) for resolution as a last resort. I am considering making this a runtime exception, and would like to hear what you think about that.



Mutation

To talk about mutation I’m going to stick with the shopping cart example. Imagine you’re creating a new cart for a visiting shopper. You create a ShoppingCart instance, add the toaster add the flambe set, and persist it. Meanwhile a network partition occurred and your user already added a steak knife set to a different cart. You’re not really creating a new value, but you weren’t to know. If you save this value you have a conflict to be resolved at a later date. Instead, the high level client executes a store operation as a fetch, convert, resolve siblings, apply a mutation and then store. In the case of the shopping cart that mutation would again be to merge the values of your new ShoppingCart with the resolved value fetched from Riak.
You provide an implementation of Mutation to any store operation. You never really know if you are creating a new value or updating an old one, so it is safer to model your write as a mutation to an existing value that results in a new value. This can be as simple as incrementing a number or adding the items in your Cart to the fetched Cart.
By default the library provides a ClobberMutator (it ignores the old value and overwrites it with a new one) but this is simply a default behaviour and not the best in most situations. It is better to provide your own Mutation implementation on a store operation. If you can model your values as logically monotonic or as transformations to existing values, then creating mutation implementations is a lot simpler.



Noise

As your project matures, you will firm up your ConflictResolvers, Mutations, and Converters into concrete classes, and at this point adding them for each operation is a lot more typing and code noise than you need (especially if you were using anonymous classes for your Mutation/ConflictResolver/Converter).
bucket.store(o)
 .withConverter(converter)
 .withMutator(mutation)
 .withResolver(resolver)
 .r(r)
 .w(w)
 .dw(dw)
 .retrier(retrier)
 .returnBody(false)
.execute();
The library provides the DomainBucket class as a wrapper around the Bucket. DomainBuckets are constructed with a ConflictResolver, Mutation, and Converter and thereafter use those implementations for each operation. DomainBuckets are a convenient way to get a strongly typed view of a Bucket and only store/fetch values of that type. They are a touch of sugar that reduce noise and I advise you use them once your domain is established.

illustrates the usage.




The Next Steps

That’s about it. There is a Retrier interface and a default try-3-times-with-a-short-wait implementation (if the database is fault-tolerant,the client should be too, right?) but I’m going to push that down the stack to the RawClient layer so we can add cluster awareness to the client (with load balancing and all that good stuff).
I haven’t covered querying (MapReduce and Link Walking) but I plan to in the next post (“Why Map/Reduce is easy with Java”, maybe?). I can say that is one aspect that has hardly changed from the original client. The original versions used a fluent builder and so does this client. The main difference is the common API and the ability to convert M/R results into Java Collections or domain specific objects (again, thanks to Jackson). Please read the README on the repo for details and the integration tests for examples.
At the moment the code is in the master branch on GitHub. If you get the chance to work with it I’d love to hear your feedback. The Riak Mailing List is the best place to make your feelings and opinions known. There are a few wrinkles to iron out before the next release of the Java Client, and your input will shape the future direction of this code so please, don’t be shy. We are on the lookout for contributors…
And go download Riak if you haven’t already.
Russell


