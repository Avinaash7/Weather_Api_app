package com.avi.weatherapp


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    var BaseUrl = "http://api.openweathermap.org/"
    var AppId = "16c8958e8c659eae5a1e007ba4dd6163"
    var lat = "35"
    var lon = "139"

    private var weatherData: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherData = findViewById(R.id.textView)

        findViewById<View>(R.id.button).setOnClickListener { getCurrentData() }
    }

    fun getCurrentData() {
        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service: WeatherService = retrofit.create(WeatherService::class.java)
        val call: Call<WeatherResponse?>? = service.getCurrentWeatherData(lat, lon, AppId)
        call?.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {
                if (response.code() == 200) {
                    val weatherResponse: WeatherResponse = response.body()!!
                    val stringBuilder = """
                            Country: ${weatherResponse.sys?.country.toString()}
                            Temperature: ${weatherResponse.main?.temp.toString()}
                            Temperature(Min): ${weatherResponse.main?.temp_min.toString()}
                            Temperature(Max): ${weatherResponse.main?.temp_max.toString()}
                            Humidity: ${weatherResponse.main?.humidity.toString()}
                            Pressure: ${weatherResponse.main?.pressure}
                            """.trimIndent()
                    weatherData!!.text = stringBuilder
                }
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                weatherData!!.text = t.message
            }
        })
    }
}