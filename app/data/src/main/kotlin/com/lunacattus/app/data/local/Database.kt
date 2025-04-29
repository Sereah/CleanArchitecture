package com.lunacattus.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lunacattus.app.data.local.api.WeatherDao
import com.lunacattus.app.data.local.entity.GaoDeDailyWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherLocationEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import com.lunacattus.app.data.local.entity.GaoDeLiveWeatherEntity

@Database(
    entities = [
        GaoDeLiveWeatherEntity::class,
        GaoDeDailyWeatherEntity::class,
        QWeatherLocationEntity::class,
        QWeatherNowEntity::class,
        QWeatherDailyEntity::class,
        QWeatherHourlyEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}