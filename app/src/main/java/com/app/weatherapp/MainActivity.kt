package com.app.weatherapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.weatherapp.api.isNetworkAvailable
import com.app.weatherapp.databinding.ActivityMainBinding
import com.app.weatherapp.module.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val formatter = SimpleDateFormat("h:mm a", Locale.ENGLISH)

    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }

    //private var mGoogleApiClient: GoogleApiClient? = null

    private val locationManager: LocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    var location: Location? = null
        set(value) {
            if (value != null) {
                fetchWeather(value)
            }
            field = value
        }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    var weatherResponse: WeatherResponse? = null
        set(value) {
            field = value
            if (value != null) {
                formatter.timeZone = TimeZone.getTimeZone(value.timezone)
                binding.weather = value.currently
                binding.time = formatter.format(Date(value.currently.time * 1000))
                binding.location = value.timezone
                binding.iconImageView.setImageResource(value.currently.iconId)
            }
        }

    override fun onResume() {
        super.onResume()
        if (location != null) {
            return
        }
        fetchLocation { this.location = it }
    }

    private fun fetchLocation(action: (Location?) -> Unit) {
        if (!isLocationEnabled) {
            showAlert()
            return
        }
        if (!haveLocationPermission) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                145
            )
            return
        }
        if (!isNetworkAvailable()) {
            connectionAlert()
            return
        }
        mFusedLocationClient
            .lastLocation
            .addOnSuccessListener(this)
            { location ->
                action(location)
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

//    override fun onStart() {
//        super.onStart()
//        mGoogleApiClient?.connect()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mGoogleApiClient?.disconnect()
//    }

    fun fetchWeather(location: Location) {
        (application as WeatherApplication)
            .retrofitModule
            .weatherApiService
            .fetchWeather(BuildConfig.API_KEY, location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<WeatherResponse>() {
                override fun onSuccess(t: WeatherResponse) {
                    this@MainActivity.weatherResponse = t
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }


    fun set(weatherResponse: WeatherResponse) {
        println(weatherResponse)
    }

    private val haveLocationPermission: Boolean
        get() = havePermission(permissions.first()) && havePermission(
            permissions.last()
        )

    private fun havePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    //Show the alert dialog if location is not enable
    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use ${BuildConfig.APP_NAME}")
            .setPositiveButton(
                "Location Settings"
            ) { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel")
            { _, _ -> }

        dialog.show()
    }

    private fun connectionAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Network")
            .setMessage("There is a problem with network. Please check your internet connection")
            .setPositiveButton(
                "OK"
            ) { _, _ ->

            }
        dialog.show()
    }

    private val isLocationEnabled: Boolean
        get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 145) {
            if (!haveLocationPermission) {
                finish()
                return
            }
            fetchLocation { this.location = it }
        }
    }
}
