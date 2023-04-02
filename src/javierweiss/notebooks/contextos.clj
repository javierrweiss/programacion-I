(ns javierweiss.notebooks.contextos
  {:nextjournal.clerk/no-cache true}
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as viewer]
            [javierweiss.cljs.user-inputs :as inp]
            [clojure.edn :as edn]))
 
(clerk/with-viewer
  '(fn [_ _] 
     [:div.viewer-code [nextjournal.clerk.render.code/editor inp/codigo]])
  (inp/testeando))

(clerk/html [:p (clerk/eval-cljs (edn/read-string @inp/codigo))])


 


(comment
(ns-unalias *ns* 'clerk-utils)
(clerk/show! "src/javierweiss/notebooks/contextos.clj")
  )
