package com.lunacattus.app.data.di

import android.content.Context
import android.location.LocationManager
import com.lunacattus.app.data.remote.api.GaoDeApiService
import com.lunacattus.app.data.remote.api.QWeatherService
import com.lunacattus.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideQWeatherServiceApi(): QWeatherService {
        return Retrofit.Builder()
            .baseUrl("https://${BuildConfig.QWeatherHost}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QWeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationManager(
        @ApplicationContext context: Context
    ): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideAppContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }
}