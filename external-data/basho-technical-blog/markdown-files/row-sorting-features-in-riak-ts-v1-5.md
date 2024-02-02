---
title: "Row Sorting Features in Riak TS v1.5"
description: "Riak TS v1.5 introduces two new features for retrieving rows in a sort order other than the order that LevelDB naturally stores keys. The first is a hint to the storage engine about how the keys for a table should be stored on disk. The second is a traditional SQL ORDER BY clause that works the same"
project: community
lastmod: 2017-01-19T11:47:35+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Andy Till"
pub_date: 2017-01-19T10:50:01+00:00
---
Riak TS v1.5 introduces two new features for retrieving rows in a sort order other than the order that LevelDB naturally stores keys. The first is a hint to the storage engine about how the keys for a table should be stored on disk. The second is a traditional SQL ORDER BY clause that works the same way you’d expect in a relational database.
LevelDB Sort Order
LevelDB is the backend storage engine for Riak TS. It is useful to understand how rows are sorted by the local key in LevelDB without the new features. We will use the table and data below for the first example, note that columns a and b are in the partition key, and the local key contains columns a, b and importantly c which is additional to the partition key.
The partition key’s hash determines which partition the rows get written to in the cluster. The local key is used to store the value on disk, and is stored in sorted order. Each row is stored against a unique local key.

CREATE TABLE table1(
 a VARCHAR NOT NULL ,
 b TIMESTAMP NOT NULL,
 c VARCHAR NOT NULL,
 PRIMARY KEY((a,QUANTUM(b,1,'m')),a,b,c)
);
INSERT INTO table1 VALUES
 ('dby', '2017-01-08 11:05:51','a'),
 ('dby', '2017-01-08 11:05:51','b'),
 ('dby', '2017-01-08 11:05:52','b'),
 ('dby', '2017-01-08 11:05:53','c');

With table1 created, we can run the following query in the shell. It covers all of the rows that were inserted. Rows are returned in the order that they are stored in LevelDB. Insertion order does not affect how keys are stored on disk.

SELECT \* FROM table1 WHERE a = 'dby' AND b >= '2017-01-08 11:05:51' AND b <= '2017-01-08 11:05:53';

+---+--------------------+-+
| a | b |c|
+---+--------------------+-+
|dby|2017-01-08T11:05:51Z|a|
|dby|2017-01-08T11:05:51Z|b|
|dby|2017-01-08T11:05:52Z|b|
|dby|2017-01-08T11:05:53Z|c|
+---+--------------------+-+

LevelDB sorts by column so rows are sorted by column a, then all rows with the same column a value are sorted by column b starting with the lowest value and so on. VARCHAR and BLOB values are sorted by byte values smallest to largest e.g. 0x01 before 0x02 and 'a' before 'b'.
Crafting a local key that stores keys in the order required by your queries will provide the lowest overhead on writes and queries.
Changing the Storage Order
In Riak TS 1.5 we don’t have to settle for LevelDBs default sort order. Adding DESC to a column name in the local key will reverse the order that the rows are stored in for that table.
This makes queries equally as fast as those on the table in the first example because the data on disk is already in the correct order, and no additional work has to be done. A negligible amount of work has to be done on writes to create the local key, for integer and timestamp columns the value is negated and for varchar and blob columns each byte is negated so that 255 becomes 0.
This table is similar to the previous one but applies the DESC keyword to column b in the local key which is the timestamp.

CREATE TABLE table2(
 a VARCHAR NOT NULL,
 b TIMESTAMP NOT NULL,
 c VARCHAR NOT NULL,
 PRIMARY KEY((a,QUANTUM(b,1,'m')),a,b DESC)
);
INSERT INTO table2 VALUES
 ('dby', '2017-01-08 11:05:51','a'),
 ('dby', '2017-01-08 11:05:51','b'),
 ('dby', '2017-01-08 11:05:52','b'),
 ('dby', '2017-01-08 11:05:53','c');
SELECT \* FROM table2
WHERE a = 'dby' AND b >= '2017-01-08 11:05:51' AND b <= '2017-01-08 11:05:53';

+---+--------------------+-+
| a | b |c|
+---+--------------------+-+
|dby|2017-01-08T11:05:53Z|c|
|dby|2017-01-08T11:05:52Z|b|
|dby|2017-01-08T11:05:51Z|b|
+---+--------------------+-+

This method of sorting is preferable if the queries are known when the table is being designed. By sorting when the data is written, which occurs once, Riak TS does not have to sort per query using ORDER BY.
Sorting with ORDER BY
It is possible that this will not cover your sorting requirements. In this case, we have a flexible ORDER BY clause that allows ascending and descending sorting on any column name that is defined in the table.
Using table1 and data from the first example we can create a query to order the rows by column c in descending order, greatest first.

SELECT \* FROM table1
WHERE a = 'dby' AND b >= '2017-01-08 11:05:51' AND b <= '2017-01-08 11:05:53'
ORDER BY c DESC;

+---+--------------------+--+
| a | b |c |
+---+--------------------+--+
|dby|2017-01-08T11:05:53Z|c |
|dby|2017-01-08T11:05:51Z|b |
|dby|2017-01-08T11:05:52Z|b |
|dby|2017-01-08T11:05:51Z|a |
+---+--------------------+--+

When a query with an ORDER BY clause is issued, an instance of LevelDB is created on the node that received the request. When the query results are retrieved by that node, they are put into the temporary LevelDB instance and a key is created for each row using the ORDER BY clause. When DESC is applied to a column in the ORDER BY clause. The sort order is the same as described in the LevelDB Sort Order section above and the value “negation” for local keys is used, described in the Changing the Storage Order section above.
The required flexibility of ORDER BY means that the sorting is done as part of the query and you may notice an increase in latency.
Conclusion
We’ve gone through three ways that you can get results from Riak TS in the order that your application requires, each with different trade-offs and performance.
Get Started Now with Riak TS 1.5

Download Open Source
Sign In to download Enterprise Edition
Install on a supported distribution
Use Riak Shell to execute the example statements and explore the new features.

