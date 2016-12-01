(ns my-adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))

(def the-map
  { :entrance {:desc "In front of you stands the castle of Durham. Its walls
                      are charred with the dragon’s flame breath, and you can still see the
                      distinct claw marks of the beast had left behind. Somehow you feel like from
                      this point on there is no return. It is your duty to save the kingdom!
                      Venture forward brave adventurer."
                  :title "at the entrance"
                  :dir {
                        :north :courtyard}
                  :contents []}

    :courtyard {:desc "The walls are burned and the hall still reeks of burnt flesh (smells like BBQ chicken...well rotting BBQ chicken)"
                  :title "in the entrance"
                  :dir {
                        :south :entrance}
                  :contents []}

    :grue-pen {:desc "It is very dark. You are about to be eaten by a grue."
                  :title "in the grue pen"
                  :dir {:south :courtyard}
                  :contents []}})


(def adventurer
  { :location :entrance
    :inventory #{}
    :seen #{}})

(defn status [player]
  (let [location (player :location)]
   (print (str "You are " (-> the-map location :title) ". "))
   (when-not ((player :seen) location)
    (print (-> the-map location :desc)))
   (update-in player [:seen] #(conj % location))))


(defn to-keywords [commands]
  ;; should be a list of strings
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn go [dir player]
  (let [location (player :location)
        dest (->> the-map location :dir dir)]
      (if (nil? dest)
          (do (println "You can't go that way.")
              player)
       (assoc-in player [:location] dest))))

(defn search [player])
  ;;If there is an item in the room, print the item's description.
  ;;Move the item into the player's inventory.
  ;;Remove the item from the room.
  ;;If there is no item, print "There is nothing to be found."

(defn help [player])
  ;;Print out possible commands based on the player's location.

(defn respond [player command]
  (match command
                [:look] (update-in player [:seen] #(disj % (-> player :location)))
                [:search] (search player)
                [:help] (help)
                [:north] (go :north player)
                [:south] (go :south player)
                [:east] (go :east player)
                [:west] (go :west player)
                _ (do (println "I don't understand you.")
                      player)))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (println "It has been 1000 moons since the land of Durham has been shrouded under the
    wings of Bálormr Flame-Shroud The Repugnant! But the mighty dragon stalks our land
    again. He breathes vile, sulfurous fumes, poisoning all! His scales glow as hot as
    flames, and he brings death without mercy. Even the King was not safe from the dragon's
    wrath. Bálormr has sacked the King's keep in Deira and captured the King's young daughter.
    Now the kingdom teeters on the edge of death, and the people of Durham wastes away as
    the dragon ravages the land.")

  (println)

  (println "Yet all hope is not lost, and a lone rider has appeared, carrying the sign of
    the dragon. The rider has arrived at the entrance of the King's castle in Deira, which
    now lay deserted. The rider slowly dismounts and enters the castle from the drawbridge.
    You are the rider. It is up to you to save the land of Durham and restore peace and
    harmony once again!")

  (println)

  (loop [local-map the-map local-player adventurer] ;; basically a way to loop
    ;;recursively

      (let [pl (status local-player)
            _  (println " what do you want to do?")
            command (read-line)]

       (recur local-map (respond pl (to-keywords command))))))  ;; jump back to a loop thing with updated info
                               ;; may need to retool to capture additional detail
                               ;; such as tools and whatnot
