(defproject zoomdata-slack-bot "0.1.0"
  :description "Slack service to return chart snapshots from zoomdata"
  :url "http://zoomdata.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [compojure "1.4.0"]
                 [clj-http "2.0.0"]
                 [environ "1.0.0"]]

  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler zoomdata-slack-bot.core/app}

  :min-lein-version "2.0.0"
  :aot  [zoomdata-slack-bot.core]
  :uberjar-name "zoomdata-slack-bot.jar"
  :hooks [environ.leiningen.hooks]
  :main zoomdata-slack-bot.core
)
