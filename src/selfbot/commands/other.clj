(ns selfbot.commands.other
  (:gen-class)
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User Guild Message MessageChannel]
           [net.dv8tion.jda.core EmbedBuilder]
           [java.awt Color]))

(defn pi
  [^MessageReceivedEvent event
   ^MessageChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (-> message
      (.editMessage
        (->
          (EmbedBuilder.)
          (.setColor (Color/GREEN))
          (.setTitle "Pi" nil)
          (.setDescription "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")
          (.build)
          )
        )
      (.queue)
      )
  )