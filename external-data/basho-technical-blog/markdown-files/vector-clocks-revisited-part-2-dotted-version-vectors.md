---
title: "Vector Clocks Revisited Part 2: Dotted Version Vectors"
description: "In my previous blog, I wrote about Vnode Version Vectors, and how they simplify client interaction with Riak while keeping logical clock sizes small. I also mentioned an issue with "sibling explosion." Today's blog post is going to describe the sibling explosion issue in more detail and how Riak and"
project: community
lastmod: 2015-11-12T07:41:56+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Russell Brown"
pub_date: 2015-11-10T15:52:13+00:00
---
In my previous blog, I wrote about Vnode Version Vectors, and how they simplify client interaction with Riak while keeping logical clock sizes small. I also mentioned an issue with “sibling explosion.” Today’s blog post is going to describe the sibling explosion issue in more detail and how Riak and some leading distributed systems researchers came together to solve this problem using Dotted Version Vectors.
Vnode Version Vector Recap
In the last post we saw how Vnode Version Vectors tweak the standard version vector algorithm. Let’s quickly recap how.
With a ClientId Version Vector Riak generated siblings like this:

If Local Version Vector descends Incoming

Discard/ignore incoming write


If Incoming descends Local

Write new value to disk


If Incoming concurrent with Local

Merge Incoming and Local Version Vectors
Store Incoming value as a sibling



But with Vnode Version Vectors this becomes:

If Incoming descends Local

Increment Vnode’s entry in incoming Version Vector
Write new value to disk


Otherwise

Merge Incoming and Local Version Vectors
Increment Vnode’s entry in merged Version Vector
Store Incoming value as a sibling



We use a single version vector to cover the full set of sibling values. But a version vector is not enough to correctly track causality when the actor – the vnode – is a proxy for multiple clients.
Sibling Explosion
What is sibling explosion, and how does it happen?
Imagine two clients X, and Y. They’re both updating the same key. For simplicity this illustration will use Riak’s Return Body option, which means Riak replies to every PUT with the latest result it stored on disk at the coordinating vnode. This is just simpler than having the client fetch after every PUT.

In the above diagram we see a “sibling explosion.” Vnode Version Vectors are incapable of tracking causality in a fine grained manner, and these interleaving writes generate false concurrency. Let’s walk through the steps as numbered on the diagram.

Client Y PUTs the value “Bob” with an empty context.
Vnode A sees that [] descends the local version vector (also []) and increments its entry in the incoming version vector, storing the value with new version vector [{a, 1}]. The value "Bob" and context [{a, 1}] are returned to Client Y.
Client X PUTs the value “Sue” with an empty context. This PUT is causally concurrent with Client Y’s at Step 1.
Using the algorithm described above, Vnode A detects that the context [] does not descend [{a, 1}] and treats the write as concurrent, it increments the local version vector to [{a, 2}] and stores the incoming value as a sibling. So far so good. The Vnode Version Vector correctly captured concurrency! Vnode A returns the sibling values ["Bob", "Sue"] [{a, 2}] to Client X.
Client Y PUTs the value “Rita” with the context [{a, 1}]. Remember that Client Y saw the result of its own PUT before Client X PUT at step 3.
Vnode A detects that the incoming version vector [{a, 1}] does not descend the local version vector of [{a, 2}]. It increments its entry in the local version vector, and adds “Rita” as a sibling value. Wait! What? We know that Client Y saw “Bob” as a value, after all, it PUT that value! So “Rita” should at least replace “Bob”. Yes, it is concurrent with “Sue,” but only “Sue”.
Client X PUTs a new value “Michelle” with context [{a, 2}]. Client Y means to replace what it has read, the sibling values “Bob” and “Sue” with a new, single value “Michelle”.
As before Vnode A detects that [{a, 2}] does not descend [{a, 3}] and adds the incoming value as a sibling. Again, we can see this is wrong. At Step 4 Client X saw both “Bob” and “Sue” and this new write intends to replace those values.

What Just Happened?
Vnode A loses some essential causal information. At Step 4 it ends up storing both “Bob” and “Sue” with a version vector of [{a, 2}]. It has “forgotten” that “Bob” was associated with time [{a, 1}]. When Client Y PUTs again, with the now stale context of [{a,1}] the vnode is unable to determine that this PUT means to replace “Bob” since “Bob” is now associated with the version vector [{a, 2}].
Dots To The Rescue
What we need is a fine grained mechanism to detect which siblings are removed by an update, and which siblings are actually concurrent or unrelated causally. That mechanism is the “dot.”

Recall the basics of a Version Vector: that each actor must update its own entry in the vector by incrementing a counter. This counter is a summary of all the actors events. An entry {a, 4} says that actor a has issued 4 updates. Integers make great summaries. 4 includes 1, 2, 3, 4. Each of those steps that the counter went through is an update. And that is all a dot is: a single update, an event. Take the update {a, 4}. That is an event, an update, a dot. You can think of the Version Vector entry {a, 4} as a set of discrete events, and a dot as any one of those events.

