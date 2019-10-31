package com.app.weatherapp.module

import com.google.gson.Gson
import org.json.JSONObject

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val currently: WeatherInfo,
    val hourly: HourlyInfo
) {

    companion object {
        fun fromJSON(gson: Gson, jsonObject: JSONObject): WeatherResponse {
            return gson.fromJson<WeatherResponse>(
                jsonObject.toString(),
                WeatherResponse::class.java
            )
        }
    }
}