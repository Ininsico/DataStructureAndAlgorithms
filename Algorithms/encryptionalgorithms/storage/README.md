# Secure Document Management System (SDMS)

## Overview
A secure document management system implementing hybrid encryption with AES-256 for documents and RSA-2048 for key exchange, featuring SHA-256 integrity verification and role-based access control.

## Features
- **SHA-256 Password Hashing**: Secure user authentication
- **RSA-2048 Key Pairs**: Individual key pairs for each user
- **AES-256 Encryption**: Fast document encryption
- **Hybrid Encryption**: Combines AES (symmetric) and RSA (asymmetric)
- **Document Sharing**: Secure key re-encryption for sharing
- **Integrity Verification**: SHA-256 hash verification
- **Role-Based Access**: Admin/User roles with appropriate permissions

## Installation

1. **Install MongoDB**
   - Download from [mongodb.com](https://www.mongodb.com/try/download/community)
   - Start MongoDB service: `mongod`

2. **Install Python Dependencies**
   ```bash
   pip install -r requirements.txt