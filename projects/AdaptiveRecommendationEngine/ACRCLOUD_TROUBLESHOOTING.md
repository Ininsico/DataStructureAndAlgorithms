# 🔧 ACRCloud Troubleshooting Guide

## Current Status

The app is now configured to:
- ✅ Try 3 different ACRCloud regions automatically
- ✅ Show detailed error messages
- ✅ Fall back to Spotify if ACRCloud fails

## How to Verify Your ACRCloud Setup

### Step 1: Login to ACRCloud Console
1. Go to https://console.acrcloud.com/
2. Login with your account

### Step 2: Check Your Project
1. Click on **"Audio & Video Recognition"** in the left menu
2. You should see your project listed
3. Click on your project name

### Step 3: Verify Credentials
Look for these values and compare with the app:

**In ACRCloud Console:**
- Access Key: `e582fe76e4a4c02117e4056c10da736c`
- Access Secret: `SXe3gZz5SQZp1GrBQFTv1sVYDIOpWWOEVxeb2POV`
- Host: `identify-eu-west-1.acrcloud.com` (or similar)

**In the App (ShazamService.java):**
```java
private static final String ACRCLOUD_ACCESS_KEY = "e582fe76e4a4c02117e4056c10da736c";
private static final String ACRCLOUD_SECRET_KEY = "SXe3gZz5SQZp1GrBQFTv1sVYDIOpWWOEVxeb2POV";
```

### Step 4: Check Console Output
When you click "Listen" in the app, check the console for messages like:

```
🔍 Trying ACRCloud host: identify-eu-west-1.acrcloud.com
ACRCloud Response (identify-eu-west-1.acrcloud.com): 200
Response body: {"status":{"msg":"Success","code":0},...}
ACRCloud Status: 0 - Success
✅ ACRCloud identified: Song Name - Artist Name
```

## Common Error Codes

### 1001 - Missing/Invalid Access Key
**Cause**: Wrong credentials or wrong project type
**Fix**: 
1. Go to ACRCloud console
2. Make sure you're using **"Audio & Video Recognition"** project (NOT "Broadcast Monitoring")
3. Copy the exact Access Key and Access Secret
4. Update `ShazamService.java` with correct values

### 3001 - No Result
**Cause**: Song not in database OR audio quality too low
**Fix**:
- Try with popular songs first
- Make sure music is loud enough
- Reduce background noise
- Move microphone closer to source

### 3003 - Can't Generate Fingerprint
**Cause**: Audio data is corrupted or too short
**Fix**:
- Check microphone permissions
- Try recording for longer (increase from 5 to 10 seconds)

### HTTP 403 - Forbidden
**Cause**: Wrong host region
**Fix**: The app now tries all 3 regions automatically!

## What the App Does Now

### Automatic Region Detection
```java
String[] hosts = {
    "identify-eu-west-1.acrcloud.com",      // Europe
    "identify-us-west-2.acrcloud.com",      // USA
    "identify-ap-southeast-1.acrcloud.com"  // Asia
};
```

The app will try each one until it finds the right region for your account!

## If ACRCloud Still Doesn't Work

### Option 1: Use Spotify Fallback (Already Working)
- Play a song on YOUR Spotify
- Click "Listen" button
- App identifies what YOU are playing
- Still useful for testing!

### Option 2: Check ACRCloud Account Status
1. Login to https://console.acrcloud.com/
2. Check "Usage" tab
3. Make sure you haven't exceeded free tier (2000/month)
4. Check if account is active

### Option 3: Create New Project
1. In ACRCloud console
2. Click "Create Project"
3. Choose **"Audio & Video Recognition"** (important!)
4. Get new credentials
5. Update `ShazamService.java`

## Testing Steps

### Test 1: Check Console Output
1. Run the app
2. Go to "🎤 Identify Songs" tab
3. Click "🎵 Listen"
4. Watch the console for detailed messages

### Test 2: Try Popular Song
1. Play a very popular song (e.g., "Blinding Lights" by The Weeknd)
2. Turn up volume
3. Click "Listen"
4. Should identify successfully

### Test 3: Check Microphone
1. Windows Settings → Privacy → Microphone
2. Make sure Java/your IDE has permission
3. Test microphone in Windows Sound settings

## Current Configuration

Your credentials are:
```
Access Key: e582fe76e4a4c02117e4056c10da736c
Secret Key: SXe3gZz5SQZp1GrBQFTv1sVYDIOpWWOEVxeb2POV
```

The app will automatically try:
1. EU server first
2. US server if EU fails
3. Asia server if US fails
4. Spotify fallback if all fail

## Next Steps

1. **Restart the app** (it's compiling now with the new code)
2. **Try identifying a song**
3. **Check console output** for detailed error messages
4. **Report back** what error code you see

The new code will tell us EXACTLY what's wrong! 🔍
