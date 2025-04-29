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
    suspend fun getGeo(lat: Double, lon: Double): QWeatherGeoDTO {
        return service.getGeo("$lat,$lon")
    }

    suspend fun getNowWeather(lat: Double, lon: Double): QWeatherNowDTO {
        return service.getNowWeather("$lat,$lon")
    }

    suspend fun getDailyWeather(lat: Double, lon: Double): QWeatherDailyDTO {
        return service.getDailyWeather("$lat,$lon")
    }

    suspend fun getHourlyWeather(lat: Double, lon: Double): QWeatherHourlyDTO {
        return service.getHourlyWeather("$lat,$lon")
    }
}