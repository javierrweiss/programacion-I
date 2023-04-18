(ns javierweiss.custom-viewers.viewers
  {:nextjournal.clerk/no-cache true}
  (:require [nextjournal.clerk :as clerk]
            [mentat.clerk-utils.show :as us]
            [nextjournal.clerk.viewer :as viewer]
            [clojure.edn :as edn]))

(def editor-sync-viewer
  (assoc viewer/viewer-eval-viewer
         :render-fn
         '(fn [!code _]
            [:div
             [:link
              {:rel "stylesheet"
               :type "text/css"
               :href "https://storage.googleapis.com/app.klipse.tech/css/codemirror.css"}]
             [:script
              "window.klipse_settings = {selector: '.language-klipse'};"]
             [:script
              {:src
               "https://storage.googleapis.com/app.klipse.tech/plugin/js/klipse_plugin.js"}]
             [:div.bg-neutral-50 [:code {:class "language-klipse"} [nextjournal.clerk.render.code/editor !code]]]])))


(def greeting-viewer
  {:transform-fn clerk/mark-presented
   :render-fn
   '(fn [x]
      [:pre (javierweiss.cljs.user-inputs/testeando x) " 👋👋👋"])})
 
(comment
  
  (viewer/viewer-eval-viewer )
  )

