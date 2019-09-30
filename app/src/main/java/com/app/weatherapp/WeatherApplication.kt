package com.app.weatherapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true || capabilities?.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    ) == true
}