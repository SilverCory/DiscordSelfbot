(ns selfbot.commands.ping
  (:gen-class)
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User TextChannel Guild Message]
           [net.dv8tion.jda.core EmbedBuilder]
           [selfbot.java Utils]
           [java.awt Color]
           [java.time OffsetDateTime]
           [java.time.temporal ChronoUnit]))

(defn ping
  [^MessageReceivedEvent event
   ^TextChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (-> message (.editMessage (str "\uD83C\uDFD3 Ping: " (.getPing (.getJDA message)) "ms")) (.queue))
  )