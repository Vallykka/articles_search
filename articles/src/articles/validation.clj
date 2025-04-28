(ns articles.validation
  (:require [clojure.string :as str]
            [malli.core :as m]
            [malli.error :as me]))

(defn collect-errors
  [vals]
  (->> vals
       flatten
       (keep identity)
       (distinct)
       (str/join ",")))

(defn validate [spec data]
  (if (m/validate spec data)
    data
    (let [message (->> (me/humanize (m/explain spec data))
                       (map #(format "param '%s' %s" (name (key %)) (->> (val %)
                                                                         (collect-errors))))
                       (str/join ";"))]
      (throw (ex-info (format "Invalid request parameters: %s" message) {})))))
