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
  (let [sentTime ^OffsetDateTime (.getCreationTime message)]

    (-> message
        (.editMessage
          (-> (EmbedBuilder.)
              (.setTitle "Ping - Computing..." nil)
              (.setColor (Color/YELLOW))
              (.build)
              )
          )

        (.queue
          (Utils/fnConsumer
            #(let [msg ^Message %
                   creation ^OffsetDateTime (.getCreationTime msg)]

               (-> msg
                   (.editMessage
                     (->
                       (EmbedBuilder.)
                       (.setTitle "Ping - Complete!" nil)
                       (.setColor (Color/GREEN))
                       (.addField "Ping" (str (.until sentTime creation (ChronoUnit/MILLIS)) "ms") true)
                       (.build)
                       )
                     )
                   (.queue)
                   )
               )
            )
          )
        )
    )
  )