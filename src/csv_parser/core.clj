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
  (let [row-1 (nth (get-data) 0)
        rest (rest (get-data))
        acc (agent {})]
    (doseq [x rest]
      (let [choices (nth x 4)]
        (if-let [chs (clojure.string/includes? choices ";")
                 chss (or (clojure.string/split chs #";") (conj [] choices))]
          (doseq [x chss]
            (send acc assoc x custom-inc)))))
    @acc))
