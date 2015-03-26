(ns marsrovers
  (:import (java.io BufferedReader FileReader)))

(def directions {:N [0 1] :W [-1 0] :S [0 -1] :E [1 0]})

(defn left [plateau point [x y]]
  [plateau point [(- y) x]])

(defn right [plateau point [x y]]
  [plateau point [y (- x)]])

(defn in-plateau? [[width height] [x y]]
  (and
    (>= x 0) (< x width)
    (>= y 0) (< y height)))

(defn move-point [[x y] [dx dy]]
  [(+ x dx) (+ y dy)])

(defn move [plateau point direction]
  (let [newPoint (move-point point direction)]
    (cond
      (in-plateau? plateau newPoint) [plateau newPoint direction]
      :else [plateau point direction])))

(def actions {:L (partial left) :R (partial right) :M (partial move)})

(defn action [a]
  (actions (keyword a)))

(defn move-rover [[plateau point direction] moves]
  (reduce
    (fn [[pl loc dir] x] ((action x) pl loc dir))
    [plateau point direction]
    moves))

;; file operations
(defn read-plateau-size [reader]
  (let [str (first (line-seq reader))]
    (vec (map #(Integer/parseInt %) (re-seq #"\d+" str)))))

(defn read-xy [str]
  (vec (map #(Integer/parseInt %) (re-seq #"\d+" str))))

(defn read-direction [str]
  (directions (keyword (re-find (re-matcher #"[NWSE]" str)))))

(defn read-moves [s]
  (map #(str %) (seq s)))

(defn read-start-position [str]
  [(read-xy str) (read-direction str)])

(defn process-file [reader]
  (doall
    (let [plateau (read-plateau-size reader)]
      (map
        (fn [lines]
          (let [[point direction] (read-start-position (first lines))
                moves (read-moves (last lines))]
            (move-rover [plateau point direction] moves)))
        (partition 2 (line-seq reader))))))

(defn process [filename]
  (with-open [reader (BufferedReader. (FileReader. filename))]
    (process-file reader)))

;; run
(println
  (map
    (fn [[plateau point direction]]
      [point
       (map #(key %)
         (filter #(= (val %) direction) directions))
       ])
    (process "input.txt")))