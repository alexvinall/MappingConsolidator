# MappingConsolidator

Helper API for consolidating collections of linked Strings into aggregated sets

This helper accepts a collection of paired strings (a key and a corresponding value), 
and consolidates them into a map of unique keys and the associated set of values.

The helper is designed to handle any cardinality of keys to values:
 # One-to-one
 # One-to-many
 # Many-to-one

For example, given the following input:

A -> 1 
A -> 2 
B -> 3 
C -> 4 
D -> 4 

The helper will return the following map:

[A]:1,2 
[B]:3 
[C+D]:4