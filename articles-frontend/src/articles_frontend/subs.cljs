(ns articles-frontend.subs
  (:require [re-frame.core :as rf]
            [clojure.string :as str]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
  ::articles
  (fn [db]
    (:articles db)))

(rf/reg-sub
  ::current-page
  (fn [db]
    (:current-page db)))

(rf/reg-sub
  ::pages
  (fn [db]
    (into [] (range 1 (:last-page-number db)))))

(rf/reg-sub
  ::last-page?
  (fn [db]
    (= (:current-page db)
       (dec (:last-page-number db)))))


(rf/reg-sub
  ::current-term
  (fn [db]
    (:current-term db)))

(rf/reg-sub
  ::search-terms
  (fn [db]
    (str/join "," (:search-terms db))))

(rf/reg-sub
  ::disable-search-button?
  (fn [db]
    (->> db :search-terms (empty?))))

(rf/reg-sub
  ::error-text
  (fn [db]
    (or (->> db :error-message) "")))

(rf/reg-sub
  ::disable-error-note?
  (fn [db]
    (->> db :error-message (some?) (not))))