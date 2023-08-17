---
title: "Riak CS Release Notes"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Release Notes"
    identifier: "reference_release_notes"
    weight: 100
    parent: "index"
project: "riak_cs"
project_version: "3.1.0"
aliases:
  - /riak/cs/3.1.0/cookbooks/release-notes/
  - /riak/cs/3.1.0/cookbooks/Riak-CS-Release-Notes/
  - /riakcs/3.1.0/cookbooks/release-notes/
  - /riakcs/3.1.0/cookbooks/Riak-CS-Release-Notes/
---

[riak_cs_multibag_support]: {{<baseurl>}}riak/cs/3.0.1/configuration/supercluster
[riak_docs_cs_2.1.2_release_notes]: {{<baseurl>}}riak/cs/2.1.2/cookbooks/release-notes/

[riak_1.4_release_notes]: https://github.com/basho/riak/blob/1.4/RELEASE-NOTES.md
[riak_2.0_release_notes]: https://github.com/basho/riak/blob/2.0/RELEASE-NOTES.md
[riak_2.0_release_notes_bitcask]: https://github.com/basho/riak/blob/2.0/RELEASE-NOTES.md#bitcask
[riak_cs_1.4_release_notes]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#riak-cs-145-release-notes
[riak_cs_1.5_release_notes]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#riak-cs-154-release-notes
[riak_cs_1.5_release_notes_upgrading]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#notes-on-upgrading
[riak_cs_1.5_release_notes_upgrading_1]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#notes-on-upgrading-1
[riak_cs_1.5_release_notes_incomplete_mutipart]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#incomplete-multipart-uploads
[riak_cs_1.5_release_notes_leeway_and_disk]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#leeway-seconds-and-disk-space
[upgrading_to_2.0]: {{<baseurl>}}riak/2.0.5/upgrade-v20/
[proper_backend]: {{<baseurl>}}riak/cs/2.0.0/cookbooks/configuration/Configuring-Riak/#Setting-up-the-Proper-Riak-Backend
[configuring_elvevedb]: {{<baseurl>}}riak/latest/ops/advanced/backends/leveldb/#Configuring-eLevelDB
[bitcask_capactiy_planning]: {{<baseurl>}}riak/2.0.5/ops/building/planning/bitcask/
[upgrading_your_configuration]: {{<baseurl>}}riak/2.0.5/upgrade-v20/#Upgrading-Your-Configuration-System
[storage_statistics]: {{<baseurl>}}riak/cs/latest/cookbooks/Usage-and-Billing-Data/#Storage-Statistics
[downgrade_notes]:  https://github.com/basho/riak/wiki/2.0-downgrade-notes

[riak_cs_1.5_release_notes_upgrading]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#notes-on-upgrading
[riak_cs_1.5_release_notes_upgrading_1]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#notes-on-upgrading-1
[riak_cs_1.5_release_notes_incomplete_mutipart]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#incomplete-multipart-uploads
[riak_cs_1.5_release_notes_leeway_and_disk]: https://github.com/basho/riak_cs/blob/release/1.5/RELEASE-NOTES.md#leeway-seconds-and-disk-space
[riak_cs_2.0.0_release_notes]: https://github.com/basho/riak_cs/blob/develop/RELEASE-NOTES/

[riak_cs_docker_bundle]: https://github.com/TI-Tokyo/riak_cs_service_bundle
[ti_tokyo_github]: https://github.com/TI-Tokyo
[riak_cs_3_codebeam_america_2021]: https://youtu.be/CmmeYza5HPg

[riak_cs_3_object_versions]: https://docs.aws.amazon.com/AmazonS3/latest/userguide/Versioning.html
[riak_cs_3_getbucketversioning]: https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetBucketVersioning.html
[riak_cs_3_putbucketversioning]: https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketVersioning.html
[riak_cs_3_listobjectversions]: https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjectVersions.html

