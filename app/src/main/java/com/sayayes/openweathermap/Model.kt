package com.sayayes.openweathermap

//city
data class CityModel (
        val city: String,
        val lat: String,
        val lon:String

)

//------------------------------
//current weather
data class CurrentWeatherModel(
        var cityName:String,
        val coord: Coordinate,
        val weather: List<Weather>,
        val main:Main
)

data class Coordinate (
        val lon: String,
        val lat: String
)

data class Main (
        val temp: String,
        val temp_min: String,
        val temp_max: String
)

//------------------------------------------
//forecast weather for 3 days
data class ForecastWeatherModel (
        var cityName:String,
        val lat: String,
        val lon: String,
        val timezone_offset: String,
        val current: Current,
        val daily: List<Daily>

)

data class Current (
        val dt: String,
        val sunrise: String,
        val sunset: String,
        val temp: String
)

data class Daily (
        val dt: String,
        val sunrise: String,
        val sunset: String,

        //forecast temp
        val temp: Temp,

        //forecast weather
        val weather:List<Weather>,
        var date:String

)

data class Temp (
        val day: String,
        val min: String,
        val max: String,
        var min_string:String,
        var max_string:String
)

data class Weather (
        val id: String="",
        val main: String="",
        val description: String="",
        val icon: String=""
)

data class CurrentAndForecast(
        var currentWeatherModel: CurrentWeatherModel?=null,
        var forecastWeatherModel: ForecastWeatherModel?=null

)