(ns attendance.auth
  (:require
   [friend-oauth2.workflow :as oauth2]
   [friend-oauth2.util     :refer [format-config-uri]]
   [environ.core           :refer [env]]))


(def oauth2-client-config
  {:client-id     (env :friend-oauth2-client-id)
   :client-secret (env :friend-oauth2-client-secret)
   :callback      {:domain (env :home-url)
                   :path "/oauth2callback"}})

(def oauth2-uri-config
  {:authentication-uri {:url "https://accounts.google.com/o/oauth2/auth"
                        :query {:client_id (:client-id oauth2-client-config)
                                :response_type "code"
                                :redirect_uri (format-config-uri oauth2-client-config)
                                :scope "email"}}

   :access-token-uri {:url "https://accounts.google.com/o/oauth2/token"
                      :query {:client_id (:client-id oauth2-client-config)
                              :client_secret (:client-secret oauth2-client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (format-config-uri oauth2-client-config)}}})

(defn friend-credential-fn
  "Upon successful authentication with the third party, Friend calls
  this function with the user's token. This function is responsible for
  translating that into a Friend identity map with at least the :identity
  and :roles keys. How you decide what roles to grant users is up to you;
  you could e.g. look them up in a database.

  You can also return nil here if you decide that the token provided
  is invalid. This could be used to implement e.g. banning users.

  This example code just automatically assigns anyone who has
  authenticated with the third party the nominal role of ::user."
  [token]
    {:identity token
     :roles #{::user}})

(def friend-config
  {:allow-anon? false
   :workflows [(oauth2/workflow
                {:client-config oauth2-client-config
                 :uri-config oauth2-uri-config
                 :credential-fn friend-credential-fn})]})
