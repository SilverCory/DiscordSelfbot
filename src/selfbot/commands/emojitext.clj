(ns selfbot.commands.emojitext
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities MessageChannel User Guild Message]
           [selfbot.java Return Utils]))

(defn emojitext
  [^MessageReceivedEvent event
   ^MessageChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (if (= (count (drop 1 args)) 0)
    (do
      (-> message (.editMessage "You need to specify text to emoji-fy.") (.queue))
      (throw (Return.))))

  (let [builder (StringBuilder.)
        strings (clojure.string/split (clojure.string/join " " (drop 1 args)) #" ")]
    (doseq [i (range (count strings))]
      (let [string (nth strings i)
            chars (char-array string)]
        (doseq [chari (range (count chars))]
          (let [char (nth chars chari)
                charStr (str char)]
            (if (re-matches #"[a-zA-Z]" charStr)
              (.append builder (str ":regional_indicator_" (clojure.string/lower-case charStr) \:))
              (if (re-matches #"[0-9]" charStr)
                (.append builder (str \: (Utils/numberStr (Integer/parseInt charStr)) \:))
                (.append builder charStr)))
            ))))
    (-> message (.editMessage (.toString builder)) (.queue)))
  )