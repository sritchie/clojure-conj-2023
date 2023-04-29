
^#:nextjournal.clerk
{:toc true
 :visibility :hide-ns}
(ns conj.talk
  (:refer-clojure
   :exclude [+ - * / zero? compare divide numerator denominator
             infinite? abs ref partial =])
  (:require [conj.viewers :as cv]
            [emmy.differential]
            [emmy.env :as e :refer :all]
            [emmy.matrix]
            [nextjournal.clerk :as clerk]))

;; # Generics and Calculus

^{::clerk/visibility {:code :hide :result :hide}}
(def tex (comp clerk/tex ->TeX))

;; This function returns a dual number, $x+\varepsilon$.
(defn bundle-element [x]
  (emmy.differential/bundle-element x 0))

;; Let's install a Clerk "viewer" that will render dual numbers using $\TeX$:

^{::clerk/visibility {:result :hide}}
(clerk/add-viewers! [cv/diff-viewer])

;; ## Autodiff with Emmy!

(def epsilon
  (bundle-element 0))

(+ 'x epsilon)

(sin (+ 'x epsilon))

(expt (+ 'x epsilon) 4)

;; In Emmy, the `D` operator applied to a function takes derivatives.

((D sin) 'x)

(((square D) sin) 'x)

;; How bananas is this??

(tex
 (((exp D) (literal-function 'f)) 'x))

;; Next, head over to `taylor_series.clj`.
