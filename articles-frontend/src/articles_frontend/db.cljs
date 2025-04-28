(ns articles-frontend.db)

(def default-db
  {:name "Articles"
   :articles [{:title "Test"
               :author "Ivan Ivanov"
               :date "2021-03-03"
               :doi "100.03/doi.org"}]
   :total-count 20
   :count-per-page 10
   :current-page 1
   :last-page-number 2
   :search-terms []
   :current-term nil})