[riak_cs_3_docker_service]: https://github.com/TI-Tokyo/riak_cs_service_bundle
[riak_cs_3_load_test]: https://github.com/TI-Tokyo/s3-benchmark

## Riak CS 3.1.0

Released March 15, 2023.

This release is centred around a single architectural change: the merge of Stanchion into Riak CS. There are no substantial additions in the scope of supported S3 methods, and no changes to the behaviour or feature set otherwise.

## New Features

- Stanchion is now colocated on one of the Riak CS nodes. Stanchion was a separate Erlang application serving the purpose of serializing CRUD operations on users and buckets. It previously had to run alongside Riak CS nodes on a dedicated Stanchion node.
  - In auto mode, it is dynamically created at the Riak CS node receiving the first request that needs to be serialized. This node will then store Stanchion details (ip:port) in a special service bucket on a configured Riak KV node. Riak CS nodes will read that ip:port and send subsequent Stanchion requests to that endpoint. If a Riak CS node finds that Stanchion is unreachable, it will spawn a new instance on its premises and update the details in Riak KV. When a node that previously hosted Stanchion, after being temporarily unavailable, sees the Stanchion ip:port has changed, it will stop its Stanchion instance.
- Riak CS now can be built on OTP-22 through 25.
- A new subcommand, `supps` has been added to `riak-cs admin`, which will produce a ps-like output for the processes in the Riak CS main supervisor tree with some stats.

## Changes

### User-Visible Changes

- New configuration parameters:
  - `stanchion_hosting_mode`, with acceptable values: `auto`, `riak_cs_with_stanchion`, `riak_cs_only`, `stanchion_only` (default is `auto`).
  - `tussle_voss_riak_host` ("voss" stands for "VOlatile, Storage, Serialized"), which can set to be `auto` or a `fqdn:port` at which Riak CS will store Stanchion details. A value of `auto` is equivalent to setting it to riak_host. The purpose of this parameter is to enable users operating in suboptimal networking conditions to set it to a dedicated, single-node Riak cluster on a separate network, which can be made more reliable than the one carrying S3 traffic.
  - `stanchion_port`, port at which Stanchion instance will listen (if/when this node gets to start it).
  - `stanchion_subnet` and `stanchion_netmask` (with default values of `127.0.0.1` and `255.255.255.255` respectively), to use when selecting which network to place Stanchion on.

- The `riak-cs admin stanchion` switch command has been removed. The purpose of this command was to enable operators to change the ip:port of Stanchion endpoint without restarting the node. With Stanchion location now being set dynamically and discovered automatically, there is no need to expose an option for operators to intervene in this process.

### Other Changes

- A fair bit of work has gone to uplift riak_cs_test, hopefully making life easier for the next decade. Specifically:
  - we switched erlcloud from an ancient, Basho-patched fork to upstream (tag 3.6.7), incidentally triggereing (and fixing) a bug with /stats endpoint, which previously only accepted v2 signatures.
  - riak_cs_test can be built with OTP-24, and has lager replaced by the standard kernel logger facility.
  - php and ruby tests have been updated and re-included in external client tests.
