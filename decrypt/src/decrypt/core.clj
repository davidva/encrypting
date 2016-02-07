(ns decrypt.core
  (:gen-class)
  (:require [buddy.core.crypto :as crypto]
            [buddy.core.codecs :as codecs]
            [clojure.data.codec.base64 :as b64]
            [buddy.core.bytes :as bytes]))

(def secret-key (codecs/str->bytes "aaaaaaaaaaaaaaaa"))

(def cipher (crypto/block-cipher :aes :cbc))

(def filename "../out/encrypted")

(defn- decrypt [text iv]
  (println "decrypting" text)
  (let [input (b64/decode (codecs/str->bytes text))
        iv    (bytes/slice (b64/decode (codecs/str->bytes iv)) 0 16)]
    (println (count iv))
    (-> (crypto/decrypt-cbc (crypto/block-cipher :aes :cbc) input secret-key iv)
        (codecs/bytes->str))))

(defn- decrypt-from-file [filename]
  (let [content (clojure.string/split (slurp filename) #"--")
        text    (first content)
        iv      (last content)]
    (decrypt text iv)))

(defn -main [& args]
  (println "plain is" (decrypt-from-file filename)))
