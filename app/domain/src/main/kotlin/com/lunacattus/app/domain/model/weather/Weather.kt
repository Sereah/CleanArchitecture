package com.lunacattus.app.domain.model.weather

data class WeatherInfo(
    val adCode: Int,
    val provence: String,
    val city: String,
    val condition: WeatherCondition,
    val temperature: Float,
    val windDirection: WindDirection,
    val windPower: String,
    val humidity: Int,
    val dailyForecast: List<DailyForecast> = emptyList(),
    val updateTime: Long,
)

data class DailyForecast(
    val date: Long,
    val week: Int,
    val minTemp: Float,
    val maxTemp: Float,
    val condition: WeatherCondition
)

enum class WeatherCondition {
    SUNNY, CLOUDY, RAINY, SNOWY, THUNDERSTORM, FOGGY, WINDY, UNKNOWN
}

enum class WindDirection {
    EAST, SOUTH, WEST, NORTH, SOUTHEAST, NORTHEAST, SOUTHWEST, NORTHWEST, NONE, INSTABILITY
}

data class CityInfo(
    val name: String,
    val pName: String,
    val adCode: Int
)

sealed class WeatherException() : Throwable() {
    data class ApiError(val code: Int, val msg: String) : WeatherException()
    data class OtherError(val msg: String) : WeatherException()
}