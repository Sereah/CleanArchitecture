package com.lunacattus.app.data.local.datasource

import com.lunacattus.app.data.local.api.WeatherDao
import com.lunacattus.app.data.local.entity.GaoDeDailyWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherLocationEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import com.lunacattus.app.data.local.entity.GaoDeLiveWeatherEntity
import com.lunacattus.app.data.local.entity.GaoDeWeatherWithDailyWeather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao
) {
    suspend fun insertGaoDeLiveWeather(weatherEntity: GaoDeLiveWeatherEntity): Long {
        return weatherDao.insertGaoDeLiveWeather(weatherEntity)
    }

    suspend fun insertGaoDeDailyWeather(
        adCode: String,
        dailyWeathers: List<GaoDeDailyWeatherEntity>
    ): List<Long> {
        weatherDao.clearGaoDeDailyWeather(adCode.toInt())
        return weatherDao.insertGaoDeDailyWeather(dailyWeathers)
    }

    fun getGaoDeWeatherWithDailyWeather(): Flow<GaoDeWeatherWithDailyWeather?> {
        return weatherDao.getGaoDeLastWeatherWithDailyWeather()
    }

    suspend fun insertQWeatherLocation(weatherLocation: List<QWeatherLocationEntity>): List<Long> {
        return weatherDao.insertQWeatherLocation(weatherLocation)
    }

    suspend fun insertQWeatherNow(locationId: String, weatherNow: QWeatherNowEntity): Long {
        weatherDao.clearQWeatherNow(locationId)
        return weatherDao.insertQWeatherNow(weatherNow)
    }

    suspend fun insertQWeatherDaily(
        locationId: String,
        weatherDaily: List<QWeatherDailyEntity>
    ): List<Long> {
        weatherDao.clearQWeatherDaily(locationId)
        return weatherDao.insertQWeatherDaily(weatherDaily)
    }

    suspend fun insertQWeatherHourly(
        locationId: String,
        weatherHourly: List<QWeatherHourlyEntity>
    ): List<Long> {
        weatherDao.clearQWeatherHourly(locationId)
        return weatherDao.insertQWeatherHourly(weatherHourly)
    }
}