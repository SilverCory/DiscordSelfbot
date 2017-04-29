(defproject discordselfbot "0.1.0-SNAPSHOT"
  :description "A discord selfbot made in Clojure using some Java wrappers & workarounds. "
  :url "https://github.com/Proximyst/DiscordSelfbot"
  :license {:name "GNU GPLv3"
            :url  "https://github.com/Proximyst/DiscordSelfbot/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"] [net.dv8tion/JDA "3.0.0_188"]]
  :repositories [["jcenter" "http://jcenter.bintray.com"]]
  :main ^:skip-aot com.proximyst.discordselfbot.core
  :java-source-paths ["src"]
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:options"]
  :uberjar-name "UberJar-%s.jar"
  :source-paths ["src"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
