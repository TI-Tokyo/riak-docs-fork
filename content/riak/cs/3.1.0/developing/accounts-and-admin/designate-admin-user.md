---
title: "Designating an Admin User"
description: ""
menu:
  riak_cs-3.1.0:
    name: "Designating an Admin User"
    identifier: "develop_accounts_admin_designate_admin_user"
    weight: 200
    parent: "develop_accounts_admin"
project: "riak_cs"
project_version: "3.1.0"
aliases:
  - /riakcs/3.1.0/cookbooks/Designating-an-Admin-User/
  - /riak/cs/3.1.0/cookbooks/Designating-an-Admin-User/
  - /riak/cs/3.1.0/cookbooks/designate-admin-user/
---

Once a user has been created, you should designate a user as an admin by
editing and replacing the `admin_key` and `admin_secret` in `app.config`
with the user's credentials. Once this is done, do not forget to update
the same credentials in the Stanchion `app.config` as well.

{{% note title="Note on the admin role" %}}
This is a powerful role and gives the designee administrative capabilities
within the system. As such, caution should be used to protect the access
credentials of the admin user.
{{% /note %}}
