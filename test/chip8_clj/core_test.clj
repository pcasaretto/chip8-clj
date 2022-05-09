(ns chip8-clj.core-test
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]
            [chip8-clj.machine :as machine]))

(deftest fetch-opcode-test
  (testing "simple spec"
    (let [chip8 {:pc 0 :memory [0 1]}
          opcode (fetch-opcode chip8)]
         (is (= 1 opcode))))
  (testing "simple spec"
    (let [chip8 {:pc 1 :memory [0 1 2]}
          opcode (fetch-opcode chip8)]
         (is (= 258 opcode))))
  (testing "simple spec"
    (let [
          memory (->
                  (vec (repeat 4096 0))
                  (assoc 0x201 0xE0))
          chip8 {:pc 0x200 :memory memory}
          opcode (fetch-opcode chip8)]
         (is (= 0x00E0 opcode)))))
