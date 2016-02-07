(ns decrypt.core
  (:gen-class)
  (:require [buddy.core.crypto :as crypto]
            [buddy.core.codecs :as codecs]
            [clojure.data.codec.base64 :as b64]))

(def algorithm :aes128-cbc-hmac-sha256)

; (def iv (nonce/random-bytes 16))   ;; 16 bytes random iv
(def iv (codecs/str->bytes "aaaaaaaaaaaaaaaa"))
; (def secret-key (hash/sha256 "mysecret")) ;; 32 bytes key
(def secret-key (codecs/str->bytes "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))

(def filename "../out/encrypted-clojure")

(defn- encrypt [text]
  (println "encrypting" text)
  (-> (codecs/str->bytes text)
      (crypto/encrypt secret-key iv {:algorithm algorithm})
      (b64/encode)
      (codecs/bytes->str)))

(defn- print-and-return [text]
  (println "decrypting" text)
  text)

(defn- decrypt [filename]
  (-> (slurp filename)
      (print-and-return)
      (codecs/str->bytes)
      (b64/decode)
      (crypto/decrypt secret-key iv {:algorithm algorithm})
      (codecs/bytes->str)))

(def encrypted-text (slurp "../encrypted"))

(defn -main [& args]
  (->> (encrypt "Banana split FTW") (spit filename))
  (println "plain is" (decrypt filename)))
