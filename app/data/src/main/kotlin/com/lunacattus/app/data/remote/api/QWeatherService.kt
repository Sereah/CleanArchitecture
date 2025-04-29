package com.lunacattus.app.data.remote.api

import com.lunacattus.app.data.remote.dto.QWeatherDailyDTO
import com.lunacattus.app.data.remote.dto.QWeatherGeoDTO
import com.lunacattus.app.data.remote.dto.QWeatherHourlyDTO
import com.lunacattus.app.data.remote.dto.QWeatherNowDTO
import com.lunacattus.data.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface QWeatherService {
    @GET("geo/v2/city/lookup?key=${BuildConfig.QWeatherApi}")
    suspend fun getGeo(
        @Query("location") location: String
    ): QWeatherGeoDTO

    @GET("v7/weather/now?key=${BuildConfig.QWeatherApi}")
    suspend fun getNowWeather(
        @Query("location") location: String
    ): QWeatherNowDTO

    @GET("v7/weather/10d?key=${BuildConfig.QWeatherApi}")
    suspend fun getDailyWeather(
        @Query("location") location: String
    ): QWeatherDailyDTO

    @GET("v7/weather/24h?key=${BuildConfig.QWeatherApi}")
    suspend fun getHourlyWeather(
        @Query("location") location: String
    ): QWeatherHourlyDTO
}