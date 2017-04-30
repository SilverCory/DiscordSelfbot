(ns selfbot.commands.remove
  (:gen-class)
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User Guild Message MessageHistory MessageChannel]
           [selfbot.java Return MutableVar]
           [java.util List ArrayList]
           [java.util.stream Collectors]))

(defn removeCmd
  [^MessageReceivedEvent event
   ^MessageChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (if (= (count (drop 1 args)) 0)
    (do
      (-> message (.editMessage "Specify an amount of messages to remove.") (.queue))
      (throw (Return. "The user needs to specify an amount of messages to remove from history."))))
  (let [^MutableVar amount (MutableVar. 0)
        ^MutableVar history (MutableVar. (ArrayList.))]
    (try (.setValue amount (Long/parseLong (re-find #"\A-?\d+" (second args))))
         (catch NumberFormatException ex
           (-> message (.editMessage "Couldn't parse long to remove.") (.queue))
           (throw (Return. "Couldn't parse."))))
    (.setValue history (-> channel (.getHistoryAround message ^Long (+ 1 ^Long (.getValue amount))) ^MessageHistory (.complete) (.getRetrievedHistory)))
    (doseq [i (range (.size ^List (.getValue history)))]
      (let [msg ^Message (cast Message (.get ^List (.getValue history) i))]
        (if (= (.getAuthor msg) (.getSelfUser (.getJDA msg)))
          (.queue (.delete msg))))
      )
    )
  )