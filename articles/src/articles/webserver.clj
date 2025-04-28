(ns articles.webserver
  (:require [articles.config :refer [config]]
            [articles.routes :as routes]
            [mount.core :refer [defstate]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defstate ^{:on-reload :noop} webserver
          :start (let [{jetty-config :server} config]
                   (-> routes/app
                       (run-jetty jetty-config)))
          :stop (.stop webserver))

