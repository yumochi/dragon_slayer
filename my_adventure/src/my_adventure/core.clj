(ns my-adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))

(def the-map
  { :entrance     {:desc "In front of you stands the castle of Durham. Its walls
                      are charred with the dragon’s flame breath, and you can still see the
                      distinct claw marks of the beast had left behind. Somehow you feel like from
                      this point on there is no return. It is your duty to save the kingdom!
                      Venture forward brave adventurer."
                  :title "at the Entrance"
                  :dir {
                        :north :courtyard}
                  :contents []}

    :courtyard    {:desc "You’ve crossed drawbridge into the courtyard. The once
                      lush grass is not blackened into soot, and down the path
                      stands the massive door to the ransacked castle."
                  :title "in the Courtyard"
                  :dir {
                        :north :foyer
                        :south :entrance}
                  :contents []}

    :foyer        {:desc "I moved past the courtyard, and finally came into the
                          foyer. I can see the damage the dragon did as he
                          forced his way into the castle. He seemed to have left
                           behind a few of his scales by accident, and I
                          pick one up and notice that it's harder than any armor
                          I have ever seen. It is clear that the mighty beast’s
                          size was too great for this dwelling of man. Bálormr
                          must be at least twenty feet tall! I will need to find
                           a suitable weapon to defeat the beast."
                  :title "in the Foyer"
                  :dir {
                  :west  :servantQuarter
                  :east  :mageTower
                  :north :greatHall
                  :south :courtyard}
                  :contents []}

    :greatHall    {:desc "As I finally I stepped into the famed Hall of the
                          Durham Castle, my mind drift back to my childhood
                          briefly, to the tales I heard of this place. Yet,
                          instead of a chamber lit by a thousand candles, I
                          only found the destruction left in Bálormr’s wake.
                          Instead of a bountiful feast fit for one of the most
                          powerful King in the realm, I can only hear the hiss
                          of the wind blowing through hole Bálormr tore through
                          the ceiling. Ahead of me, I can see the king’s throne,
                          further up,  I see the entrance to the inner keep."
                   :title "in the Great HAll"
                   :dir {
                   :north :innerKeep
                   :south :foyer}
                   :contents []}

    :innerKeep    {:desc "The the room looks like it has been scorched and
                            torn apart. The dragon must have been through here!
                             I must be getting closer. Better keep an eye out."
                    :title "in the Inner Keep"
                    :dir {
                    :north :keepHallway
                    :south :greatHall
                    :east  :potionChamber
                    :west  :servantQuarter}
                    :contents []}

    :keepHallway   {:desc "I hear my steps echo through the hallway as I walk
                          to the other end. It feels so ominous in the dark, a
                          shiver runs down my spine. However, I keep moving,
                          not letting my guard down."
                    :title "in the Keep Hallway"
                    :dir {
                      :north :keeproom1 ;;want this to be blocked at the moment
                      :south :innerKeep
                    }
                    :contents []}

    :keeproom1   {:desc "As I step inside, I smell the burning of wood. There
                        are glowing embers along the walls; the dragon can’t be
                        much further now"
                  :title "in the first Keep Room"
                  :dir {
                    :north :keeproom2
                    :south :keepHallway}
                  :contents []}

    :keeproom2   {:desc "I start feeling the heat now, sweat beading down my
                          face. I start to grow anxious as I continue forward,
                          unsure of what lies ahead"
                  :title "in the second Keep Room"
                  :dir {
                    :north :keeproom3
                    :south :keeproom2}
                  :contents []}

    :keeproom3   {:desc "The heat in this room is sweltering, the flickering of
                        flames eating away at the walls and floorboards. I
                        carefully venture forward, but then suddenly, the floor
                        behind me falls out! There is no way back now, only
                        forward. I gulp and prepare myself for the worst,
                        the dragon is now ahead."
                  :title "in the third Keep Room"
                  :dir {
                  :north :bossroom
                  }
                  :contents []}

    :bossroom   {:desc "Through the final door, I step into a large chamber,
                        the heat emanating from it is so strong. In front of me
                        lies the gigantic dragon, his powerful body standing in
                        front of me. His armored scales glisten in the light of
                        the flames, and his eyes stare down at me. Without
                        wasting any time, I charge to the dragon, giving it
                        my all!"
                  :title "in the Final Battle"
                  :dir {
                  }
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


(defn respond [player command]
  (match command
                [:look] (update-in player [:seen] #(disj % (-> player :location)))
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
