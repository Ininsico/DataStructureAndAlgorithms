# Data models and schemas
from datetime import datetime
from typing import Dict, List, Optional

class User:
    def __init__(self, username: str, password_hash: str, role: str, 
                 private_key: bytes, public_key: bytes, created_at: datetime = None):
        self.username = username
        self.password_hash = password_hash
        self.role = role
        self.private_key = private_key
        self.public_key = public_key
        self.created_at = created_at or datetime.utcnow()
    
    def to_dict(self) -> dict:
        return {
            "username": self.username,
            "password_hash": self.password_hash,
            "role": self.role,
            "private_key": self.private_key,
            "public_key": self.public_key,
            "created_at": self.created_at
        }
    
    @staticmethod
    def from_dict(data: dict) -> 'User':
        return User(
            username=data["username"],
            password_hash=data["password_hash"],
            role=data["role"],
            private_key=data["private_key"],
            public_key=data["public_key"],
            created_at=data["created_at"]
        )

class Document:
    def __init__(self, doc_id: str, owner: str, original_filename: str,
                 storage_filename: str, iv_b64: str, aes_keys: Dict[str, str],
                 integrity_hash: str, uploaded_at: datetime = None):
        self.doc_id = doc_id
        self.owner = owner
        self.original_filename = original_filename
        self.storage_filename = storage_filename
        self.iv_b64 = iv_b64
        self.aes_keys = aes_keys  # {username: encrypted_aes_key_b64}
        self.integrity_hash = integrity_hash
        self.uploaded_at = uploaded_at or datetime.utcnow()
    
    def to_dict(self) -> dict:
        return {
            "doc_id": self.doc_id,
            "owner": self.owner,
            "original_filename": self.original_filename,
            "storage_filename": self.storage_filename,
            "iv_b64": self.iv_b64,
            "aes_keys": self.aes_keys,
            "integrity": self.integrity_hash,
            "uploaded_at": self.uploaded_at
        }
    
    @staticmethod
    def from_dict(data: dict) -> 'Document':
        return Document(
            doc_id=data["doc_id"],
            owner=data["owner"],
            original_filename=data["original_filename"],
            storage_filename=data["storage_filename"],
            iv_b64=data["iv_b64"],
            aes_keys=data["aes_keys"],
            integrity_hash=data["integrity"],
            uploaded_at=data["uploaded_at"]
        )

class Session:
    def __init__(self, username: str, role: str, private_key: bytes, public_key: bytes):
        self.username = username
        self.role = role
        self.private_key = private_key
        self.public_key = public_key