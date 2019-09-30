package com.app.weatherapp

import com.app.weatherapp.module.WeatherResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class SampleDataUnitTest {

    val gson: Gson = GsonBuilder().create()

    @Test
    fun fetchWeatherData() {
        val weatherResponse =
            WeatherResponse.fromJSON(gson, JSONObject(readFromFile("/data1.json")))
        assert(weatherResponse.timezone.isNotEmpty())
        assert(weatherResponse.latitude != 0.0)
        assert(weatherResponse.longitude != 0.0)
    }


    @Throws(IOException::class)
    fun readFromFile(filename: String): String {
        val stream =
            javaClass.getResourceAsStream(filename) ?: throw IOException("Input stream is null")
        val stringBuilder = StringBuilder()
        val b = ByteArray(4096)
        var i: Int = stream.read(b)
        while (i != -1) {
            stringBuilder.append(String(b, 0, i))
            i = stream.read(b)
        }
        return stringBuilder.toString()
    }
}
