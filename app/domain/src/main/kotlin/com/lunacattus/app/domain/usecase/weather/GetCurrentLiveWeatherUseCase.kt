package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.weather.WeatherInfo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentLiveWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(): Result<WeatherInfo> {
        return repository.getWeather()
    }
}