package com.lunacattus.clean.data.repository.weather

import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.data.mapper.WeatherMapper.mapper
import com.lunacattus.clean.data.remote.datasource.GaoDeWeatherRemoteDataSource
import com.lunacattus.clean.domain.model.weather.LivesWeather
import com.lunacattus.clean.domain.model.weather.WeatherException
import com.lunacattus.clean.domain.repository.weather.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val gaoDeWeatherDataSource: GaoDeWeatherRemoteDataSource
) : IWeatherRepository {

    override suspend fun getLivesWeather(cityCode: String): Result<LivesWeather> {
        return try {
            val response = gaoDeWeatherDataSource.getLiveWeather(cityCode)
            Logger.d(TAG, "getLivesWeather, response: $response")
            if (response.status == 0) {
                throw WeatherException.FailNetworkRequest
            }
            if (response.lives.isEmpty()) {
                throw WeatherException.EmptyWeatherInfo
            }
            Result.success(response.mapper())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLivesWeather(): Result<LivesWeather> {
        val response = gaoDeWeatherDataSource.getCurrentAdCode()
        Logger.d(TAG, "getCurrentAdCode, response: $response")
        return if (response.adCode !is String) {
            Result.failure(WeatherException.EmptyAdCode)
        } else {
            getLivesWeather(response.adCode)
        }
    }


    companion object {
        const val TAG = "WeatherRepository"
    }
}