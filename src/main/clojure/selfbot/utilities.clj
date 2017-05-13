(ns selfbot.utilities)

(def numbers (atom nil))

(defn numToStr
  [number]
  (if (not (number? number))
    (if (string? number)
      (numToStr (Integer/parseInt number))
      "zero")
    (do
      (when (nil? @numbers)
        (reset! @numbers (hash-map 0 "zero" 1 "one" 2 "two" 3 "three" 4 "four" 5 "five" 6 "six" 7 "seven" 8 "eight" 9 "nine")))
      (@numbers number)
      ))
  )