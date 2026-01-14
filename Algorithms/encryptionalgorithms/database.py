# Database connection and operations
from pymongo import MongoClient
from pymongo.errors import ConnectionFailure
from config import DB_HOST, DB_NAME, DB_COLLECTIONS
import models

class Database:
    def __init__(self):
        self.client = None
        self.db = None
        self.users_col = None
        self.documents_col = None
        self.connect()
    
    def connect(self):
        """Establish connection to MongoDB"""
        try:
            self.client = MongoClient(DB_HOST)
            self.db = self.client[DB_NAME]
            self.users_col = self.db[DB_COLLECTIONS["users"]]
            self.documents_col = self.db[DB_COLLECTIONS["documents"]]
            print("✓ Connected to MongoDB")
            self.create_indexes()
        except ConnectionFailure as e:
            print(f"✗ MongoDB connection failed: {e}")
            raise
    
    def create_indexes(self):
        """Create necessary indexes"""
        self.users_col.create_index("username", unique=True)
        self.documents_col.create_index("doc_id", unique=True)
        self.documents_col.create_index("owner")
        print("✓ Database indexes created")
    
    # User Operations
    def insert_user(self, user: models.User) -> bool:
        """Insert a new user"""
        try:
            self.users_col.insert_one(user.to_dict())
            return True
        except Exception as e:
            print(f"✗ Error inserting user: {e}")
            return False
    
    def find_user(self, username: str) -> models.User:
        """Find a user by username"""
        data = self.users_col.find_one({"username": username})
        if data:
            return models.User.from_dict(data)
        return None
    
    def user_exists(self, username: str) -> bool:
        """Check if user exists"""
        return self.users_col.find_one({"username": username}) is not None
    
    # Document Operations
    def insert_document(self, document: models.Document) -> bool:
        """Insert a new document"""
        try:
            self.documents_col.insert_one(document.to_dict())
            return True
        except Exception as e:
            print(f"✗ Error inserting document: {e}")
            return False
    
    def find_document(self, doc_id: str) -> models.Document:
        """Find a document by ID"""
        data = self.documents_col.find_one({"doc_id": doc_id})
        if data:
            return models.Document.from_dict(data)
        return None
    
    def update_document_aes_keys(self, doc_id: str, username: str, encrypted_aes_key: str) -> bool:
        """Update document's AES keys for sharing"""
        try:
            self.documents_col.update_one(
                {"doc_id": doc_id},
                {"$set": {f"aes_keys.{username}": encrypted_aes_key}}
            )
            return True
        except Exception as e:
            print(f"✗ Error updating document: {e}")
            return False
    
    def find_user_documents(self, username: str):
        """Find all documents accessible to a user"""
        cursor = self.documents_col.find({
            "$or": [
                {"owner": username},
                {f"aes_keys.{username}": {"$exists": True}}
            ]
        })
        return [models.Document.from_dict(doc) for doc in cursor]
    
    def cleanup(self):
        """Clean up database (for testing)"""
        try:
            self.users_col.drop()
            self.documents_col.drop()
            print("✓ Database cleaned up")
        except:
            pass
    
    def close(self):
        """Close database connection"""
        if self.client:
            self.client.close()
            print("✓ Database connection closed")

# Global database instance
db = Database()