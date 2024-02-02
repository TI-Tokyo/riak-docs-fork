---
title: "Create Your First Riak TS Table"
description: "Customers like Intellicore are doing real-time analysis of IoT data using Riak TS. We want to make it easy for you to get started with Riak TS and take it for a test drive. In this blog post, we will demonstrate the installation of a single node of Riak TS on Mac OS X and then show the basic func"
project: community
lastmod: 2017-02-21T20:07:44+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Dorothy Pults"
pub_date: 2017-02-21T19:05:56+00:00
---
Customers like Intellicore are doing real-time analysis of IoT data using Riak TS. We want to make it easy for you to get started with Riak TS and take it for a test drive.
In this blog post, we will demonstrate the installation of a single node of Riak TS on Mac OS X and then show the basic functionality of creating a table, storing data, and doing simple queries. You can use this code to quickly see how Riak TS works.
Installation
Before running Riak TS on your Mac, you will want to change the Open Files Limit.
The examples below use Riak TS 1.5.2. You will want to download and install the latest release of Riak. Once you have downloaded the file, untar it, and cd to the riak directory.
tar zxvf riak-ts-1.5.2-OSX-x86\_64.tar.gz
cd riak-ts-1.5.2
After you install Riak, you start it on your node. To do this, you run riak start from the /bin directory. Then to see that your node has started, you run riak ping. If the node is running, you will get the response pong.
Riaks-MacBook-Air:riak-ts-1.5.2 bashotechnologies$ pwd
/Users/bashotechnologies/riak-ts-1.5.2
Riaks-MacBook-Air:riak-ts-1.5.2 bashotechnologies$ bin/riak start
Riaks-MacBook-Air:riak-ts-1.5.2 bashotechnologies$ bin/riak ping
pong
We will use riak-shell to create a table and run queries. To see a list of riak-shell commands use ‘help;’ or ‘help sql;’ . To quit riak-shell use ‘q;’.
Riaks-MacBook-Air:riak-ts-1.5.2 bashotechnologies$ bin/riak-shell
Connected...
riak-shell(1)>help sql;
The following SQL help commands are supported:
CREATE   - using CREATE TABLE statements
DELETE   - deleting data with DELETE FROM
DESCRIBE - examining table structures
EXPLAIN  - understanding SELECT query execution paths
INSERT   - inserting data with INSERT INTO statements
SELECT   - querying data
SHOW     - listing tables or showing table creation SQL
riak-shell(2)>q;
Toodle Ooh!
Create Table
Now that Riak TS is installed and running, we can create our first table using riak-shell. A Riak TS table is made up of a few basic structures shown below.
Basic Table Structures

Table name – used to identify the table.
Column definitions – define the structure of the data including the column name, data type, and inline constraint. Columns that are part of the primary key must have the inline constraint of NOT NULL.
Primary key – defines data location and is composed of the partition key and the local key.

Partition key – defines where data is placed on the cluster, i.e which node the data will be written to
Local key – defines where data is written in the partition on that given node.


Quantum – partition keys can use time quantization to group data that will be queried together in the same physical part of the cluster.

We won’t go into data modeling or the details on how to determine your keys or quantum. You can find out more about these in the Riak TS documentation.
Let’s create the SensorData table shown below.
CREATE TABLE SensorData

