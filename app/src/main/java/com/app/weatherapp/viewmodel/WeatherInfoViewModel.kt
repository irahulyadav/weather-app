package com.app.weatherapp.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.weatherapp.module.WeatherResponse
import com.app.weatherapp.repository.WeatherRepo
import javax.inject.Inject


class WeatherInfoViewModel @Inject constructor(val repository: WeatherRepo) :
    ViewModel() {

    private val data = MutableLiveData<WeatherResponse>()

    fun subscribe(): MutableLiveData<WeatherResponse> {
        return data
    }

    fun setLocation(location: Location) {
        repository.fetchWeather(location) {
            data.value = it
        }
    }

}