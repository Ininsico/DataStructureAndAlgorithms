# 🚀 Quick Start Guide

## What Changed?

### ❌ BEFORE (Broken)
```
User clicks button → App checks "currently playing" on Spotify → Shows that song
```
**This was just a "now playing" widget, NOT a recommendation engine!**

### ✅ AFTER (Fixed)
```
User connects → App fetches 1000+ tracks from Spotify's database
              → Builds Trie index for search
              → Analyzes audio features (energy, valence)
              → Generates personalized recommendations
              → Creates daily mixes by genre/mood
              → Enables smart search
```
**This is a REAL recommendation engine with DSA algorithms!**

## Running the App

### Step 1: Install Maven (if not installed)
```bash
# Download from: https://maven.apache.org/download.cgi
# Extract and add bin/ to PATH
# Verify:
mvn --version
```

### Step 2: Configure Spotify API
Edit `src/main/java/com/dsa/core/SpotifyConfig.java`:
```java
public static final String CLIENT_ID = "YOUR_CLIENT_ID";
public static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
```

Get credentials from: https://developer.spotify.com/dashboard
- Create new app
- Set redirect URI: `http://localhost:8888/callback`

### Step 3: Run
```bash
mvn clean javafx:run
```

Or double-click `run_app.bat`

### Step 4: Use the App
1. Click "🔗 Connect Spotify"
2. Browser opens → Login to Spotify
3. Authorize the app
4. **Wait 30-60 seconds** while it builds the database
5. You'll see:
   - "Made For You" recommendations
   - Daily Mixes (Desi, K-Pop, Hype, Chill)
   - Search bar (type to search 1000+ tracks)

## What the App Does

### 🎯 Personalized Recommendations
- Analyzes your listening history
- Calculates your "vibe vector" (avg energy, avg valence)
- Finds similar songs using Euclidean distance
- Boosts scores for matching genres

### 🔍 Smart Search
- Type song name or artist
- Instant results using Trie data structure
- Searches through 1000+ tracks

### 🎧 Daily Mixes
Auto-generated playlists:
- **Desi Mix**: Hindi/Punjabi tracks
- **K-Pop Essentials**: Korean pop
- **Hype Mode**: High energy (>0.75)
- **Chill Station**: Low energy + valence

### 📊 Adaptive Learning
- Click any track to add to your history
- Recommendations update in real-time
- More clicks = better recommendations

## Database Sources

The app fetches tracks from:
1. Your top tracks (last 4 weeks, 6 months, all time)
2. Your saved/liked tracks
3. Spotify's featured playlists
4. 18 genre seeds (pop, rock, hip-hop, k-pop, etc.)
5. New releases

**Total: ~1000-2000 unique tracks**

## DSA Algorithms Used

### 1. Trie (Prefix Tree)
```
Purpose: Fast search
Complexity: O(m) where m = query length
Usage: Index all track names and artists
```

### 2. Vector Space Model
```
Purpose: Recommendation
Algorithm: Euclidean distance in 2D space (energy, valence)
Formula: distance = √((e1-e2)² + (v1-v2)²)
```

### 3. Clustering
```
Purpose: Daily mixes
Method: Group by genre and audio features
Filters: Energy > 0.75, Valence < 0.5, etc.
```

## Troubleshooting

### "Maven not found"
**Solution**: Install Maven and add to PATH
```bash
# Windows
setx PATH "%PATH%;C:\path\to\maven\bin"

# Verify
mvn --version
```

### "Spotify API Error 401"
**Solution**: Check SpotifyConfig.java has correct credentials

### "No recommendations shown"
**Solution**: 
- Ensure you have Spotify listening history
- Save/like some tracks on Spotify
- Wait for database to finish building

### "Search returns no results"
**Solution**: Database might still be building. Wait for status to show "✅ Ready!"

## Performance Tips

- First run takes 30-60 seconds (building database)
- Subsequent searches are instant (<10ms)
- Click tracks to improve recommendations
- More listening history = better recommendations

## Next Steps

1. **Explore Recommendations**: See what the algorithm suggests
2. **Try Search**: Type artist or song names
3. **Click Tracks**: Add to history to refine recommendations
4. **Check Daily Mixes**: Discover new music by genre/mood

---

**Enjoy your REAL recommendation engine!** 🎵
