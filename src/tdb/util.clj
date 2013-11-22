(ns tdb.util
 (:require [clj-time.core :as tc]
           [clj-time.format :as fmt]
           ))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn time-to-string [t]
  "Produces a formatted text string representation of a clj-time date-time."
  (fmt/unparse (fmt/formatters :basic-date-time) t))

(defn string-to-time [s]
  "Takes a formatted string representing a datetime and returns a clj-time date-time" 
  (fmt/parse (fmt/formatters :basic-date-time) s))
    
(defn before? [ts1 ts2]
  "Takes two strings representing datetimes.  Returns true if the first 
   occurs before the second, false otherwise."
  (tc/before? (string-to-time ts1) (string-to-time ts2)))

(defn after? [ts1 ts2]
  "Takes two strings representing datetimes.  Returns true if the second
   occurs after the first, false otherwise."
  (tc/before? (string-to-time ts1) (string-to-time ts2)))

(defn plus-one [t]
  "Takes a formatted datetime string.  Returns a string representing the time
   occurring one second after."
  (time-to-string (tc/plus (string-to-time t) (tc/seconds 1))))
