# 🚀 How to Run the Recommendation Engine

## The "One-Click" Fix
Since Maven was missing from your system, I created a magic script that handles everything (downloads a portable Maven, configures it, and runs the app).

**Just run this command in your terminal:**
```powershell
.\run_auto.ps1
```

## What I Fixed
1.  **Compilation Errors**: You were running `javac` manually, which doesn't work for modern JavaFX apps. My script uses the correct build process.
2.  **Missing Maven**: The script detects if Maven is missing and auto-downloads a portable version to `.local-maven/`. It keeps your system clean.
3.  **Visuals**: I overhauled the UI with the Glassmorphism theme and Physics Graph as requested.

## Manual Run (VS Code)
I also fixed the VS Code settings (`.vscode/`). You should now be able to:
1.  Open `MainApp.java`
2.  Click the **Run** button (Top Right)
3.  (If it asks for environment, restart VS Code so it picks up the new config).
