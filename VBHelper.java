import binascii
import hashlib
import os
import struct
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

# Read the binary data from the info.vbe file
with open('info.vbe', 'rb') as f:
    data = f.read()

# Generate the key using PBKDF2WithHmacSHA1 algorithm
password = b'hs;d,hghdk[;'
salt = b'\x12\x0a\x0b\x0c\x0d\x0e\x0f\x10'
iterations = 19
key_length = 16
key = hashlib.pbkdf2_hmac('sha1', password, salt, iterations, key_length)

# Get the absolute path of the info.vbe file
file_path = os.path.abspath('info.vbe')

# Create the initialization vector (IV) using the file path
file_size = os.path.getsize(file_path)
file_mod_time = os.path.getmtime(file_path)
iv = bytearray([117, 115, 111, 102, 103, 104, 111, 111, 108, 122, 120, 119, 111, 91, 110, 109])
iv.extend(struct.pack('<QQ', file_size, int(file_mod_time)))

# Decrypt the binary data using the generated key and the m3425h method
cipher = AES.new(key, AES.MODE_CBC, iv)
decrypted_data = cipher.decrypt(data)

# Check for padding
if len(decrypted_data) % AES.block_size != 0:
    raise ValueError("Padding is incorrect.")
padding_length = decrypted_data[-1]
if padding_length > AES.block_size or (padding_length > 0 and not all(b == padding_length for b in decrypted_data[-padding_length:])):
    raise ValueError("Padding is incorrect.")

# Remove the padding from the decrypted data
unpadded_data = decrypted_data[:-padding_length]

# Try to decode the decrypted data as UTF-8
try:
    decoded_data = unpadded_data.decode('utf-8')
except UnicodeDecodeError:
    # If decoding as UTF-8 fails, try decoding as ISO-8859-1
    decoded_data = unpadded_data.decode('iso-8859-1')

# Save the decoded data to a file named dec_info.vbe
with open('dec_info.vbe', 'w', encoding='utf-8') as f:
    f.write(decoded_data)