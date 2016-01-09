(ns attendance.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(defonce app-state (r/atom {:text "Hello Chestnut!"}))

(defn root []
  [:h1 (:text @app-state)])

(r/render-component [root]
                    (.-body js/document))
