package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNowWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(id: String): Result<NowWeather> {
        return repository.getNowWeather(id)
    }
}