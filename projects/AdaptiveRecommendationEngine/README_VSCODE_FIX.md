# 🛠️ fixing the "Red Bars" in VS Code

The red bars appear because **Maven** is not detected, so VS Code cannot download the required JavaFX libraries.

## 1. Quick Fix (If you just want to run it)
Since you are on Windows, we can bypass the complexity by checking if you have the "Java Extension Pack" installed in VS Code.

1.  Open VS Code Extensions (Ctrl+Shift+X).
2.  Search for **"Extension Pack for Java"** by Microsoft.
3.  Install it if missing.

## 2. The Real Fix: Install Maven
To compile this project properly, you need Apache Maven.

1.  **Download Maven**: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) (Binary zip archive).
2.  **Extract**: Unzip it to a folder like `C:\Program Files\Maven`.
3.  **Add to PATH**:
    *   Press `Win` key, type **"Environment Variables"**.
    *   Click "Edit the system environment variables".
    *   Click "Environment Variables" button.
    *   Under **System variables**, find `Path` -> Edit -> New.
    *   Paste the path to the `bin` folder (e.g., `C:\Program Files\Maven\apache-maven-3.9.6\bin`).
    *   Click OK on all windows.
4.  **Restart VS Code**.

## 3. Reload Project
Once Maven is set up:
1.  Open the Command Palette (`Ctrl+Shift+P`).
2.  Type **"Java: Reload Projects"**.
3.  VS Code will now download the dependencies defined in `pom.xml` and the red bars will disappear.
