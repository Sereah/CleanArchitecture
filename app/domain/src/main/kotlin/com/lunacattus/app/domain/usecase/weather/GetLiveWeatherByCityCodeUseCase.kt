package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.weather.WeatherInfo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLiveWeatherByCityCodeUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(cityCode: String): Result<WeatherInfo> {
        return repository.getWeather(cityCode)
    }
}