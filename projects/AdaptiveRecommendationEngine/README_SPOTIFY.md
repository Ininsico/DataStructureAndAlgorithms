# SpotiNode - Music Recommendation Engine

A JavaFX-based clone of the Spotify Desktop app, featuring:

## 🎵 Features
1.  **"Made For You" Engine**: Analyzes the songs you play (mock history) and finds mathematically similar tracks based on:
    *   **Energy** (0.0 - 1.0)
    *   **Valence** (Sad - Happy)
2.  **Daily Mixes**: Uses K-Means style clustering to split the library into specific vibes:
    *   High Energy (Workouts/Party)
    *   Chill Vibes (Study/Sleep)
    *   Moody (Sad hours)
3.  **Real Data**: Pre-loaded with hits from The Weeknd, Drake, Arctic Monkeys, etc., complete with **Album Art**.

## 🚀 How to Run
```powershell
.\run_auto.ps1
```

## 🎧 Usage
1.  Click **Home**.
2.  See "Your Daily Mixes".
3.  Click on any song card to **Play** it.
4.  Notice how the "Made For You" section updates based on what you click!
