(ns articles.routes
  (:require [articles.articles :as articles]
            [articles.validation :as v]
            [clojure.tools.logging :as log]
            [compojure.core :refer :all]
            [compojure.route :as compojure-route]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [jumblerg.middleware.cors :as cors]
            [ring.middleware.json :refer :all]))

(defroutes app-routes
           (POST "/find" req (->> (some-> req :body)
                                  (v/validate [:map
                                               [:words
                                                [:vector
                                                 [:and string? [:fn {:error/message "should not be empty"} not-empty]]]]])
                                  (articles/load-articles)
                                  (assoc-in {:status 200
                                             :headers {"content-type" "application/json"}}
                                            [:body :inserted-count])))
           (POST "/articles" req (let [{:keys [page limit]} (->> (some-> req :body)
                                                                 (v/validate [:map
                                                                              [:page :int]
                                                                              [:limit :int]]))]
                                   {:status 200
                                    :headers {"content-type" "application/json"}
                                    :body (articles/get-articles page limit)}))
           (compojure-route/not-found "<h1>Page not found</h1>"))


(defn exception-wrapper
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (log/errorf "Exception occurred: %s" (ex-message e))
        {:status 500
         :headers {"content-type" "application/json"}
         :body (ex-message e)}))))

(def app (-> app-routes
             (wrap-json-body {:keywords? true})
             (wrap-defaults api-defaults)
             (exception-wrapper)
             (cors/wrap-cors identity)
             (wrap-json-response)))


