package com.lunacattus.app.domain.repository.weather

import com.lunacattus.app.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getWeatherGeographicId(latitude: Double, longitude: Double, isCurrentLocation: Boolean): Result<String>
    suspend fun requestAllWeatherInfo(locationId: String): Result<Unit>
    fun getCurrentWeather(): Flow<Weather?>
    suspend fun requestNowWeatherInfo(locationId: String): Result<Unit>
}