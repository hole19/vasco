package com.hole19.vasco

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_CODE_REQ = 123
        private const val tag = "Vasco"
    }

    private val dummyLat = 41.489265
    private val dummyLong = -7.180559
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest = LocationRequest().apply {
        interval = 0
        smallestDisplacement = 1f
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result?.let {
                for (location in result.locations) {
                    Log.d(tag, "$location")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!isMockLocationEnabled()) {
            Toast.makeText(this, "Enable mock locations on Developer Options", Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            onSendLocation()
        }
    }

    private fun onSendLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this
                    , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_CODE_REQ)
        } else {
            sendLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        val intent = Intent().apply {
            putExtra("lat", dummyLat.toString())
            putExtra("lon", dummyLong.toString())
            action = "send.mock"
        }

        sendBroadcast(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_CODE_REQ -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendLocation()
                } else {
                    Toast.makeText(this, "No location permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun isMockLocationEnabled(): Boolean {
        var isMockLocation = false

        try {
            isMockLocation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val opsManager = this.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

                (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(),
                        BuildConfig.APPLICATION_ID) == AppOpsManager.MODE_ALLOWED)
            } else {
                // in marshmallow this will always return true
                android.provider.Settings.Secure.getString(this.contentResolver, "mock_location") != "0"
            }
        } catch (e: Exception) {
            return isMockLocation
        }

        return isMockLocation
    }
}
