(ns chip8-clj.instructions)


(defn clear-screen
  [chip8 _]
  (assoc chip8 :screen (vec (repeat (* 64 32) 0))))

(defn jump
  "1NNN Set PC to NNN"
  [chip8 opcode]
  (let [{pc :pc} chip8
        operand (bit-and 0x0FFF opcode)]
       (assoc chip8 :pc operand)))

(defn add-to-v-register
  "7XNN Add the value NN to VX."
  [chip8 opcode]
  (let [register (bit-shift-right (bit-and 0x0F00 opcode) 8)
        operand (bit-and 0x00FF opcode)]
       (update-in chip8 [:v register] #(rem (+ % operand) 0xFF))))

(defn set-v-register
  "6XNN Set the value NN to X."
  [chip8 opcode]
  (let [register (bit-shift-right (bit-and 0x0F00 opcode) 8)
        operand (bit-and 0x00FF opcode)]
       (assoc-in chip8 [:v register] operand)))

(defn set-index-register
  "ANNN Set the value NNN to I."
  [chip8 opcode]
  (let [operand (bit-and 0x0FFF opcode)]
       (assoc chip8 :I operand)))

(defn draw
  [chip8 opcode])
