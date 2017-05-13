(ns selfbot.commands.aesthetics
  (:use [selfbot.utilities :only [numToStr]])
  (:import [net.dv8tion.jda.core.entities Guild TextChannel Message User]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [selfbot.framework Return]))

(defn commandEmoji
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (when (= (count args) 0)
    (-> message (.editMessage "* You need to specify text to emojify.") .queue)
    (throw (Return/instance)))
  (let [builder (StringBuilder.)]
    (doseq [string (clojure.string/split (clojure.string/join " " args) #" ")]
      (doseq [character (char-array string)
              :let [^String strver (str character)]]
        (if (re-matches #"[a-zA-Z]" strver)
          (.append builder (str ":regional_indicator_" (.toLowerCase strver) \:))
          (if (re-matches #"[0-9]" strver)
            (.append builder (str \: (numToStr strver) \:))
            (if (= character \!)
              (.append builder ":exclamation:")
              (.append builder strver))))
        )
      (.append builder "   ")
      )
    (-> message (.editMessage (.trim (.toString builder))) .queue)
    )
  )
