(ns urlshort.store)

(defn lookup-hash
  "Looks up hash in store, heavily depends on k and v both being unique"
  [hash store]
  (let [store (clojure.set/map-invert store)]
    {(store hash) hash}))

(defn lookup-url
  "Looks up redirect URL in store"
  [url store]
  {url (store url)})


