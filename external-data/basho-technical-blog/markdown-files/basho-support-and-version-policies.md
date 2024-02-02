---
title: "Riak Support and Version Policies"
description: "Long Term Support Releases At Riak, we are committed to delivering high-quality software that powers the critical applications of your business. Naturally, this presents an interesting and non-trivial challenge - how to quickly deploy new features and functionality while ensuring long-term stabili"
project: community
lastmod: 2016-06-22T14:16:45+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Pavel Hardak"
pub_date: 2015-08-24T14:08:24+00:00
---
Long Term Support Releases
At Riak, we are committed to delivering high-quality software that powers the critical applications of your business. Naturally, this presents an interesting and non-trivial challenge – how to quickly deploy new features and functionality while ensuring long-term stability. It is tempting to think of these goals as being mutually exclusive, but they are not. The problem is not new to the software development field and there are many approaches that have been adopted. Our choice is the one which allows us to achieve both objectives.
To achieve this target, we proceed in two parallel streams. There is a “fast track”, where we make codebase commits on a daily basis. This results in the periodic release of major versions, delivering significant changes, and frequent release of minor versions, with bug fixes and other improvements. The focus is on fast progress and cutting edge functionality. In contrast, there is also a “stable track”, where the focus is on long term stability. It is achieved by taking a major release and hardening it over time. Instead of introducing new features, emphasis is on stability and bug fixes. At some point, based on extensive testing, such releases get designated as “LTS”, which stands for “Long Term Support”. Once the version achieves LTS status further fixes will only be released for the most critical issues. LTS releases are supported for two years from the date a release is promoted to LTS status.
We should note, that “fast track” does not imply “non stable” – Riak’s powerful distributed architecture provides a very strong foundation and when it comes to stability at scale, Riak is second to none.
Riak products use the industry standard semantic versioning scheme, which looks like MAJOR.MINOR.PATCH, i.e. using three numbers for each release. We intend to assign the LTS designation for, at most, 2 minor versions in a major version, ending in .0 and optionally .5 (for example 2.0 and 2.5). Typically, LTS will be several patch releases later. Thus, for the Riak 2.0 product line, we are designating Riak KV 2.0.6 as LTS and Riak S2 (formerly Riak CS) 2.0.2 as LTS.
Minor versions other than those ending in .0 or .5 will not be promoted to LTS status. For example, no release in 2.1 or 2.2 series will be promoted to LTS. At this time we are only introducing LTS status for Riak KV and Riak S2 (formerly Riak CS). New Riak products, like Riak Data Platform, may be promoted to LTS status at a later date.
The support period for LTS releases will be two (2) years. We plan to promote to LTS status at least one release per year. An LTS release can be directly upgraded from the immediately prior LTS. Outside of that path, intermediate steps may be required.
EOL Announcement
As a separate, but related issue, we are announcing the EOL (End of Life) for Riak KV 1.4.x on Nov 30, 2015. We strongly encourage our users, who still use any 1.x version, to upgrade to Riak KV 2.x  – either the LTS version or latest GA release – depending on their requirements. Customers with active support agreements may continue to request support on 1.4 after the EOL date, however issue resolutions may require upgrading to a current release.
Below is the EOL announcement that is going out through our customer support channels.
Riak® Riak (now known as Riak KV) software version 2.1 was released for general availability in April 2015. Support for Riak KV version 1.4.x will end on November 30, 2015. Users are encouraged to upgrade to Riak® Riak KV version 2.0.6 or greater. Customers using version 1.4.x with active support contracts will continue to receive support for this release from Riak’s client services team until the end of support date.
Riak® Riak CS (now known as Riak S2) software version 2.0 was released for general availability in March 2015. Support for Riak CS version 1.4.x will end on November 30, 2015. Users are encouraged to upgrade to Riak® Riak CS version 2.0.2 or greater. Customers using version 1.4.x with active support contracts will continue to receive support for this release from Riak’s client services team until the end of support date.
We believe this strategy will provide the excellent answer to the demanding requirements  for both open source users and enterprise customers. We are always eager to hear from you – please Contact Us with your feedback.
Pavel Hardak