- [Riak CS Service Bundle](https://github.com/TI-Tokyo/riak_cs_service_bundle) has been updated to accommodate stanchion-less version of Riak CS.

## Riak CS 3.0.1

Released June 10, 2022.

### General

This is a correction release that includes one feature that slipped from 3.0.0.

### New Features

- Support for fqdn data type for `riak_host` and `stanchion_host` configuration items in _riak-cs.conf_.

## Riak CS 3.0.0

Released May 30, 2022.

### General

This release was originally envisaged as an uplift of 2.1.2 to OTP-22 and rebar3. There were no known critical bugs that needed fixing. We did, however, identifiy and implement a new S3 API call (ListObjectVersions and related), to give more substance to the major version bump.

We also provide Dockerfiles, and a [Riak CS Docker Service Bundle][riak_cs_docker_bundle], as a convenient way to set up the full Riak CS suite locally.

All principal repositories are in [TI Tokyo org on Github][ti_tokyo_github].

This release was [presented][riak_cs_3_codebeam_america_2021] at Code BEAM America 2021.

### New features

- Support for [object versions][riak_cs_3_object_versions], including new S3 API calls:
  - [GetBucketVersioning][riak_cs_3_getbucketversioning], [PutBucketVersioning][riak_cs_3_putbucketversioning] and [ListObjectVersions][riak_cs_3_listobjectversions].
    - For buckets with versioning enabled, `GetObject` will return the specific version if it is given in the request, or the `null` version if it is not.
    - As a Riak CS extension, rather than generating a random id for the new version, `PutObject` will read a `versionId` from header `x-rcs-versionid`, and use that instead.
- Riak CS Suite as a [Docker service][riak_cs_3_docker_service], allowing the (prospective) users quickly to bring up a fully functional Riak CS cluster running locally, complete with Riak CS Control, and
  - properly configured and set up with a new user, whose credentials will be shown;
  - with riak data persisted;
  - ready for a [load-test][riak_cs_3_load_test].
- Packaging:
  - New packages are provided for FreeBSD 13 and OSX 14 (in the latter case, the package is the result of `make rel` tarred; no special user is created).
  - Packages have been verified for: o RPM-based: Centos 7 and 8, Amazon Linux 2, SLES 15, Oracle Linux 8; o DEB-based: Debian 8 and 11, Ubuntu Bionic and Xenial; o Other: FreeBSD 13, OSX 14; Alpine Linux 3.15.
- A Dockerfile, bypassing cuttlefish mechanism to enable run-time configuration via environment variables.
- `riak-cs-admin` now has a new option, `test`, which creates a bucket and performs a basic write-read-delete cycle in it (useful to test that the riak cluster is configured properly for use with Riak CS).

### Changes

#### User-visible changes

- S3 request signature v4 is now the default. The old (v2) signatures
  continue to be supported.
- A change of internal structures needed to support object versions,
  meaning downgrade to 2.x is no longer possible (even if the objects
  created with 3.0 have no versions). Upgrade from 2.x is possible.
- The rpm and deb packages now rely on systemd (old-style SysV init
  scripts are no longer included).

#### Other changes

- Riak CS and Stanchion now require OTP-22 and rebar3.
- Riak CS Test framework:
  - The framework, along with a suite of tests (also the [multibag
    additions](https://github.com/TI-Tokyo/riak_cs_multibag)), has been
    upgraded to OTP-22/rebar3 and moved into a separate project,
    [riak_cs_test](https://github.com/TI-Tokyo/riak_cs_test).
  - A new battery of tests is written for `s3cmd` as a client.
  - The Python client tests have been upgraded to boto3 and python-3.9.
- A refactoring of code shared between Riak CS and stanchion resulted
  in that code being collected into a separate dependency,
  [rcs_common](https://github.com/TI-Tokyo/rcs_common).
- [Riak CS Control](https://github.com/TI-Tokyo/riak_cs_control)
  application has been upgraded to OTP-22/rebar3, too, however without
  any new features.
- All EQC tests have been converted to use PropEr (no shortcuts taken,
  all coverage is preserved).

### Upgrading

Existing data in the riak cluster underlying a 2.x instance of Riak CS
can be used with Riak CS 3.0 without any modification.

*Note:* Once a new object is written into a database by Riak CS 3.0,
that database cannot be used again with 2.x.

### Compatibility

Riak CS 3.0 has been tested with Riak versions 2.2.6, 2.9.8 through
.10, and 3.0.7 and .9. It requires Stanchion 3.0.x (2.x versions not
supported due to changes in the manifest record).

## CS 2.1.2 and Earlier

[Click here][riak_docs_cs_2.1.2_release_notes] to read the Riak CS 2.1.2 release notes, which include all earlier releases.
