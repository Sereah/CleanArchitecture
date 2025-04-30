package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentLocationWeatherUseCase @Inject constructor(
    private val repository: IWeatherRepository
) {
    operator fun invoke(): Flow<Weather?> {
        return repository.getCurrentWeather()
    }
}