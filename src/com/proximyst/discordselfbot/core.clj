(ns com.proximyst.discordselfbot.core
  (:gen-class)
  (:import [net.dv8tion.jda.core JDA JDABuilder]
           [com.proximyst.discordselfbot.java ListenerWrapper]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities TextChannel User Guild Message]
           [javax.script ScriptEngineManager]
           [java.io File InputStream FileOutputStream]
           [java.nio.file Files StandardCopyOption Path]
           [java.net URL]
           [javax.imageio ImageIO]
           [java.awt Image]))

(def ^JDA jda)
(def ^File dataDir)
(def data '[])

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
  (def dataDir (File. (str "." (File/separator) "selfbot-data")))
  (when (not (.exists dataDir))
    (.mkdirs dataDir))

  (conj data '[:boi (File. dataDir "boi.jpg")])
  (when (not (.exists ^File (get data :boi)))
    (let [^File boi (get data :boi)
          ^InputStream stream (-> (URL. "http://i.imgur.com/fhHuvIP.jpg") (.openConnection) (.getInputStream))]
      (Files/copy stream (.toPath boi))
      (.close stream)))

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

  (.registerCommand (ListenerWrapper/instance) "quit" com.proximyst.discordselfbot.commands.quit/quit)
  (.registerCommand (ListenerWrapper/instance) "exit" com.proximyst.discordselfbot.commands.quit/quit)

  (.registerCommand (ListenerWrapper/instance) "remove" com.proximyst.discordselfbot.commands.remove/remove)
  (.registerCommand (ListenerWrapper/instance) "purge" com.proximyst.discordselfbot.commands.remove/remove)
  (.registerCommand (ListenerWrapper/instance) "delete" com.proximyst.discordselfbot.commands.remove/remove)

  (.addEventListener jda (ListenerWrapper/instance))
  )