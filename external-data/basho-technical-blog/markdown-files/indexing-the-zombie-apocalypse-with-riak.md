---
title: "Indexing the Zombie Apocalypse With Riak"
description: "Using Riak to save the world during the zombie apocalypse."
project: community
lastmod: 2015-05-28T19:23:36+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Dan Kerrigan"
pub_date: 2013-09-04T22:22:06+00:00
---
September 4, 2013
For more background on the indexing techniques described, check out our blog “Index for Fun and for Profit“
The War Against Zombies is Still Raging!
In the United States, the CDC has recovered 1 million Acute Zombilepsy victims and has asked for our help loading the data into a Riak cluster for analysis and ground team support.
Know the Zombies, Know Thyself
The future of the world rests in a CSV file with the following fields:

DNA
Gender
Full Name
StreetAddress
City
State
Zip Code
TelephoneNumber
Birthday
National ID
Occupation
BloodType
Pounds
Feet Inches
Latitude
Longitude

For each record, we’ll serialize this CSV document into JSON and use the National ID as the Key. Our ground teams need the ability to find concentrations of recovered zombie victims using a map so we’ll be using the Zip Code as an index value for quick lookup. Additionally, we want to enable a geospatial lookup for zombies so we’ll also GeoHash the latitude and longitude, truncate the hash to four characters for approximate area lookup, and use that as an index term. We’ll use the G-Set Term-Based Inverted Indexes that we created since the dataset will be exclusively for read operations once the dataset has been loaded. We’ve hosted this project at Github so that, in the event we’re over taken by zombies, our work can continue.
In our load script, we read the text file and create new zombies, add Indexes, then store the record:

load\_data.rb script
Our Zombie model contains the code for serialization and adding the indexes to the object:

zombie.rb add index
Let’s run some quick tests against the Riak HTTP interface to verify that zombie data exists.
First let’s query for a known zombilepsy victim:
curl -v http://127.0.0.1:8098/buckets/zombies/keys/427-69-8179
Next, let’s query the inverted index that we created. If the index has not been merged, then a list of siblings will be displayed:
Zip Code for Jackson, MS:
curl -v -H "Accept: multipart/mixed" http://127.0.0.1:8098/buckets/zip\_inv/keys/39201
GeoHash for Washington DC:
curl -v -H "Accept: multipart/mixed" http://127.0.0.1:8098/buckets/geohash\_inv/keys/dqcj
Excellent. Now we just have to get this information in the hands of our field team. We’ve created a basic application which will allow our user to search by Zip Code or by clicking on the map. When the user clicks on the map, the server converts the latitude/longitude pair into a GeoHash and uses that to query the inverted index.
Colocation and Riak MDC will Zombie-Proof your application
First we’ll create small Sinatra application with the two endpoints required to search for zip code and latitude/longitude:

server.rb endpoints
Our zombie model does the work to retrieve the indexes and build the result set:

zombie.rb search index
Saving the world, one UI at a time
Everything is wired up with a basic HTML and JavaScript application:

Searching for zombies in the Zip Code 39201 yields the following:

Clicking on Downtown New York confirms your fears and suspicions:

The geographic bounding inherent to GeoHashes is obvious in a point-dense area so, in this case, it would be best to query the adjacent GeoHashes.
Keep Fighting the Good Fight!
There is plenty left to do in our battle against zombies!

Create a Zombie Sighting Report System so the concentration of live zombies in an area can quickly be determined based on the count and last report date.
Add a crowdsourced Inanimate Zombie Reporting System so that members of the non-zombie population can report inanimate zombies. Incorporate Baysian filtering to prevent false reporting by zombies. They kind of just mash on the keyboard so this shouldn’t be too difficult.
Add a correlation feature, utilizing Graph CRDTs, so we can find our way back to Patient Zero.

Dan Kerrigan & Drew Kerrigan
