package com.app.weatherapp.api


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.app.weatherapp.BuildConfig
import com.app.weatherapp.api.service.WeatherApiService
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class RetrofitModule(application: Application, baseUrl: String) {

    val gson = GsonBuilder().create()
    val retrofit: Retrofit

    init {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize)

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)

        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        val okHttpClient = httpClient.build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    val weatherApiService: WeatherApiService get() = retrofit.create(WeatherApiService::class.java)

    fun isNetworkAvailable(context: Context): Boolean {
        return context.isNetworkAvailable()
    }

    companion object {

        private var retrofitModule: RetrofitModule? = null

        fun getInstance(application: Application): RetrofitModule {
            val retrofitModule = retrofitModule ?: RetrofitModule(application, BuildConfig.API_URL)
            this.retrofitModule = retrofitModule
            return retrofitModule
        }

    }

}

// check if internet connection is available or not
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true || capabilities?.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    ) == true
}
