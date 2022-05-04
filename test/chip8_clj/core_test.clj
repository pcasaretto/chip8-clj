(ns chip8-clj.core-test
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest fetch-opcode-test
  (testing "it always increments pc by 2"
    (let [chip8 (new-chip8)
          old-pc (:pc chip8)
          [chip8 _] (fetch-opcode chip8)
          new-pc (:pc chip8)]
         (is (= 2 (- new-pc old-pc)))))
  (testing "simple spec"
    (let [chip8 {:pc 0 :memory [0 1]}
          [_ opcode] (fetch-opcode chip8)]
         (is (= 1 opcode))))
  (testing "simple spec"
    (let [chip8 {:pc 1 :memory [0 1 2]}
          [_ opcode] (fetch-opcode chip8)]
         (is (= 258 opcode)))))
