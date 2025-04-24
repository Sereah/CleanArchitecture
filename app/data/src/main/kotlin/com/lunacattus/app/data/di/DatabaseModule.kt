package com.lunacattus.app.data.di

import android.content.Context
import androidx.room.Room
import com.lunacattus.app.data.local.Database
import com.lunacattus.app.data.local.api.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context, Database::class.java, "clean-app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideResumeDao(database: Database): WeatherDao {
        return database.weatherDao()
    }
}