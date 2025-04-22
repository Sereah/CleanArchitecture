package com.lunacattus.app.data.repository.weather

import com.lunacattus.app.data.mapper.GaoDeWeatherMapper.mapper
import com.lunacattus.app.data.remote.datasource.GaoDeWeatherRemoteDataSource
import com.lunacattus.app.domain.model.weather.WeatherException
import com.lunacattus.app.domain.model.weather.WeatherInfo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import com.lunacattus.clean.common.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val gaoDeWeatherDataSource: GaoDeWeatherRemoteDataSource
) : IWeatherRepository {

    override suspend fun getWeather(cityCode: String): Result<WeatherInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val liveResponse = gaoDeWeatherDataSource.getLiveWeather(cityCode)
                val dailyResponse = gaoDeWeatherDataSource.getDailyWeather(cityCode)
                Logger.d(TAG, "getLiveWeather, response: $liveResponse")
                Logger.d(TAG, "getDailyWeather, response: $dailyResponse")
                if (liveResponse.status == 0) {
                    throw WeatherException.ApiError(
                        code = liveResponse.infoCode,
                        msg = liveResponse.info
                    )
                }
                if (dailyResponse.status == 0) {
                    throw WeatherException.ApiError(
                        code = dailyResponse.infoCode,
                        msg = dailyResponse.info
                    )
                }
                if (liveResponse.lives.isEmpty()) {
                    throw WeatherException.OtherError("Weather info empty")
                }
                Result.success(mapper(liveResponse, dailyResponse))
            } catch (e: Throwable) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getWeather(): Result<WeatherInfo> {
        return withContext(Dispatchers.IO) {
            val response = gaoDeWeatherDataSource.getCurrentAdCode()
            Logger.d(TAG, "getCurrentAdCode, response: $response")
            when {
                response.status == 0 -> {
                    Result.failure(WeatherException.ApiError(response.infoCode, response.info))
                }

                response.adCode !is String -> {
                    Result.failure(WeatherException.OtherError("AdCode info empty"))
                }

                else -> getWeather(response.adCode)
            }
        }
    }

    companion object {
        const val TAG = "WeatherRepository"
    }
}