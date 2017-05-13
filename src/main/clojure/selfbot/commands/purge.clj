(ns selfbot.commands.purge
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User TextChannel Guild Message MessageHistory]
           [selfbot.framework Return]))

(defn commandPurge
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (when (= (count args) 0)
    (do
      (-> message (.editMessage "* Error: You need to specify an amount of messages to purge.") .queue)
      (throw (Return/instance))))

  (let [amount (atom 0)
        history (atom [])]
    (try
      (reset! amount (Long/parseLong (clojure.string/replace (first args) #"[^0-9]+" "")))
      (catch NumberFormatException ex
        (-> message (.editMessage "* Couldn't parse the specified long to purge messages.") .queue)
        (throw (Return/instance))
        ))

    (reset! history (-> channel (.getHistoryAround message ^Long (+ 1 @amount)) ^MessageHistory .complete .getRetrievedHistory))

    (doseq [iteration @history
            :let [^Message message (cast Message iteration)]]
      (when (= (.getAuthor message) (.getSelfUser (.getJDA message)))
        (.queue (.delete message)))
      )
    )
  )