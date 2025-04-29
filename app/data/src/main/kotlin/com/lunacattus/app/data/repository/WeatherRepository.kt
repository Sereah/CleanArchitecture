package com.lunacattus.app.data.repository

import com.lunacattus.app.data.local.datasource.WeatherLocalDataSource
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToEntity
import com.lunacattus.app.data.remote.datasource.QWeatherRemoteDataSource
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import com.lunacattus.clean.common.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
    private val locationRepository: LocationRepository
) : IWeatherRepository {

    override suspend fun requestAllWeatherInfo(): Result<Unit> {
        return try {
            locationRepository.getLocation().first().let {
                withContext(Dispatchers.IO) {
                    val locationId = getWeatherGeo(it.latitude, it.longitude)
                    supervisorScope {
                        val nowJob = launch {
                            val nowResponse =
                                qWeatherRemoteDataSource.getNowWeather(it.latitude, it.longitude)
                            if (nowResponse.code == Q_WEATHER_SUCCESS) {
                                weatherLocalDataSource.insertQWeatherNow(
                                    locationId,
                                    nowResponse.mapperToEntity(locationId)
                                )
                            }
                        }
                        val dailyJob = launch {
                            val dailyResponse =
                                qWeatherRemoteDataSource.getDailyWeather(it.latitude, it.longitude)
                            if (dailyResponse.code == Q_WEATHER_SUCCESS) {
                                weatherLocalDataSource.insertQWeatherDaily(
                                    locationId,
                                    dailyResponse.mapperToEntity(locationId)
                                )
                            }
                        }
                        val hourlyJob = launch {
                            val hourlyResponse =
                                qWeatherRemoteDataSource.getHourlyWeather(it.latitude, it.longitude)
                            if (hourlyResponse.code == Q_WEATHER_SUCCESS) {
                                weatherLocalDataSource.insertQWeatherHourly(
                                    locationId,
                                    hourlyResponse.mapperToEntity(locationId)
                                )
                            }
                        }
                        joinAll(nowJob, dailyJob, hourlyJob)
                    }
                    Result.success(Unit)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getWeatherGeo(latitude: Double, longitude: Double): String {
        val geo = qWeatherRemoteDataSource.getGeo(latitude, longitude)
        Logger.d(TAG, "getGeo: $geo")
        if (geo.code != Q_WEATHER_SUCCESS || geo.location.isEmpty()) {
            throw Exception("request geo fail.")
        }
        weatherLocalDataSource.insertQWeatherLocation(geo.mapperToEntity())
        return geo.location[0].id
    }

    companion object {
        const val TAG = "WeatherRepository"
        private const val Q_WEATHER_SUCCESS = 200
    }
}