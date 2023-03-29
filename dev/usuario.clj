(ns usuario
  (:require [mentat.clerk-utils.build :as build]))


(build/serve!
 {:show? true
  :index "src/javierweiss/notebooks/contextos.clj"
  :cljs-namespaces ['clerk-utils.sci-extensions]})

(build/halt!)
