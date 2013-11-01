# tdb

tdb is a clojure library for creating and working with temporal records.

For our purposes, a temporal record is a data record that has the 
following properties:

1.  The record is append-only.  Exisiting data is never deleted or
changed.  Instead, the record is amended by adding a new entry that
may add new information to the record or override previous information.

2.  Every data item is marked with two timestamps"
    i.  a valid time,  the time at which the item become effective;
   ii.  a transaction time, the time at which the item was entered. 

## Usage

FIXME

## License

Copyright © 2013 Tom Clark <tom.clark@op.ac.nz>

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
