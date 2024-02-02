---
title: "What does Time Series mean to me?"
description: "I’m from New York City. All the things to see, places to be, a food for every taste, people everywhere... and traffic. Wall to wall traffic. New York is renowned for many things and at the top of that list would be parking lots. Huge parking lots. The best parking lots. I’m looking at you Midtown. W"
project: community
lastmod: 2016-06-17T09:20:37+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Alexander Sicular"
pub_date: 2016-06-16T08:16:58+00:00
---
I’m from New York City. All the things to see, places to be, a food for every taste, people everywhere… and traffic. Wall to wall traffic. New York is renowned for many things and at the top of that list would be parking lots. Huge parking lots. The best parking lots. I’m looking at you Midtown. West Side Highway. FDR. BQE.
Let’s say you’re at the Plaza at 6pm and have a show to catch at Radio City Music Hall. If you’re from New York, you’d know the quickest way to do it is to walk. Walking half a mile is quicker than taking a cab, I assure you (assuming you can get one), because traffic. But how much traffic? Where is it? When does it happen? Why does it happen?[0] Nowadays, people want to know these things. If you’re a city planner at the Department of Transportation, you really want to know these things. We’ll get to review some traffic data specifically a little later on.
Planners have been measuring traffic in cities all over the world for some time. Although there are a number of ways to actually get the numbers, once you’ve got them they should tell you how many cars are in a specific location and how fast they are going. And, oh ya, when did that happen. It’s the “when did that happen” part that makes this type of data, time series data.
The predominant consideration when working with time series data is the relationship of data points to one another over time. On Wall Street, it could be the fluctuating price of some stock over the trading day, or the executed order flow of trades from the exchange. On an assembly line in Detroit it could be tracking when assembled goods come off the line. In your home, it could be your smart energy meter recording consumption throughout the day. That spike at 3:30pm? That’s your kids plugging in all their portables and playing Xbox when they get home. Ok, fine, maybe that’s just me.
The opportunities for developers to work with time series data is as limitless as the use cases are varied, yet they all boil down to how exactly does one record and retrieve time series data at scale and at an acceptable speed. Riak TS was designed specifically to address these concerns.
Why Riak TS?
A decision made early on in the development of Riak TS was to leverage battle tested distributed computing characteristics from Riak KV. From an architectural perspective, Riak TS inherits certain core capabilities from Riak KV such as high availability, resilience, and scalability to name a few. However, from a data model perspective the two are quite different. While Riak KV stores data at a specific key – keys have no relationship to one another and their values are unstructured – Riak TS is structured. Table schemas are employed and data is co-located on disk. As delved into in a previous post on Riak TS architecture, Riak TS physically groups and lays out data on disk differently from Riak KV. Let’s walk through how you can use Riak TS to store your time series data.
Working with Time Series Data in Riak TS 
We’ll be working with traffic data provided by the city of Aarhus, Denmark. I’ll review code from a repo created by my colleague, Stephen Etheridge. Stephen’s code walks through exactly how to set up your own environment with Riak, Spark and Python and step through code in a series of Jupyter notebooks. When you follow along at home and get things up and running you can thank him. If you do run into trouble, blame me.
Riak TS will require you to define a schema by creating a table via a CREATE TABLE statement. Whoa, whoa, whoa. Pause. Did we just put the SQL back in NoSQL? Ya, we did. The SQL we’ve introduced in Riak TS is a subset of ANSI standard SQL with slight time series specific modifications. As features and capabilities find their way into Riak TS, they will be reflected in the available SQL subset. Here’s some of that real SQL now:
CREATE TABLE aarhus (
status varchar not null,
extid varchar not null,
ts timestamp not null,
avgMeasuredTime sint64 not null,
avgSpeed sint64 not null,
medianMeasuredTime sint64 not null,
vehicleCount sint64 not null,
PRIMARY KEY (
(status,extid,quantum(ts,30,'d')),
status,extid,ts
)
)
In this example we create a table called “aarhus” that will record a number of traffic related data points. For those of you who have relational database experience, this should look fairly straightforward and familiar. There are a few interesting bits to note here. “QUANTUM” is a new keyword we introduced that instructs Riak TS how to group data over a configurable time duration. A given data point, or conceptually a row in a TS table, is identified via a flexible primary key. A primary key may have more than one column in it, often referred to as a composite primary key. The primary key will most often, but does not have to, include a timestamp. The timestamp data type is defined as an integer representing UNIX epoch time in milliseconds.
Recall that Riak TS stores data in sorted order on disk. In the example above, the sorted order would be the columns status, extid, ts as described by the ordering in the CREATE TABLE statement. The first line of the PRIMARY KEY statement is the partition key, responsible for distribution of data within the cluster. The second line is the local key that determines the sort order within a given quantum (a quantum represents a set of data within a duration of time.) Note that the local key may be a superset of the primary key.
Interestingly, due to the tabular nature of the Riak TS data model, a “data point” representing some data collected at a specific point in time may, if desired, contain multiple columns worth of data. This is a differentiator when contrasting against other time series dedicated systems. Often a data point at a specific point in time consists of a single value commonly referred to as a “quadruple” (identifier, metric, time, value). This data point will get written to the database in a single write operation.
Consider the above example storing avgSpeed as the only value at a point in time and not vehicleCount as well. If you wanted to record vehicleCount then you would execute a second write to the database. You can quickly see how this can get expensive if you are recording dozens of metrics per identifier per point in time, let’s say, every second.
In Riak TS, you are able to execute a single write against a table that has as many columns as you have metrics (practical limitation notwithstanding.) The more values you can record at a single point in time, the more valuable this feature becomes. As outlined above, you may be recording complex traffic information from sensors that track different types of related data. That complex data may contain a number of different variables and associated values collected at the same time. Riak TS allows you to create a table structure where all of those values have their own column in a single row.
To Quantum Or Not To Quantum…
Location, location, location. Considering Riak’s distributed nature, how keys are distributed, a.k.a. where they are located in the cluster, should be a concept architects and developers are aware of when designing applications on top of Riak TS. In Riak KV, all keys are independent from other keys in the system. Riak TS changes this approach by allowing you to designate a composite primary key when you create a table. The primary key may be one or more columns of which one column may be a timestamp, but does not need to be. Additionally, when one of your primary key columns is a timestamp you may provide a quantum for that timestamp. It is the partition key, the first part of the PRIMARY KEY statement that determines how Riak TS distributes keys around the cluster. The partition key determines where your data is located in the cluster because that is what gets hashed via the consistent hashing algorithm SHA256.
Let’s say you have many traffic-data gathering devices deployed around your city. Each device has a unique ID, the device ID, and is programmed to upload data every minute. If you were to have a partition key with the device ID, timestamp and a quanta of one hour, then all data for a single device for one hour at a time will be located in the same place in the cluster.
The rule of thumb when selecting a quantum size is as follows:

