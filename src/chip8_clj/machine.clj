(ns chip8-clj.machine)

(def memory-size 4096)
(def screen-size-x 64)
(def screen-size-y 32)
(def pc-offset 0x200)
(def zero-screen
  (vec (repeat 8 (vec (repeat 4 0)))))

(defn new-chip8
  []
  {:pc pc-offset
   :I 0
   :memory (vec (repeat memory-size 0))
   :screen zero-screen
   :v (vec (repeat 16 0))})

(defn copy-vector
  [to from offset]
  (loop [v to
         from from
         i 0]
        (if (empty? from)
          v
          (let [[element & tail] from]
            (recur (assoc v (+ i offset) element) tail (inc i))))))


(defn load-cartdridge
  [chip8 data]
  (update chip8 :memory #(copy-vector % data pc-offset)))
