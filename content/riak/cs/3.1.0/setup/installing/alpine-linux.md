---
title: "Alpine Linux"
title_supertext: "Installing on"
menu:
  riak_cs-3.1.0:
    name: "Alpine Linux"
    identifier: "install-alpine-linux"
    parent: "installing"
    weight: 100
project: "riak_cs"
project_version: 3.1.0
toc: true
aliases:
---

[configure cs]:          {{<baseurl>}}riak/cs/3.1.0/configuring

Riak KV can be installed on Alpine Linux using our Alpine package repo.

Riak CS has been tested on:

* Alpine Linux 3.16
* Alpine Linux 3.18

## Alpine Linux 3.16

To install Riak CS on Alpine Linux 3.16:

1. Add the Riak repository by running:

    ```bash
    echo https://files.tiot.jp/alpine/v3.16/main >> /etc/apk/repositories
    ```

2. Download and install the Riak repository public key by running:

    ```bash
    wget http://files.tiot.jp/alpine/alpine@tiot.jp.rsa.pub -O /etc/apk/keys/alpine@tiot.jp.rsa.pub
    ```

3. Update your list of packages by running:

    ```bash
    apk update
    ```

4. Install Riak:
    * For the latest version, run:

        ```bash
        apk add riak
        ```

    * For a specific version, run `apk add riak=w.x.y.z-r0` where `w.x.y` is the Riak CS version and `z` is the OTP version.

        For example, to install Riak CS 3.1.0 using OTP 25, run:

        ```bash
        apk add riak=3.1.0.25-r0
        ```

## Alpine Linux 3.18

To install Riak CS on Alpine Linux 3.18:

1. Add the Riak repository by running:

    ```bash
    echo https://files.tiot.jp/alpine/v3.18/main >> /etc/apk/repositories
    ```

2. Download and install the Riak repository public key by running:

    ```bash
    wget http://files.tiot.jp/alpine/alpine@tiot.jp.rsa.pub -O /etc/apk/keys/alpine@tiot.jp.rsa.pub
    ```

3. Update your list of packages by running:

    ```bash
    apk update
    ```

4. Install Riak:
    * For the latest version, run:

        ```bash
        apk add riak
        ```

    * For a specific version, run `apk add riak=w.x.y.z-r0` where `w.x.y` is the Riak CS version and `z` is the OTP version.

        For example, to install Riak CS 3.1.0 using OTP 25, run:

        ```bash
        apk add riak=3.1.0.25-r0
        ```

## What's Next?

Once you've completed installation of Riak CS and Riak, you're ready to
learn more about [configuring Riak CS][configure cs].
