package com.lunacattus.clean.domain.usecase.weather

import com.lunacattus.clean.domain.model.weather.LivesWeather
import com.lunacattus.clean.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentLiveWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(): Result<LivesWeather> {
        return repository.getLivesWeather()
    }
}