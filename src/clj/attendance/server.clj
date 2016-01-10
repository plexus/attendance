(ns attendance.server
  (:require [clojure.java.io :as io]

            [compojure.core           :refer [GET defroutes]]
            [compojure.route          :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip     :refer [wrap-gzip]]
            [ring.middleware.json     :refer [wrap-json-body wrap-json-response]]
            [ring.adapter.jetty       :refer [run-jetty]]
            [ring.util.response       :refer [resource-response]]

            [cemerick.friend          :as friend]

            [environ.core             :refer [env]]
            [attendance.google.sheets :refer [reflect-spreadsheets]]
            [attendance.auth          :as auth])
  (:gen-class))

(defroutes routes
  (GET "/" _
       (if (friend/authorized? ::admin)
         (resource-response "index.html")
         (resource-response "login.html")))
  (GET "/sheets" _
    (if (friend/authorized? ::admin)
      {:body (reflect-spreadsheets)}
      {:status 403
       :body {:error "Not authorized to access this resource"
              }}))
  (resources "/"))

(def http-handler
  (-> routes
      wrap-json-body
      wrap-json-response
      (friend/authenticate auth/friend-config)
      (wrap-defaults api-defaults)
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (run-jetty http-handler {:port port :join? false})))
