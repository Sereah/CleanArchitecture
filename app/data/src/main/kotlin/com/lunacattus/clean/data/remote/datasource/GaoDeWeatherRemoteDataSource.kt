package com.lunacattus.clean.data.remote.datasource

import com.lunacattus.clean.data.remote.model.GaoDeWeatherDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GaoDeWeatherRemoteDataSource @Inject constructor(
    private val service: GaoDeWeatherApiService
) {

    suspend fun getLiveWeather(cityCode: String): GaoDeWeatherDTO {
        return service.getLiveWeather(cityCode)
    }
}