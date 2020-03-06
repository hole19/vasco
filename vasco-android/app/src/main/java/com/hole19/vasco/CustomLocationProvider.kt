package com.hole19.vasco

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock


class CustomLocationProvider(private val name: String,
                             private val context: Context) {

    init {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.addTestProvider(name, false, false, false,
                false, false, true, true,
                0, 5)

        locationManager.setTestProviderEnabled(name, true)
    }

    fun pushLocation(lat: Double, lon: Double, alt: Double, accuracy: Float) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val mockLocation = Location(name).apply {
            latitude = lat
            longitude = lon
            altitude = alt
            time = System.currentTimeMillis()
            setAccuracy(accuracy)
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        locationManager.setTestProviderLocation(name, mockLocation)
    }

    fun shutdown() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeTestProvider(name)
    }


}