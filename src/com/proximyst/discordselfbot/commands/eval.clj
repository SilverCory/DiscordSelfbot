(ns com.proximyst.discordselfbot.commands.eval
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities TextChannel Guild User Message]
           [javax.script ScriptEngine]
           [com.proximyst.discordselfbot.java Return]
           [net.dv8tion.jda.core EmbedBuilder]))

(def ^ScriptEngine engine)

(defn evaluate
  [^MessageReceivedEvent event
   ^TextChannel channel
   ^User author
   ^Guild guild
   ^Message message
   #^"[Ljava.lang.String;" args]
  (if (= (count args) 0)
    (throw (Return. "The user has to specify something in JS to run.")))
  (.queue (.editMessage message
                        (-> (EmbedBuilder.)
                            (.addField "Input"
                                       (str
                                         "```js"
                                         (newline)
                                         (String/join " " args)
                                         (newline)
                                         "```")
                                       true)
                            (.addField "Output"
                                       (str
                                         "```"
                                         (newline)
                                         "Evalutating..."
                                         (newline)
                                         "```")
                                       true)
                            (.build)
                            )
                        )
          )
  (let [^String response nil]
    (try
      (do
        (def response (.toString (.eval engine (str ("var imports = new JavaImporter('java.lang','java.io','java.math','java.util','java.util.concurrent','java.time'); with (imports) { " (String/join " " args) " }")))))
        )
      (catch Exception ex
        (def response (.getMessage ex))))
    (.queue (.editMessage message
                          (-> (EmbedBuilder.)
                              (.addField "Input"
                                         (str
                                           "```javascript"
                                           (newline)
                                           (String/join " " args)
                                           (newline)
                                           "```")
                                         true)
                              (.addField "Output"
                                         (str
                                           "```javascript"
                                           (newline)
                                           response
                                           (newline)
                                           "```")
                                         true)
                              (.build))))
    )

  ; var imports = new JavaImporter('java.lang','java.io','java.math','java.util','java.util.concurrent','java.time'); with (imports) { %s }
  )