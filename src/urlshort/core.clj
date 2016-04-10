(ns urlshort.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [urlshort.hash :as urlhash]
            [urlshort.store :as urlstore]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :as hiccup]
            [clojure.data.json :as json]
            [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]])
  (:import org.apache.commons.validator.UrlValidator))

;; stored has {url hash} but both would work normally since both
;; should be unique
(def store {})

(defn save-hash!
  [store url hash]
  "Saves hash in the store, returns hash"
  (def store (assoc store url hash))
  hash)

(defn valid-url?
  "Returns true or false if url is a valid url"
  [url]
  (.isValid (UrlValidator. (into-array ["http" "https"])) url))

(defn route-url [url]
  (let [short ((urlstore/lookup-url url store) url)
        short (if (and (valid-url? url) (nil? short))
                (save-hash! store url (urlhash/hash url store))
                short)
        resp (if short
               {:url url :short short}
               (if (nil? url) ;; different errors...
                 {:err "Invalid Request"}
                 {:err "Invalid URL to shorten"}))]
    {:status 200 :headers {"Content-Type" "application/json"} :body (json/write-str resp)}))

(defn route-redirect [hash]
  ;; TODO if there is no hash in store, do something else
  (let [url (first (first (urlstore/lookup-hash hash store)))]
    {:status 302 :headers {"Location" url} :body ""}))

(defn route-home []
  (hiccup/html [:html
                [:body
                 [:h1 "url shortener"]]]))

(defroutes app
  (GET "/" [] (route-home))
  (GET "/new" {url :params} (route-url (get url :url)))
  (GET "/:url-id" [url-id] (route-redirect url-id)))

(defn -main [& port]
  (let [port (Integer. (or port (env :port) 3000))]
    (println "HTTP Server starting on:" port)
    (run-server (wrap-defaults #'app site-defaults) {:port port})))

