(ns urlshort.hash
  (:require [urlshort.store :refer [lookup-hash]]))

;; 62 characters in `char-src` sequence
;; with a min/max of 1/5 => 1 million combinations
;; if it all get used, `gen` will inifinite loop
;; TODO
;; detect when we have run out of combinations
;; purge oldest (last visited) hash to make room

(def char-src
  (into [] (concat
            (range 0 10)
            (map char (range (int \A) (inc (int \Z))))
            (map char (range (int \a) (inc (int \z)))))))

(defn gen
  "Generates random hash"
  ([]
   (let [min 1
         max 5
         len (+ (.nextInt (java.util.Random.) (+ (- max min) 1)) min)]
     (gen "" len)))
  ([hash len]
   (if (= (count hash) len)
     hash
     (gen (str hash (char-src (.nextInt (java.util.Random.) (count char-src)))) len))))

(defn hash
  "gens a hash & checks to make sure this hash is unique to store's values (k,v)"
  [url store]
  (let [h (gen)]
    (if (lookup-hash h store) h (hash url store))))
