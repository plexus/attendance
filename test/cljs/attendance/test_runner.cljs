(ns attendance.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [attendance.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'attendance.core-test))
    0
    1))
