(ns selfbot.core
  (:gen-class)
  (:import [net.dv8tion.jda.core JDA JDABuilder AccountType]
           [selfbot.java ListenerWrapper]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User Guild Message MessageChannel]
           [java.io File InputStream FileOutputStream]
           [java.nio.file Files StandardCopyOption Path]
           [java.net URL URI]
           [javax.imageio ImageIO]
           [java.awt Image]
           [net.dv8tion.jda.core.hooks ListenerAdapter])
  (:use [selfbot.commands.resolve :only [resolveUrl]]
        [selfbot.commands.eval :only [evaluate evaluateClojure]]
        [selfbot.commands.quit :only [quit]]
        [selfbot.commands.remove :only [removeCmd]]
        [selfbot.commands.ping :only [ping]]
        [selfbot.commands.other :only [pi now]]
        [selfbot.commands.embed :only [embed]]
        [selfbot.commands.emojitext :only [emojitext]] :reload-all))

(def ^JDA jda)
(def ^File dataDir)
(def data (hash-map))

(defn copy-uri-to-file [uri file]
  (with-open [in (clojure.java.io/input-stream uri)
              out (clojure.java.io/output-stream file)]
    (clojure.java.io/copy in out)))

(defn -main
  [& args]
  (if (= (count args) 0)
    (throw (RuntimeException. "You need to specify a token.")))

  (def jda
    (->
      (JDABuilder. (AccountType/CLIENT))
      (.setToken (.replace (cast java.lang.String (first args)) "\"" ""))
      (.setCorePoolSize 6)
      (.buildBlocking)
      )
    )
  (def dataDir (File. (str "." (File/separator) "selfbot-data")))
  (when (not (.exists dataDir))
    (.mkdirs dataDir))

  (let [boiFile ^File (File. dataDir "boi.jpg")]
    (def data (conj data {:boi boiFile})))
  (when (not (.exists (data :boi)))
    (let [^File boi (get data :boi)
          ^URI uri (-> (URL. "http://i.imgur.com/fhHuvIP.jpg") (.toURI))]
      (copy-uri-to-file uri boi)
      )
    )

  (set! (ListenerWrapper/jda) jda)
  (.setFallback (ListenerWrapper/instance) (fn
                                             [^MessageReceivedEvent event
                                              ^MessageChannel channel
                                              ^User author
                                              ^Guild guild
                                              ^Message message
                                              args]
                                             (.queue (.editMessage message (str "The command specified doesn't exist: " (first args))))
                                             )
                )

  (let [lw ^ListenerWrapper (ListenerWrapper/instance)]

    (.registerCommand lw "resolve" #(resolveUrl %1 %2 %3 %4 %5 %6))

    ; As long as the eval registering points to the same methods, there should be no problem.
    (.registerCommand lw "eval" #(evaluate %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "evaluate" #(evaluate %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "ceval" #(evaluateClojure %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "clojure" #(evaluateClojure %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "cevaluate" #(evaluateClojure %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "clojureeval" #(evaluateClojure %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "clojureval" #(evaluateClojure %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "quit" #(quit %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "exit" #(quit %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "remove" #(removeCmd %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "purge" #(removeCmd %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "delete" #(removeCmd %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "ping" #(ping %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "pi" #(pi %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "now" #(now %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "time" #(now %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "embed" #(embed %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "quote" #(embed %1 %2 %3 %4 %5 %6))

    (.registerCommand lw "emoji" #(emojitext %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "emojis" #(emojitext %1 %2 %3 %4 %5 %6))
    (.registerCommand lw "memeify" #(emojitext %1 %2 %3 %4 %5 %6))
    )

  (.addEventListener jda (to-array [^ListenerAdapter (ListenerWrapper/instance)]))
  )
