package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryNowWeatherUseCase @Inject constructor(
    private val repository: IWeatherRepository
) {
    operator fun invoke(id: String): Result<Flow<NowWeather>> {
        return repository.queryNowWeather(id)
    }
}