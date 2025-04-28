(ns articles.db.db
  (:require [articles.config :refer [config]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [hikari-cp.core :as pool]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [mount.core :refer [defstate]]))

(defstate ^{:on-reload :noop} db
          :start (let [datasource (pool/make-datasource (:db config))]
                   {:datasource datasource})
          :stop (-> db :datasource pool/close-datasource))


(defn insert-articles
  [values]
  (let [query (-> (h/insert-into :article)
                  (h/values values)
                  (sql/format {:pretty true}))]
    (try
     (jdbc/with-db-transaction [t-con db]
                               (jdbc/execute! t-con query))
     (catch Exception e
       (println (type e))
       (log/errorf "Exception occured while inserting articles: %s" (ex-message e))
       (throw e)))))

(def articles-table "ARTICLE")
(defn select
  [from limit]
  (jdbc/query db
              (-> (h/select :article.* [:info.row_count_estimate :total])
                  (h/from :article [:information_schema.tables :info])
                  (h/where [:= :info.table_name articles-table])
                  (h/offset from)
                  (h/limit limit)
                  (sql/format))))



