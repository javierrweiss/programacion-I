(ns javierweiss.programacion-I
  (:require [nextjournal.clerk :as clerk])
  (:gen-class))

(defn serve-notebooks
  "Serve notebooks on desired port"
  [{:keys [port] :or {port 7777}}]
  (clerk/serve! {:browse true? :port port}))
 
(defn -main
  "Serve notebooks on selected port. Defaults to 7777"
  [& args]
  {:pre (number? (first args))}
  (serve-notebooks {:port (first args)}))
