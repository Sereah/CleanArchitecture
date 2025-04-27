package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.WeatherInfo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    operator fun invoke(): Result<Flow<WeatherInfo?>> {
        return repository.getWeather()
    }
}