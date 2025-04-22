package com.lunacattus.app.domain.repository.weather

import com.lunacattus.app.domain.model.weather.WeatherInfo

interface IWeatherRepository {
    suspend fun getWeather(cityCode: String): Result<WeatherInfo>
    suspend fun getWeather(): Result<WeatherInfo>
}