package com.lunacattus.app.data.di

import com.lunacattus.app.data.remote.api.GaoDeApiService
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
    fun provideGaoDeWeatherServiceApi(): GaoDeApiService {
        return Retrofit.Builder()
            .baseUrl("https://restapi.amap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GaoDeApiService::class.java)
    }
}