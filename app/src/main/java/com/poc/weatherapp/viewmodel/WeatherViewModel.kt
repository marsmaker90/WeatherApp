package com.poc.weatherapp.viewmodel

import android.content.Context
import com.poc.weatherapp.model.WeatherData
import io.reactivex.Observable


class WeatherViewModel(context: Context) : BaseViewModel(context) {

    /**
     * Method to get the weather report , Which emits WeatherData as Observable
     * @return Disposable Observable which contains the Weather Data
     */
    fun getWeatherData(): Observable<WeatherData> {
        return Observable.just(WeatherData(temperature = 0.0))
    }
}