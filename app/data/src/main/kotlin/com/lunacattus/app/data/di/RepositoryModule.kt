package com.lunacattus.app.data.di

import com.lunacattus.app.data.repository.weather.WeatherRepository
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
}