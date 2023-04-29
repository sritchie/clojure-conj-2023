(ns conj.sci-extensions
  (:require [conj.custom]
            [conj.quartic-well]
            [emmy-viewers.sci]
            [sci.ctx-store]
            [sci.core :as sci]))

;; ## SCI Environment Extension
;;
;; This namespace extends the SCI environment used by Clerk, making available
;; any custom ClojureScript you'd like to use while [writing
;; viewers](https://book.clerk.vision/#writing-viewers) for your notebooks.

;; ## Emmy-Viewers installation
;;
;; This first section installs all `Emmy-Viewers` namespaces into the project:

(emmy-viewers.sci/install!)

;; See [`Emmy-Viewers` via
;; SCI](https://emmy-viewers.mentat.org/#emmy-viewers-via-sci) for more detail.

;; ## SCI Environment Extension

;; This form creates a "lives-within-SCI" version of the `clerk-utils.custom`
;; namespace by copying all public vars.
(def custom-namespace
  (sci/copy-ns conj.custom
               (sci/create-ns 'conj.custom)))

;; Adding an entry to this map is equivalent to adding an entry like
;; `(:require [clerk-utils.custom])` to a Clojure namespace.
(def custom-namespaces
  {'conj.custom custom-namespace
   ;; Add any more namespaces here! Make sure to `:require` anything you add at
   ;; the top of this file.
   })

;; Add aliases here for namespaces that you've included above. This adds an
;; `:as` form to a namespace: `(:require [conj.custom :as custom])`
(def custom-aliases
  {'custom 'conj.custom})

;; This next form mutates SCI's default environment, merging in your custom code
;; on top of what Clerk has already configured.

(sci.ctx-store/swap-ctx!
 sci/merge-opts
 {;; Use `:classes` to expose JavaScript classes that you'd like to use in your
  ;; viewer code. `Math/sin` etc will work with this entry:
  :classes    {'Math  js/Math}
  :namespaces custom-namespaces
  :aliases    custom-aliases})

;; ## JavaScript Libraries
;;
;; See the [Extending SCI](https://clerk-utils.mentat.org/#extending-sci)
;; section of the [Clerk-Utils docs](https://clerk-utils.mentat.org) for
;; instructions on how to make NPM dependencies like "react" available inside of
;; Clerk's viewers.
