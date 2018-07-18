package com.ruigoncalo.vasco

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log


class CustomLocationReceiver : BroadcastReceiver() {

    private var mockGPS: CustomLocationProvider? = null
    private var mockNetwork: CustomLocationProvider? = null
    private val tag = "CustomLocationReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "stop.mock") {
            mockGPS?.shutdown()
            mockNetwork?.shutdown()
        } else {
            mockGPS = CustomLocationProvider(LocationManager.GPS_PROVIDER, context)
            mockNetwork = CustomLocationProvider(LocationManager.NETWORK_PROVIDER, context)

            val lat = intent.getStringExtra("lat")?.toDoubleOrNull() ?: 0.0
            val lon = intent.getStringExtra("lon")?.toDoubleOrNull() ?: 0.0
            val alt = intent.getStringExtra("alt")?.toDoubleOrNull() ?: 0.0
            val acc = intent.getStringExtra("acc")?.toFloatOrNull() ?: 0f
            Log.i(tag, "Location update: lat=$lat lon=$lon alt=$alt acc=$acc")

            mockGPS?.pushLocation(lat, lon, alt, acc)
            mockNetwork?.pushLocation(lat, lon, alt, acc)
        }
    }
}