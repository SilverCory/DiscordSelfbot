(ns selfbot.commands.quit
  (:import [net.dv8tion.jda.core.entities Guild TextChannel Message User]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core JDA]))

(defn commandQuit
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (-> message .delete .complete)        ; Make sure the message gets deleted first.
  (.shutdown (.getJDA message))
  (Thread/sleep 3000)                   ; Give JDA time.
  (System/exit 0)
  )