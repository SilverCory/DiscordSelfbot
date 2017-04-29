(ns com.proximyst.discordselfbot.commands.resolve
  (:import [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities TextChannel User Guild Message]
           [java.net URL HttpURLConnection]
           [com.proximyst.discordselfbot.java Utils]
           [java.util.concurrent TimeUnit]))

(defn resolveFullUrl
  [^String url]
  (if (not (or (.startsWith url "https://") (.startsWith url "http://")))
    (def url (str "http://" url)))
  (let [connection (-> (URL. url) (.openConnection) (cast HttpURLConnection))]
    (.addRequestProperty connection "User-Agent" "Mozilla/4.76")
    (.setInstanceFollowRedirects connection false)
    (.connect connection)
    (.getInputStream connection)        ; Simply to force the connection to read the page.
    (if (or (= (.getResponseCode connection) (HttpURLConnection/HTTP_MOVED_TEMP)) (= (.getResponseCode connection) (HttpURLConnection/HTTP_MOVED_PERM)))
      (resolveFullUrl (^String .getHeaderField connection "Location"))
      (url))
    )
  )

(defn resolveUrl
  [^MessageReceivedEvent event
   ^TextChannel channel
   ^User author
   ^Guild guild
   ^Message message
   #^"[Ljava.lang.String;" args]
  (let [url (String/join "%20" args)
        resolved (resolveFullUrl url)]
    (.queue (.sendMessage channel (str url " resolved to: " resolved)) (Utils/fnConsumer #(.queueAfter (.delete ^Message %) 15 (TimeUnit/SECONDS))))
    )
  )
