(ns selfbot.commands.resolveurl
  (:use [clojure.string :only [starts-with?]])
  (:import [java.net HttpURLConnection URL]
           [net.dv8tion.jda.core.events.message MessageReceivedEvent]
           [net.dv8tion.jda.core.entities Message Guild TextChannel User]))

(defn- resolveUrl
  [^String url]
  (let [properUrl (if (or (starts-with? url "https://") (starts-with? url "http://")) url (str "http://" url))
        ^HttpURLConnection connection (.openConnection (URL. properUrl))]
    (.addRequestProperty connection "User-Agent" "Mozilla/4.76")
    (.setInstanceFollowRedirects connection false)
    (.connect connection)
    (.getInputStream connection)        ; Force connection to read contents
    (if (or (= (.getResponseCode connection) (HttpURLConnection/HTTP_MOVED_TEMP)) (= (.getResponseCode connection) (HttpURLConnection/HTTP_MOVED_PERM)))
      (resolveUrl ^String (.getHeaderField connection "Location"))
      url)
    )
  )

(defn commandResolve
  [^MessageReceivedEvent event
   ^User author
   ^Message message
   ^TextChannel channel
   ^Guild guild
   args
   & unimplementedArguments]
  (let [url (clojure.string/join "%20" args)
        resolved (resolveUrl url)]
    (-> message (.editMessage (str "```\n" "Input URL: " url "\nResolved URL: " resolved "\n```")) .queue)
    )
  )