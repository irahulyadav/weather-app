package com.app.weatherapp

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.weatherapp.databinding.ActivityMainBinding
import com.app.weatherapp.module.WeatherResponse
import com.app.weatherapp.repository.LocationService
import com.app.weatherapp.viewmodel.WeatherInfoViewModel
import com.appszum.idl.di.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), LocationService.Listener {

    private val formatter = SimpleDateFormat("h:mm a", Locale.ENGLISH)


    var requestCode: Int? = 0
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var locationService: LocationService

    lateinit var weatherInfoViewModel: WeatherInfoViewModel

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
        getAppComponent().inject(this)
        weatherInfoViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(WeatherInfoViewModel::class.java)
        locationService.findCurrentLocation(this, this)
        weatherInfoViewModel.subscribe().observe(
            this, Observer<WeatherResponse> {
                this.weatherResponse = it
            })
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


    override fun onLocationFound(location: Location) {
        weatherInfoViewModel.setLocation(location)
    }

    override fun requestPermissions(requestCode: Int) {
        this.requestCode = requestCode
    }

    override fun gpsAlert() {
        showGpsAlert()
    }

    override fun networkAlert() {
        connectionAlert()
    }

    override fun locationNotFoundAlert() {
        //show dialog to user if location not found TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
//        (application as WeatherApplication)
//            .retrofitModule
//            .weatherApiService
//            .fetchWeather(BuildConfig.API_KEY, location.latitude, location.longitude)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : DisposableSingleObserver<WeatherResponse>() {
//                override fun onSuccess(t: WeatherResponse) {
//                    this@MainActivity.weatherResponse = t
//                }
//
//                override fun onError(e: Throwable) {
//                    e.printStackTrace()
//                }
//            })


    }


    //Show the alert dialog if location is not enable
    private fun showGpsAlert() {
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (!locationService.haveLocationPermission) {
                finish()
                return
            }
            locationService.findCurrentLocation(this, this)
        }
    }
}
