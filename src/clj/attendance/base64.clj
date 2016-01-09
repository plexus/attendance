(ns attendance.base64
  (:import [java.util Base64]))


(defn encode [bytes]
  (.encode (Base64/getEncoder) bytes))

(defn decode [string]
  (.decode (Base64/getDecoder) string))
