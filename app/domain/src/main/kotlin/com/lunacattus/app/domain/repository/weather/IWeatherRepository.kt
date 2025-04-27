package com.lunacattus.app.domain.repository.weather

import com.lunacattus.app.domain.model.CityInfo
import com.lunacattus.app.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    fun getWeather(): Result<Flow<WeatherInfo?>>
    suspend fun requestCurrentWeatherInfo(): Result<Unit>
    suspend fun searchCity(keyword: String): Result<List<CityInfo>>
}