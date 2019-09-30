package com.app.weatherapp

import android.app.Application
import com.app.weatherapp.api.RetrofitModule

class WeatherApplication : Application() {
    val retrofitModule: RetrofitModule by lazy { RetrofitModule.getInstance(this) }
}