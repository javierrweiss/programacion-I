^#:nextjournal.clerk
{ ;;:toc true
 :no-cache true
 :visibility :hide-ns}
(ns javierweiss.notebooks.contextos
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as viewer] 
            #_[mentat.clerk-utils :as u]
            [javierweiss.custom-viewers.viewers :as v]
            [mentat.clerk-utils.show :as us]
            [clojure.edn :as edn]))

(def squared-viewer
  {:transform-fn clerk/mark-presented
   :render-fn
   '(fn [x]
      [:pre x "² is equal to " (javierweiss.cljs.user-inputs/square x) "."])})

(clerk/with-viewer squared-viewer 10)

(clerk/with-viewer v/greeting-viewer "Martín Martínez")

^{::clerk/sync true ::clerk/viewer v/editor-sync-viewer ::clerk/visibility {:code :hide}}
(defonce editable-code
  (atom "(def fib x)"))

(clerk/with-viewer v/code-viewer nil)

{:nextjournal.clerk/visibility {:code :hide}}
(comment
  (clerk/serve! {:browse? true})

  (clerk/show! "src/javierweiss/notebooks/contextos.clj")

  (clerk/eval-cljs '(+ 2 4))

  (clerk/eval-cljs-str "(+ 2 42)") 
  (nextjournal.clerk.sci-env/eval-form '(+ 2 3))
  )
