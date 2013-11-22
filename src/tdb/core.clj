(ns tdb.core
 (:require [tdb.util :as utl]))


(defn id [record]
  (:id (first (filter #(= (:type %) :open) record))))

(defn open-record [id vtime ttime] 
  "Creates a new temporal record using the supplied id and valid timestamp"
  #{{:type :open :id id :vtime vtime :ttime ttime}})

(defn closed? [record]
  "Used to tell if a record is closed or not.  Returns
   the closing amendment if there is one or nil otherwise."
  (some #(= (:type %) :close) record))

(defn open? [record]
  "Returns true if the record is open for updates, false otherwise."
  (not (closed? record))) 

(defn amend-record [record vtime ttime amedmenttype label value]
  "A general function to amend a record.  We don't typically call this function
   directly.  Instead we call special case methods like update-record or close-record.
   If you want to use a special type of amendment you could call this or write a
   function that calls amend-record to produce your admendment."
  (if (open? record)
    (let [amendment { :type amedmenttype :label label :value value :ttime ttime :vtime vtime}]
      (set (cons amendment record)))
    record))

(defn update-record [record vtime ttime label value]
  "Produces an update amendment to a record that either
   adds a new label-value pair or overrides a previous one."
  (amend-record record vtime ttime :update label value))

(defn close-record [record vtime ttime reason]
  "Closes a record by adding a close amendment.  A closed
   record cannot be amended further."
    (amend-record record vtime ttime :close :reason reason))

(defn temporal-filter [record qtime timetype]
  "Filters a record's items by removing all items
   with a timestamp after qtime.  Filters against 
   the timetype, which may be :vtime or :ttime."
  (let [mytime (utl/plus-one qtime)]
    (set (filter  #(utl/before? (timetype %) mytime) record))))

(defn valid-filter [record qtime]
  (temporal-filter record qtime :vtime))

(defn transaction-filter [record qtime]
  (temporal-filter record qtime :ttime))

(defn get-latest-update [record label qtime timetype]
  "Given a temporal record, searches for the most recently added update
   relative to the query time and timetype (vtime or ttime) having the 
   supplied label.  Returns the matching update or nil of no match is found"
  (last (sort-by timetype (filter #(= (:label %) label) (temporal-filter record qtime timetype)))))
