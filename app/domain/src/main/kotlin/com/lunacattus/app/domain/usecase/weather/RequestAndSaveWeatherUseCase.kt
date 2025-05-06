package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestAndSaveWeatherUseCase @Inject constructor(
    private val repository: IWeatherRepository
) {
    suspend operator fun invoke(location: String, isCurrentLocation: Boolean): Result<Unit> {
        return repository.requestAndSaveWeather(location, isCurrentLocation)
    }
}