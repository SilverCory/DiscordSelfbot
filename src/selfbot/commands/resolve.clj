(ns selfbot.commands.resolve
  (:gen-class)
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities User Guild Message MessageChannel]
           [java.net URL HttpURLConnection]
           [selfbot.java Utils]
           [java.util.concurrent TimeUnit]))

(defn resolveFullUrl
  [^String url]
  (if (not (or (.startsWith url "https://") (.startsWith url "http://")))
    (def url (str "http://" url)))
  (let [connection ^HttpURLConnection (-> (URL. url) (.openConnection))]
    (.addRequestProperty connection "User-Agent" "Mozilla/4.76")
    (.setInstanceFollowRedirects connection false)
    (.connect connection)
    (.getInputStream connection)        ; Simply to force the connection to read the page.
    (if (or (= (.getResponseCode connection) (HttpURLConnection/HTTP_MOVED_TEMP)) (= (.getResponseCode connection) (HttpURLConnection/HTTP_MOVED_PERM)))
      (resolveFullUrl ^String (.getHeaderField connection "Location"))
      url)
    )
  )

(defn resolveUrl
  [^MessageReceivedEvent event
   ^MessageChannel channel
   ^User author
   ^Guild guild
   ^Message message
   args]
  (let [url (clojure.string/join "%20" (drop 1 args))
        resolved (resolveFullUrl url)]
    (.queue (.editMessage message (str url " resolved to: " resolved)) (Utils/fnConsumer #(.queueAfter (.delete ^Message %) 15 (TimeUnit/SECONDS))))
    )
  )
