package com.lunacattus.app.domain.repository.weather

import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherGeo
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun requestAndSaveWeather(location: String, isCurrentLocation: Boolean): Result<Unit>
    fun queryNowWeather(id: String): Result<Flow<NowWeather>>
    fun queryDailyWeather(id: String): Result<Flow<List<DailyWeather>>>
    fun queryHourlyWeather(id: String): Result<Flow<List<HourlyWeather>>>
    suspend fun getGeoList(keyword: String): Result<List<WeatherGeo>>
    suspend fun getNowWeather(locationId: String): Result<NowWeather>
    suspend fun getDailyWeather(locationId: String): Result<List<DailyWeather>>
    suspend fun getHourlyWeather(locationId: String): Result<List<HourlyWeather>>
    fun queryAllWeather(): Result<Flow<List<Weather>>>
    suspend fun updateSavedCityWeather(): Result<Unit>
    suspend fun queryGeo(id: String): Result<WeatherGeo?>
}