#!/usr/bin/env ruby
require 'openssl'
require 'base64'

ALG = 'aes-128-cbc'
KEY = 'aaaaaaaaaaaaaaaa'
IV = 'aaaaaaaaaaaaaaaa'
FILENAME = 'out/encrypted'

Dir.mkdir('out') unless File.exists?('out')

# puts OpenSSL::Cipher.ciphers.select { |c| c.downcase.include?('aes') }

def encrypt(text)
  puts "encrypting #{text}"
  cipher = OpenSSL::Cipher::Cipher.new(ALG)
  cipher.encrypt
  cipher.key = KEY
  cipher.iv = IV
  encrypted = cipher.update(text)
  encrypted << cipher.final
  encrypted64 = Base64.encode64(encrypted)
  File.open(FILENAME, 'w') { |file| file.puts(encrypted64) }
end

def decrypt(filename = FILENAME)
  decipher = OpenSSL::Cipher::Cipher.new(ALG)
  decipher.decrypt
  decipher.key = KEY
  decipher.iv = IV
  encrypted64 = File.readlines(FILENAME).first
  puts "decrypting #{encrypted64}"
  encrypted = Base64.decode64(encrypted64)
  plain = decipher.update(encrypted)
  plain << decipher.final
  puts "plain is #{plain}"
end

encrypt('Banana split FTW')
decrypt
