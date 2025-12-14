# 🔑 How to Get Your FREE Audd.io API Token

## The Problem
You're seeing this error:
```
error_code: 901
error_message: "no api_token passed and the limit was reached"
```

This means the free tier (without API token) has been exhausted. But don't worry - **you can get 300 FREE requests per day** with a free API token!

## Solution: Get Your Free API Token

### Step 1: Visit Audd.io Dashboard
Go to: **https://dashboard.audd.io/**

### Step 2: Sign Up (FREE)
- Click "Sign Up" or "Get Started"
- Create a free account (takes 30 seconds)
- You can use Google/GitHub login for faster signup

### Step 3: Get Your API Token
- Once logged in, you'll see your **API Token** on the dashboard
- It looks something like: `a1b2c3d4e5f6g7h8i9j0`
- Copy this token

### Step 4: Add Token to the Code
Open `ShazamService.java` and find this line (around line 69):
```java
String apiToken = "test"; // Replace with your token from dashboard.audd.io
```

Replace `"test"` with your actual token:
```java
String apiToken = "a1b2c3d4e5f6g7h8i9j0"; // Your real token here
```

### Step 5: Rebuild and Run
```powershell
.\run_auto.ps1
```

## What You Get (FREE Plan)
✅ **300 requests per day** (more than enough for testing!)  
✅ **Accurate song recognition**  
✅ **Spotify metadata included**  
✅ **No credit card required**  

## Alternative: Use ACRCloud
If you prefer, you can also use ACRCloud (already configured in the code):
- Visit: https://www.acrcloud.com/
- Sign up for free account
- Get API credentials
- Update the credentials in `ShazamService.java` (lines 24-25)

## Need More Requests?
If you need more than 300/day:
- Audd.io Pro: $9/month for 10,000 requests
- ACRCloud: Various paid tiers available

---

**That's it! Now you'll have reliable song recognition! 🎵**
