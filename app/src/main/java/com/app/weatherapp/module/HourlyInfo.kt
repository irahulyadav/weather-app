package com.app.weatherapp.module

import com.google.gson.annotations.SerializedName

class HourlyInfo {
    @SerializedName("data")
    val hourlyInfo: List<WeatherInfo> = arrayListOf()
}