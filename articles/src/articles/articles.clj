(ns articles.articles
  (:require [articles.db.db :as db]
            [articles.scopus :as scopus]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.tools.logging :as log])
  (:import (org.h2.jdbc JdbcBatchUpdateException)))

(defn scopus-dto->article
  [rf]
  (fn
    ([] ())
    ([result] result)
    ([result input] (rf result
                        (-> input
                            (select-keys ["prism:publicationName"
                                          "prism:coverDate"
                                          "dc:creator"
                                          "prism:doi"])
                            (set/rename-keys {"prism:publicationName" :title
                                              "prism:coverDate"       :date
                                              "dc:creator"            :author
                                              "prism:doi"             :doi}))))))
(def take-transformed-articles (comp (filter #(some? (get % "prism:doi")))
                                     (take 10)
                                     scopus-dto->article))


(defn process-scopus-articles
  "Converts each Scopus DTO to app entity, collects unique articles"
  [rf]
  (fn
    ([] ())
    ([result] result)
    ([result input] (->> (get-in input ["search-results" "entry"])
                         (transduce take-transformed-articles (fn [acc arg]
                                                                (assoc acc (:doi arg) arg)) {})
                         (rf result)))))

(defn store-to-db
  [articles]
  (try
    (db/insert-articles articles)
    (catch JdbcBatchUpdateException e
      (->> articles
           (partition 1 1)
           (map #(try
                   (db/insert-articles %)
                   (catch JdbcBatchUpdateException e
                     (log/error "Article has been stored already"))))))))

(defn load-articles
  "Sends request to Scopus API for each search term, takes articles with unique DOI, then stores found articles to db"
  [words]
  (try
    (let [res (map #(scopus/find-articles %) (some->> words :words))]
      (->> res
           (transduce process-scopus-articles (fn [acc arg] (merge acc arg)) {})
           (vals)
           (store-to-db)
           (keep identity)
           (flatten)
           (reduce + 0)))
    (catch Exception e
      (throw (ex-info (format "Exception occurred while inserting articles for terms: %s" (str/join "," words)) {})))))


(defn articles->dto
  [articles]
  (-> {}
      (assoc :total (-> articles first (get :total 0)))
      (assoc :articles (->> articles
                            (mapv #(select-keys % [:title :author :date :doi]))))))

(defn get-articles
  "Retrieves limited count of articles for requested page"
  [page limit]
  (let [from (if (> page 1)
               (* (dec page) limit)
               0)]
    (-> (db/select from limit)
        (articles->dto))))