(
 id SINT64 NOT NULL,
 time TIMESTAMP NOT NULL,
 value DOUBLE,
 PRIMARY KEY (
 (id, QUANTUM(time, 15, 'm')),
 id, time
 )
);
Start riak-shell and use the CREATE TABLE SQL command. Cut and paste the CREATE TABLE code above to get the below results.
Connected...
riak-shell(1)>CREATE TABLE SensorData
riak-shell(1)>(
riak-shell(1)>      id SINT64 NOT NULL,
riak-shell(1)>      time TIMESTAMP NOT NULL,
riak-shell(1)>      value DOUBLE,
riak-shell(1)>      PRIMARY KEY (
riak-shell(1)>        (id, QUANTUM(time, 15, 'm')),
riak-shell(1)>        id, time
riak-shell(1)>      )
riak-shell(1)>);
Table SensorData successfully created and activated.
The SensorData table has three columns: id, time, and value. The partition key includes the id with a 15 minute time quantum, and a local key made up of id and time.
A couple of commands that you may find helpful are SHOW TABLES and DESCRIBE. The output of these commands is shown below.
riak-shell(2)>SHOW TABLES;
+----------+------+
|  Table   |Status|
+----------+------+
|SensorData|Active|
+----------+------+
riak-shell(3)>DESCRIBE SensorData;
+------+---------+--------+-------------+---------+--------+----+----------+
|Column|  Type   |Nullable|Partition Key|Local Key|Interval|Unit|Sort Order|
+------+---------+--------+-------------+---------+--------+----+----------+
|  id  | sint64  | false  |      1      |    1    |        |    |          |
| time |timestamp| false  |      2      |    2    |   15   | m  |          |
|value | double  |  true  |             |         |        |    |          |
+------+---------+--------+-------------+---------+--------+----+----------+
Next, we will insert a few rows of data into the table. Below is a list of SQL INSERT statements. You can cut and paste this code one line at a time into riak-shell.
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 01:00:00Z', 65.0);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 02:00:00Z', 64.2);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 03:00:00Z', 63.0);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 04:00:00Z', 62.3);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 05:00:00Z', 62.4);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 06:00:00Z', 61.0);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 07:00:00Z', 65.9);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 08:00:00Z', 70.1);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 09:00:00Z', 72.0);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 10:00:00Z', 73.9);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 11:00:00Z', null);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 12:00:00Z', null);
You will see something like the below as you run these commands.
riak-shell(4)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 01:00:00Z', 65.0);
Inserted 1 row.
riak-shell(5)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 02:00:00Z', 64.2);
Inserted 1 row.
riak-shell(6)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 03:00:00Z', 63.0);
Inserted 1 row.
riak-shell(7)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 04:00:00Z', 62.3);
Inserted 1 row.
riak-shell(8)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 05:00:00Z', 62.4);
Inserted 1 row.
riak-shell(9)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 06:00:00Z', 61.0);
Inserted 1 row.
riak-shell(10)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 07:00:00Z', 65.9);
Inserted 1 row.
riak-shell(11)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 08:00:00Z', 70.1);
Inserted 1 row.
riak-shell(12)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 09:00:00Z', 72.0);
Inserted 1 row.
riak-shell(13)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 10:00:00Z', 73.9);
Inserted 1 row.
riak-shell(14)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 11:00:00Z', null);
Inserted 1 row.
riak-shell(15)>INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-13 12:00:00Z', null);
Inserted 1 row.
In this sample code, we have used an easy to read ISO 8601 string. When you create your actual app and store your data, you will want to use the Riak TS supported client libraries to pass timestamps as integers (Unix Epoch Time).
Queries
Next, we will look at a few simple Riak TS queries. Let’s start with a query of the rows we just added to our table.
riak-shell(16)>SELECT id, time, value FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-13 24:00:00';
+--+--------------------+-----+
|id|        time        |value|
+--+--------------------+-----+
|2 |2016-12-13T02:00:00Z|64.2 |
|2 |2016-12-13T03:00:00Z|63.0 |
|2 |2016-12-13T04:00:00Z|62.3 |
|2 |2016-12-13T05:00:00Z|62.4 |
|2 |2016-12-13T06:00:00Z|61.0 |
|2 |2016-12-13T07:00:00Z|65.9 |
|2 |2016-12-13T08:00:00Z|70.1 |
|2 |2016-12-13T09:00:00Z|72.0 |
|2 |2016-12-13T10:00:00Z|73.9 |
|2 |2016-12-13T11:00:00Z|     |
|2 |2016-12-13T12:00:00Z|     |
+--+--------------------+-----+
There are a variety of aggregation functions support by Riak TS including COUNT, SUM, MEAN, AVG, MIN, MAX, and STDDEV. Below are several example queries that use aggregation functions.
SELECT AVG(value) FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-13 12:00:00';
+-----------------+
|   AVG(value)    |
+-----------------+
|66.08888888888889|
+----------------
SELECT COUNT(value), AVG(value), MAX(value), MIN(value)FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-13 24:00:00';
+------------+-----------------+----------+----------+
|COUNT(value)|   AVG(value)    |MAX(value)|MIN(value)|
+------------+-----------------+----------+----------+
|     9      |66.08888888888889|   73.9   |   61.0   |
SELECT COUNT(time), AVG(value), MAX(value), MIN(value)FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-13 24:00:00';
+-----------+-----------------+----------+----------+
|COUNT(time)|   AVG(value)    |MAX(value)|MIN(value)|
+-----------+-----------------+----------+----------+
|    11     |66.08888888888889|   73.9   |   61.0   |
+-----------+-----------------+----------+----------+
Arithmetic operations are also supported. Assuming that the values in the table are Fahrenheit temperatures, we can convert them to Celcius.
SELECT ((value - 32) \* 5) / 9 FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-13 24:00:00';
 +------------------+
 |(((value-32)\*5)/9)|
 +------------------+
 |17.88888888888889 |
 |17.22222222222222 |
 |16.833333333333332|
 |16.88888888888889 |
 |16.11111111111111 |
 |18.833333333333336|
 |21.166666666666664|
 |22.22222222222222 |
 |23.277777777777782|
 +------------------+
