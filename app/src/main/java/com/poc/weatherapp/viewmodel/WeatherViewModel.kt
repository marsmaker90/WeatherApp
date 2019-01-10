package com.poc.weatherapp.viewmodel

import android.content.Context
import com.poc.weatherapp.api.ServiceHandler
import com.poc.weatherapp.model.WeatherData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class WeatherViewModel(context: Context) : BaseViewModel(context) {

    /**
     * Method to get the weather report , Which emits WeatherData as Observable in MainThread
     * @param mLatitude - Supplies latitude of the requesting location
     * @param mLongitude - Supplies longitude of the requesting location
     * @return Disposable Observable which contains the Weather Data
     */
    fun getWeatherData(mLatitude: Double, mLongitude: Double): Observable<WeatherData> {
        return ServiceHandler.getApi().getWeather(mLatitude, mLongitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * Method to get the weather report , Which emits WeatherData as Observable on Non-UI Thread.
     * This method has only difference to the "getWeatherData" is "observeOn(Schedulers.newThread())" to run and
     * verify the service without instrumentation
     *
     * @param mLatitude - Supplies latitude of the requesting location
     * @param mLongitude - Supplies longitude of the requesting location
     * @return Disposable Observable which contains the Weather Data
     */
    fun getWeatherDataUT(mLatitude: Double, mLongitude: Double): Observable<WeatherData> {
        return ServiceHandler.getApi().getWeather(mLatitude, mLongitude)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
    }
}