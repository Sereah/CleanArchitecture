package com.lunacattus.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "live_weather")
data class WeatherEntity(
    @PrimaryKey val adCode: Int,
    val provence: String,
    val city: String,
    val condition: String,
    val temperature: Float,
    val windDirection: String,
    val windPower: String,
    val humidity: Int,
    val reportTime: String,
    val updateTime: Long
)

@Entity(
    tableName = "daily_weather",
    foreignKeys = [
        ForeignKey(
            entity = WeatherEntity::class,
            parentColumns = ["adCode"],
            childColumns = ["adCode"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val adCode: Int,
    val date: String,
    val week: Int,
    val dayCondition: String,
    val nightCondition: String,
    val dayTemp: Float,
    val nightTemp: Float,
    val dayWindDirection: String,
    val nightWindDirection: String,
    val dayWindPower: String,
    val nightWindPower: String,
    val reportTime: String,
    val updateTime: Long
)

data class WeatherWithDailyWeather(
    @Embedded val weather: WeatherEntity,
    @Relation(
        parentColumn = "adCode",
        entityColumn = "adCode"
    )
    val dailyWeather: List<DailyWeatherEntity>
)