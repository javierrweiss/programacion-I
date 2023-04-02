(ns usuario
  (:require [mentat.clerk-utils.build :as build])) 

(build/serve!
 {:show? true
  :watch-paths ["src"]
  :index "src/javierweiss/notebooks/contextos.clj"
  :cljs-namespaces ['javierweiss.cljs.extending-sci]})
  
(build/halt!)

 