package com.poc.weatherapp.api

import com.poc.weatherapp.model.WeatherData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface RestAPI {
    /**
     * Method to fetch the Weather Data using latitude and longitude
     * @param mLatitude - Holds the latitude value of the location for which the Weather report need to be optained
     * @param mLongitude - Holds the longitude value of the location for which the Weather report need to be optained
     *
     * Units=si - Query is to obtain the response in Celsius format
     */
    @GET("{lat},{lng}?units=si")
    fun getWeather(@Path("lat") mLatitude: Double, @Path("lng") mLongitude: Double): Observable<WeatherData>

}