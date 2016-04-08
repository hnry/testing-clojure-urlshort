(ns urlshort.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [urlshort.hash :as urlhash]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :as hiccup]
            [clojure.data.json :as json]
            [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]]))

(def store #{})

(defn valid-url? [url]
  "Returns true or false if url is a valid url"
  true)

(defn save-hash [hash store]
  "Saves hash somewhere, returns hash"
  hash)

(defn lookup-hash [url store]
  "Looks up hash in store by URL"
  nil)

(defn lookup-url [hash store]
  "Looks up redirect URL by hash")

(defn route-url [url]
  (let [short (if (valid-url? url)
                (if (nil? (lookup-hash url store))
                  (save-hash (urlhash/hash url store) store)))
        resp (if short {:url url :short short} {:err "Invalid URL to shorten"})]
    {:status 200 :headers {"Content-Type" "application/json"} :body (json/write-str resp)}))

(defn route-redirect [url-hash]
  "Redirect")

(defn route-home []
  "Hello whats up!")

(defroutes app
  (GET "/" [] (route-home))
  (GET "/:url-id" [url-id] (route-redirect url-id))
  (GET "/new/:url" [url] (route-url url)))

(defn -main [& port]
  (let [port (Integer. (or port (env :port) 3000))]
    (println "HTTP Server starting on:" port)
    (run-server (wrap-defaults app site-defaults) {:port port})))

