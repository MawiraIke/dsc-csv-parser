(ns csv-parser.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn get-data []
  (with-open [reader (io/reader "resources/info-session.csv")]
    (doall
      (csv/read-csv reader))))

(defn custom-inc [pre]
  (if (nil? pre) 0 (inc pre)))

(defn parse-data []
  (let [data (get-data)
        rest (rest data)
        acc (atom {})]
    (dosync
      (doseq [x rest]
        (let [choices (nth x 4)]
          (if (clojure.string/includes? choices ";")
            (doseq [y (clojure.string/split choices #";")]
              (swap! acc update y custom-inc))
            (swap! acc update choices custom-inc))))
      @acc)))
