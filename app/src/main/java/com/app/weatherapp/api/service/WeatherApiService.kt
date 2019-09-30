package com.app.weatherapp.api.service

import com.app.weatherapp.module.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {

    @GET("/forecast/{key}/{latitude},{longitude}")
    fun fetchWeather(@Path("key") apiKey: String, @Path("latitude") latitude: Double, @Path("longitude") longitude: Double): Single<WeatherResponse>

}