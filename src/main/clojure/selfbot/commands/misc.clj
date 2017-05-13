(ns selfbot.commands.misc
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities TextChannel User Message Guild]
           [net.dv8tion.jda.core EmbedBuilder JDA]
           [java.awt Color]
           [java.text SimpleDateFormat]
           [java.util Date]))

(def ^:private dateFormat (SimpleDateFormat. "EEE, d MMMM yyyy HH:mm:ss"))

(defn commandPi
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (-> message (.editMessage (->
                              (EmbedBuilder.)
                              (.setColor (Color/GREEN))
                              (.setTitle "Pi" nil)
                              (.setDescription "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")
                              .build)
                            )
      .queue)
  )

(defn commandNow
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (let [now (System/currentTimeMillis)]
    (-> message (.editMessage (->
                                (EmbedBuilder.)
                                (.setColor (Color/GREEN))
                                (.setTitle "Now" nil)
                                (.addField "Millis/UNIX" (str now "ms since Epoch.") false)
                                (.addField "Date" (-> dateFormat (.format (Date. now))) false)
                                .build)
                              )
        .queue)
    )
  )

(defn commandPing
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (-> message (.editMessage (str "\uD83C\uDFD3 Ping: " (.getPing (.getJDA message)) "ms")) .queue)
  )