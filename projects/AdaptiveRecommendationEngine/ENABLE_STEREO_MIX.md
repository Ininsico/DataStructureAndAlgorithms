# 🔊 Enable Stereo Mix in Windows

## Why You Need This

**Problem**: The app is recording from your MICROPHONE, which picks up sound through the air. This causes:
- ❌ Poor audio quality
- ❌ Background noise
- ❌ Low volume
- ❌ ACRCloud can't recognize songs

**Solution**: Enable **"Stereo Mix"** to record DIRECTLY from your computer's audio output (perfect quality!)

## How to Enable Stereo Mix

### Step 1: Open Sound Settings
1. **Right-click** the speaker icon in taskbar (bottom-right)
2. Click **"Sounds"** or **"Sound settings"**
3. Click **"Sound Control Panel"** (or go to Recording tab)

### Step 2: Show Disabled Devices
1. Go to **"Recording"** tab
2. **Right-click** in empty space
3. Check ✅ **"Show Disabled Devices"**
4. Check ✅ **"Show Disconnected Devices"**

### Step 3: Enable Stereo Mix
1. You should now see **"Stereo Mix"** in the list
2. **Right-click** on "Stereo Mix"
3. Click **"Enable"**
4. **Right-click** again
5. Click **"Set as Default Device"**
6. Click **"OK"**

### Step 4: Test
1. **Restart the app**
2. **Play music** on your computer (Spotify, YouTube, etc.)
3. **Click "Listen"** in the app
4. Should now record PERFECT quality!

## If You Don't See "Stereo Mix"

### Option 1: Update Audio Drivers
1. **Right-click Start** → **Device Manager**
2. Expand **"Sound, video and game controllers"**
3. **Right-click** your audio device
4. Click **"Update driver"**
5. Restart computer

### Option 2: Check Realtek Settings
If you have Realtek audio:
1. Open **Realtek HD Audio Manager**
2. Go to **"Mixer"** or **"Recording"** tab
3. Enable **"Stereo Mix"**

### Option 3: Use VB-Audio Cable (Alternative)
If Stereo Mix doesn't exist:
1. Download **VB-CABLE** from: https://vb-audio.com/Cable/
2. Install it
3. It creates a virtual audio device
4. Set it as default playback device
5. App will record from it

## After Enabling Stereo Mix

### Restart the App:
```bash
# Stop current app (Ctrl+C)
# Then run:
powershell -ExecutionPolicy Bypass -File run_auto.ps1
```

### What You'll See:
```
🔍 Looking for audio input devices...
  Found: Microphone (Realtek)
  Found: Stereo Mix (Realtek)
✅ Using: Stereo Mix (Realtek) (system audio)
🎤 Recording started... Play music now!
```

### Test It:
1. **Play "Dynamite" by BTS** on Spotify/YouTube
2. **Click "Listen"** in app
3. **Wait 10 seconds**
4. **Song should be identified!** ✅

## Troubleshooting

### "Stereo Mix" is grayed out
- Update audio drivers
- Restart computer
- Try VB-Audio Cable instead

### Still using microphone
- Make sure Stereo Mix is **"Default Device"**
- Restart the app
- Check console output

### No sound in recording
- Make sure music is actually playing
- Check Windows volume (not muted)
- Stereo Mix should be enabled AND set as default

---

**Once Stereo Mix is enabled, the app will record PERFECT quality audio directly from your computer!** 🎵
