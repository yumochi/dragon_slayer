(ns my-adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))

(def the-map
  { :foyer {:desc "The walls are freshly painted but I do not have any pictures.
  You get the feeling it was just created for a game or something."
                :title "in the foyer"
                :dir {
                      :south :grue-pen}
                :contents [:raw-egg]}
    :grue-pen {:desc "It is very dark. You are about to be eaten by a grue."
                          :title "in the grue pen"
                          :dir {:north :foyer}
                          :contents [:raw-egg]}
                        })

(def adventurer
  { :location :foyer
    :inventory #{}
    :seen #{}})

(defn status [player]
  (let [location (player :location)]
  (print (str "You are " (-> the-map location :title) ". "))
  (when-not ((player :seen) location)
  (print (-> the-map location :desc)))
  (update-in player [:seen] #(conj % location))
  ))

(defn to-keywords [commands]
  ;; should be a list of strings
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn go [dir player]
  (let [location (player :location)
        dest (->> the-map location :dir dir)]
      (if (nil? dest)
            (do (println "You can't go that way.")
                player)
      (assoc-in player [:location] dest)))
  )

(defn respond [player command]
  (match command
                [:look] (update-in player [:seen] #(disj % (-> player :location)))
                [:north] (go :north player)
                [:south] (go :south player)
                _ (do (println "I don't understand you.")
                        player)

                ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [local-map the-map local-player adventurer] ;; basically a way to loop
    ;;recursively

      (let [pl (status local-player)
      _  (println " what do you want to do?")
      command (read-line)]

      (recur local-map (respond pl (to-keywords command) ))  ;; jump back to a loop thing with updated info
                               ;; may need to retool to capture additional detail
                               ;; such as tools and whatnot
)))
