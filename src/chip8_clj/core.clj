(ns chip8-clj.core
  (:require [chip8-clj.instructions :as instructions])
  (:gen-class))

(defn new-chip8
  []
  {:pc 0
   :memory (vec (repeat 4096 0))
   :screen (vec (repeat (* 64 32) 0))
   :v (vec (repeat 16 0))})


(defn fetch-opcode
  [chip8]
  (let [pc (:pc chip8)
        first-byte (get (:memory chip8) pc)
        second-byte (get (:memory chip8) (inc pc))
        opcode (bit-or (bit-shift-left first-byte 8) second-byte)]
       [(update chip8 :pc #(+ 2 %)) opcode]))

(defn decode
  [opcode]
  (let [prefix (bit-and 0xF000 opcode)]
       (cond
         (= 0x00E0 opcode) instructions/clear-screen
         (= 0x1000 prefix) instructions/jump
         (= 0x6000 prefix) instructions/set-register
         (= 0x7000 prefix) instructions/add-to-register
         (= 0xA000 prefix) instructions/set-index-register
         (= 0xD000 prefix) instructions/draw
         :else (throw "Unrecognized opcode"))))


(defn tick
  [chip8]
  (let [[chip8 opcode ] (fetch-opcode chip8)
        instruction (decode opcode)
        chip8 (instruction chip8 opcode)]
       chip8))

(defn -main
  [& args]

  (loop [chip8 (new-chip8)] (recur (tick chip8))))
