package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDailyWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(id: String): Result<List<DailyWeather>> {
        return repository.getDailyWeather(id)
    }
}