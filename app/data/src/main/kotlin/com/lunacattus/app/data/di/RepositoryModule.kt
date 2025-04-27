package com.lunacattus.app.data.di

import com.lunacattus.app.data.repository.LocationRepository
import com.lunacattus.app.data.repository.WeatherRepository
import com.lunacattus.app.domain.repository.location.ILocationRepository
import com.lunacattus.app.domain.repository.weather.IWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideWeatherRepository(impl: WeatherRepository): IWeatherRepository

    @Binds
    abstract fun provideLocationRepository(impl: LocationRepository): ILocationRepository
}