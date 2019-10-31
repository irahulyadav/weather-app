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
import com.app.weatherapp.adapter.HourlyAdapter
import com.app.weatherapp.databinding.ActivityMainBinding
import com.app.weatherapp.module.WeatherResponse
import com.app.weatherapp.repository.LocationService
import com.app.weatherapp.viewmodel.WeatherInfoViewModel
import com.appszum.idl.di.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), LocationService.Listener {



    var requestCode: Int? = 0
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var locationService: LocationService

    lateinit var weatherInfoViewModel: WeatherInfoViewModel


    lateinit var binding: ActivityMainBinding

    val adapter = HourlyAdapter()

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
        binding.recycleView.adapter = adapter
    }

    var weatherResponse: WeatherResponse? = null
        set(value) {
            field = value
            if (value != null) {
                val formatter = MainActivity.DateFormat
                formatter.timeZone = TimeZone.getTimeZone(value.timezone)
                binding.weather = value.currently
                binding.time = formatter.format(Date(value.currently.time * 1000))
                binding.location = value.timezone
                binding.iconImageView.setImageResource(value.currently.iconId)
                adapter.hourlyInfo = value.hourly.hourlyInfo
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

    companion object{
        val DateFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    }
}
