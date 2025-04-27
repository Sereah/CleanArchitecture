package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.model.CityInfo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchCityUseCase @Inject constructor(
    private val repository: IWeatherRepository
) {
    suspend operator fun invoke(keyword: String): Result<List<CityInfo>> {
        return repository.searchCity(keyword)
    }
}