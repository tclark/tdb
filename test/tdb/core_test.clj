(ns tdb.core-test
  (:require [clojure.test :refer :all]
            [tdb.core :refer :all]
            [clj-time.core :as time]))


(def times [(time/date-time 2013 6 15 14 00)
                  (time/date-time 2013 6 16 11 14)
                  (time/date-time 2013 7 1  22 5)
                  ])
(def new-open-record #{{:type :open :id 1 :vtime (get times 0) :ttime (get times 0)}})
(def updated-record #{
                 {:type :update :label :foo :value "bar" :vtime (get times 1) :ttime (get times 1)}
                 {:type :open :id 1 :vtime (get times 0) :ttime (get times 0)}
                  })
(def bigger-record #{
                 {:type :update :label :foo :value "baz" :vtime (get times 2) :ttime (get times 2)}
                 {:type :update :label :foo :value "bar" :vtime (get times 1) :ttime (get times 1)}
                 {:type :open :id 1 :vtime (get times 0) :ttime (get times 0)}
                  })
(def closed-record #{
                 {:type :close :label :reason :value "test" :vtime (get times 1) :ttime (get times 1)}
                 {:type :open :id 1 :vtime (get times 0) :ttime (get times 0)}
                  })

  
(deftest test-open-record
  (testing "Test open-record"
  (is (= (open-record 1 (get times 0) (get times 0)) new-open-record) "Open record failed")))

(deftest test-amend
  (testing "Test amend-record"
           (is (= (amend-record new-open-record (get times 1) (get times 1) :update :foo "bar")
                  updated-record) "Update amendment failed") 
             
           (is (= (amend-record new-open-record (get times 1) (get times 1) :close :reason "test")
                  closed-record) "Close amendment failed") 
           (is (= (amend-record closed-record (get times 1) (get times 1) :update :foo "bar")
                  closed-record) "Amend error: Performed amendment of a closed record")
   )
)

(deftest test-closed
  (testing "Test closed?"
     (is (closed? closed-record) "Closed test failed")
     (is (not (closed? new-open-record)) "Closed test failed on open record")
     (is (not (closed? updated-record)) "Closed test failed on open record")
  )
)

(deftest test-open
  (testing "Test open?"
     (is (open? new-open-record) "Open test failed")
     (is (open? updated-record) "Open test failed on open record")
     (is (not (open? closed-record)) "Open test failed o closed record")
  )
)

(deftest test-update
  (testing "Test update-record"
    (is (= (update-record new-open-record (get times 1) (get times 1) :foo "bar")
           updated-record) "Update failed")
    (is (= (update-record closed-record (get times 1) (get times 1) :foo "bar")
           closed-record) "Update error: Performed update of closed record")
  )
)

(deftest test-close
  (testing "Test close-record"
     (is (= (close-record new-open-record (get times 1) (get times 1) "test")
              closed-record) "Close record failed")
     (is (= (close-record closed-record (get times 1) (get times 1) "test")
              closed-record) "Close should be idempotent")
  )
 )

(deftest test-filter
  (testing "Test temporal filter"
           (is (= (temporal-filter bigger-record (get times 2) :vtime) bigger-record) "Temporal filter failed with vtime")
           (is (= (temporal-filter bigger-record (get times 1) :vtime) updated-record) "Temporal filter failed with vtime")
           (is (= (temporal-filter bigger-record (get times 2) :ttime) bigger-record) "Temporal filter failed with ttime")
           (is (= (temporal-filter bigger-record (get times 1) :ttime) updated-record) "Temporal filter failed with ttime")
           (is (= (valid-filter bigger-record (get times 1)) updated-record) "Valid filter failed")
           (is (= (transaction-filter bigger-record (get times 1)) updated-record) "Valid filter failed")
  )
)

(deftest test-get-latest
  (testing "Test get-latest-update"
    (is (= (get-latest-update bigger-record :foo (get times 2) :vtime) 
      {:type :update :label :foo :value "baz" :vtime (get times 2) :ttime (get times 2)})
      "Get-latest failed")
    (is (= (get-latest-update bigger-record :foo (get times 1) :vtime) 
      {:type :update :label :foo :value "bar" :vtime (get times 1) :ttime (get times 1)})
      "Get-latest failed")
  )
)
