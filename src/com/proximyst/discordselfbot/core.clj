(ns com.proximyst.discordselfbot.core
  (:gen-class)
  (:import [net.dv8tion.jda.core JDA JDABuilder]
           [com.proximyst.discordselfbot.java ListenerWrapper]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities TextChannel User Guild Message]))

(def ^JDA jda)

(defn -main
  [& args]
  (set! *warn-on-reflection* true)
  (if (= (count args) 0)
    (throw (RuntimeException. "You need to specify a token.")))
  (def jda
    (->
      (JDABuilder.)
      (.setToken (first args))
      (.setCorePoolSize 6)
      (.buildBlocking)
      )
    )
  (set! (ListenerWrapper/jda) jda)
  (.setFallback (ListenerWrapper/instance)
                (fn [^MessageReceivedEvent event
                     ^TextChannel channel
                     ^User author
                     ^Guild guild
                     ^Message message
                     #^"[Ljava.lang.String;" args]
                  (.sendMessage channel (str "The command specified doesn't exist: " (first args)))
                  )
                )
  (.registerCommand (ListenerWrapper/instance) "resolve" com.proximyst.discordselfbot.commands.resolve/resolveUrl)
  (.addEventListener jda (ListenerWrapper/instance))
  )