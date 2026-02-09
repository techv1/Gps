package com.example.mocklocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var currentLocationTextView: TextView
    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    
    private var isSimulating = false
    private var simulationThread: Thread? = null
    private var currentLat = 28.319769
    private var currentLon = 74.282778
    
    private val LOCATION_PERMISSION_CODE = 100
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        checkPermissions()
        setupListeners()
    }
    
    private fun initViews() {
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        statusTextView = findViewById(R.id.statusTextView)
        currentLocationTextView = findViewById(R.id.currentLocationTextView)
        latitudeEditText = findViewById(R.id.latitudeEditText)
        longitudeEditText = findViewById(R.id.longitudeEditText)
        
        stopButton.isEnabled = false
    }
    
    private fun setupListeners() {
        searchButton.setOnClickListener {
            val searchQuery = searchEditText.text.toString()
            if (searchQuery.isNotEmpty()) {
                searchLocation(searchQuery)
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }
        
        startButton.setOnClickListener {
            if (validateCoordinates()) {
                startSimulation()
            }
        }
        
        stopButton.setOnClickListener {
            stopSimulation()
        }
    }
    
    private fun validateCoordinates(): Boolean {
        val latStr = latitudeEditText.text.toString()
        val lonStr = longitudeEditText.text.toString()
        
        if (latStr.isEmpty() || lonStr.isEmpty()) {
            Toast.makeText(this, "Please enter coordinates", Toast.LENGTH_SHORT).show()
            return false
        }
        
        try {
            currentLat = latStr.toDouble()
            currentLon = lonStr.toDouble()
            
            if (currentLat < -90 || currentLat > 90 || currentLon < -180 || currentLon > 180) {
                Toast.makeText(this, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    
    private fun searchLocation(query: String) {
        // Updated to use .lowercase() as per Kotlin 1.5+ standards
        when (query.lowercase()) {
            "delhi", "new delhi" -> {
                currentLat = 28.6139
                currentLon = 77.2090
            }
            "mumbai" -> {
                currentLat = 19.0760
                currentLon = 72.8777
            }
            "bangalore", "bengaluru" -> {
                currentLat = 12.9716
                currentLon = 77.5946
            }
            "jaipur" -> {
                currentLat = 26.9124
                currentLon = 75.7873
            }
            else -> {
                Toast.makeText(this, "Try: Delhi, Mumbai, Bangalore, or Jaipur", Toast.LENGTH_LONG).show()
                return
            }
        }
        
        latitudeEditText.setText(currentLat.toString())
        longitudeEditText.setText(currentLon.toString())
        Toast.makeText(this, "Location set to $query", Toast.LENGTH_SHORT).show()
    }
    
    private fun checkPermissions() {
        // We only request Runtime permissions here. 
        // Mock Location is an Install-time permission and must be enabled in Developer Options.
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                LOCATION_PERMISSION_CODE
            )
        } else {
            checkMockLocationSettings()
        }
    }
    
    private fun checkMockLocationSettings() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            // Attempt to add a test provider to verify if the app is selected in Developer Options
            locationManager.addTestProvider(
                LocationManager.GPS_PROVIDER,
                false, false, false, false, true, true, true,
                1, 1
            )
            locationManager.removeTestProvider(LocationManager.GPS_PROVIDER)
            statusTextView.text = "Status: App authorized as Mock Provider"
        } catch (e: SecurityException) {
            statusTextView.text = "Error: Select this app in Developer Options"
            Toast.makeText(this, "Please select this app as Mock Location app in Developer Options", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            statusTextView.text = "Status: Verification failed"
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkMockLocationSettings()
            } else {
                Toast.makeText(this, "Location permissions required", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun startSimulation() {
        if (isSimulating) return
        
        isSimulating = true
        startButton.isEnabled = false
        stopButton.isEnabled = true
        statusTextView.text = "Status: Simulating..."
        
        val baseLat = latitudeEditText.text.toString().toDouble()
        val baseLon = longitudeEditText.text.toString().toDouble()
        
        simulationThread = Thread {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            
            try {
                locationManager.addTestProvider(
                    LocationManager.GPS_PROVIDER,
                    false, false, false, false, true, true, true,
                    1, 1
                )
                locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
            } catch (e: SecurityException) {
                runOnUiThread {
                    Toast.makeText(this, "Security Error: Check Dev Options", Toast.LENGTH_SHORT).show()
                    stopSimulation()
                }
                return@Thread
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            
            while (isSimulating) {
                try {
                    val latNoise = Random.nextDouble(-0.000008, 0.000008)
                    val lonNoise = Random.nextDouble(-0.000008, 0.000008)
                    val simulatedAccuracy = Random.nextDouble(3.5, 9.5).toFloat()
                    val simulatedSpeed = Random.nextDouble(0.15, 0.70).toFloat()
                    
                    val mockLocation = Location(LocationManager.GPS_PROVIDER).apply {
                        latitude = baseLat + latNoise
                        longitude = baseLon + lonNoise
                        accuracy = simulatedAccuracy
                        speed = simulatedSpeed
                        time = System.currentTimeMillis()
                        elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
                        altitude = 0.0
                        bearing = 0.0f
                    }
                    
                    locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation)
                    
                    runOnUiThread {
                        currentLocationTextView.text = String.format(
                            "Lat: %.6f\nLon: %.6f\nAcc: %.2fm\nSpeed: %.2fm/s",
                            mockLocation.latitude, mockLocation.longitude, simulatedAccuracy, simulatedSpeed
                        )
                    }
                    
                    Thread.sleep(1000)
                    
                } catch (e: InterruptedException) {
                    break
                } catch (e: Exception) {
                    break
                }
            }
            
            try {
                locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false)
                locationManager.removeTestProvider(LocationManager.GPS_PROVIDER)
            } catch (e: Exception) { /* Cleanup */ }
        }
        
        simulationThread?.start()
    }
    
    private fun stopSimulation() {
        isSimulating = false
        simulationThread?.interrupt()
        simulationThread = null
        
        startButton.isEnabled = true
        stopButton.isEnabled = false
        statusTextView.text = "Status: Stopped"
        
        Toast.makeText(this, "Simulation stopped", Toast.LENGTH_SHORT).show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopSimulation()
    }
}