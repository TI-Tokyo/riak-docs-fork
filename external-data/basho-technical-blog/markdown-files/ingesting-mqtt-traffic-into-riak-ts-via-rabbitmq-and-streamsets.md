---
title: "Ingesting MQTT Traffic into Riak TS via RabbitMQ and StreamSets"
description: "This post was originally published by StreamSets here and is republished with permission.  Thanks to Pat Patterson at StreamSets for building this proof of concept!  The recent release of Riak TS 1.3 as an open source product under the Apache V2 license got me thinking – how would I get sensor"
project: community
lastmod: 2016-06-29T12:46:46+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Stephen Condon"
pub_date: 2016-06-29T10:01:09+00:00
---
This post was originally published by StreamSets here and is republished with permission.  Thanks to Pat Patterson at StreamSets for building this proof of concept!

The recent release of Riak TS 1.3 as an open source product under the Apache V2 license got me thinking – how would I get sensor data into Riak TS? There are a range of client libraries, which communicate with the data store via protocol buffers, but connected devices tend to use protocols such as MQTT, an extremely lightweight publish/subscribe messaging transport. StreamSets to the rescue!
In this blog post, I’ll show you how to build an IoT integration with Riak TS, StreamSets Data Collector (SDC) and RabbitMQ. An MQTT client will publish messages to a topic via RabbitMQ’s MQTT Adapter; StreamSets will subscribe to the topic via AMQP, enrich and filter records, and write them to Riak TS via a new custom destination. I used Riak TS’ weather data use case, since the documentation does a great job in explaining how to create the GeoCheckin table, write data to it, etc. Here’s the GeoCheckin table’s DDL:
CREATE TABLE GeoCheckin
(
 region VARCHAR NOT NULL,
 state VARCHAR NOT NULL,
 time TIMESTAMP NOT NULL,
 weather VARCHAR NOT NULL,
 temperature DOUBLE,
 PRIMARY KEY (
 (region, state, QUANTUM(time, 15, 'm')),
 region, state, time
 )
)
MQTT and RabbitMQ
Let’s start with the MQTT client. I decided to use Node.js for the job, since there is an excellent mqtt module available. We’re going to simulate a very simple device that emits JSON messages of the form:
{
"state" : "CA",
"time" : 1465407252147,
"weather" : "cool",
"temp" : 10.13
}
I wanted realistic looking data, so I grabbed the randgen module to generate random temperature readings with a normal distribution. I also wanted to drop in an erroneous value (-100) every so often to simulate the glitches that sometimes appear in real-world sensor data. Just a few minutes hacking produced a simple Node.js app, mqtt-pub.js:
const commandLineArgs = require('command-line-args'),
 randgen = require("randgen"),
 mqtt = require('mqtt');
 
const optionDefinitions = [
 { name: 'url', alias: 'u', type: String, defaultValue: 'mqtt://localhost' },
 { name: 'mean', alias: 'm', type: Number, defaultValue: 10 },
 { name: 'sd', alias: 's', type: Number, defaultValue: 1 },
 { name: 'topic', alias: 't', type: String, defaultValue: 'geocheckin' },
 { name: 'delay', alias: 'd', type: Number, defaultValue: 1000 },
 { name: 'verbose', alias: 'v', type: Boolean, defaultValue: false }
]

// TBD - add 46 more :-)
const states = ['CA', 'OR', 'SC', 'WA'];

const options = commandLineArgs(optionDefinitions);

const client = mqtt.connect(options.hostname);

client.on('connect', function () {
 var counter = 0;
 setInterval(function(){
 counter++;
 var state = states[Math.floor(Math.random() \* states.length)];
 var temperature = (counter % 10 == 0) 
 ? -100
 : randgen.rnorm(options.mean, options.sd);
 // Payload has format
 // {
 // "state":"CA",
 // "time":1465405491468,
 // "weather":"variable",
 // "temp":11.05
 // }
 var payload = {
 state : state,
 time : Date.now(),
 weather : 'variable',
 temp : Math.round(temperature \* 100) / 100
 };
 payload = JSON.stringify(payload);
 if (options.verbose) {
 console.log(payload);
 }
 client.publish(options.topic, payload, {
 qos: 1
 });
 }, options.delay);
});
Running an MQTT client with random data means we have all the components for a demo in one place, but it would certainly be straightforward to build an MQTT publisher for the Raspberry Pi or even Arduino, emitting real sensor data.
After installing and starting RabbitMQ, It was a snap to subscribe to the topic with mqtt’s command line tool and send data from one terminal window to another:
$ node mqtt-pub.js
$ mqtt sub -t geocheckin -h localhost -v 
geocheckin {"state":"OR","time":1465533528969,"weather":"variable","temp":9.23} 
geocheckin {"state":"SC","time":1465533529073,"weather":"variable","temp":11.54} 
geocheckin {"state":"OR","time":1465533529177,"weather":"variable","temp":9.35} 
geocheckin {"state":"OR","time":1465533529284,"weather":"variable","temp":-100}
Subscribing to the topic from AMQP, RabbitMQ’s ‘native’ protocol, proved more tricky. A bit of searching, however, turned up a couple of really useful blog entries:

