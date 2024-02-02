---
title: "Documentation - Designing for Clarity and Comprehension"
description: "A look at the principles behind our documentation redesign."
project: community
lastmod: 2015-05-28T19:23:37+00:00
sitemap:
  priority: 0.2
project_section: technicalblogpost
author_name: "Riak"
pub_date: 2013-07-24T09:00:49+00:00
---
July 24, 2013
It is tempting, when considering documentation, to decide that it is “someone else’s problem.” In truth, writing and maintaining documentation is a cross-disciplinary function. Content is paramount, clarity and comprehension of design determines whether the content is accessible, and information architecture will expedite learning…or frustration.
At Riak, we are proud of our documentation. All design, updates, and edits are done in the open and we encourage community participation. Recently, we launched a major refresh to our docs and, in the interest of sharing our learnings with our community, we wanted to describe some of the ideas and principles behind it.
Design
With this recent update, we were particularly interested in a clean and legible design. We wanted a redesign that was easy to read, easy to reference, and easy to reuse.
To that end, we updated the font set to include a serif and a sans serif (Gandhi and Open Sans, respectively). Our design team selected two open source types that worked well together, but also had the best cross-browser and cross-display consistency.
We made the text larger, changed its color to be black on white for higher contrast, and limited the width of the page for ease of reading (à la Matthew Butterick’s suggestions). This focus on legibility allows us to scale content within the same design theme as needed.
As we continue updating Riak, prior documentation remains relevant and accessible. Previously, the Riak version selection was displayed horizontally, with all major releases visible. We added a selection menu that flows vertically and now only indicates the currently-viewed product version.
The navigation has also been updated so the collapse behavior maintains state across pages and links to the Help Page and GitHub repository remain static.
Information Architecture
To appeal to our audience of both developers and operators, we now have two distinct tracks of content that are highlighted and organized in the left-hand navigation menu. These tracks are bookended by new introductory content (a slimmed-down version of “The Riak Fast Track”) and conceptual information relevant to both developers and operators.
Furthermore, since developers tend to actually write code, our examples are being refreshed to use code samples, rather than HTTP calls. This will be an ongoing process.
Where Art Meets Science
The decisions about the documentation refresh combined instinct, preference, and empirical data about usage. As the community provides feedback, we will continue to make changes to improve usability.
As with any project of this scope, many members of the Riak team were involved: our engineers who write documentation, the Docs Cabal that managed the process, and the Riak design team that provided dozens of possible designs. This distributed team was able to leverage the best of each others work to produce something beautiful and, most important of all, useful.
Riak Docs Cabal
