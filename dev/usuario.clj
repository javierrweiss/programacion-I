(ns usuario
  (:require [mentat.clerk-utils.build :as build])) 


(comment
  (build/serve!
   {:show? true 
    :watch-paths ["src"]
    :index "src/javierweiss/notebooks/contextos.clj"
    :cljs-namespaces ['javierweiss.cljs.extending-sci]})
        
  (build/serve! {}) 
   
  (build/halt!) 
  )

  