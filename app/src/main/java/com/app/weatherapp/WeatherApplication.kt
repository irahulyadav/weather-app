package com.app.weatherapp

import android.app.Activity
import android.app.Application
import com.app.weatherapp.di.AppComponent
import com.app.weatherapp.di.AppModule
import com.app.weatherapp.di.DaggerAppComponent

class WeatherApplication : Application() {
    lateinit var appComponent: AppComponent
    //val retrofitModule: RetrofitModule by lazy { RetrofitModule.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .retrofitModule(com.app.weatherapp.di.RetrofitModule(BuildConfig.API_URL))
            .build()
    }
}

fun Application.getAppComponent(): AppComponent {
    return (this as WeatherApplication).appComponent
}


fun Activity.getWeatherApplication(): WeatherApplication {
    return application as WeatherApplication
}

fun Activity.getAppComponent(): AppComponent {
    return application.getAppComponent()
}