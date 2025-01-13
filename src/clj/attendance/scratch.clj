(ns attendance.scratch
  (:require [clojure.pprint :refer [pprint]]))

(defn wrap-pprint [handler]
  (fn [req]
    (let [resp (handler req)]
      (print ">>")
      (pprint req)
      (print "<<")
      (pprint resp)
      resp)))

(defn wrap-clj-http-pprint [handler]
  (fn [req]
    (clj-http.client/with-middleware
      (into clj-http.client/default-middleware [#'wrap-pprint])
      (let [resp (handler req)]
        resp))))
