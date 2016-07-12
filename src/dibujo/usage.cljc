(ns dibujo.usage
  (:require [dibujo.abstractions.point :refer [->Point round add]]
            [dibujo.abstractions.line :as line :refer [new-line]]
            [dibujo.abstractions.circle :as circle :refer [new-circle]]
            [dibujo.algorithms.line :as line-algos :refer [dda dsc dda2 bresenham1 bresenham2 bresenham3 bresenham4]]
            [dibujo.algorithms.circle :as circle-algos :refer [bresenham]]
            [dibujo.algorithms.development :as dev :refer [infinite-line]]
            #?(:clj [dibujo.utils.jframe :refer [draw]]
               :cljs [dibujo.utils.canvas :refer [draw]])))

(def p1 (->Point 1 1))
(def p2 (->Point 26 24))

(def line-by-point #(-> (line/compute p1 p2 bresenham4)
                        draw))

(def line-by-angle #(draw (take 25 (infinite-line 30 p2))))

(def circle (circle/compute p2 20 bresenham))

(defn octant [n circle]
  {:pre [(<= 0 n 7)]}
  (let [octant (fn [n circle] (map #(nth % n) (partition 8 circle)))]
    (octant n circle)))

(def octants #(concat (octant 3 circle) (octant 7 circle)))

(def less-dense (take-nth 3 circle))
(def random (random-sample 0.05 circle))

(defn missing-head [n circle]
  (let [points (apply interleave (partition 8 circle))]
    (drop n points)))

(defn missing-tail [n circle]
  (let [points (apply interleave (partition 8 circle))]
    (drop-last n points)))

(defn arcs [m]
  (flatten (for [n (range 8)]
             (drop m (octant n circle)))))

(def continuous #(concat (drop 4 (octant 3 circle)) (octant 7 circle) (octant 6 circle)))
