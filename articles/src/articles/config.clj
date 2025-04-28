(ns articles.config
  (:require [cprop.core :refer [load-config]]
            [cprop.source :as source]
            [mount.core :refer [defstate]]))

(defstate config
          :start (let [config (load-config :merge [(source/from-resource)])]
                      (if-some [api-key (System/getenv "SCOPUS_API_KEY")]
                               (assoc-in config [:scopus :api-key] api-key)
                               config)))

