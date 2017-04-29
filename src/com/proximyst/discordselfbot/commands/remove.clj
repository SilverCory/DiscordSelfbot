(ns com.proximyst.discordselfbot.commands.remove
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User TextChannel Guild Message]
           [com.proximyst.discordselfbot.java Return]
           [java.util List]))

(defn remove
  [^MessageReceivedEvent event
   ^TextChannel channel
   ^User author
   ^Guild guild
   ^Message message
   #^"[Ljava.lang.String;" args]
  (if (= (count args) 0)
    (throw (Return. "The user needs to specify an amount of messages to remove from history.")))
  (let [^Integer amount nil
        ^List history nil]
    (try (def amount (Integer/parseInt (re-find #"\A-?\d+" (first args))))
         (catch NumberFormatException ex
           (-> message (.editMessage "Couldn't parse integer to remove.") (.queue))))
    (def history (-> channel (.getHistory) (.getRetrievedHistory)))
    (for [i (range 0 (.size history))
          :let [^Message msg (nth history i)]]
      (-> msg (.delete) (.queue)))
    )
  )