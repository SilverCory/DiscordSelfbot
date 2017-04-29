(ns com.proximyst.discordselfbot.core
  (:gen-class)
  (:import [net.dv8tion.jda.core JDA JDABuilder]))

(def ^JDA jda)

(defn -main
  [& args]
  (if (= (count args) 0)
    (throw (RuntimeException. "You need to specify a token.")))
  (def jda
    (->
      (JDABuilder.)
      (.setToken (first args))
      (.setCorePoolSize 6)
      ; TODO: Add message handler here. Use java as a wrapper.
      (.buildBlocking)
      )
    )
  )