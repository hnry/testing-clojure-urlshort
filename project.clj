(defproject urlshort "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.0"]
                 [http-kit "2.2.0-alpha1"]
                 [environ "1.0.2"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.json "0.2.6"]
                 [commons-validator "1.5.0"]
                 [ring/ring-defaults "0.2.0"]]
  :main ^:skip-aot urlshort.core
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler urlshort.core/app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
