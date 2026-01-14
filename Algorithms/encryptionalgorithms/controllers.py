# Business logic controllers
import os
import uuid
import base64
from datetime import datetime
from database import db
from crypto_utils import crypto
from models import User, Document, Session
from config import STORAGE_DIR, DEFAULT_ROLE, ALLOWED_ROLES

class AuthController:
    @staticmethod
    def register(username: str, password: str, role: str = DEFAULT_ROLE) -> bool:
        """Register a new user"""
        if db.user_exists(username):
            raise ValueError("Username already exists")
        
        if role not in ALLOWED_ROLES:
            role = DEFAULT_ROLE
        
        # Hash password
        password_hash = crypto.sha256_hash(password.encode())
        print(f"✓ Password hashed with SHA-256: {password_hash[:16]}...")
        
        # Generate RSA keys
        print("Generating RSA keypair for secure key exchange...")
        private_key, public_key = crypto.generate_rsa_keypair()
        
        # Create user object
        user = User(
            username=username,
            password_hash=password_hash,
            role=role,
            private_key=private_key,
            public_key=public_key
        )
        
        # Save to database
        if db.insert_user(user):
            print("✓ RSA keys stored in database")
            return True
        return False
    
    @staticmethod
    def login(username: str, password: str) -> Session:
        """Authenticate user and create session"""
        user = db.find_user(username)
        if not user:
            return None
        
        # Verify password
        password_hash = crypto.sha256_hash(password.encode())
        if password_hash != user.password_hash:
            return None
        
        # Create session
        session = Session(
            username=user.username,
            role=user.role,
            private_key=user.private_key,
            public_key=user.public_key
        )
        
        return session

class DocumentController:
    @staticmethod
    def upload_document(session: Session, filepath: str) -> str:
        """Upload and encrypt a document"""
        # Validate file
        filepath = os.path.abspath(os.path.normpath(filepath))
        if not os.path.isfile(filepath):
            raise FileNotFoundError(f"File not found: {filepath}")
        
        # Read file
        with open(filepath, "rb") as f:
            plaintext = f.read()
        
        # Generate integrity hash
        integrity_hash = crypto.sha256_hash(plaintext)
        print(f"✓ Document integrity hash (SHA-256): {integrity_hash[:16]}...")
        
        # Generate AES key
        aes_key = crypto.generate_aes_key()
        print("✓ Generated 256-bit AES key for document encryption")
        
        # Encrypt document with AES
        encrypted_data = crypto.aes_encrypt(aes_key, plaintext)
        doc_id = str(uuid.uuid4())
        
        # Save encrypted file
        storage_path = os.path.join(STORAGE_DIR, f"{doc_id}.enc")
        with open(storage_path, "wb") as f:
            f.write(base64.b64decode(encrypted_data["ciphertext"]))
        
        # Encrypt AES key with user's RSA public key
        encrypted_aes_key = crypto.rsa_encrypt(session.public_key, aes_key)
        encrypted_aes_b64 = base64.b64encode(encrypted_aes_key).decode()
        print("✓ AES key encrypted with owner's RSA public key")
        
        # Create document object
        document = Document(
            doc_id=doc_id,
            owner=session.username,
            original_filename=os.path.basename(filepath),
            storage_filename=f"{doc_id}.enc",
            iv_b64=encrypted_data["iv"],
            aes_keys={session.username: encrypted_aes_b64},
            integrity_hash=integrity_hash
        )
        
        # Save to database
        if db.insert_document(document):
            return doc_id
        raise Exception("Failed to save document to database")
    
    @staticmethod
    def share_document(session: Session, doc_id: str, target_username: str) -> str:
        """Share document with another user"""
        # Validate target user
        target_user = db.find_user(target_username)
        if not target_user:
            raise ValueError("Target user does not exist")
        
        # Get document
        document = db.find_document(doc_id)
        if not document:
            raise ValueError("Document not found")
        
        # Check ownership
        if session.username != document.owner:
            raise PermissionError("Only document owner can share")
        
        print(f"Sharing document {doc_id} with {target_username}...")
        
        # Get owner's encrypted AES key
        encrypted_aes_owner = base64.b64decode(document.aes_keys[document.owner])
        
        # Decrypt AES key with owner's private key
        aes_key = crypto.rsa_decrypt(session.private_key, encrypted_aes_owner)
        print("✓ Decrypted AES key with owner's private RSA key")
        
        # Re-encrypt AES key with target user's public key
        encrypted_aes_target = crypto.rsa_encrypt(target_user.public_key, aes_key)
        encrypted_aes_target_b64 = base64.b64encode(encrypted_aes_target).decode()
        print("✓ Re-encrypted AES key with target user's public RSA key")
        
        # Update document access
        if db.update_document_aes_keys(doc_id, target_username, encrypted_aes_target_b64):
            return f"Document shared successfully with {target_username}"
        
        raise Exception("Failed to share document")
    
    @staticmethod
    def list_documents(session: Session):
        """List all documents accessible to user"""
        documents = db.find_user_documents(session.username)
        return [
            {
                "doc_id": doc.doc_id,
                "filename": doc.original_filename,
                "owner": doc.owner,
                "uploaded_at": doc.uploaded_at
            }
            for doc in documents
        ]
    
    @staticmethod
    def download_document(session: Session, doc_id: str, output_path: str) -> str:
        """Download and decrypt a document"""
        # Get document
        document = db.find_document(doc_id)
        if not document:
            raise ValueError("Document not found")
        
        # Check access permission
        if session.username not in document.aes_keys:
            raise PermissionError("Access denied")
        
        print(f"Downloading document {doc_id}...")
        
        # Get user's encrypted AES key
        encrypted_aes_b64 = document.aes_keys[session.username]
        encrypted_aes_key = base64.b64decode(encrypted_aes_b64)
        
        # Decrypt AES key with user's private key
        aes_key = crypto.rsa_decrypt(session.private_key, encrypted_aes_key)
        print("✓ Decrypted AES key with user's private RSA key")
        
        # Read encrypted file
        storage_path = os.path.join(STORAGE_DIR, document.storage_filename)
        if not os.path.isfile(storage_path):
            raise FileNotFoundError("Encrypted file missing")
        
        with open(storage_path, "rb") as f:
            raw_ciphertext = f.read()
        ciphertext_b64 = base64.b64encode(raw_ciphertext).decode()
        
        # Decrypt document with AES
        plaintext = crypto.aes_decrypt(aes_key, document.iv_b64, ciphertext_b64)
        print("✓ Decrypted document with AES key")
        
        # Verify integrity
        if not crypto.verify_integrity(plaintext, document.integrity_hash):
            raise ValueError("Integrity check failed! File may be corrupted")
        print("✓ Integrity verified with SHA-256 hash")
        
        # Prepare output path
        if os.path.isdir(output_path):
            original_name = document.original_filename
            output_path = os.path.join(output_path, f"decrypted_{original_name}")
        
        os.makedirs(os.path.dirname(output_path) if os.path.dirname(output_path) else ".", exist_ok=True)
        
        # Save decrypted file
        with open(output_path, "wb") as f:
            f.write(plaintext)
        
        return output_path