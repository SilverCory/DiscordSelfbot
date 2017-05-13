(ns selfbot.commands.evaluation
  (:import [net.dv8tion.jda.core EmbedBuilder JDA]
           [java.awt Color]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities Guild TextChannel Message User MessageEmbed MessageEmbed$Field]
           [selfbot.framework Return]
           [javax.script ScriptException ScriptEngine ScriptEngineManager]))

(def ^:private ^ScriptEngine engine (-> (ScriptEngineManager.) (.getEngineByName "nashorn")))

(defn- ^MessageEmbed evalWorkingEmbed
  [language input]
  (-> (EmbedBuilder.) (.setTitle (str "Evaluation (" language ") - Computing...") nil) (.setColor (Color/YELLOW))
      (.addField "Input" (str "```" (.toLowerCase language) "\n" input "\n```") false)
      (.addField "Output" "```\nComputing...\n```" false) .build))

(defn commandJs
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (when (= (count args) 0)
    (.queue (.editMessage message "* You need to specify something in JS to evaluate."))
    (throw (Return/instance)))
  (.queue (.editMessage message (evalWorkingEmbed "JS" (clojure.string/join " " args))))
  (let [^EmbedBuilder builder (EmbedBuilder.)
        title (atom "Output")
        output (atom "An error occurred.")
        footer (atom nil)]
    (try
      (.setTitle builder "Evaluation (JS) - Complete!" nil)
      (.addField builder "Input" (str "```js\n" (clojure.string/join " " args) "\n```") nil)
      (.put engine "channel" channel)
      (.put engine "args" (into-array args))
      (.put engine "jda" ^JDA (.getJDA message))
      (.put engine "guild" guild)
      (.put engine "message" message)
      (.put engine "self" (.getSelfUser (.getJDA message)))

      (let [before (System/currentTimeMillis)]
        (reset! output (str "```js\n" (.eval engine (str "var imports = new JavaImporter('java.lang','java.io','java.math','java.util','java.util.concurrent','java.time'); with (imports) { " (clojure.string/join " " args) " }")) "\n```"))
        (reset! footer (str "Time elapsed: " (- (System/currentTimeMillis) before) "ms")))
      (.setColor builder (Color/GREEN))
      (catch ScriptException ex
        (reset! output (str "```js\n" (.getMessage ex) "\n```"))
        (.setTitle builder "Evaluation (JS) - Error!" nil)
        (.setColor builder (Color/RED))
        (reset! footer nil)
        )
      )
    (.setFooter builder @footer nil)
    (.addField builder @title @output false)
    (.queue (.editMessage message (.build builder)))
    )
  )