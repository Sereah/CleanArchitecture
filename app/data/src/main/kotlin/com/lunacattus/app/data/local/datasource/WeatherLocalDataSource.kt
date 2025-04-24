package com.lunacattus.app.data.local.datasource

import com.lunacattus.app.data.local.api.WeatherDao
import com.lunacattus.app.data.local.entity.DailyWeatherEntity
import com.lunacattus.app.data.local.entity.WeatherEntity
import com.lunacattus.app.data.local.entity.WeatherWithDailyWeather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao
) {
    suspend fun insertLiveWeather(weatherEntity: WeatherEntity): Long {
        return weatherDao.insertOrUpdateWeather(weatherEntity)
    }

    suspend fun insertDailyWeather(
        adCode: String,
        dailyWeathers: List<DailyWeatherEntity>
    ): List<Long> {
        weatherDao.clearDailyWeather(adCode.toInt())
        return weatherDao.insertOrUpdateDailyWeather(dailyWeathers)
    }

    fun getWeatherWithDailyWeather(): Flow<WeatherWithDailyWeather?> {
        return weatherDao.getWeatherWithDailyWeather()
    }
}