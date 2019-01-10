package com.poc.weatherapp.model

/**
 *  Weather Data  - To capture the temperature
 */
data class WeatherData(
    var latitude: Double, var longitude: Double, var timezone: String,
    var currently: Currently, var minutely: Minutely, var hourly: Hourly, var daily: Daily,
    var flags: Flags, var offset: Double
)


data class Currently(
    var time: Long, var summary: String, var icon: String, var nearestStormDistance: Int,
    var nearestStormBearing: Int, var precipIntensity: Double, var precipProbability: Double,
    var temperature: Double, var apparentTemperature: Double, var dewPoint: Double,
    var humidity: Double, var pressure: Double, var windSpeed: Double, var windGust: Double,
    var windBearing: Int, var cloudCover: Double, var uvIndex: Int, var visibility: Double, var ozone: Double
)


data class Daily(var summary: String, var icon: String, var data: List<Data>)

data class Data(
    var time: Long, var summary: String, var icon: String, var sunriseTime: Long, var sunsetTime: Long,
    var moonPhase: Double, var precipIntensity: Double, var precipIntensityMax: Double,
    var precipIntensityMaxTime: Long, var precipProbability: Double, var temperatureHigh: Double,
    var temperatureHighTime: Long, var temperatureLow: Double, var temperatureLowTime: Long,
    var apparentTemperatureHigh: Double, var apparentTemperatureHighTime: Long, var apparentTemperatureLow: Double,
    var apparentTemperatureLowTime: Long, var dewPoint: Double, var humidity: Double, var pressure: Double,
    var windSpeed: Double, var windGust: Double, var windGustTime: Long, var windBearing: Int,
    var cloudCover: Double, var uvIndex: Int, var uvIndexTime: Long, var visibility: Double, var ozone: Double,
    var temperatureMin: Double, var temperatureMinTime: Long, var temperatureMax: Double, var temperatureMaxTime: Long,
    var apparentTemperatureMin: Double, var apparentTemperatureMinTime: Long, var apparentTemperatureMax: Double,
    var apparentTemperatureMaxTime: Long, var temperature: Double
)


data class Flags(var sources: List<String>, var isdStations: List<String>, var units: String)

data class Hourly(var summary: String, var icon: String, var data: List<Data>)

data class Minutely(var summary: String, var icon: String, var data: List<Data>)
