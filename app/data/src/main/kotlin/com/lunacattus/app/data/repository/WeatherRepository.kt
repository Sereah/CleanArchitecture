package com.lunacattus.app.data.repository

import com.lunacattus.app.data.local.datasource.WeatherLocalDataSource
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToEntity
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToModel
import com.lunacattus.app.data.remote.datasource.QWeatherRemoteDataSource
import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherGeo
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

    override suspend fun requestAndSaveWeather(
        location: String,
        isCurrentLocation: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val geo = qWeatherRemoteDataSource.getGeo(location)
            if (geo.code != Q_WEATHER_SUCCESS) {
                throw Exception("request geographic info fail.")
            }
            if (geo.location.isEmpty()) {
                throw Exception("request geographic null.")
            }
            weatherLocalDataSource.insertWeatherGeo(geo.mapperToEntity(isCurrentLocation)[0])
            val id = geo.location[0].id
            supervisorScope {
                val nowJob = launch {
                    val now = qWeatherRemoteDataSource.getNowWeather(id)
                    if (now.code == Q_WEATHER_SUCCESS) {
                        weatherLocalDataSource.insertNowWeather(
                            now.mapperToEntity(id, isCurrentLocation)
                        )
                    }
                }
                val dailyJob = launch {
                    val daily = qWeatherRemoteDataSource.getDailyWeather(id)
                    if (daily.code == Q_WEATHER_SUCCESS && daily.daily.isNotEmpty()) {
                        weatherLocalDataSource.insertDailyWeather(
                            daily.mapperToEntity(id, isCurrentLocation)
                        )
                    }
                }
                val hourlyJob = launch {
                    val hourly = qWeatherRemoteDataSource.getHourlyWeather(id)
                    if (hourly.code == Q_WEATHER_SUCCESS && hourly.hourly.isNotEmpty()) {
                        weatherLocalDataSource.insertHourlyWeather(
                            hourly.mapperToEntity(id, isCurrentLocation)
                        )
                    }
                }
                joinAll(nowJob, dailyJob, hourlyJob)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun queryNowWeather(id: String): Result<Flow<NowWeather>> {
        return try {
            val query = weatherLocalDataSource.queryNowWeather(id)
            Result.success(query.map { it.mapperToModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun queryDailyWeather(id: String): Result<Flow<List<DailyWeather>>> {
        return try {
            val query = weatherLocalDataSource.queryDailyWeather(id)
            Result.success(query.map { it.map { it.mapperToModel() } })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun queryHourlyWeather(id: String): Result<Flow<List<HourlyWeather>>> {
        return try {
            val query = weatherLocalDataSource.queryHourlyWeather(id)
            Result.success(query.map { it.map { it.mapperToModel() } })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGeoList(keyword: String): Result<List<WeatherGeo>> =
        withContext(Dispatchers.IO) {
            try {
                val geo = qWeatherRemoteDataSource.getGeo(keyword)
                if (geo.code != Q_WEATHER_SUCCESS) {
                    throw Exception("request geographic info fail.")
                }
                Result.success(geo.mapperToModel(false))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getNowWeather(locationId: String): Result<NowWeather> =
        withContext(Dispatchers.IO) {
            try {
                val now = qWeatherRemoteDataSource.getNowWeather(locationId)
                if (now.code != Q_WEATHER_SUCCESS) {
                    throw Exception("request now weather info fail.")
                }
                Result.success(now.mapperToModel(locationId, false))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getDailyWeather(locationId: String): Result<List<DailyWeather>> =
        withContext(Dispatchers.IO) {
            try {
                val daily = qWeatherRemoteDataSource.getDailyWeather(locationId)
                if (daily.code != Q_WEATHER_SUCCESS) {
                    throw Exception("request daily weather info fail.")
                }
                Result.success(daily.mapperToModel(locationId, false))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getHourlyWeather(locationId: String): Result<List<HourlyWeather>> =
        withContext(Dispatchers.IO) {
            try {
                val hourly = qWeatherRemoteDataSource.getHourlyWeather(locationId)
                if (hourly.code != Q_WEATHER_SUCCESS) {
                    throw Exception("request hourly weather info fail.")
                }
                Result.success(hourly.mapperToModel(locationId, false))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun queryAllWeather(): Result<Flow<List<Weather>>> {
        return try {
            val query = weatherLocalDataSource.queryAllWeather()
            Result.success(query.map { it.map { it.mapperToModel() } })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSavedCityWeather(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            weatherLocalDataSource.queryGeo(false).forEach {
                Logger.d(TAG, "updateSavedCityWeather: ${it.name}")
                requestAndSaveWeather(it.locationId, false)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun queryGeo(id: String): Result<WeatherGeo?> = withContext(Dispatchers.IO) {
        try {
            val geo = weatherLocalDataSource.queryGeo(id)
            Result.success(geo?.mapperToModel())
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val TAG = "WeatherRepository"
        private const val Q_WEATHER_SUCCESS = 200
    }
}