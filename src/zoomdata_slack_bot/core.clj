(ns zoomdata-slack-bot.core
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.data.json :as json]
            [clj-http.client :as http]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response]]
            [environ.core :refer [env]])
  (:gen-class))


(defn random-visualisation [params]
  (let [path (format "http://live.zoomdata.com/zoomdata/service/sources")
        response (http/get path {:accept :json})]
    (if (= (:status response) 200)
      (->> (:body response)
           (json/read-str)
           (mapcat
            (fn [e]
              (let [id (get e "id") vs (get e "visualizations")]
                (map (fn [v] [id (get v "visId")]) vs))))
           (rand-nth)
           ;; apply format?
           ((fn [[source vis]]
              (let [random-link
                    (format "http://live.zoomdata.com/zoomdata/service/screenshot/%s/%s"
                            source vis)]
                {:body
                 {:attachments
                  [{:title "Random Visualisation"
                    :title_link (format "http://live.zoomdata.com/zoomdata/visualization#%s-%s"
                                        source vis)
                    :image_url random-link}]}}))))
      
      ;; else
      {:body {:text (str "Sorry, something is broken!" "\n" (:body response))}})))

(defroutes app-routes
  (GET "/zoomdata" {params :params} (random-visualisation params))
  (route/resources "/"))

(def app
  (-> app-routes
      wrap-keyword-params
      wrap-params
      wrap-json-response))

(defn -main [& args]
  (ring/run-jetty
   #'app
   {:port (Integer. (or (env :port) 5000))
    :join? false}))

  
