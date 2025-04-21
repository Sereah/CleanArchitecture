package com.lunacattus.clean.data.remote.datasource

import com.lunacattus.clean.data.remote.model.GaoDeWeatherDTO
import com.lunacattus.data.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface GaoDeWeatherApiService {
    @GET("v3/weather/weatherInfo?extensions=base&key=${BuildConfig.gaoDeApi}")
    suspend fun getLiveWeather(
        @Query("city") cityCode: String,
    ): GaoDeWeatherDTO
}