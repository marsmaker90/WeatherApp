package com.poc.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.poc.weatherapp.viewmodel.WeatherViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = WeatherViewModel(this)

        getWeatherReport(0.0, 0.0)

    }

    /**
     * Method to fetch the current weather report
     */
    private fun getWeatherReport(lat: Double, lng: Double) {
        viewModel.compositeDisposable.add(
            viewModel.getWeatherData(lat, lng)
                .subscribe({
                    Toast.makeText(this, it.temperature.toInt(), Toast.LENGTH_LONG).show()
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                })
        )

    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

}
