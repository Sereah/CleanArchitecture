package com.lunacattus.clean.domain.model.weather

data class LivesWeather(
    val provence: String = "",
    val city: String = "",
    val weather: String = "",
    val temperature: Float = 0.0f,
    val windDirection: String = "",
    val windPower: String = "",
    val humidity: Int = 0,
    val reportTime: String = ""
)

sealed class WeatherException: Exception() {
    data object EmptyAdCode: WeatherException()
    data object EmptyWeatherInfo: WeatherException()
    data object FailNetworkRequest: WeatherException()
}