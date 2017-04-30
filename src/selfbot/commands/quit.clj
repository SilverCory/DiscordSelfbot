(ns selfbot.commands.quit
  (:gen-class)
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User Guild Message MessageChannel]))

(defn quit
  [^MessageReceivedEvent event
   ^MessageChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (-> message (.delete) (.complete))    ; Make sure the message gets deleted first, then exit.
  (.shutdown (.getJDA message))
  (System/exit 0)
  )