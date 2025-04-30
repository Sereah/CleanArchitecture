package com.lunacattus.app.data.repository

import com.lunacattus.app.data.local.datasource.WeatherLocalDataSource
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToEntity
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToModel
import com.lunacattus.app.data.remote.datasource.QWeatherRemoteDataSource
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import com.lunacattus.clean.common.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val qWeatherRemoteDataSource: QWeatherRemoteDataSource,
) : IWeatherRepository {

    override suspend fun requestAllWeatherInfo(locationId: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                supervisorScope {
                    val nowJob = launch {
                        val nowResponse =
                            qWeatherRemoteDataSource.getNowWeather(locationId)
                        if (nowResponse.code == Q_WEATHER_SUCCESS) {
                            weatherLocalDataSource.insertQWeatherNow(
                                nowResponse.mapperToEntity(locationId)
                            )
                        }
                    }
                    val dailyJob = launch {
                        val dailyResponse =
                            qWeatherRemoteDataSource.getDailyWeather(locationId)
                        if (dailyResponse.code == Q_WEATHER_SUCCESS && dailyResponse.daily.isNotEmpty()) {
                            weatherLocalDataSource.insertQWeatherDaily(
                                dailyResponse.mapperToEntity(locationId)
                            )
                        }
                    }
                    val hourlyJob = launch {
                        val hourlyResponse =
                            qWeatherRemoteDataSource.getHourlyWeather(locationId)
                        if (hourlyResponse.code == Q_WEATHER_SUCCESS && hourlyResponse.hourly.isNotEmpty()) {
                            weatherLocalDataSource.insertQWeatherHourly(
                                hourlyResponse.mapperToEntity(locationId)
                            )
                        }
                    }
                    joinAll(nowJob, dailyJob, hourlyJob)
                }
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentWeather(): Flow<Weather?> {
        return weatherLocalDataSource.getCurrentLocationWeather().map {
            it?.mapperToModel()
        }
    }

    override suspend fun requestNowWeatherInfo(locationId: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val response = qWeatherRemoteDataSource.getNowWeather(locationId)
                if (response.code != Q_WEATHER_SUCCESS) {
                    throw Exception("request now weather fail")
                }
                val result = weatherLocalDataSource.insertQWeatherNow(response.mapperToEntity(locationId))
                Logger.d(TAG, "insertQWeatherNow, result: $result")
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherGeographicId(
        latitude: Double,
        longitude: Double,
        isCurrentLocation: Boolean
    ): Result<String> {
        return try {
            withContext(Dispatchers.IO) {
                val geo = qWeatherRemoteDataSource.getGeo(latitude, longitude)
                Logger.d(TAG, "getGeo: $geo")
                if (geo.code != Q_WEATHER_SUCCESS || geo.location.isEmpty()) {
                    throw Exception("request geographic info fail.")
                }
                weatherLocalDataSource.insertQWeatherLocation(geo.mapperToEntity(isCurrentLocation))
                Result.success(geo.location[0].id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val TAG = "WeatherRepository"
        private const val Q_WEATHER_SUCCESS = 200
    }
}