# Configuration settings
import os

# Database Configuration
DB_NAME = "information_security"
DB_HOST = "mongodb://localhost:27017/"
DB_COLLECTIONS = {
    "users": "users",
    "documents": "documents"
}

# Storage Configuration
STORAGE_DIR = "storage"
os.makedirs(STORAGE_DIR, exist_ok=True)

# Security Configuration
RSA_KEY_SIZE = 2048
AES_KEY_SIZE = 32  # 256 bits
HASH_ALGORITHM = "sha256"

# Role Configuration
ALLOWED_ROLES = ["Admin", "User"]
DEFAULT_ROLE = "User"