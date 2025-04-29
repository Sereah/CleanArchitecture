package com.lunacattus.app.domain.repository.weather

interface IWeatherRepository {
    suspend fun requestAllWeatherInfo(): Result<Unit>
}