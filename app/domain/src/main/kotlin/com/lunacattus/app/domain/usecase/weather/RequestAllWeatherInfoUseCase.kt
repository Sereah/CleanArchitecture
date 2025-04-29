package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestAllWeatherInfoUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.requestAllWeatherInfo()
    }
}