Smaller quanta favor greater distribution which is better for write throughput
Larger quanta favor less distribution which is better for read throughput

When leveraging the time series data model, recall that data co-locality is important because we want sequential data to be stored in sorted order in the same place in the cluster. Sorted data allows for sequential read i/o from disk which is always faster than random read i/o. This design pattern is at odds with the general concept of data distribution. What distribution grants us is increased parallelism. An architecture, with a certain degree of distribution, is able to better leverage all the computational assets of a cluster simultaneously, re. compute cores, memory and disk i/o.
So, one might ask oneself, “self, should I design a schema with a very small quanta to better leverage parallelism or a large quanta to better serve read queries?”. The answer, of course, is entirely based on your use case. One consideration is what volume of data will you acquire over a period of time. Think MB/s. One pretty useful aspect of Riak TS is that you can service multiple use cases with different quanta sizes in different tables at the same time. You may have some devices that push 1KB of data every hour so your quantum may be 30 days for this use case. On the other hand you may have devices pushing 1KB of data every second, so this case may warrant a smaller quantum of ten minutes.
Loading Data into Riak TS
In load-data.py in the repo we see just how simple it is to load data into Riak TS. The file is no more than 50 lines of python code that opens a csv file, loops through the contents, makes a few data model specific transformations, and stores the data in Riak TS. What is notable here, as a departure from Riak KV, is that Riak TS supports loading data in bulk. This bulk loading saves time on the networking side of the house in connection handling. Instead of sending every row over in its own write operation, here we batch 100 rows at a time and send the entire batch over in one go. Each row in the batch will still be processed individually once it gets to Riak TS, most notably in order to identify the appropriate partition to write the data to by hashing the primary key.
Querying Data in Riak TS
As promised, our example analysis of traffic data. If your city makes traffic data publicly available the following code will serve as a good starting off point. Let’s walk through querying-aarhus-data.ipynb in the repo. Recall, Jupyter notebooks allows you to step through python code that executes on your own machine. I’ll compact some of that code here for brevity, which I am a fan of, although the length of this post may suggest otherwise.

