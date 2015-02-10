(ns sbltrig.dynamic
  (:require
    [quil.core :as q]
    [quil.middleware :as m]))

(defn parse-int [s]
  (Integer. (re-find  #"\d+" s )))

(defn new-pdf []
  (q/create-graphics
    768
    768
    :pdf
    (str
      "pleat-" (q/year)
      "-" (q/month)
      "-" (q/day)
      "-" (q/hour)
      (q/minute)
      (q/seconds)
      ".pdf")))

(defn setup []
  (q/smooth)
  (q/frame-rate 30)
  (q/color-mode :rgb)
  ;; State
  {:x 0 :y 0
   :pleats 10
   :graphics (new-pdf)
   :logo (q/load-shape "sbl.svg")})

(defn upkeep [state]
  (let []

    state))

(defn pleat [pleats x y]
  (loop [n 1]
    (if (= n (- pleats 1))
      (q/line 0 (* n y) (* n x) 0)
      (do
        (q/line 0 (* n y) (* n x) 0)
        (recur (inc n))))))

(defn pattern [state edge]
  (let [pleats (:pleats state)
        x (:x state)
        y (:y state)
        nthx (/ x pleats)
        nthy (/ y pleats)]
    (q/push-matrix)

    (q/translate 384 384)
    (q/rotate (* edge q/HALF-PI))
    (q/scale 0.5)

    (if (even? edge)
      (do (q/line 0 x y 0)
          (pleat pleats nthy nthx))
      (do (q/line 0 y x 0)
          (pleat pleats nthx nthy)))

    (q/pop-matrix)))

(defn azero [n]
  (if (= 0 n)
    (inc n)
    n))

(defn display-label [state]
  (let [pleats (:pleats state)
        x (azero (:x state))
        y (azero (:y state))
        ø (q/atan (/ y x))
        b (q/atan (/ x y))
        r (/ y x)]
    (q/push-matrix)

    (q/fill 0)
    (q/text (str "r:   " r "\nB˚  " (q/round (q/degrees b)) "  |  ø˚  " (q/round (q/degrees ø)) "\nv:   " (q/floor x) "\nh:   " (q/floor y) "\npleats: " pleats) 10 685)
    (q/stroke-weight 1)
    (q/line 10 755 60 755)
    (q/line 10 750 10 755)
    (q/line 60 750 60 755)
    (q/text "100px" 68 756)

    (q/pop-matrix)))

(defn display-ui [state]
  (let [logo (:logo state)]
    (q/push-matrix)

    (q/fill 100 100 200)
    (q/text "Pleat Generator v0.2 \n\nPress:\n- P = Generate PDF\n- UP Arrow = Increase # of Pleats\n- DOWN Arrow = Decrease # of Pleats\n\nImport PDF into a vector\ndrawing app (illustrator, \nsketch, etc) to edit." 10 20)
    (q/shape logo 600 720 145 35)
    (q/pop-matrix)))

(defn display-logo [state]
  (let [logo (:logo state)]
    (q/push-matrix)

    ;;(q/shape logo 100 100)

    (q/pop-matrix)))

(defn draw [state]
  (q/background 255)
  (q/no-fill)
  (q/stroke-weight 2)

  (pattern state 0)
  (pattern state 1)
  (pattern state 2)
  (pattern state 3)

  (display-label state)
  (display-ui state)
  (display-logo state))

(defn save-graphics [state]
  (let [pg (:graphics state)]
    (q/with-graphics pg
                     (.beginDraw pg)

                     (q/no-fill)
                     (q/stroke-weight 1)

                     (pattern state 0)
                     (pattern state 1)
                     (pattern state 2)
                     (pattern state 3)

                     (q/fill 0)
                     (display-label state)

                     (.endDraw pg)
                     (.dispose pg)))
  (assoc state
         :graphics (new-pdf)))

(defn mouse-moved [state event]
  (-> state
      (assoc :x (:x event) :y (:y event))))

(defn key-pressed [state event]
  (let [key (:key event)
        pleats (:pleats state)]
    (case key
      :p  (save-graphics state)
      :up (assoc state :pleats (inc pleats))
      :down (if (<= pleats 2)
              state
              (assoc state :pleats (dec pleats)))
      state)))