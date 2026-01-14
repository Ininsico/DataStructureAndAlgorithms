# Cryptographic utilities
import base64
import hashlib
import os
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP, AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
from config import RSA_KEY_SIZE, AES_KEY_SIZE, HASH_ALGORITHM

class CryptoUtils:
    @staticmethod
    def sha256_hash(data: bytes) -> str:
        """Generate SHA-256 hash"""
        return hashlib.sha256(data).hexdigest()
    
    @staticmethod
    def generate_rsa_keypair(key_size: int = RSA_KEY_SIZE):
        """Generate RSA keypair"""
        key = RSA.generate(key_size)
        private_key = key.export_key()
        public_key = key.publickey().export_key()
        print(f"✓ Generated RSA-{key_size} keypair")
        return private_key, public_key
    
    @staticmethod
    def rsa_encrypt(public_key_bytes: bytes, data: bytes) -> bytes:
        """Encrypt with RSA public key"""
        rsa_key = RSA.import_key(public_key_bytes)
        cipher = PKCS1_OAEP.new(rsa_key)
        return cipher.encrypt(data)
    
    @staticmethod
    def rsa_decrypt(private_key_bytes: bytes, encrypted_data: bytes) -> bytes:
        """Decrypt with RSA private key"""
        rsa_key = RSA.import_key(private_key_bytes)
        cipher = PKCS1_OAEP.new(rsa_key)
        return cipher.decrypt(encrypted_data)
    
    @staticmethod
    def generate_aes_key() -> bytes:
        """Generate random AES key"""
        return get_random_bytes(AES_KEY_SIZE)
    
    @staticmethod
    def aes_encrypt(key: bytes, plaintext: bytes) -> dict:
        """Encrypt with AES-CBC"""
        iv = get_random_bytes(16)
        cipher = AES.new(key, AES.MODE_CBC, iv=iv)
        ct = cipher.encrypt(pad(plaintext, AES.block_size))
        return {
            "iv": base64.b64encode(iv).decode(),
            "ciphertext": base64.b64encode(ct).decode()
        }
    
    @staticmethod
    def aes_decrypt(key: bytes, iv_b64: str, ct_b64: str) -> bytes:
        """Decrypt with AES-CBC"""
        iv = base64.b64decode(iv_b64)
        ct = base64.b64decode(ct_b64)
        cipher = AES.new(key, AES.MODE_CBC, iv=iv)
        return unpad(cipher.decrypt(ct), AES.block_size)
    
    @staticmethod
    def verify_integrity(data: bytes, expected_hash: str) -> bool:
        """Verify data integrity using SHA-256"""
        return CryptoUtils.sha256_hash(data) == expected_hash

# Global crypto instance
crypto = CryptoUtils()