GROUP BY and ORDER BY are also supported in SELECT statements. There is more information about optimizing your queries in the Riak TS documentation. When using ORDER BY, you can also use the optional keywords LIMIT, OFFSET, ASC, DESC, NULLS FIRST, and NULLS LAST.
Let’s add a few more rows to our table.
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-14 01:00:00Z', 65.0);
INSERT INTO SensorData (id,time, value) VALUES (2, '2016-12-14 02:00:00Z', 65.9);
Now, let’s GROUP BY the value.
SELECT value, count(value), count(id) FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-14 24:00:00' GROUP BY value;
+-----+------------+---------+
|value|COUNT(value)|COUNT(id)|
+-----+------------+---------+
|73.9 |     1      |    1    |
|65.9 |     2      |    2    |
|65.0 |     1      |    1    |
|72.0 |     1      |    1    |
|70.1 |     1      |    1    |
|61.0 |     1      |    1    |
|62.4 |     1      |    1    |
|62.3 |     1      |    1    |
|63.0 |     1      |    1    |
|     |     0      |    2    |
|64.2 |     1      |    1    |
+-----+------------+---------+
As you can see, there is no guaranteed order for the returned rows. When you want rows returned in a specific sort order, you use ORDER BY. The default for ORDER BY is to return rows in ascending order. The sample code below returns the rows in descending order.
SELECT \* FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-14 24:00:00' ORDER BY time DESC, value DESC;
+--+--------------------+-----+
|id|        time        |value|
+--+--------------------+-----+
|2 |2016-12-14T02:00:00Z|65.9 |
|2 |2016-12-14T01:00:00Z|65.0 |
|2 |2016-12-13T12:00:00Z|     |
|2 |2016-12-13T11:00:00Z|     |
|2 |2016-12-13T10:00:00Z|73.9 |
|2 |2016-12-13T09:00:00Z|72.0 |
|2 |2016-12-13T08:00:00Z|70.1 |
|2 |2016-12-13T07:00:00Z|65.9 |
|2 |2016-12-13T06:00:00Z|61.0 |
|2 |2016-12-13T05:00:00Z|62.4 |
|2 |2016-12-13T04:00:00Z|62.3 |
|2 |2016-12-13T03:00:00Z|63.0 |
|2 |2016-12-13T02:00:00Z|64.2 |
+--+--------------------+-----+
You can sort with NULLS FIRST or NULLS LAST and you can LIMIT the number of rows returned.
SELECT \* FROM SensorData WHERE id = 2 AND time > '2016-12-13 01:00:00' AND time < '2016-12-14 24:00:00' ORDER BY value NULLS FIRST LIMIT 5;
+--+--------------------+-----+
|id|        time        |value|
+--+--------------------+-----+
|2 |2016-12-13T11:00:00Z|     |
|2 |2016-12-13T12:00:00Z|     |
|2 |2016-12-13T06:00:00Z|61.0 |
|2 |2016-12-13T04:00:00Z|62.3 |
|2 |2016-12-13T05:00:00Z|62.4 |
+--+--------------------+-----+
Congratulations! You have now created and queried your first table in Riak TS.
When you are ready to develop your first app, you will want to select from the list of supported operating systems and client libraries. In production, we recommend no fewer than 5 nodes to ensure availability of your data. Below is a list of additional resources to help you learn more about Riak TS.
Resources
Getting Started Guide
Documentation
Riak TS Datasheet
Riak TS Technical Overview Whitepaper
Professional Services
Dorothy Pults
@deepults
 
 
 
