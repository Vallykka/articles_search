(defproject articles "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [mount "0.1.16"]
                 [cprop "0.1.17"]

                 [com.h2database/h2 "2.2.224"]
                 [com.github.seancorfield/honeysql "2.7.1295"]
                 [hikari-cp "3.2.0"]
                 [migratus "1.6.3"]
                 [org.clojure/java.jdbc "0.7.12"]

                 [compojure "1.7.1" :exclusions [ring/ring-codec]]
                 [ring/ring-core "1.13.1"]
                 [ring/ring-defaults "0.6.0"]
                 [ring/ring-jetty-adapter "1.13.1"]
                 [ring/ring-json "0.5.1" :exclusions [ring/ring-core]]
                 [clj-http "3.13.0" :exclusions [ring/ring-codec]]
                 [cheshire "6.0.0"]
                 [jumblerg/ring-cors "3.0.0"]
                 [metosin/malli "0.16.0"]

                 [org.clojure/tools.logging "1.2.4"]
                 [org.apache.logging.log4j/log4j-api "2.13.3"]
                 [org.apache.logging.log4j/log4j-core "2.13.3"]
                 [org.apache.logging.log4j/log4j-slf4j-impl "2.13.3"]
                 [com.fasterxml.jackson.core/jackson-databind "2.11.3"]]
  :repl-options {:init-ns articles.core}
  :source-paths ["src"]
  :main articles.core
  :profiles {:dev {:resource-paths ["resources"]}
             :test {:resource-paths ["test/resources"]}}
             :uberjar {:resource-paths ["resources"]})
