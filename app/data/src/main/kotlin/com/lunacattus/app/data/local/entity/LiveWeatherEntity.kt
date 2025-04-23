package com.lunacattus.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val provence: String,
    val city: String,
    val condition: String,
    val temperature: Float,
    val windDirection: String,
    val windPower: String,
    val humidity: Int,
    val reportTime: Long,
    val dailyWeather: List<DailyWeatherEntity> = emptyList()
)

@Entity(tableName = "daily_weather")
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weatherInfoId: Int,
    val date: Long,
    val week: Int,
    val minTemp: Float,
    val maxTemp: Float,
    val condition: String
)

data class WeatherInfoWithDailyForecast(
    @Embedded val liveWeather: WeatherEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherInfoId"
    )
    val dailyForecast: List<DailyWeatherEntity>
)