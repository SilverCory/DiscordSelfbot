(ns selfbot.commands.embed
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities MessageChannel User Guild Message Member]
           [net.dv8tion.jda.core EmbedBuilder]
           [java.awt Color]))

(defn embed
  [^MessageReceivedEvent event
   ^MessageChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (let [properArgs (drop 1 args)]
    (-> message (.editMessage (->
                                (EmbedBuilder.)
                                (.setTitle (clojure.string/replace (first properArgs) "_" " ") nil)
                                (.setAuthor ^String (.getName ^User (first (.getMentionedUsers message))) nil (.getAvatarUrl (first (.getMentionedUsers message))))
                                (.setDescription (clojure.string/join " " (drop 2 properArgs)))
                                (.build)))
        (.queue)))
  )