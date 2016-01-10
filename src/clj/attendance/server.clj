(ns attendance.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [attendance.google.sheets :refer [reflect-spreadsheets]])
  (:gen-class))

(defroutes routes
  (resources "/")
  (GET "/sheets" _
    {:body (reflect-spreadsheets)}))

(def http-handler
  (-> routes
      wrap-json-body
      wrap-json-response
      (wrap-defaults api-defaults)
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (run-jetty http-handler {:port port :join? false})))
