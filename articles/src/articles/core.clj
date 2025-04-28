(ns articles.core
  (:require [articles.config :refer [config]]
            [articles.db.db :refer [db]]
            [articles.db.migration :as migration]
            [articles.scopus :refer [scopus]]
            [articles.webserver :refer [webserver]]
            [clojure.tools.logging :as log]
            [mount.core :refer [start stop]])
  (:gen-class))

(defn run []
  (start [#'config #'db #'webserver #'scopus])
  (migration/migrate))

(defn shutdown []
  (stop [#'config #'db #'webserver #'scopus]))

(defn restart []
  (shutdown)
  (start [#'config #'db #'webserver #'scopus]))

(defn -main [& args]
  (run)
  (log/info "Application started"))
