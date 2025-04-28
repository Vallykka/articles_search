(ns articles.db.migration
  (:require [articles.config :refer [config]]
            [articles.db.db :refer [db]]
            [migratus.core :as migratus]))

(defn- reset []
  (let [config (merge (:migratus config)
                      {:db db})]
    (migratus/reset config)))

(defn migrate []
  (let [config (merge (:migratus config)
                      {:db db})]
   (migratus/migrate config)))