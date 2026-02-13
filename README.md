# Mock Location Simulator App

A realistic GPS mock location app for testing web apps that simulates authentic user movement with noise, jitter, and accuracy variations based on real GPS data patterns.

## Features

✅ **Realistic GPS Simulation**: Mimics actual GPS behavior with natural noise and jitter
✅ **Location Search**: Search for cities or enter custom coordinates
✅ **Stationary Simulation**: Simulates a user holding their phone (no walking movement)
✅ **Based on Real Data**: Uses patterns from your actual GPS sample data
✅ **Easy Controls**: Simple start/stop interface

## Simulation Parameters (Based on Your Sample Data)

The app generates realistic GPS data matching your sample:

- **Position Jitter**: ±0.8 meters (varying latitude/longitude slightly)
- **Accuracy Range**: 3.5m to 9.5m (random variation per update)
- **Speed Range**: 0.15 to 0.70 m/s (simulating hand tremors/micro-movements)
- **Update Frequency**: 1 second intervals
- **Movement Pattern**: Stationary with natural device movement noise

## Setup Instructions

### 1. Import Project in IDE JStudio

1. Open IDE JStudio app
2. Choose "Import Project" or "Open"
3. Navigate to the `MockLocationApp` folder
4. Wait for Gradle sync to complete

### 2. Enable Developer Options on Your Android Device

1. Go to **Settings** > **About Phone**
2. Tap **Build Number** 7 times to enable Developer Options
3. Go back to **Settings** > **Developer Options**
4. Enable **Developer Options** if not already enabled

### 3. Enable Mock Locations

**For Android 6.0+:**
1. In **Developer Options**, find **Select mock location app**
2. Select **Mock Location** (this app)

**For Android 5.x and below:**
1. In **Developer Options**, enable **Allow mock locations**

### 4. Build and Install

1. Connect your Android device via USB
2. Enable **USB Debugging** in Developer Options
3. In IDE JStudio, click **Run** or the ▶️ button
4. Select your device from the list
5. Wait for the app to install and launch

## How to Use

### Method 1: Search for Location

1. Open the app
2. Type a city name in the search box (e.g., "Delhi", "Mumbai", "Bangalore", "Jaipur")
3. Click **Search**
4. The coordinates will auto-fill
5. Click **Start Simulation**

### Method 2: Manual Coordinates

1. Enter latitude in the first field (e.g., `38.310769`)
2. Enter longitude in the second field (e.g., `94.289778`)
3. Click **Start Simulation**

### Monitoring Simulation

The app will display real-time data:
- Current latitude and longitude
- Current accuracy (meters)
- Current speed (m/s)

### Stopping Simulation

Click **Stop Simulation** to stop sending mock locations.

## Testing with Your Web App

1. Start the simulation at your desired location
2. Open your web app in a browser on the same device
3. Grant location permissions to your web app
4. Your web app will receive the mock GPS coordinates
5. The location will show realistic GPS behavior with natural variations

## Troubleshooting

### "Mock Locations not enabled"
- Make sure you've enabled Developer Options
- Select this app as the mock location app in Developer Options

### "Location permissions required"
- Grant location permissions when prompted
- Check app permissions in Android Settings if needed

### Web app not receiving location
- Ensure your web app has location permissions
- Try refreshing the web app after starting simulation
- Check if the web app is using HTTPS (required for geolocation API)

### Simulation stops unexpectedly
- Make sure the app stays in foreground
- Check if battery optimization is affecting the app
- Disable battery optimization for this app if needed

## GPS Data Characteristics

The simulation replicates the following patterns from your sample data:

| Parameter | Your Sample Range | Simulation Range |
|-----------|-------------------|------------------|
| Lat Variation | ±0.000052° (~5.8m) | ±0.000008° (~0.9m) |
| Lon Variation | ±0.000042° (~4.7m) | ±0.000008° (~0.9m) |
| Accuracy | 3.86m - 9.40m | 3.5m - 9.5m |
| Speed | 0.16 - 0.67 m/s | 0.15 - 0.70 m/s |
| Update Rate | 1 second | 1 second |

## Adding More Preset Locations

Edit `MainActivity.kt` and add cases to the `searchLocation()` function:

```kotlin
"london" -> {
    currentLat = 51.5074
    currentLon = -0.1278
}
```

## System Requirements

- Android 6.0 (API 23) or higher
- Developer Options enabled
- USB debugging enabled (for installation)

## Permissions Used

- `ACCESS_FINE_LOCATION`: Required for GPS access
- `ACCESS_COARSE_LOCATION`: Required for location services
- `ACCESS_MOCK_LOCATION`: Required to inject fake GPS data
- `INTERNET`: For future location search API integration

## Notes

- This app is designed for **testing purposes only**
- Keep the app running in the foreground during simulation
- Some apps may detect mock locations and block them
- The simulation uses realistic noise patterns based on actual GPS behavior

## License

Free to use for testing and development purposes.
