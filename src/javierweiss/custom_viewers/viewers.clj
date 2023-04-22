(ns javierweiss.custom-viewers.viewers
  {:nextjournal.clerk/no-cache true}
  (:require [nextjournal.clerk :as clerk]
            [mentat.clerk-utils.show :as us]
            [nextjournal.clerk.viewer :as viewer]
            [clojure.tools.reader :as r]
            [clojure.tools.reader.edn :as edn]
            [clojure.set :as set]))
 
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

(def code-viewer
  {:render-fn
   '(fn [value]
      (let [default-value value
            !input (reagent.core/atom default-value)
            !compiled (reagent.core/atom)
            safe-str? (fn [code] (if (re-seq #"load-string|eval|sh|read-string" code)
                                   false
                                   true))
            click-handler (fn []
                            (let [i @!input]
                              (when (safe-str? i))
                              (reset! !compiled (load-string i))))]
        (fn [value]
          [:div
           [:div.flex
            [:div.viewer-code.flex-auto.w-80.mb-2 [nextjournal.clerk.render.code/editor !input]]
            [:button.flex-none.bg-slate-100.mb-2.pl-2.pr-2
             {:on-click click-handler}
             "Evaluar"]]
           [:div.bg-slate-50 
            [nextjournal.clerk.render/inspect @!compiled]]])))})

 
(comment
  (load-string "(defn mas-diez [x] (+ x 10))
                           (mas-diez 10)")
  (r/read-string (str "#=" "(mas-diez 10)"))

(defn safe-str?
  [code]
  (if (re-seq #"load-string|eval|sh|read-string" code)
    false
    true))  
  (safe-str? "(+ 1 2) (sort [2 4 3 423 32 1]) (eval \"()\")")
  (safe-str? "(((load-string \"(+ 43 43)\")))")

(seq '(1 23 4))
  (viewer/viewer-eval-viewer)
  )
 
