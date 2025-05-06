package com.lunacattus.app.data.local.datasource

import com.lunacattus.app.data.local.api.WeatherDao
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherGeoEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao
) {
    suspend fun insertWeatherGeo(geo: QWeatherGeoEntity) {
        if (geo.isCurrentLocation) {
            weatherDao.deleteOldLocationQWeatherGeo()
        }
        weatherDao.insertQWeatherGeo(geo)
    }

    suspend fun insertNowWeather(now: QWeatherNowEntity) {
        if (now.isCurrentLocation) {
            weatherDao.deleteOldLocationQWeatherNow()
        }
        weatherDao.insertQWeatherNow(now)
    }

    suspend fun insertDailyWeather(daily: List<QWeatherDailyEntity>) {
        if (daily.isNotEmpty() && daily[0].isCurrentLocation) {
            weatherDao.deleteOldLocationQWeatherDaily()
        }
        weatherDao.insertQWeatherDaily(daily)
    }

    suspend fun insertHourlyWeather(hourly: List<QWeatherHourlyEntity>) {
        if (hourly.isNotEmpty() && hourly[0].isCurrentLocation) {
            weatherDao.deleteOldLocationQWeatherHourly()
        }
        weatherDao.insertQWeatherHourly(hourly)
    }

    fun queryNowWeather(locationId: String): Flow<QWeatherNowEntity> {
        return weatherDao.queryQWeatherNow(locationId)
    }

    fun queryDailyWeather(locationId: String): Flow<List<QWeatherDailyEntity>> {
        return weatherDao.queryQWeatherDaily(locationId)
    }

    fun queryHourlyWeather(locationId: String): Flow<List<QWeatherHourlyEntity>> {
        return weatherDao.queryQWeatherHourly(locationId)
    }

    fun queryAllWeather(): Flow<List<QWeatherEntity>> {
        return weatherDao.queryAllQWeather()
    }
}