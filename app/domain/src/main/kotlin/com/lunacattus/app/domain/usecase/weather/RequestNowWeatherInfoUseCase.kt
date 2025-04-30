package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestNowWeatherInfoUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(locationId: String): Result<Unit> {
        return repository.requestNowWeatherInfo(locationId)
    }
}