---
title: "Practical Map-Reduce - Forwarding and Collecting"
description: "This post is an example of how you can solve a practical querying problem in Riak with Map-Reduce. The Problem This query problem comes via Jakub Stastny, who is building a task/todolist app with Riak as the datastore. The question we want to answer is: for the logged-in user, find all of the"
project: community
lastmod: 2015-05-28T19:24:17+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Sean Cribbs"
pub_date: 2010-04-14T23:52:36+00:00
---
This post is an example of how you can solve a practical querying problem in Riak with Map-Reduce.
The Problem
This query problem comes via Jakub Stastny, who is building a task/todolist app with Riak as the datastore. The question we want to answer is: for the logged-in user, find all of the tasks and their associated “tags”. The schema looks kind of like this:

Each of our domain concepts has its own bucket – users, tasks and tags. User objects have links to their tasks, tasks link to their tags, which also link back to the tasks. We’ll assume the data inside each object is JSON.
The Solution
We’re going to take advantage of these features of the map-reduce interface to make our query happen:
1. You can use link phases where you just need to follow links on an object.
2. Inputs to map phases can include arbitrary key-specific data.
3. You can have as many map, reduce, and link phases as you want in the same job.
Let’s construct the JSON job step-by-step, starting with the input – the user object.
Next, we’ll use a link phase to find my tasks.
Now that we’ve got all of my tasks, we’ll use this map function to extract the relevant data we need from the task — including the links to its tags — and pass them along to the next map phase as the keydata. Basically it reads the task data as JSON, filters the object’s links to those only in the “tags” bucket, and then uses those links combined with our custom data to feed the next phase.
Here’s the phase that uses that function:
Now in the next map phase (which operates over the associated tags that we discovered in the last phase) we’ll insert the tag object’s parsed JSON contents into the “tags” list of the keydata object that was passed along from the previous phase. That modified object will become the input for our final reduce phase.
Here’s the phase specification for this phase (basically the same as the previous except for the function):
Finally, we have a reduce phase to collate the resulting objects with their included tags into single objects based on the task name.
Our final phase needs to return the results, so we add \*”keep”:true\* to the phase specification:
Here’s the final format of our Map/Reduce job, with indentation for clarity:
I input some sample data into my local Riak node, linked it up according to the schema described above and this is what I got:
Conclusion
What I’ve shown you above is just a taste of what you can do with Map/Reduce in Riak. If the above query became common in your application, you would want to store those phase functions we created as built-ins and refer to them by name rather than by their source. Happy querying!
Sean
