[basho docs]: http://docs.basho.com/
[task list]: https://github.com/basho/private_basho_docs/issues/11
[middleman]: https://middlemanapp.com/
[rvm]: https://rvm.io/

# Riak's Documentation Generation

This repository contains all the bits and pieces, large and small required to
render and deploy Basho's documentation.

### https://docs.riak.com/

This is updated for each new version of Riak once reviewed.

This is a Work In Progress!
Please let us know if you'd like to help out!

### https://www.tiot.jp/riak-docs/

This is updated for each new version of Riak as soon as written, and gets regular small updates.

### https://www.tiot.jp/riak-docs-beta/

This is updated for each new version of Riak as each doc section is updated, and often will be a WIP.

## Building The HTML Locally

We moved to a Docker image to build the docs to avoid the issues with getting the various versions of things to work together.

1. Install [Docker](https://docs.docker.com/engine/install/)

1. Clone the repository with:

    ```
    git clone https://github.com/ti-tokyo/riak-docs-fork.git
    cd riak-docs-fork
    ```

    Or:

    ```
    git clone https://github.com/basho/basho_docs.git
    cd basho_docs
    ```
   
1. Build the Docker image:

    ```
    ./docker/docker-build-image.titokyo.sh
    ```

1. Run the docker image as a local server to test it all works:

    ```
    docker-compose -f ./docker/docker-compose.localhost-preview.yaml up riakdocs
    ```

1. Play by visiting <http://localhost:1314/riak-docs/>.


### No Really, _Go_ Play

<sub>See what we did there?</sub>

At this point, any changes you make to the markdown files in the `content/`
directory will be automatically detected and rendered live in your local browser.
Change some stuff! Have fun!

If you want to modify the [content templates][hugo content templates] that
define how each pages' HTML is generated, modifying the [Go Templates][hugo go template primer]
in `layouts/_default/` and the [partial templates][hugo partial templates] in
`layouts/partials/` will also be automatically detected and rendered live in your browser.

[hugo content templates]: https://gohugo.io/templates/content/
[hugo go template primer]: https://gohugo.io/templates/go-templates/
[hugo partial templates]: https://gohugo.io/templates/partials/
[hugo shortcodes]: https://gohugo.io/extras/shortcodes/

## Modifying the `.js` and `.css` Files

>**Note:** Generally, unless you're helping us out with a specific task or project that you've discussed with us, you should not be altering the .js or .css files in this repo.

If you want to mess with the scripts and CSS that this site uses, it's not
_quite_ as easy as modifying the HTML.

The scripts and CSS files used to render Hugo content are expected to live in
the `static/` directory. We use a lot of [Coffee Script][coffee] and [Sass][sass]
for our scripting and styling needs, and we convert those files to `.js` and
`.css` as a pre-render step. We put those `.coffee` and `.scss` files into the
`dynamic/` directory.

>**Note:** For files manually generated, place the source of the generation in
a directory parallel to the generated file(s), rooted in `public_src/`. If
possible, include a script to generate the output. For example, the uml
deployment diagram images in `static/images/redis/` were generated by the .uml
files in `public_src/images/redis/` via the script `gen_diagrams.sh` w/ the list
of source files for generation explicitly listed in `diagrams.lst`.

To convert the Coffee and Sass into `.js` and `.css` files, you'll need to:

1. **Install [RVM][rvm]** or equivalent.  
    You might need to restart your shell to get the `rvm` command to be recognized.
1. **Install Ruby.**  
    Use the following command: ``rvm install `cat .ruby-version` `` or manually
    install the current version specified in our .ruby-version and Gemfile files.
1. **Install [Bundler]** with `gem install bundler`.
1. **Install the rest of the dependencies** with `bundle install`.
1. **Use [Rake] to do everything else**, like rebuild a copy of everything that
   should live in `static/`. You can use `rake build` for that. For a more
   debug-friendly version of everything, run `rake build:debug`.

   In case you want any changes you make to `.coffee` and `.scss` files to be
   automatically detected and rendered live in your browser, you can run
   `rake watch`.

   For a list of some of the useful commands, just run `rake`.

[coffee]: coffeescript.org
[sass]: http://sass-lang.com/
[rvm]: https://rvm.io/
[bundler]: http://bundler.io/
[rake]: http://docs.seattlerb.org/rake/

## Would You Like to Contribute?

Awesome! <sub>(We're assuming you said yes. Because you're reading this. And you're _awesome_.)</sub>

This repository operates just like any other open source repo, and only thrives
through the efforts of everyone who contributes to it. If you see something wrong,
something that could be improved, or something that's simply missing please
don't hesitate to:

* **Open Up a [New Issue]**
    and let us know what you think should change.

* **[Find the File] You Want to Change**
    and use GitHub's online editor to open a Pull Request right here.

* **[Fork] This Repository**
    so you can make (and see) your changes locally.

Don't forget to check out our [Contributing Guidelines][contributing] so you
can read up on all our weird little quirks, like how we
[don't want you to use `<h1>` headers][contributing_headers].

[new issue]: https://github.com/basho/basho_docs/issues/new
[find the file]: https://github.com/basho/basho_docs/find/master
[fork]: https://github.com/basho/basho_docs/#fork-destination-box
[contributing]: CONTRIBUTING.md
[contributing_headers]: CONTRIBUTING.md
