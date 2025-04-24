package com.lunacattus.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lunacattus.app.data.local.api.WeatherDao
import com.lunacattus.app.data.local.entity.DailyWeatherEntity
import com.lunacattus.app.data.local.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, DailyWeatherEntity::class],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}