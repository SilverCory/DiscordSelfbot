(ns com.proximyst.discordselfbot.core
  (:gen-class)
  (:import [net.dv8tion.jda.core JDA JDABuilder]
           [com.proximyst.discordselfbot.java ListenerWrapper]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities TextChannel User Guild Message]
           [javax.script ScriptEngineManager]))

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

  (def com.proximyst.discordselfbot.commands.eval/engine (-> (ScriptEngineManager.) (.getEngineByName "nashorn")))

  (.registerCommand (ListenerWrapper/instance) "resolve" com.proximyst.discordselfbot.commands.resolve/resolveUrl)

  ; As long as the eval registering points to the same methods, there should be no problem.
  (.registerCommand (ListenerWrapper/instance) "eval" com.proximyst.discordselfbot.commands.eval/evaluate)
  (.registerCommand (ListenerWrapper/instance) "evaluate" com.proximyst.discordselfbot.commands.eval/evaluate)

  (.addEventListener jda (ListenerWrapper/instance))
  )