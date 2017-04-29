(ns com.proximyst.discordselfbot.commands.quit
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User TextChannel Guild Message]))

(defn quit
  [^MessageReceivedEvent event
   ^TextChannel channel
   ^User author
   ^Guild guild
   ^Message message
   #^"[Ljava.lang.String;" args]
  (-> message (.delete) (.complete))    ; Make sure the message gets deleted first, then exit.
  (System/exit 0)
  )