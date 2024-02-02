---
title: "How RAID Fits in With Horizontal Scalability on Commodity Hardware"
description: "A look at using RAID for your application."
project: community
lastmod: 2015-05-28T19:23:38+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Hector Castro"
pub_date: 2013-06-18T18:49:41+00:00
---
Riak shines when scaling horizontally with commodity hardware. A question frequently arises: should a redundant array of independent disks (RAID) be used, and if so, how?
Key reasons RAID is used:

Performance
Disk-level fault tolerance

Let’s take a look at each in turn.
Performance
Particularly when using a hardware controller, RAID can offer significant read and write performance enhancements.
Optimizing writes involves striping, which reduces fault tolerance. In general, striping also provides excellent read performance. Mirrored configurations reduce performance on writes, but tend to provide excellent read performance. A balance can be struck by mixing stripes and mirrors.
Below is a table to help make the trade-offs clearer:



RAID Level
Type
Read
Write
Fault Tolerance
Disk Minimum
Usable Space\*




RAID 0
Stripe
Excellent
Excellent
None
2
1000GB


RAID 1
Mirror
Excellent
Poor
Excellent
2
500GB


RAID 10
Stripe + Mirror
Excellent
Good
Good
4
1000GB


RAID 5/6/z
Combination/Parity
Good
Poor
Excellent
3
1000GB



\*Usable space calculations assume all disks are 500GB.
Tools such as basho\_bench can help you evaluate whether such a boost is useful for your environment and application needs.
Disk-level fault tolerance
Riak provides a layer of fault tolerance by distributing data to 3 servers by default. If a disk or server fails, it can be replaced as a matter of course without worrying about the data that was on it.
However, a RAID setup that allows for disk failures without crashing the file system (or entire system) has its benefits. A node utilizing RAID can still participate in reads and other cluster-wide operations. It can also ease the burden of replacing failed hardware so that the node can be reinserted into the cluster faster.
An example repair workflow:

Disk failure detected by RAID
Leave node up until a spare disk is acquired
Shut down Riak and take the node offline
Rebuild the disks completely
Bring the node back online and start Riak

What’s the downside to RAID?
A hardware RAID controller is a single point of failure, and RAID (either software or hardware) adds a layer of complexity to configuration and troubleshooting. Drives are not necessarily portable to other RAID controllers (even similar controllers with different firmware revision numbers).
Even if a node does not fail completely, a degraded RAID array can significantly impact node performance and have an impact on the cluster. For example, if a node is configured with RAID 5 and a disk is damaged, then writes on that node will likely suffer.
Flavors of RAID
Hardware RAID
Hardware RAID has a few benefits over software RAID. A major one is that hardware RAID allows you to open only a single I/O stream to write to multiple disks, leading to potentially less I/O wait and fewer blocking threads. With software RAID, an I/O stream has to be opened to each disk in the RAID set, causing multiple blocking threads.
Some other important considerations when selecting a hardware RAID device:

Do you need a battery-backed cache? This aids in avoiding data loss during power
interruptions. Flash-based caches are also an option.
How much non-volatile random-access memory (NVRAM) do you need?
What processor speed do you need? This is important for replica rebuilds and parity calculation.
Do you care for out-of-band management via something like a web interface?

All of these decisions impact cost. You can get away with a cheap RAID device and still get decent performance for RAID 1/0/10, but once you throw parity into the mix, battery backups and caches have a noticeable impact.
Software RAID
mdadm
On Linux, the most popular software RAID solution is mdadm. It can create, assemble, report on, and monitor arrays.

# RAID 0
$ mdadm --create /dev/md0 --level=0 --raid-devices=2 /dev/sdb /dev/sdc
# RAID 1
$ mdadm --create /dev/md0 --level=1 --raid-devices=2 /dev/sdb /dev/sdc
# RAID 10
$ mdadm --create /dev/md0 --level=10 --raid-devices=4 /dev/sdb /dev/sdc /dev/sdd /dev/sde
# RAID 5
$ mdadm --create /dev/md0 --level=5 --raid-devices=3 /dev/sdb /dev/sdc /dev/sdd

In a failure scenario, assuming a RAID level greater than zero, replacing a drive can be as simple as:

$ mdadm /dev/md0 --fail /dev/sdb
# Swap out the failed drive
$ mdadm --add /dev/md0 /dev/sdb

ZFS
On Solaris or FreeBSD, the most common solution is ZFS.

# Striped zpool
$ zpool create tank /dev/dsk/c0t4d0 /dev/dsk/c0t5d0
# Mirrored zpool
$ zpool create tank mirror /dev/dsk/c0t4d0 /dev/dsk/c0t5d0
# Striped and mirrored zpool
$ zpool create tank mirror /dev/dsk/c0t4d0 /dev/dsk/c0t5d0
$ zpool add tank mirror /dev/dsk/c0t6d0 /dev/dsk/c0t7d0
# raidz
$ zpool create tank raidz /dev/dsk/c0t4d0 /dev/dsk/c0t5d0 /dev/dsk/c0t6d0

Replacing a drive in a zpool:
$ zpool replace tank /dev/dsk/c0t5d0
Or, if you are replacing a device in a zpool with a disk in a different physical location:
$ zpool replace tank /dev/dsk/c0t5d0 /dev/dsk/c0t6d0
Conclusion
There’s no universally correct answer to the question of RAID or no RAID. The needs of your application should drive the decision of whether RAID will be useful, and if so, at what level.
Write heavy, high performance applications should probably use RAID 0 or avoid RAID altogether and consider using a larger n\_val and cluster size. Read heavy applications have more options, and generally demand more fault tolerance with the added benefit of easier hardware replacement procedures.
Either way, Riak provides redundancy by generating replicas of data and automatically spinning up fallback vnodes. In addition, having at least 5 nodes in a cluster decreases the likelihood of any single disk/node failure causing a service degradation or disruption.
Shout out to Brent Woodruff, John Daily, Paul Hagan, and Seth Thomas for their help in assembling this post.
Hector Castro
