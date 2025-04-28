(ns articles-frontend.http
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]))

(def urls {:find-url "http://localhost:5000/find"
           :articles-url "http://localhost:5000/articles"})

(rf/reg-event-db
  ::good-http-result
  (fn [_ [db on-success res]]
    {:db db
     :fx [[:dispatch (conj on-success res)]]}))

(rf/reg-event-db
  ::bad-http-result
  (fn [db [_ res]]
    (->> (get-in res [:parse-error :original-text])
         (assoc db :error-message))))

(rf/reg-event-fx
  ::search
  (fn [{:keys [db]} [_ uri body on-success]]
    (let []
      {:db         db
       :http-xhrio {:method          :post
                    :uri             (get urls uri)
                    :timeout         10000
                    :body            (ajax.json/write-json-native body)
                    :headers         {"Access-Control-Allow-Origin" "*"
                                      "Accept"                      "application/json"
                                      "Content-Type"                "application/json"}
                    :format          (ajax/json-request-format)
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success      on-success
                    :on-failure      [::bad-http-result]}})))
