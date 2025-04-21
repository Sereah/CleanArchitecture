package com.lunacattus.clean.domain.usecase.weather

import com.lunacattus.clean.domain.model.weather.LivesWeather
import com.lunacattus.clean.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLiveWeatherByCityCodeUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(cityCode: String): Result<LivesWeather> {
        return repository.getLivesWeather(cityCode)
    }
}