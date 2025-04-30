package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentLocationIdAndInsertUseCase @Inject constructor(
    private val repository: IWeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<String> {
        return repository.getWeatherGeographicId(latitude, longitude, true)
    }
}