from riak import RiakClient
from datetime import datetime
import calendar
def changetime(stime):
dt=datetime.strptime(stime,'%Y-%m-%dT%H:%M:%S')
return calendar.timegm(datetime.timetuple(dt))\*1000
c=RiakClient()
c.ping()

Very simply, we import the Riak client library, some date libraries, establish the connection and check that connection via a built-in Riak function, ping().

startdate=changetime('2014-02-13T00:00:00')
enddate=changetime('2014-04-12T23:59:59')
print startdate, enddate

Riak TS 1.3 expects unix epoch integers for time in SQL sent to it. Above we convert ISO8601 dates to milliseconds since unix epoch. An enhancement in a future Riak TS version will move this conversion server side. Next, we create an SQL query as a python string and replace the time variables with data created in the last step. Executing the query is as simple as calling the Riak TS ts\_query() function with the Riak TS table and SQL string:

q="""
select count(\*) from aarhus where ts > {t1} and ts < {t2} and status='OK' and extid='668'
"""
query=q.format(t1=startdate, t2=enddate)
print query
ds=c.ts\_query('aarhus', query)
for r in range(0,10):
print ds.rows[r]
print ds.rows

Making sure it all worked, we print out the top 10 rows and the total row count. And that’s that. It’s that simple. The rest of the notebook walks through a number of other queries to further our traffic data analysis with popular Python libraries pandas and data plotting library matplotlib.
Riak will support an increasing array of SQL commands as we continue to quickly release updates. As of Riak TS 1.3 we support arithmetic operations and certain aggregations like  %(start\_date)s
AND ts < %(end\_date)s
AND status = '%(status)s'
AND extid = '%(extid)s'
""" % ({'start\_date': start\_date, 'end\_date': end\_date, 'status': status, 'extid': extid}))

#show we have got the data
df.printSchema()
df.show()
df.count()

Most of the above code is very straight forward. The interesting piece is passing initialization variables and the query into the sqlContext which is the bit that actually fetches your data from Riak TS. The rest of the notebook goes on to conduct analysis in a mixture of Spark and Python.
In short, accessing data from Riak TS could not be simpler. Because we know our users and their use cases are varied we provide a number of ways for you to get your data in the way you want to get your data. Use riak shell to quickly interact with Riak TS and develop queries, program in the language of your choosing when developing your application, or bring distributed computing resources to bear when applying machine learning algorithms to your data via Spark.
Try This at Home
When you’ve recovered from your trip to New York City and have had a moment to consider that cab ride to Laguardia or JFK, you may be inclined to crunch some numbers on your own to see just how, uh, fortunate you are not to have to drive in the Five Boroughs. If you must drive in NYC, my sincerest condolences. New York City puts out a lot of open source data. Perhaps there are a few traffic related data sets you can load into Riak TS to start analyzing. I’ll leave that as an exercise to the reader. Send me a link to your blog post when you write it up and I’ll get you in the next Riak Recap.
Whether you are looking to deploy real time transactional applications or batch oriented long term analysis, consider using Riak TS to help you get it done. Take a look at the documentation, download the open source version and let us know if we can help you with your project. What features are you interested in? What time series projects are you working on? Your feedback directly influences the product roadmap and we’re happy to hear it.
Good luck making that show at Radio City. See you next time.



[0]: Answer from a New Yorker: All of it. Everywhere. All the time. Who knows.
