package com.lunacattus.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QWeatherGeoDTO(
    val code: Int,
    val location: List<GeoLocation>
)

data class QWeatherNowDTO(
    val code: Int,
    val now: WeatherNow
)

data class QWeatherDailyDTO(
    val code: Int,
    val daily: List<WeatherDaily>
)

data class QWeatherHourlyDTO(
    val code: Int,
    val hourly: List<WeatherHourly>
)

data class GeoLocation(
    val name: String,
    val id: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    @SerializedName("adm1") val provence: String,
    @SerializedName("adm2") val city: String,
    @SerializedName("tz") val timeZone: String,
)

data class WeatherNow(
    val obsTime: String,
    val temp: Int,
    @SerializedName("feelsLike") val feelsTemp: Int,
    val text: String,
    val wind360: Float,
    val windScale: String,
    val windSpeed: Int,
    val humidity: Int,
    @SerializedName("precip") val preCip: Float,
    val pressure: Int,
    val vis: Int,
    val cloud: Int,
    val dew: Int
)

data class WeatherDaily(
    val fxDate: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    @SerializedName("moonset") val moonSet: String,
    val moonPhase: String,
    val tempMax: Int,
    val tempMin: Int,
    val textDay: String,
    val textNight: String,
    val wind360Day: Float,
    val windScaleDay: String,
    val windSpeedDay: Int,
    val wind360Night: Float,
    val windScaleNight: String,
    val windSpeedNight: Int,
    val humidity: Int,
    @SerializedName("precip") val preCip: Float,
    val pressure: Int,
    val vis: Int,
    val cloud: Int,
    val uvIndex: Int
)

data class WeatherHourly(
    val fxTime: String,
    val temp: Int,
    val text: String,
    val wind360: Float,
    val windScale: String,
    val windSpeed: Int,
    val humidity: Int,
    val pop: Int,
    @SerializedName("precip") val preCip: Float,
    val pressure: Int,
    val cloud: Int,
    val dew: Int
)