(ns tdb.util-test
  (:require [clojure.test :refer :all]
            [tdb.util :refer :all]
            [clj-time.core :as tm]
            [clj-time.format :as fmt]
            ))

(def sample-time (tm/date-time 2013 6 15 14 00))
(def sample-string "20130615T140000.000Z")
(def later-string  "20130615T150000.000Z")
(def plus-one-string "20130615T140001.000Z")

(deftest test-time-to-string
  (testing "Test time-to-string"
           (is (= (time-to-string sample-time) sample-string) 
               "time-to-string test failed")))


(deftest test-string-to-time
  (testing "Test string-to-time"
           (is (= (string-to-time sample-string) sample-time) 
               "string-to-time test failed")))

(deftest test-time-inverses
  (testing "time-to-string and string-to-time should be inverses"
           (is (= (time-to-string (string-to-time sample-string)) sample-string))
           (is (= (string-to-time (time-to-string sample-time)) sample-time ))))


(deftest test-plus-one
  (testing "Testing plus-one"
           (is (= (plus-one sample-string) plus-one-string))))

(deftest test-before
  (testing "Test before?"
           (is (before? sample-string later-string))
           (is (= false (before? later-string sample-string)))
           ))

(deftest test-after
  (testing "Test after?"
           ; note that the second argument to after? is the one
           ; expected to be later than the first
           (is (after? sample-string later-string)) 
           (is (= false (after? later-string sample-string)))
           ))


