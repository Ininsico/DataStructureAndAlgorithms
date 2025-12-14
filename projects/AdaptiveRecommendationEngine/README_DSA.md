# 🎵 SpotiShazam: DSA Implementation Guide

This project is not just a wrapper around the Spotify API; it implements core **Data Structures and Algorithms (DSA)** to simulate how large-scale music identification and recommendation systems work under the hood.

## 🧠 Core DSA Concepts Used

### 1. Trie (Prefix Tree) - *The "Shazam" Lookup*
*   **File**: `src/main/java/com/dsa/core/Trie.java`
*   **Problem**: How do you search a database of millions of songs instantly when a user types or "hears" a title?
*   **Solution**: We implemented a **Trie**.
*   **Complexity**: **O(L)**, where *L* is the length of the song title.
    *   This is significantly faster than iterating through a List (O(N)) or a balanced tree (O(log N)) when N is huge.
    *   It allows for efficient **Prefix Matching** (e.g., typing "Blin" instantly finds "Blinding Lights").

### 2. Vector Space Model (Euclidean Distance) - *The "Vibe" Engine*
*   **File**: `src/main/java/com/dsa/algorithms/SpotifyRecommender.java`
*   **Problem**: How do we recommend songs that "feel" the same?
*   **Solution**: We represent every song as a **Vector** in a 2D plane: `[Energy, Valence]`.
*   **Algorithm**: We calculate the **Euclidean Distance** between the User's "Taste Vector" (average of their history) and every candidate song.
    *   *Formula*: `√((Energy1 - Energy2)² + (Valence1 - Valence2)²)`
    *   Songs with the smallest distance are mathematically the "closest match" to your taste.

### 3. Clustering (Simplified K-Means) - *Daily Mixes*
*   **File**: `src/main/java/com/dsa/algorithms/SpotifyRecommender.java`
*   **Problem**: How do we group 50+ songs into distinct playlists like "Hype Mode" or "Chill Vibes"?
*   **Solution**: We use a clustering approach.
*   **Logic**:
    *   We define **Centroids** (e.g., High Energy > 0.8 = "Hype").
    *   We iterate through the dataset and bucket songs into these clusters based on their attribute values.

### 4. Hash Maps - *O(1) Caching*
*   **File**: `src/main/java/com/dsa/data/SpotifyService.java`
*   **Usage**: To store and retrieve tracks by ID instantly.
*   **Why**: When fetching "Currently Playing" data, we often need to cross-reference it with our local cache. A `HashMap` allows us to do this in **Constant Time O(1)**, ensuring the UI never lags.

---

## 🚀 How to Run
1.  **Open Terminal**.
2.  Run the auto-script:
    ```powershell
    .\run_auto.ps1
    ```
3.  **Connect Spotify** to use the API + DSA Engine hybrid.
