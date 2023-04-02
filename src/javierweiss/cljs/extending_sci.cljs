(ns javierweiss.cljs.extending-sci
    (:require [javierweiss.cljs.user-inputs]
              ["react" :as react]
              [sci.ctx-store]
              [sci.core :as sci]))

(def custom-namespace
  (sci/copy-ns javierweiss.cljs.user-inputs
               (sci/create-ns 'javierweiss.cljs.user-inputs)))

(sci.ctx-store/swap-ctx!
 sci/merge-opts
 {:namespaces {'javierweiss.cljs.user-inputs custom-namespace}})