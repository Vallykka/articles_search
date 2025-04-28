(ns articles-frontend.core
  (:require [articles-frontend.events :as events]
            [articles-frontend.views :as views]
            [articles-frontend.config :as config]
            [day8.re-frame.http-fx]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root)
    (rdom/render [views/main-panel] root)))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
