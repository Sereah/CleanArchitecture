package com.lunacattus.app.data.remote.datasource

import com.lunacattus.app.data.remote.api.QWeatherService
import com.lunacattus.app.data.remote.dto.QWeatherDailyDTO
import com.lunacattus.app.data.remote.dto.QWeatherGeoDTO
import com.lunacattus.app.data.remote.dto.QWeatherHourlyDTO
import com.lunacattus.app.data.remote.dto.QWeatherNowDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QWeatherRemoteDataSource @Inject constructor(
    private val service: QWeatherService
) {
    suspend fun getGeo(location: String): QWeatherGeoDTO {
        return service.getGeo(location)
    }

    suspend fun getNowWeather(locationId: String): QWeatherNowDTO {
        return service.getNowWeather(locationId)
    }

    suspend fun getDailyWeather(locationId: String): QWeatherDailyDTO {
        return service.getDailyWeather(locationId)
    }

    suspend fun getHourlyWeather(locationId: String): QWeatherHourlyDTO {
        return service.getHourlyWeather(locationId)
    }
}