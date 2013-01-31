(ns marsrovers
  (:import (java.io BufferedReader FileReader)))

(def x-min 0)
(def y-min 0)

(def dirs [:N :E :S :W ])
(def dir-count (count dirs))
(def shifts [[0 1] [1 0] [0 -1] [-1 0]])

; -----------------------------------------------------------------------------
; Prepare input data.

(defn- str-to-xy [str]
  (vec (map #(Integer/parseInt %) (re-seq #"\d+" str))))

(defn- str-to-dir [str]
  (keyword (re-find (re-matcher #"[NEWS]" str))))

(defn- xy-max [file-reader]
  (vec (map #(Integer/parseInt %)
            (clojure.string/split (first (line-seq file-reader)) #" "))))

(defn- str-to-keywords [string]
  (map #(keyword (str %)) (seq string)))

; -----------------------------------------------------------------------------

(defn- write-result [result file-writer]
  (let [x (first (first result))
        y (second (first result))
        dir (name (last (first result)))]
    (.write file-writer (str x " " y " " dir "\n"))))

; -----------------------------------------------------------------
(defn- remain-between [n min-boundary max-boundary]
  (min max-boundary (max n min-boundary)))

(defn- create-rover [[x y dir] plateau]
  [(remain-between x (:x-min plateau) (:x-max plateau))
   (remain-between y (:y-min plateau) (:y-max plateau))
   dir])

(defn- dir-index [dir] (.indexOf dirs dir))

(def moves {:R (fn [[x y dir] plateau]
                 [[x y (nth dirs (rem (+ (dir-index dir) 1) dir-count))] plateau])
            :L (fn [[x y dir] plateau]
                 [[x y (nth dirs (rem (+ (- (dir-index dir) 1) dir-count) dir-count))] plateau])
            :M (fn [[x y dir] plateau]
                 [(create-rover [(+ x ((shifts (dir-index dir)) 0))
                                 (+ y ((shifts (dir-index dir)) 1))
                                 dir] plateau) plateau])})

(defn- move [[rover plateau] step]
  (let [make-move (moves step)]
    (make-move rover plateau)))

(defn- end-point [rover plateau steps]
  (reduce move [rover plateau] steps))

(defn- create-initial-rover [rover-data plateau]
  (create-rover (conj (str-to-xy (first rover-data))
                      (str-to-dir (first rover-data)))
                plateau))

(defn- process-with-open [file-reader file-writer]
  (let [max-coords (xy-max file-reader)
        plateau {:x-min x-min :x-max (first max-coords) :y-min y-min :y-max (last max-coords)}]
    (doseq [rover-data (partition 2 (line-seq file-reader))]
      (let [rover (create-initial-rover rover-data plateau)
            steps (str-to-keywords (last rover-data))]
        (write-result (end-point rover plateau steps)
                      file-writer)))))

(defn process [in-file-name out-file-name]
  (with-open [file-reader (BufferedReader. (FileReader. in-file-name))]
    (with-open [file-writer (clojure.java.io/writer out-file-name)]
      (process-with-open file-reader file-writer))))

; -----------------------------------------------------------------------------

(process "input.txt" "output.txt")
