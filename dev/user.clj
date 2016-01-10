(ns user
  (:require [attendance.server]
            [ring.middleware.reload :refer [wrap-reload]]
            [figwheel-sidecar.repl-api :as figwheel]
            [clojure.java.shell]))


(def http-handler
  (wrap-reload #'attendance.server/http-handler))

(defn run []
  (figwheel/start-figwheel!))

(def browser-repl figwheel/cljs-repl)
