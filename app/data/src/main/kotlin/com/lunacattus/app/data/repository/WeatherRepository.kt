package com.lunacattus.app.data.repository

import com.lunacattus.app.data.local.datasource.WeatherLocalDataSource
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToEntity
import com.lunacattus.app.data.mapper.WeatherMapper.mapperToModel
import com.lunacattus.app.data.remote.datasource.GaoDeWeatherRemoteDataSource
import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import com.lunacattus.app.domain.model.CityInfo
import com.lunacattus.app.domain.model.WeatherException
import com.lunacattus.app.domain.model.WeatherInfo
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import com.lunacattus.clean.common.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val gaoDeWeatherDataSource: GaoDeWeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) : IWeatherRepository {

    override fun getWeather(): Result<Flow<WeatherInfo?>> {
        return try {
            val result = weatherLocalDataSource.getWeatherWithDailyWeather()
            Logger.d(TAG, "getWeather: $result")
            Result.success(result.map { it?.mapperToModel() })
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun requestCurrentWeatherInfo(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val adCode = getCurrentAdCode()
            saveWeatherInfo(adCode)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun searchCity(keyword: String): Result<List<CityInfo>> =
        withContext(Dispatchers.IO) {
            try {
                val response = gaoDeWeatherDataSource.searchCity(keyword)
                Logger.d(TAG, "searchCity response: $response")
                if (response.status == 0) {
                    throw WeatherException.ApiError(response.infoCode, response.info)
                }
                Result.success(response.mapperToModel())
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }

    private suspend fun getCurrentAdCode(): String {
        val response = gaoDeWeatherDataSource.getCurrentAdCode()
        Logger.d(TAG, "getCurrentAdCode, response: $response")
        if (response.status == 0) throw WeatherException.ApiError(response.infoCode, response.info)
        if (response.adCode !is String) throw WeatherException.OtherError("AdCode info empty")
        return response.adCode
    }

    private suspend fun saveWeatherInfo(adCode: String) = coroutineScope {
        val liveDeferred = async {
            val liveResponse = gaoDeWeatherDataSource.getLiveWeather(adCode)
            Logger.d(TAG, "getLiveWeather, response: $liveResponse")
            validateAndSaveLiveWeather(liveResponse, adCode)
        }

        val dailyDeferred = async {
            val dailyResponse = gaoDeWeatherDataSource.getDailyWeather(adCode)
            Logger.d(TAG, "getDailyWeather, response: $dailyResponse")
            validateAndSaveDailyWeather(dailyResponse, adCode)
        }
        liveDeferred.await()
        dailyDeferred.await()
    }

    private suspend fun validateAndSaveLiveWeather(response: GaoDeLiveWeatherDTO, adCode: String) {
        if (response.status == 0) throw WeatherException.ApiError(response.infoCode, response.info)
        if (response.lives.isEmpty()) throw WeatherException.OtherError("Weather info empty")

        val result = weatherLocalDataSource.insertLiveWeather(response.mapperToEntity())
        if (result != adCode.toLong()) throw WeatherException.OtherError("Insert Live Weather fail.")
    }

    private suspend fun validateAndSaveDailyWeather(
        response: GaoDeDailyWeatherDTO,
        adCode: String
    ) {
        if (response.status == 0) throw WeatherException.ApiError(response.infoCode, response.info)

        val result = weatherLocalDataSource.insertDailyWeather(adCode, response.mapperToEntity())
        if (result.isEmpty()) throw WeatherException.OtherError("Insert Daily Weather fail.")
    }

    companion object {
        const val TAG = "WeatherRepository"
    }
}