The Internet of things, with RabbitMQ, Node.js, MQTT and AMQP
RabbitMQ for beginners – Exchanges, routing keys and bindings

MQTT topics map to RabbitMQ routing keys, but a little care is required to allow messages to flow smoothly from an MQTT publisher to an AMQP subscriber. MQTT messages are sent to the amq.topic exchange, so AMQP subscribers must specify this when they connect to RabbitMQ, and the AMQP routing key (not the queue name!) must match the MQTT topic name for messages to flow.
I strongly recommend working through the airasoul blog entry and ensuring that you have messages flowing between the command line tools before bringing StreamSets into the mix!
Configuring the StreamSets Data Collector RabbitMQ Origin
With the understanding gleaned from the resources listed above, configuring SDC is straightforward. Here are the settings required to receive data from the geocheckin MQTT topic, assuming a default install of RabbitMQ on localhost:
RabbitMQ

URI: amqp://localhost:5672
Data Format: JSON
Credentials

Username: guest
Password: guest 



Queue

Name: geocheckin-queue
Durable: checked

Exchange

Bindings

Name: amq.topic
Type: Topic
Durable: checked
Routing Key: geocheckin



 
Enriching Records
Since Riak TS is expecting state names rather than abbreviations, I used the Static Lookup processor (new in SDC 1.4.0.0!) to map between the two:


Riak TS also wants a value for the region, so I used a second Static Lookup. Inserting this \*before\* the state lookup allows us to use the state abbreviations before they are expanded:

Writing Data to Riak TS
For this integration, I created a Riak TS destination (currently in GitHub) using the Riak Java Client SDK. At present, this should be considered ‘proof of concept’ quality, but, given sufficient customer demand, we’ll look at moving it into the supported list of pipeline stages.
I used the General settings on the destination to ensure that all required fields are present, and set a precondition for the temperature value to enforce data quality:

The Riak TS tab let me configure settings for the database, and map the /temp field to the temperature column:

Finally, a simple Java app, based on sample code in the Riak TS documentation, gives us a high-level analysis of the last hour’s readings for South Carolina:
package com.streamsets.test;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.timeseries.Query;
import com.basho.riak.client.core.query.timeseries.\*;
import java.util.\*;

public class RiakTSQuery {
 public static void main(String [] args) 
 throws UnknownHostException, ExecutionException, InterruptedException {
 RiakClient client = RiakClient.newClient(8087, "localhost");

 Long now = System.currentTimeMillis();
 Long anHourAgo = now - (3600 \* 1000);

 String queryText = "SELECT COUNT(temperature), AVG(temperature), MIN(temperature), MAX(temperature)" +
 " FROM GeoCheckin" +
 " WHERE time >= "+ anHourAgo +
 " AND time <= " + now +
 " AND region = 'South Atlantic'" +
 " AND state = 'South Carolina'";

 Query query = new Query.Builder(queryText).build();
 QueryResult queryResult = client.execute(query);

 List md = queryResult.getColumnDescriptionsCopy();
 List rows = queryResult.getRowsCopy();
 List cells = rows.get(0).getCellsCopy();
 for (int i = 0; i < cells.size(); i++) {
 Cell cell = cells.get(i);
 ColumnDescription col = md.get(i);
 System.out.println(col.getName() + ": " + cell.toString().replace("Cell{ ", "").replace(" }", ""));
 }

 client.shutdown();
 }
}
Here’s a short video showing the integration in action:


Conclusion
RabbitMQ can broker messages between connected devices that talk MQTT and applications such as StreamSets Data Collector that speak AMQP. SDC itself can manipulate records in the pipeline, enriching and filtering them as required, and send data to a time series database such as Riak TS. You could, of course, just as easily emit data to Cassandra, Elasticsearch, Kudu, or
any other supported destination.
How are you working with time series data? Let us know in the comments!
