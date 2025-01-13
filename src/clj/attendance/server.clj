(ns attendance.server
  (:require [clojure.java.io :as io]

            [compojure.core           :refer [GET ANY POST PUT DELETE defroutes]]
            [compojure.route          :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.gzip     :refer [wrap-gzip]]
            [ring.middleware.json     :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.logger   :refer [wrap-with-logger]]
            [ring.adapter.jetty       :refer [run-jetty]]
            [ring.util.response       :refer [resource-response redirect]]

            [cemerick.friend          :as friend]

            [environ.core             :refer [env]]
            [attendance.google.sheets :refer [reflect-spreadsheets]]
            [attendance.auth          :as auth])
  (:gen-class))

(defroutes routes
  (GET "/" _
    {:status 200
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (io/input-stream (io/resource "public/index.html"))})
  (GET "/sheets" _
    (friend/authorize #{:auth/admin} {:body (reflect-spreadsheets)}))
  (friend/logout
   (ANY "/logout" _
     (redirect "/")))
  (resources "/"))

(def http-handler
  (-> routes
      wrap-json-body
      wrap-json-response
      (friend/authenticate auth/friend-config)
      (wrap-defaults site-defaults)
      wrap-with-logger
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (run-jetty http-handler {:port port :join? false})))
