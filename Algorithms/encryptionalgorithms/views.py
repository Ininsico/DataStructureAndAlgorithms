# CLI interface views
import os
from controllers import AuthController, DocumentController
from models import Session

class CLIView:
    @staticmethod
    def clear_screen():
        os.system("cls" if os.name == "nt" else "clear")
    
    @staticmethod
    def show_main_menu():
        CLIView.clear_screen()
        print("=== Secure Document Management System (SDMS) ===")
        print("Features: SHA-256 Hashing • RSA Encryption • AES Encryption • Role-Based Access")
        print("1) Register")
        print("2) Login")
        print("3) Exit")
        return input("Choose: ").strip()
    
    @staticmethod
    def show_user_menu(session: Session):
        CLIView.clear_screen()
        print(f"Logged in as: {session.username} ({session.role})")
        print("Your RSA keys are loaded for secure operations")
        print("1) Upload document (AES Encryption)")
        print("2) List accessible documents")
        print("3) Download document (AES Decryption)")
        print("4) Share document (RSA Key Exchange)")
        print("5) Logout")
        return input("Choose: ").strip()
    
    @staticmethod
    def show_register_form():
        username = input("Username: ").strip()
        password = input("Password: ").strip()
        role = input("Role (Admin/User) [default User]: ").strip()
        return username, password, role
    
    @staticmethod
    def show_login_form():
        username = input("Username: ").strip()
        password = input("Password: ").strip()
        return username, password
    
    @staticmethod
    def show_upload_form():
        path = input("📁 Path to file to upload: ").strip()
        return os.path.abspath(os.path.normpath(path))
    
    @staticmethod
    def show_download_form():
        doc_id = input("📥 Document ID to download: ").strip()
        out_path = input("📁 Output path: ").strip()
        return doc_id, os.path.abspath(out_path)
    
    @staticmethod
    def show_share_form():
        doc_id = input("🔗 Document ID to share: ").strip()
        target = input("👥 Share with (username): ").strip()
        return doc_id, target
    
    @staticmethod
    def display_documents(documents):
        if not documents:
            print("📄 No documents available.")
        else:
            print("📄 Accessible documents:")
            for doc in documents:
                print(f"   {doc['doc_id']} | {doc['filename']} | owner: {doc['owner']}")
    
    @staticmethod
    def show_message(message: str, is_error: bool = False):
        prefix = "✗" if is_error else "✓"
        print(f"{prefix} {message}")
    
    @staticmethod
    def wait_for_enter():
        input("Press Enter to continue...")