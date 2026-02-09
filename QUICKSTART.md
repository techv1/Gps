# Quick Start Guide

## 🚀 5-Minute Setup

### Step 1: Enable Developer Mode (30 seconds)
1. Settings → About Phone
2. Tap "Build Number" 7 times
3. Go back → Developer Options

### Step 2: Enable Mock Location (30 seconds)
1. In Developer Options
2. Find "Select mock location app"
3. Choose "Mock Location"

### Step 3: Install App (2 minutes)
1. Open IDE JStudio
2. Import the `MockLocationApp` folder
3. Connect phone via USB
4. Click Run ▶️

### Step 4: Use the App (1 minute)
1. Open "Mock Location" app
2. Search for a city OR enter coordinates
3. Click "Start Simulation"
4. Done! Your GPS is now mocked

## 📱 Test Your Web App

1. Keep Mock Location app running
2. Open your web app in Chrome/browser
3. Grant location permissions
4. Your web app will receive the mock location!

## 🎯 Example Coordinates

Try these preset cities:
- **Delhi**: Auto-filled when you search "Delhi"
- **Mumbai**: Auto-filled when you search "Mumbai"
- **Bangalore**: Auto-filled when you search "Bangalore"
- **Jaipur**: Auto-filled when you search "Jaipur"

Or enter any coordinates manually:
- **Your sample location**: 28.319769, 74.282778
- **Times Square, NY**: 40.758896, -73.985130
- **Eiffel Tower**: 48.858370, 2.294481

## ⚠️ Common Issues

**App won't start simulation?**
→ Check Developer Options → Make sure this app is selected as mock location app

**Web app not getting location?**
→ Grant location permissions to your web app
→ Try refreshing the web app

**Need help?**
→ Check the full README.md for detailed troubleshooting

## 🔍 What You'll See

The app shows real-time data:
```
Lat: 28.319765
Lon: 74.282785
Acc: 7.23m
Speed: 0.42m/s
```

These values change every second with realistic GPS noise!
