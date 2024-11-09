package com.example.retrofitweatherapi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.retrofitweatherapi.models.CurrentWeather
import com.example.retrofitweatherapi.utils.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WeatherFragment : Fragment() {

    private lateinit var cityET: EditText
    private lateinit var getWeatherBTN: Button
    private lateinit var minTempTV: TextView
    private lateinit var maxTempTV: TextView
    private lateinit var weatherIW: ImageView
    private lateinit var windDirectionTV: TextView
    private lateinit var windSpeedTV: TextView
    private lateinit var pressureTV: TextView
    private lateinit var humidityTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        cityET = view.findViewById(R.id.cityET)
        getWeatherBTN = view.findViewById(R.id.getWeatherBTN)
        minTempTV = view.findViewById(R.id.minTempTV)
        maxTempTV = view.findViewById(R.id.maxTempTV)
        weatherIW = view.findViewById(R.id.weatherIW)
        windDirectionTV = view.findViewById(R.id.windDirectionTV)
        windSpeedTV = view.findViewById(R.id.windSpeedTV)
        pressureTV = view.findViewById(R.id.pressureTV)
        humidityTV = view.findViewById(R.id.humidityTV)

        cityET.setText("Калининград")

        getWeatherBTN.setOnClickListener { getCurrentWeather() }

        return view
    }

    private fun getCurrentWeather() {

        val city = cityET.text.toString().trim()
        if (city.isEmpty()) {
            showSnackbar(false, getString(R.string.error_empty_city))
            return
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    city,
                    "metric",
                    getString(R.string.apiKey)
                )
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    showSnackbar(false, getString(R.string.error_app, e.message))
                }
                return@launch
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showSnackbar(false, getString(R.string.error_http, e.message))
                }
                return@launch
            }

            withContext(Dispatchers.Main) {
                clearFields()
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val data = response.body()!!
                    updateWeatherUI(data)
                    showSnackbar(true, getString(R.string.data_received, city))
                }
            } else {
                withContext(Dispatchers.Main) {
                    showSnackbar(false, getString(R.string.error_city_not_found))
                }
            }
        }
    }

    private fun clearFields() {
        minTempTV.text = "мин t"
        maxTempTV.text = "макс t"
        val imageURL = R.drawable.error
        Picasso.get().load(imageURL).into(weatherIW)
        windDirectionTV.text = "Направление ветра"
        windSpeedTV.text = "Скорость ветра"
        pressureTV.text = "Давление"
        humidityTV.text = "Влажность"
    }

    private fun updateWeatherUI(data: CurrentWeather) {
        minTempTV.text = getString(R.string.min_temp, data.main.temp_min.toInt())
        maxTempTV.text = getString(R.string.max_temp, data.main.temp_max.toInt())
        windDirectionTV.text = getString(R.string.wind_direction, data.wind.deg)
        windSpeedTV.text = getString(R.string.wind_speed, data.wind.speed.toInt())
        val pressure = (data.main.pressure / 1.33).toInt()
        pressureTV.text = getString(R.string.pressure, pressure)
        humidityTV.text = getString(R.string.humidity, data.main.humidity)
        val iconId = data.weather[0].icon
        val imageURL = "https://openweathermap.org/img/wn/$iconId@4x.png"
        Picasso.get().load(imageURL).into(weatherIW)
    }

    private fun showSnackbar(type: Boolean, message: String) {
        view?.let {
            if (!type) {
                Log.e("ERROR", message)
            } else {
                Log.d("SUCCESS", message)
            }
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}
