^#:nextjournal.clerk
{:toc true
 :visibility :hide-ns}
(ns conj.phase-portrait
  (:refer-clojure
   :exclude
   [+ - * / zero? compare divide numerator denominator infinite? abs ref partial =])
  (:require [conj.viewers :refer [multiviewer]]
            [emmy.env :refer :all]
            [emmy.expression.compile :as xc]
            [nextjournal.clerk :as clerk]))

;; ## Phase Portrait

;; Kinetic energy term:

(defn T [_ m l]
  (fn [[_ _ thetadot]]
    (* 1/2 m (square (* l thetadot)))))

;; potential energy term:

(defn V [g m l]
  (fn [[_ theta]]
    (- (* m g l (cos theta)))))

(def L-pendulum
  (- T V))

;; ## Equations

;; first step is show that there is some symbolic goodness.

^{::clerk/viewer multiviewer}
((L-pendulum 'g 'm 'l)
 (up 't 'theta 'thetadot))

;; Look at the equations of motion:

^{::clerk/viewer multiviewer}
(((Lagrange-equations (L-pendulum 'g 'm 'l))
  (literal-function 'theta))
 't)

#_
(println
 (-> (xc/compile-state-fn
      (compose Lagrangian->state-derivative L-pendulum)
      ['g 'm 'l]
      ['t 'theta 'theta_dot]
      {:mode :js
       :calling-convention :primitive
       :generic-params? true})
     (nth 3)))


;; ## Phase Portrait

^{::clerk/width :wide
  ::clerk/viewer
  {:transform-fn
   (comp clerk/mark-presented
         (clerk/update-val
          (fn [{:keys [L params keys initial-state] :as m}]
            (assoc m
                   :L
                   (xc/compile-state-fn
                    (compose Lagrangian->state-derivative L)
                    (mapv params keys)
                    initial-state
                    {:mode :js
                     :calling-convention :primitive
                     :generic-params? true})
                   :V
                   (xc/compile-state-fn
                    V
                    (mapv params keys)
                    initial-state
                    {:mode :js
                     :calling-convention :primitive
                     :generic-params? true})))))
   :render-fn '(fn [opts]
                 [conj.custom/Hamilton opts])}}
{:params
 {:length 1
  :gravity 9.8
  :mass 1
  :simSteps 10}
 :schema
 {:length   {:min 0.5 :max 2 :step 0.01}
  :gravity  {:min 5 :max 15 :step 0.01}
  :mass     {:min 0.5 :max 2 :step 0.01}
  :simSteps {:min 1 :max 50 :step 1}}
 :keys [:gravity :mass :length]
 :L L-pendulum
 :V V
 :initial-state [0 3 0]}
