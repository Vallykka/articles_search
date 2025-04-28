(ns articles.scopus
  (:require [articles.config :refer [config]]
            [cheshire.core :refer [parse-string]]
            [clj-http.client :as http-client]
            [mount.core :refer [defstate]]))

(defn health-check
  []
  (let [{:keys [url api-key]} (:scopus config)
        response (http-client/get url {:query-params {:query "test"
                                                      :apiKey api-key}})]
    (assert (= 200 (-> response :status)))))

(defstate scopus :start (health-check))


(defn find-articles
  [words]
  (let [{:keys [url api-key]} (:scopus config)
        response (http-client/get url {:query-params {:query  (format "all(%s)" words)
                                                      :apiKey api-key}})]
    (parse-string (:body response))))
