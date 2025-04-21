package com.lunacattus.clean.domain.repository.weather

import com.lunacattus.clean.domain.model.weather.LivesWeather

interface IWeatherRepository {
    suspend fun getLivesWeather(cityCode: String): Result<LivesWeather>
}