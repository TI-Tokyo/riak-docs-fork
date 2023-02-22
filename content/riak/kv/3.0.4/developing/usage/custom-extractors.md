---
title: "Custom Extractors"
description: ""
project: "riak_kv"
project_version: 3.0.4
menu:
  riak_kv-3.0.4:
    name: "Custom Extractors"
    identifier: "usage_custom_extractors"
    weight: 113
    parent: "developing_usage"
toc: true
aliases:
  - /riak/3.0.4/dev/search/custom-extractors
  - /riak/kv/3.0.4/dev/search/custom-extractors
---

## The Extractor Interface

Creating a custom extract involves creating an Erlang interface that
implements two functions:

* `extract/1` - Takes the contents of the object and calls `extract/2` 
    with the same contents and an empty list
* `extract/2` - Takes the contents of the object and returns an Erlang
    [proplist](http://www.erlang.org/doc/man/proplists.html) with a
    single field name and a single value associated with that name

The following extractor shows how a pure text extractor implements those
two functions:

```erlang
-module(search_test_extractor).
-include("yokozuna.hrl").
-compile(export_all).

extract(Value) ->
    extract(Value, []).

extract(Value, Opts) ->
    FieldName = field_name(Opts),
    [{FieldName, Value}].

-spec field_name(proplist()) -> any().
field_name(Opts) ->
    proplists:get_value(field_name, Opts, text).
```

This extractor takes the contents of a `Value` and returns a proplist
with a single field name (in this case `text`) and the single value.
This function can be run in the Erlang shell. Let's run it providing the
text `hello`:

```erlang
> c(search_test_extractor).
%% {ok, search_test_extractor}

> search_test_extractor:extract("hello").

%% Console output:
[{text, "hello"}]
```

Upon running this command, the value `hello` would be indexed in Solr
under the fieldname `text`. If you wanted to find all objects with a
`text` field that begins with `Fourscore`, you could use the
Solr query `text:Fourscore*`, to give just one example.

## An Example Custom Extractor

Let's say that we're storing HTTP header packet data in Riak. Here's an
example of such a packet:

```
GET http://www.google.com HTTP/1.1
```

We want to register the following information in Solr:

Field name | Value | Extracted value in this example
:----------|:------|:-------------------------------
`method` | The HTTP method | `GET`
`host` | The URL's host | `www.google.com`
`uri` | The URI, i.e. what comes after the host | `/`

The example extractor below would provide the three desired
fields/values. It relies on the
[`decode_packet`](http://www.erlang.org/doc/man/erlang.html#decode_packet-3)
function from Erlang's standard library.

```erlang
-module(yz_httpheader_extractor).
-compile(export_all).

extract(Value) ->
    extract(Value, []).

%% In this example, we can ignore the Opts variable from the example
%% above, hence the underscore:
extract(Value, _Opts) ->
    {ok,
        {http_request,
         Method,
         {absoluteURI, http, Host, undefined, Uri},
         _Version},
        _Rest} = erlang:decode_packet(http, Value, []),
    [{method, Method}, {host, list_to_binary(Host)}, {uri, list_to_binary(Uri)}].
```

This file will be stored in a `yz_httpheader_extractor.erl` file (as
Erlang filenames must match the module name). Now that our extractor has
been written, it must be compiled and registered in Riak before it can
be used.

## Registering Custom Extractors

In order to use a custom extractor, you must create a compiled `.beam`
file out of your `.erl` extractor file and then tell Riak where that
file is located. Let's say that we have created a
`search_test_extractor.erl` file in the directory `/opt/beams`. First,
we need to compile that file:

```bash
erlc search_test_extractor.erl
```

To instruct Riak where to find the resulting
`search_test_extractor.beam` file, we'll need to add a line to an
`advanced.config` file in the node's `/etc` directory (more information
can be found in our documentation on [advanced]({{<baseurl>}}riak/kv/3.0.4/configuring/reference/#advanced-configuration)). Here's an
example:

```advancedconfig
[
  %% Other configs
  {vm_args, [
    {"-pa /opt/beams", ""}
  ]},
  %% Other configs
]
```

This will instruct the Erlang VM on which Riak runs to look for compiled
`.beam` files in the proper directory. You should re-start the node at
this point. Once the node has been re-started, you can use the node's
Erlang shell to register the `yz_httpheader_extractor`. First, attach to
the shell:

```bash
riak attach
```

At this point, we need to choose a MIME type for our extractor. Let's
call it `application/httpheader`. Once you're in the shell:

```erlang
> yz_extractor:register("application/httpheader", yz_httpheader_extractor).
```

If successful, this command will return a list of currently registered
extractors. It should look like this:

```erlang
[{default,yz_noop_extractor},
 {"application/httpheader",yz_httpheader_extractor},
 {"application/json",yz_json_extractor},
 {"application/riak_counter",yz_dt_extractor},
 {"application/riak_map",yz_dt_extractor},
 {"application/riak_set",yz_dt_extractor},
 {"application/xml",yz_xml_extractor},
 {"text/plain",yz_text_extractor},
 {"text/xml",yz_xml_extractor}]
```

If the `application/httpheader` extractor is part of that list, then the
extractor has been successfully registered.





