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

(defn close-record [record vtime reason]
  (if (open? record)
    (amend-record record vtime :close :reason reason)
    record))

(defn closed? [record]
  (some #(= (:type %) :close) record))

(defn open? [record]
  (not (closed? record))) 
