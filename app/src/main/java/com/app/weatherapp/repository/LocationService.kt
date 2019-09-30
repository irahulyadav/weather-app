package com.app.weatherapp.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.weatherapp.api.isNetworkAvailable
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class LocationService @Inject constructor(private val application: Application) {

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val locationManager: LocationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val isLocationEnabled: Boolean
        get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    val haveLocationPermission: Boolean
        get() = havePermission(permissions.first()) && havePermission(
            permissions.last()
        )

    private fun havePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            application,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun findCurrentLocation(activity: Activity, listener: Listener) {

        if (!isLocationEnabled) {
            listener.gpsAlert()
            return
        }
        if (!haveLocationPermission) {
            listener.requestPermissions(requestPermissions(activity))
            return
        }
        if (!application.isNetworkAvailable()) {
            listener.networkAlert()
            return
        }
        LocationServices.getFusedLocationProviderClient(
            activity
        ).lastLocation
            .addOnSuccessListener(activity)
            { location ->
                listener.onLocationFound(location)
            }.addOnFailureListener {
                it.printStackTrace()
                listener.locationNotFoundAlert()
            }
    }

    fun requestPermissions(activity: Activity): Int {
        ActivityCompat.requestPermissions(
            activity,
            permissions,
            145
        )
        return 145
    }

    interface Listener {
        fun onLocationFound(location: Location)

        fun requestPermissions(requestCode: Int)

        fun gpsAlert()

        fun networkAlert()

        fun locationNotFoundAlert()
    }
}