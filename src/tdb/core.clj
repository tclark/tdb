(ns tdb.core
 (:require [clj-time.core :as time]))

(defn uuid[] (str (java.util.UUID/randomUUID)))

(defn open-record [id vtime]
 [{:type :open :id id :vtime vtime :ttime (time/now)}])

(defn amend-record [record vtime type label value]
 (let [amendment { :type type :label label :value value :ttime (time/now) :vtime vtime}]
    (vec (cons amendment record))))

(defn update-record [record vtime label value]
 (amend-record record vtime :update label value))

(defn closed? [record]
  (some #(= (:type %) :close) record))

(defn open? [record]
  (not (closed? record))) 

(defn close-record [record vtime reason]
  (if (open? record)
    (amend-record record vtime :close :reason reason)
    record))

(defn temporal-filter [record qtime timetype]
  (vec (filter  #(time/before? (timetype %) qtime) record)))

(defn valid-filter [record qtime]
  (temporal-filter record qtime :vtime))

(defn transaction-filter [record qtime]
  (temporal-filter record qtime :ttime))
