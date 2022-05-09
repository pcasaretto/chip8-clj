(ns chip8-clj.instructions-test
  (:require [chip8-clj.instructions :as sut]
            [chip8-clj.machine :as machine]
            [clojure.test :as t]))

(t/deftest jump-test
  (t/testing "pc jump"
    (let [chip8 (-> (machine/new-chip8)
                    (sut/jump 0x0EAB))]
         (t/is (= 0xEAB (get chip8 :pc))))))

(t/deftest set-v-register-test
  (t/testing "set V register"
    (let [chip8 (-> (machine/new-chip8)
                    (sut/set-v-register 0x0EAB))]
         (t/is (= 0xAB (get-in chip8 [:v 14]))))
    (let [chip8 (-> (machine/new-chip8)
                    (sut/set-v-register 0x0981))]
         (t/is (= 0x81 (get-in chip8 [:v 9]))))))

(t/deftest set-index-register-test
  (t/testing "set index register"
    (let [chip8 (-> (machine/new-chip8)
                    (sut/set-index-register 0x0EAB))]
         (t/is (= 0xEAB (get chip8 :I))))))

(t/deftest add-to-v-register-test
  (t/testing "add value to V register"
    (let [chip8 (-> (machine/new-chip8)
                    (assoc-in [:v 14] 12)
                    (sut/add-to-v-register 0x0E01))]
         (t/is (= 13 (get-in chip8 [:v 14]))))))

(t/deftest draw-test
  (t/testing ""
    (let [chip8 (-> (machine/new-chip8)
                    (assoc :memory [0xFF 0xFA 0XFB])
                    (assoc :I 0)
                    (assoc-in [:v 0xE] 0)
                    (assoc-in [:v 0x0] 0)
                    (sut/draw 0x0E03))]
         (println (:screen chip8)))))
