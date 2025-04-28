(ns articles-frontend.events
  (:require [articles-frontend.db :as db]
            [articles-frontend.http :as http]
            [re-frame.core :as rf]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
  ::set-current-term
  (fn [db [_ current-term]]
    (assoc db :current-term current-term)))

(rf/reg-event-db
  ::add-to-search-terms
  (fn [db [_]]
    (let [current-term (:current-term db)]
      (-> db
          (update :search-terms conj current-term)
          (assoc :current-term nil)))))

(rf/reg-event-fx
  ::search
  (fn [{:keys [db] :as cofx} [_]]
    (let [terms (:search-terms db)
          current-page (:current-page db)]
      (-> cofx
          (update :fx conj
                  [:dispatch [::http/search
                              :find-url
                              {:words terms}
                              [:articles-frontend.events/change-page current-page]]])))))

(rf/reg-event-fx
  ::on-loaded-articles
  (fn [{:keys [db]} [_ articles]]
    (let [total-count (:total articles)
          per-page (:count-per-page db)
          pages-count (if (<= total-count per-page)
                        (quot total-count per-page)
                        (inc (quot total-count per-page)))]
      {:db (-> db
               (assoc :articles (:articles articles))
               (assoc :total-count total-count)
               (assoc :last-page-number pages-count))})))

(rf/reg-event-fx
  ::change-page
  (fn [{:keys [db]} [_ page]]
    (let [per-page (:count-per-page db)]
      {:db (assoc db :current-page page)
       :fx [[:dispatch
             [::http/search
              :articles-url
              {:page page
               :limit per-page}
              [:articles-frontend.events/on-loaded-articles]]]]})))

(rf/reg-event-db
  ::remove-error-message
  (fn [db _]
    (assoc db :error-message nil)))