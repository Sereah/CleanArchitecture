package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHourlyWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(id: String): Result<List<HourlyWeather>> {
        return repository.getHourlyWeather(id)
    }
}