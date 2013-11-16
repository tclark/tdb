(ns tdb.util
 (:require [clj-time.core :as tc]
           [clj-time.format :as fmt]
           ))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn time-to-string [t]
  (fmt/unparse (fmt/formatters :basic-date-time) t))

(defn string-to-time [s]
  (fmt/parse (fmt/formatters :basic-date-time) s))
    
(defn before? [ts1 ts2]
  (tc/before? (string-to-time ts1) (string-to-time ts2)))

(defn after? [ts1 ts2]
  (tc/before? (string-to-time ts1) (string-to-time ts2)))

(defn plus-one [t]
  (tc/plus (string-to-time t) (tc/seconds 1)))
