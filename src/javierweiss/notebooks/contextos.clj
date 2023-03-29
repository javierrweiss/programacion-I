(ns javierweiss.notebooks.contextos
  {:nextjournal.clerk/no-cache true}
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as viewer]))

(clerk/with-viewer
  '(fn [code-str _] [:div.viewer-code [nextjournal.clerk.render.code/editor (reagent.core/atom code-str)]])
 "")