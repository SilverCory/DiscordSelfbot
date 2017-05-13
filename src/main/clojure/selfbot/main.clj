(ns selfbot.main
  (:gen-class)
  (:require selfbot.listener)
  (:import [net.dv8tion.jda.core JDABuilder AccountType JDA]
           [net.dv8tion.jda.core.hooks ListenerAdapter IEventManager]
           [selfbot.listener Listener])
  )

(def jda (atom nil))
(def listener (atom nil))

(defn -main
  [& args]
  (if (= (count args) 0)
    (throw (RuntimeException. "You need to specify a token.")))

  (reset! jda (-> (JDABuilder. (AccountType/CLIENT)) (.setToken (clojure.string/replace (first args) "\"" "")) (.buildBlocking)))
  (reset! listener (Listener.))
  (selfbot.listener/registerCommands)

  (.addEventListener ^JDA @jda (into-array [@listener]))
  )