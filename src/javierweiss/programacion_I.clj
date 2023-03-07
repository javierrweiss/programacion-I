(ns javierweiss.programacion-I
  (:require [nextjournal.clerk :as clerk])
  (:gen-class))

(defn serve-notebooks
  "Serve notebooks on desired port"
  [port]
  (clerk/serve! {:browse true? :port port}))

(defn -main
  "Serve notebooks on select port. Defaults to 7777"
  [& args]
  {:pre (number? (first args))}
  (serve-notebooks (first args)))
