package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryDailyWeatherUseCase @Inject constructor(private val repository: IWeatherRepository) {
    operator fun invoke(id: String): Result<Flow<List<DailyWeather>>> {
        return repository.queryDailyWeather(id)
    }
}