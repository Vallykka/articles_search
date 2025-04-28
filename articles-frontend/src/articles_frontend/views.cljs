(ns articles-frontend.views
  (:require [articles-frontend.events :as events]
            [articles-frontend.subs :as subs]
            [re-frame.core :as rf]))

(defn search-input
  []
  [:div
   [:textarea {:readOnly true
               :value @(rf/subscribe [::subs/search-terms])}]
   [:input.input {:placeholder "terms"
            :type        "text"
            :style       {:height 30
                          :width  500}
            :value @(rf/subscribe [::subs/current-term])
            :on-change   #(rf/dispatch [::events/set-current-term (.. % -target -value)])}]
   [:br]
   [:button.button {:on-click #(rf/dispatch [::events/add-to-search-terms])}
    "Add to search"]
   [:button.button {:class (when @(rf/subscribe [::subs/disable-search-button?]) "disabled")
                    :on-click #(rf/dispatch [::events/search])}
    "Search"]])

(defn pagination
  []
  (let [current-page @(rf/subscribe [::subs/current-page])]
   [:div
    [:div.pagination
     [:a {:class (when (<= current-page 1) "disabled")
          :on-click #(rf/dispatch [::events/change-page (dec current-page)])}
      "<-"]
     (for [page @(rf/subscribe [::subs/pages])]
       [:a {:key (str page)
            :style {:color (if (= current-page page)
                             "red"
                             "black")}
            :on-click #(rf/dispatch [::events/change-page page])}
        (str page)])
     [:a {:class (when @(rf/subscribe [::subs/last-page?])
                   "disabled")
          :on-click #(rf/dispatch [::events/change-page (inc current-page)])}
      "->"]]]))

(defn table
  []
  [:div {:height 500}
   [:h1 "Articles"]
   [:table
    [:thead
     [:tr
      [:th "Title"]
      [:th "Author"]
      [:th "Date"]
      [:th "Doi"]]]
    [:tbody
     (for [article @(rf/subscribe [::subs/articles])]
       [:tr {:key (get article :doi)}
        [:td (get article :title)]
        [:td (get article :author)]
        [:td (get article :date)]
        [:td (get article :doi)]])]]
   [pagination]])

(defn error-note
  []
  [:div {:class (when @(rf/subscribe [::subs/disable-error-note?]) "hideNote")}
   [:a {:on-click #(rf/dispatch [::events/remove-error-message])}
    "Hide"]
   [:textarea {:readOnly true
               :value @(rf/subscribe [::subs/error-text])}]])

(defn main-panel
  []
  [:div
   [:h1
    "Search Articles by term"]
   [search-input]
   [table]
   [error-note]])
