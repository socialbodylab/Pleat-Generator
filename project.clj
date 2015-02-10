(defproject sbltrig "0.3.2"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                           [quil "2.2.5" :exclusions [org.clojure/clojure]]]
            :main ^:skip-aot sbltrig.core
            :aot [sbltrig.core]
            :plugins [[lein-pprint "1.1.2"]
                      [lein-gorilla "0.3.4"]
                      [lein-ancient "0.6.2"]]
            :profiles {:uberjar {:aot :all}})