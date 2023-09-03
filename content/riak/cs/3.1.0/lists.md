---
title: "List Tests"
description: ""
project: "riak_cs"
project_version: "3.1.0"
draft: true
aliases:
---

This is where I test that lists work as expected.

Should be 1-5:

1. Item A
2. Item B
3. Item C
4. Item D
5. Item E

## Normal numbered list

Should be 1-5:

1. Item A
2. Item B
3. Item C
4. Item D
5. Item E

## Normal numbered list with long test

Should be 1-5:

1. Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A Item A
2. Item B
3. Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C Item C
4. Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D Item D 
5. Item E
6. Item F
7. Item G
8. Item H
9. Item I
10. Item J

## Simple nested numbered list

Should be 1-3, and 1-1 to 1-2:

1. Item A
    1. Item A-A
    2. Item A-B
2. Item B
3. Item C

## Complex nested numbered list

1. Item A
    1. Item A-A
    2. Item A-B
2. Item B
    1. Item B-A
        1. Item B-A-A
        2. Item B-A-B
    2. Item B-B
        1. Item B-B-A
        2. Item B-B-B
3. Item C
    1. Item C-A
        1. Item C-A-A
        2. Item C-A-B
    2. Item C-B

## Numbered list with a split by P

Should be 1-5:

1. Item A
2. Item B

Breaking paragraph

1. Item C
2. Item D
3. Item E

## Numbered list with a split by Note

Should be 1-5:

1. Item A
2. Item B

{{% note title="Breaking note" %}}
This should not matter.
{{% /note %}}

1. Item C
2. Item D
3. Item E

## Complex nested numbered list with breaks

This is broken, but is a markdown limitation.

1. Item A
    1. Item A-A

    {{% note title="Breaking note after A-A" %}}
This should not matter.
    {{% /note %}}

    2. Item A-B

2. Item B
    1. Item B-A
        1. Item B-A-A

        {{% note title="Breaking note after B-A-A" %}}
This should not matter.
        {{% /note %}}

        2. Item B-A-B

        {{% note title="Breaking note after B-A-B" %}}
This should not matter.
        {{% /note %}}

        {{% note title="Second breaking note after B-A-B" %}}
This should not matter.
        {{% /note %}}

    2. Item B-B
        1. Item B-B-A
        2. Item B-B-B
3. Item C
    1. Item C-A
        1. Item C-A-A
        
            Second paragraph for C-A-A

        2. Item C-A-B
    2. Item C-B
