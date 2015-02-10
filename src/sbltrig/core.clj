(ns sbltrig.core
    (:require
     [quil.core :as q]
     [quil.middleware :as m]
     [sbltrig.dynamic :as dynamic])
  (:gen-class))

(defn -main [& args]
  (q/sketch :title "Pleat Generator"
            :size [768 768]
            :setup  dynamic/setup
            :draw   dynamic/draw
            :update dynamic/upkeep
            :renderer :java2d
            :mouse-moved dynamic/mouse-moved
            :key-pressed dynamic/key-pressed
            :middleware [m/fun-mode]
            :features [:exit-on-close]))