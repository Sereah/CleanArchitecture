package com.lunacattus.clean.data.repository.weather

import androidx.core.os.BuildCompat
import com.google.gson.internal.GsonBuildConfig
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.data.mapper.WeatherMapper.mapper
import com.lunacattus.clean.data.remote.datasource.GaoDeWeatherRemoteDataSource
import com.lunacattus.clean.domain.model.weather.LivesWeather
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
            if (response.status == 1 && response.lives.isNotEmpty()) {
                Result.success(response.mapper())
            } else {
                Result.failure(Exception("fail response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val TAG = "WeatherRepository"
    }
}