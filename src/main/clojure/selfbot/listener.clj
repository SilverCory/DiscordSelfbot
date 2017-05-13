(ns selfbot.listener
  (:gen-class
    :name selfbot.listener.Listener
    :extends net.dv8tion.jda.core.hooks.ListenerAdapter
    :main false
    :prefix "or-"
    :constructors {[] []})
  (:use [selfbot.commands.misc :only [commandPi commandNow commandPing]]
        [selfbot.commands.resolveurl :only [commandResolve]]
        [selfbot.commands.purge :only [commandPurge]]
        [selfbot.commands.aesthetics :only [commandEmoji]]
        [selfbot.commands.quit :only [commandQuit]]
        [selfbot.commands.evaluation :only [commandJs]])
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [selfbot.framework Return]
           [net.dv8tion.jda.core.entities User Message TextChannel Guild])
  )

(def commands (atom (hash-map)))
(def prefix "::")
(def self 181470050039889920)

(defn -init
  [this]
  [[]]
  )

(defn registerCommands
  []
  (swap! commands assoc "pi" commandPi)

  (swap! commands assoc "now" commandNow)
  (swap! commands assoc "time" commandNow)

  (swap! commands assoc "resolve" commandResolve)
  (swap! commands assoc "resolveurl" commandResolve)

  (swap! commands assoc "purge" commandPurge)
  (swap! commands assoc "remove" commandPurge)

  (swap! commands assoc "quit" commandQuit)

  (swap! commands assoc "emoji" commandEmoji)
  (swap! commands assoc "emojis" commandEmoji)
  (swap! commands assoc "emojify" commandEmoji)

  (swap! commands assoc "ping" commandPing)
  (swap! commands assoc "pong" commandPing)

  (swap! commands assoc "js" commandJs)
  (swap! commands assoc "evaljs" commandJs)
  )

(defn or-onMessageReceived
  [this ^MessageReceivedEvent event]
  (try
    (when (-> event .getAuthor .getIdLong (= self) not)
      (throw (Return/instance)))
    (when (-> event .getMessage .getRawContent (count) (>= 3) not)
      (throw (Return/instance)))
    (when (-> event .getMessage .getRawContent (.substring 0 (count prefix)) (= prefix) not)
      (throw (Return/instance)))
    (let [whole (-> event .getMessage .getRawContent (.substring (count prefix)))
          input (clojure.string/split whole #" ")
          command (@commands (first input))]
      (when (nil? command)
        (-> event .getMessage (.editMessage "* The command specified is unknown.") .queue)
        (throw (Return/instance))
        )

      (try
        (command event (.getAuthor event) (.getMessage event) (.getTextChannel event) (.getGuild event) (drop 1 input))

        (catch Return ignored
          ; Do nothing as it returned out of method, like it wanted to.
          )
        (catch Exception ex
          (let [exception (str (.toString ^Exception ex) (if (not (nil? (.getLocalizedMessage ex))) (str ": " (.getLocalizedMessage ex)) ""))
                builder (StringBuilder.)]
            (.append builder exception)
            (doseq [trc (.getStackTrace ^Exception ex)
                    :let [strver (.toString ^StackTraceElement trc)]]
              (.append builder "\tat ")
              (.append builder strver)
              (.append builder "\n"))
            (when (>= 1950 (count (.toString builder)))
              (-> event .getMessage (.editMessage (str "* An exception was thrown:\n```\n" (.toString builder) "```")) .queue))
            (.printStackTrace ex)
            )
          )
        )
      )

    (catch Return ignored
      ; Do nothing as it returned out of method, like it wanted to.
      )
    )
  )