Hopefully the diagram above illustrates this. It’s two different visual representations of the same version vector [{A, 4}, {B, 2}, {C, 3}]. The one on the right is a type of visualization I first saw when working with a group of academics from University Minho, and it helps illustrate the idea of version vectors and the history they summarize. The one on the left “explodes” the version vector into its discrete events.
Dot The clocks
How does this help with the problem described above? We change the algorithm again. When we increment the version vector to store a new value, we take that latest event and store it, as a dot, with the value.

[{a, 2}]
["Bob", "Sue"]

Becomes
[{a, 2}]
[{a,1} -> "Bob",
 {a, 2} -> "Sue"]

That’s all we have to do add fine grained causality tracking. Now when we have interleaving writes as above, we can see which sibling values an update replaces, and which it does not, by comparing the incoming version vector with the dots for each value. If the version vector descends the dot, the client has seen that value and the new write supersedes it. If the version vector is concurrent with the dot, retain that value as a truly concurrent write, or sibling.

This diagram shows Step 6 again, this time with Dotted Version Vectors. We can see that the extra metadata, the dot stored with a value, enables Riak to discard the value “Bob” since the incoming version vector of [{a, 1}] descends the dot stored with that value. The new dot generated from this update event {a, 3} is used to tag the new sibling “Rita”. “Sue” remains as a genuine sibling, since it is unseen by the incoming version vector [{a, 1}] we know that Client Y did not mean to replace that value.
Thanks to this mechanism, Dotted Version Vectors, or DVV for short, a small increase in metadata storage fixes the Sibling Explosion bug.
All that remains is to look at the initial example of sibling explosion again, and see that with Dotted Version Vectors, Riak correctly captures only genuinely concurrent operations, and stores only those values that are actual siblings.


Why “Dots?”
Why that word “Dots?” If we think about Step 6 again, in the Dotted Version Vector case, you can imagine Vnode A generating the event {a, 3} and assigning it to the incoming value at once. Then merging the incoming DVV with the local one. The visualization of that PUT with dot assigned hopefully explains the name.

Conceptually there is gap between the incoming version vector [{a, 1}] and the event {a, 3}, and this gap leads to naming the non-contiguous event a dot, much like the dot on a lowercase letter “i”. Strictly speaking a “dot” is a non-contiguous event, but they’re used in other places (like CRDTs) simply as discrete event tags, so the simpler explanation is good enough.
Further Material
This post only covers Dotted Version Vectors as implemented in Riak. By necessity we simply added the dot-per-sibling to object metadata. Dotted Version Vectors in Riak are fully backwards compatible with previous versions of Riak, and they can be switched on or off with a simple bucket property. Since Riak 2.0 Dotted Version Vectors are the default logical clock for bucket types in Riak. However, there are other implementations out there. Ricardo Tomé Gonçalves implemented an optimized Dotted Version Vector, called a Dotted Version Vector Set. The git repository is here, and his code is also currently used in Riak for tracking Cluster Metadata.
If you use Riak you get to use DVV for free, and you need never know nor care about the details. From a client perspective it is the same as Vnode Version Vectors, minus of course the sibling explosions.
If you want to use Dotted Version Vectors in your own distributed application, then I recommend you read the paper, and Ricardo Tomé Gonçalves repo.
FURTHER READING
There is a lot of other material about Dotted Version Vectors out there, including Sean Cribbs’ Talk, an excellent Github repo and README from Ricardo Tomé Gonçalves, and a paper Scalable and Accurate Causality Tracking for Eventually Consistent Stores.
With thanks
For the sake of brevity this post does not tell the story of how we at Riak Engineering came to have Dotted Version Vectors in Riak. In short, some very brilliant academics in Portugal came up with Dotted Version Vectors as a solution to sibling explosion and then let us have it. This is the wonderful thing about research science, and I’m extremely grateful to have had the chance to work with Ricardo Tomé Gonçalves, Valter Balegas, Nuno Preguiça, Carlos Baquero, Paulo Sérgio Almeida, and others while fixing sibling explosion in Riak. The story of sibling explosion and Dotted Version Vectors in Riak truly is an example of academic expertise solving real world, industrial problems. Thanks again!
Summary
Thanks to Vnode Version Vectors client interaction with Riak is simple, Read Your Own Writes is not required, and Version Vectors remain small. Thanks to Dotted Version Vectors we get to keep all those benefits without the risk of large objects from sibling explosion.
Next Time on Logical Clocks
Just as Vnode Version Vectors were a response to inadequacies in Client Version Vectors, and Dotted Version Vectors fix Sibling Explosion, the next blog post we will look at another, less common issue that lead to the past looking like the future.


More Thanks
Further thanks to Jon Meredith, Zeeshan Lakhani, John Daily and Scott Fritchie (all Riak Engineers), Ricardo Tomé Gonçalves, Nuno Preguiça, and Carlos Baquero (again!) for reviewing this post and suggesting many improvements.
 
Russell Brown
