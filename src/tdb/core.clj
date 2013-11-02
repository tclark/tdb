(ns tdb.core
 (:require [clj-time.core :as time]))

(defn uuid[] (str (java.util.UUID/randomUUID)))

(defn open-record [id vtime]
  "Creates a new temporal record using the supplied id and valid timestamp"
  [{:type :open :id id :vtime vtime :ttime (time/now)}])

(defn closed? [record]
  "Used to tell if a record is closed or not.  Returns
   the closing amendment if there is one or nil otherwise."
  (some #(= (:type %) :close) record))

(defn open? [record]
  "Returns true if the record is open for updates, false otherwise."
  (not (closed? record))) 

(defn amend-record [record vtime type label value]
  "A general function to amend a record.  We don't typically call this function
   directly.  Instead we call special case methods like update-record or close-record.
   If you want to use a special type of amendment you could call this or write a
   function that calls amend-record to produce your admendment."
  (if (open? record)
    (let [amendment { :type type :label label :value value :ttime (time/now) :vtime vtime}]
      (vec (cons amendment record)))
    record))

(defn update-record [record vtime label value]
  "Produces an update amendment to a record that either
   adds a new label-value pair or overrides a previous one."
  (amend-record record vtime :update label value))

(defn close-record [record vtime reason]
  "Closes a record by adding a close amendment.  A closed
   record cannot be amended further."
    (amend-record record vtime :close :reason reason))

(defn temporal-filter [record qtime timetype]
  "Filters a record's items by removing all items
   with a timestamp after qtime.  Filters against 
   the timetype, which may be :vtime or :ttime."
  (vec (filter  #(time/before? (timetype %) qtime) record)))

(defn valid-filter [record qtime]
  (temporal-filter record qtime :vtime))

(defn transaction-filter [record qtime]
  (temporal-filter record qtime :ttime))
