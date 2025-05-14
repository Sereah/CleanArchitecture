package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryGeoByIdUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(id: String): Result<WeatherGeo?> {
        return repository.queryGeo(id)
    }
}