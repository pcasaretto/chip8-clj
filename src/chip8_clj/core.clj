(ns chip8-clj.core
  (:require [chip8-clj.instructions :as instructions]
            [chip8-clj.machine :as machine])
  (:gen-class))


(defn fetch-opcode
  [chip8]
  (let [pc (:pc chip8)
        first-byte (Byte/toUnsignedInt (get-in chip8 [:memory pc]))
        second-byte (Byte/toUnsignedInt (get-in chip8 [:memory (inc pc)]))
        opcode (bit-or (bit-shift-left first-byte 8) second-byte)]
       (println
        (type first-byte)
        (type second-byte)
        (type opcode)
        (Long/toBinaryString first-byte)
        (Long/toBinaryString second-byte)
        (Long/toBinaryString opcode))
       opcode))

(defn decode
  [opcode]
  (let [prefix (bit-and 0xF000 opcode)]
       (cond
         (= 0x00E0 opcode) instructions/clear-screen
         (= 0x1000 prefix) instructions/jump
         (= 0x6000 prefix) instructions/set-v-register
         (= 0x7000 prefix) instructions/add-to-v-register
         (= 0xA000 prefix) instructions/set-index-register
         (= 0xD000 prefix) instructions/draw
         :else (throw (ex-info "Unrecognized opcode" {:opcode (format "%X" opcode)})))))


(defn tick
  [chip8]
  (let [opcode (fetch-opcode chip8)
        instruction (decode opcode)]
       (-> chip8
           (update :pc #(+ 2 %))
           (instruction opcode))))

(defn -main
  [& args]
  (let [f (java.io.File. (first args))
        ary (byte-array (.length f))
        is (java.io.FileInputStream. f)]
       (.read is ary)
       (.close is)
       (let [chip8 (-> (machine/new-chip8)
                       (machine/load-cartdridge (vec ary)))]
            (loop [chip8 chip8] (recur (tick chip8))))))

(-main "resources/roms/test_opcode.ch8")
(-main "resources/roms/ibm_logo.ch8")


(def ibmchip8
  (let [f (java.io.File. "resources/roms/ibm_logo.ch8")
        ary (byte-array (.length f))
        is (java.io.FileInputStream. f)]
       (.read is ary)
       (.close is)
       (let [chip8 (-> (machine/new-chip8)
                       (machine/load-cartdridge (vec ary)))]
            chip8)))
