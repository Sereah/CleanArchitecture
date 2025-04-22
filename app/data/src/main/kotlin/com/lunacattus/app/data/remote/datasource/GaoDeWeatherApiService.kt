package com.lunacattus.app.data.remote.datasource

import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeIpInfoDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import com.lunacattus.data.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface GaoDeWeatherApiService {

    @GET("v3/weather/weatherInfo?extensions=base&key=${BuildConfig.gaoDeApi}")
    suspend fun getLiveWeather(
        @Query("city") cityCode: String,
    ): GaoDeLiveWeatherDTO

    @GET("v3/weather/weatherInfo?extensions=all&key=${BuildConfig.gaoDeApi}")
    suspend fun getDailyWeather(
        @Query("city") cityCode: String,
    ): GaoDeDailyWeatherDTO

    @GET("v3/ip?key=${BuildConfig.gaoDeApi}")
    suspend fun getCurrentAdCodeByIp(): GaoDeIpInfoDTO
}