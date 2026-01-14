# Main application entry point
from database import db
import os
from controllers import AuthController, DocumentController
from views import CLIView

class SDMSApplication:
    def __init__(self):
        self.session = None
    
    def run(self):
        """Main application loop"""
        while True:
            if not self.session:
                self.handle_main_menu()
            else:
                self.handle_user_menu()
    
    def handle_main_menu(self):
        """Handle main menu options"""
        choice = CLIView.show_main_menu()
        
        if choice == "1":
            self.handle_registration()
        elif choice == "2":
            self.handle_login()
        elif choice == "3":
            self.handle_exit()
        else:
            CLIView.show_message("Invalid choice", is_error=True)
            CLIView.wait_for_enter()
    
    def handle_registration(self):
        """Handle user registration"""
        try:
            username, password, role = CLIView.show_register_form()
            if AuthController.register(username, password, role):
                CLIView.show_message("Registered successfully with RSA keypair!")
            else:
                CLIView.show_message("Registration failed", is_error=True)
        except Exception as e:
            CLIView.show_message(str(e), is_error=True)
        CLIView.wait_for_enter()
    
    def handle_login(self):
        """Handle user login"""
        try:
            username, password = CLIView.show_login_form()
            self.session = AuthController.login(username, password)
            if self.session:
                CLIView.show_message("Login successful! RSA keys loaded.")
            else:
                CLIView.show_message("Login failed. Check credentials.", is_error=True)
        except Exception as e:
            CLIView.show_message(str(e), is_error=True)
        CLIView.wait_for_enter()
    
    def handle_exit(self):
        """Exit the application"""
        print("● Exiting SDMS. Goodbye!")
        db.close()
        exit(0)
    
    def handle_user_menu(self):
        """Handle user menu options"""
        choice = CLIView.show_user_menu(self.session)
        
        if choice == "1":
            self.handle_upload()
        elif choice == "2":
            self.handle_list_documents()
        elif choice == "3":
            self.handle_download()
        elif choice == "4":
            self.handle_share()
        elif choice == "5":
            self.handle_logout()
        else:
            CLIView.show_message("Invalid choice", is_error=True)
            CLIView.wait_for_enter()
    
    def handle_upload(self):
        """Handle document upload"""
        try:
            filepath = CLIView.show_upload_form()
            if not os.path.isfile(filepath):
                CLIView.show_message(f"File not found: {filepath}", is_error=True)
            else:
                doc_id = DocumentController.upload_document(self.session, filepath)
                CLIView.show_message(f"Uploaded. Document ID: {doc_id}")
        except Exception as e:
            CLIView.show_message(str(e), is_error=True)
        CLIView.wait_for_enter()
    
    def handle_list_documents(self):
        """List accessible documents"""
        try:
            documents = DocumentController.list_documents(self.session)
            CLIView.display_documents(documents)
        except Exception as e:
            CLIView.show_message(str(e), is_error=True)
        CLIView.wait_for_enter()
    
    def handle_download(self):
        """Handle document download"""
        try:
            doc_id, output_path = CLIView.show_download_form()
            saved_path = DocumentController.download_document(self.session, doc_id, output_path)
            CLIView.show_message(f"Downloaded and decrypted to: {saved_path}")
        except Exception as e:
            CLIView.show_message(str(e), is_error=True)
        CLIView.wait_for_enter()
    
    def handle_share(self):
        """Handle document sharing"""
        try:
            doc_id, target_user = CLIView.show_share_form()
            result = DocumentController.share_document(self.session, doc_id, target_user)
            CLIView.show_message(result)
        except Exception as e:
            CLIView.show_message(str(e), is_error=True)
        CLIView.wait_for_enter()
    
    def handle_logout(self):
        """Handle user logout"""
        self.session = None
        CLIView.show_message("Logged out successfully.")
        CLIView.wait_for_enter()

def main():
    """Application entry point"""
    print("=" * 50)
    print("Secure Document Management System (SDMS)")
    print("=" * 50)
    
    app = SDMSApplication()
    
    try:
        app.run()
    except KeyboardInterrupt:
        print("\n\n● Application terminated by user")
        db.close()
    except Exception as e:
        print(f"\n✗ Unexpected error: {e}")
        db.close()

if __name__ == "__main__":
    # Ensure storage directory exists
    from config import STORAGE_DIR
    if not os.path.exists(STORAGE_DIR):
        os.makedirs(STORAGE_DIR)
    
    main()