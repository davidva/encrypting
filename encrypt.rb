#!/usr/bin/env ruby
require 'openssl'
require 'base64'

ALG = 'aes-128-cbc'
KEY = 'aaaaaaaaaaaaaaaa'
FILENAME = 'out/encrypted'

Dir.mkdir('out') unless File.exists?('out')

def encrypt(text)
  puts "encrypting #{text}"
  cipher = OpenSSL::Cipher::Cipher.new(ALG)
  cipher.encrypt
  cipher.key = KEY
  cipher.iv = iv = cipher.random_iv
  puts iv.size
  encrypted = cipher.update(text)
  encrypted << cipher.final
  encrypted64 = Base64.encode64(encrypted).strip
  File.open(FILENAME, 'w') { |file| file.puts("#{encrypted64}--#{Base64.encode64(iv)}".strip) }
end

def decrypt(filename = FILENAME)
  (encrypted64, iv) = File.readlines(FILENAME).first.split('--')
  decipher = OpenSSL::Cipher::Cipher.new(ALG)
  decipher.decrypt
  decipher.key = KEY
  decipher.iv = Base64.decode64(iv)
  puts "decrypting #{encrypted64}"
  encrypted = Base64.decode64(encrypted64)
  plain = decipher.update(encrypted)
  plain << decipher.final
  puts "plain is #{plain}"
end

encrypt('Banana split FTW')
decrypt
