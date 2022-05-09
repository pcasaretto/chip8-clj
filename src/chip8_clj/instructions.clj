(ns chip8-clj.instructions
  (:require [chip8-clj.machine :as machine]))

(defn clear-screen
  [chip8 _]
  (assoc chip8 :screen (vec (repeat (* machine/screen-size-x machine/screen-size-y) 0))))

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
  "DXYN: Draw N pixel tall sprite from memory location I
  into screen coordinates V[X], V[Y]"
  [chip8 opcode]
  (let [{I :I memory :memory v :v} chip8
        x (-> opcode (bit-and 0x0F00) (bit-shift-right 8))
        y (-> opcode (bit-and 0x00F0) (bit-shift-right 4))
        n (-> opcode (bit-and 0x000F))
        screen-x (rem (get v x) machine/screen-size-x)
        screen-y (rem (get v y) machine/screen-size-x)]
       (loop [chip8 chip8
              i 0]
         (if
           (>= i n) chip8
           (do
             (let [sprite (get-in chip8 [:memory (+ I i)])
                   _ (println screen-x screen-y)
                   chip8 (update-in chip8 [:screen screen-x (+ screen-y i)] #(bit-xor % sprite))]
                  (recur chip8 (inc i))))))))

;
; start := I
; for i := 0; i < n; i++ {
;   take memory at memory[I+i]
;   XOR with screen at screenX,screenY+i
;   if memory[I+i] AND pixel > 0, set V[0xF]
; }
