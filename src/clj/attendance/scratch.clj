(ns attendance.scratch)

(defn instrument-mw [handler desc]
  (fn [req]
    (let [resp (handler req)]
      (println desc)
      (pprint resp)
      resp)))
