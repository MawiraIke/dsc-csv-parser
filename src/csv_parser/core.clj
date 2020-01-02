(ns csv-parser.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn get-data [] (with-open [reader (io/reader "resources/info-session.csv")] (doall (csv/read-csv reader))))

(defn parse-data []
  (let [data (get-data) rest (rest data) acc (atom {})]
    (doseq [x rest]
      (let [name (nth x 1) number (nth x 2) year (nth x 3) choices (nth x 4) exp (nth x 5)
            fx (fn [x] (if (= (@acc x) nil) (assoc @acc x []) @acc))
            fx-2 (fn [x] (assoc (fx x) x (conj ((fx x) x) (assoc {} :name name :number number :year year :experience exp))))]
        (if (clojure.string/includes? choices ";")
          (doseq [y (clojure.string/split choices #";")] (reset! acc (fx-2 y))) (reset! acc (fx-2 choices))))) @acc))

(defn get-groups [path]
  (let [data ((parse-data) path)]
    (partition 10 10 nil data)))
