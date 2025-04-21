package com.lunacattus.clean.data.di

import com.lunacattus.clean.data.remote.datasource.GaoDeWeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGaoDeWeatherServiceApi(): GaoDeWeatherApiService {
        return Retrofit.Builder()
            .baseUrl("https://restapi.amap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GaoDeWeatherApiService::class.java)
    }
}