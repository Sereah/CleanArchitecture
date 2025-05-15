package com.lunacattus.app.domain.usecase.weather

import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCityUseCase @Inject constructor(private val repository: IWeatherRepository) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteCity(id)
    }
}