(ns attendance.google.sheets
  (:require [attendance.base64 :as b64]
            [clojure.java.io :as io]
            [environ.core :refer [env]])
  (:import
    (java.net URL URI)
    (java.security KeyStore)
    (java.io ByteArrayInputStream)
    (java.nio.charset StandardCharsets)
    (com.google.gdata.data.spreadsheet SpreadsheetFeed CellFeed)
    (com.google.gdata.client.spreadsheet SpreadsheetService)
    (com.google.api.client.util SecurityUtils)
    (com.google.api.client.googleapis.auth.oauth2 GoogleCredential GoogleCredential$Builder)
    (com.google.api.client.json.jackson2 JacksonFactory)
    (com.google.api.client.googleapis.javanet GoogleNetHttpTransport)))

(def sheet-feed-url
  (URL. "https://spreadsheets.google.com/feeds/spreadsheets/private/full"))

(defn client-id-from-env []
  (env :attendance-client-id))

(defn p12-key-from-env []
  (SecurityUtils/loadPrivateKeyFromKeyStore
   (KeyStore/getInstance "PKCS12")
   (ByteArrayInputStream. (b64/decode (env :attendance-p12-key)))
   "notasecret", "privatekey", "notasecret"))

(defn google-credentials []
  (-> (GoogleCredential$Builder.)
      (.setServiceAccountId (client-id-from-env))
      (.setServiceAccountPrivateKey (p12-key-from-env))
      (.setTransport (GoogleNetHttpTransport/newTrustedTransport))
      (.setJsonFactory (JacksonFactory/getDefaultInstance))
      (.setServiceAccountScopes ["https://spreadsheets.google.com/feeds"])
      .build))

(defn spreadsheets-feed []
  (let [service (SpreadsheetService. "google-spreadsheet")
        credentials (google-credentials)]
    (.setOAuth2Credentials service credentials)
    (.getFeed service sheet-feed-url SpreadsheetFeed)
    ))

(defn title [s]
  (-> s .getTitle .getPlainText))

(defn Worksheet->map [ws]
  {:name (title ws)
   :rows (.getRowCount ws)
   :cols (.getColCount ws)})

(defn Spreadsheet->map [sheet]
  {:name (title sheet)
   :worksheets (map Worksheet->map (.getWorksheets sheet))})


(defn reflect-spreadsheets []
  (map Spreadsheet->map (.getEntries (spreadsheets-feed))))

;; (reflect-spreadsheets)
