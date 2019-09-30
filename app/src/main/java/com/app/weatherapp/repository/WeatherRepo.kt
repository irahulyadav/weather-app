package com.app.weatherapp.repository

import android.location.Location
import com.app.weatherapp.BuildConfig
import com.app.weatherapp.api.service.WeatherApiService
import com.app.weatherapp.module.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val weatherApiService: WeatherApiService) {

    fun fetchWeather(location: Location, action: (WeatherResponse) -> Unit) {
        weatherApiService
            .fetchWeather(BuildConfig.API_KEY, location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<WeatherResponse>() {
                override fun onSuccess(t: WeatherResponse) {
                    action(t)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }
}