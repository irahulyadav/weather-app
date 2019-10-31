package com.app.weatherapp.module

import com.app.weatherapp.R

data class WeatherInfo(
    val time: Long,
    val summary: String,
    private val icon: String,
    val precipIntensity: Double,
    val precipProbability: Double,
    val precipType: String,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Int,
    val visibility: Double,
    val ozone: Double
) {

//    var sunriseTime: Long? = null
//    var sunsetTime: Long? = null
//    var moonPhase: Double? = null
//
//    var precipIntensityMax: Double? = null
//    var precipIntensityMaxTime: Long? = null
//
//    var temperatureHigh: Double? = null
//    var temperatureHighTime: Long? = null
//    var temperatureLow: Double? = null
//    var temperatureLowTime: Long? = null
//    var apparentTemperatureHigh: Double? = null
//    var apparentTemperatureHighTime: Long? = null
//    var apparentTemperatureLow: Double? = null
//    var apparentTemperatureLowTime: Long? = null
//
//    var windGustTime: Long? = null
//
//    var uvIndexTime: Long? = null
//
//    var temperatureMin: Double? = null
//    var temperatureMinTime: Long? = null
//    var temperatureMax: Double? = null
//    var temperatureMaxTime: Long? = null
//    var apparentTemperatureMin: Double? = null
//    var apparentTemperatureMinTime: Long? = null
//    var apparentTemperatureMax: Double? = null
//    var apparentTemperatureMaxTime: Long? = null

    val iconId: Int
        get() {
            var iconId = R.drawable.clear_day

            when (icon) {
                "clear-day" -> iconId = R.drawable.clear_day
                "clear-night" -> iconId = R.drawable.clear_night
                "rain" -> iconId = R.drawable.rain
                "snow" -> iconId = R.drawable.snow
                "sleet" -> iconId = R.drawable.sleet
                "wind" -> iconId = R.drawable.wind
                "fog" -> iconId = R.drawable.fog
                "cloudy" -> iconId = R.drawable.cloudy
                "partly-cloudy-day" -> iconId = R.drawable.partly_cloudy
                "partly-cloudy-night" -> iconId = R.drawable.cloudy_night
            }

            return iconId
        }
}