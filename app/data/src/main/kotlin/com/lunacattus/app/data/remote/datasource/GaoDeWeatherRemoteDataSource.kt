package com.lunacattus.app.data.remote.datasource

import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeIpInfoDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GaoDeWeatherRemoteDataSource @Inject constructor(
    private val service: GaoDeWeatherApiService
) {

    suspend fun getLiveWeather(cityCode: String): GaoDeLiveWeatherDTO {
        return service.getLiveWeather(cityCode)
    }

    suspend fun getDailyWeather(cityCode: String): GaoDeDailyWeatherDTO {
        return service.getDailyWeather(cityCode)
    }

    suspend fun getCurrentAdCode(): GaoDeIpInfoDTO {
        return service.getCurrentAdCodeByIp()
    }
}