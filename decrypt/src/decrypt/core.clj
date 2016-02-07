(ns decrypt.core
  (:gen-class)
  (:require [buddy.core.crypto :as crypto]
            [buddy.core.codecs :as codecs]
            [clojure.data.codec.base64 :as b64]))

; (def iv (nonce/random-bytes 16))   ;; 16 bytes random iv
(def iv (codecs/str->bytes "aaaaaaaaaaaaaaaa"))
; (def secret-key (hash/sha256 "mysecret")) ;; 32 bytes key
(def secret-key (codecs/str->bytes "aaaaaaaaaaaaaaaa"))

(def cipher (crypto/block-cipher :aes :cbc))

(def filename "../out/encrypted-clojure")

(defn- encrypt [text]
  (println "encrypting" text)
  (let [input  (codecs/str->bytes text)]
    (-> (crypto/encrypt-cbc cipher input secret-key iv)
        (b64/encode)
        (codecs/bytes->str))))

(defn- encrypt-and-save [text]
  (->> (encrypt text)
       (spit filename)))

(defn- print-and-return [text]
  (println "decrypting" text)
  text)

(defn- decrypt [filename]
  (let [input (-> (slurp filename) (print-and-return) (codecs/str->bytes) (b64/decode))]
    (-> (crypto/decrypt-cbc (crypto/block-cipher :aes :cbc) input secret-key iv)
        (codecs/bytes->str))))

(defn -main [& args]
  (encrypt-and-save "Banana split FTW")
  (println "plain is" (decrypt filename)))
