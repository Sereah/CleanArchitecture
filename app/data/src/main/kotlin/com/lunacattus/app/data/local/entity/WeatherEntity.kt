package com.lunacattus.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "q_weather_geo")
data class QWeatherGeoEntity(
    @PrimaryKey val locationId: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val province: String,
    val city: String,
    val timeZone: String,
    val isCurrentLocation: Boolean
)

@Entity(
    tableName = "q_weather_now",
    foreignKeys = [
        ForeignKey(
            entity = QWeatherGeoEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QWeatherNowEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val locationId: String,
    val isCurrentLocation: Boolean,
    val obsTime: String,
    val temp: Int,
    val feelsTemp: Int,
    val text: String,
    val wind360: Float,
    val windScale: String,
    val windSpeed: Int,
    val humidity: Int,
    val preCip: Float,
    val pressure: Int,
    val vis: Int,
    val cloud: Int,
    val dew: Int
)

@Entity(
    tableName = "q_weather_daily",
    foreignKeys = [
        ForeignKey(
            entity = QWeatherGeoEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QWeatherDailyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val locationId: String,
    val isCurrentLocation: Boolean,
    val fxDate: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonSet: String,
    val moonPhase: String,
    val tempMax: Int,
    val tempMin: Int,
    val textDay: String,
    val textNight: String,
    val wind360Day: Float,
    val windScaleDay: String,
    val windSpeedDay: Int,
    val wind360Night: Float,
    val windScaleNight: String,
    val windSpeedNight: Int,
    val humidity: Int,
    val preCip: Float,
    val pressure: Int,
    val vis: Int,
    val cloud: Int,
    val uvIndex: Int
)

@Entity(
    tableName = "q_weather_hourly",
    foreignKeys = [
        ForeignKey(
            entity = QWeatherGeoEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QWeatherHourlyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val locationId: String,
    val isCurrentLocation: Boolean,
    val fxTime: String,
    val temp: Int,
    val text: String,
    val wind360: Float,
    val windScale: String,
    val windSpeed: Int,
    val humidity: Int,
    val pop: Int,
    val preCip: Float,
    val pressure: Int,
    val cloud: Int,
    val dew: Int
)

data class QWeatherEntity(
    @Embedded val geo: QWeatherGeoEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId",
        entity = QWeatherNowEntity::class
    )
    val now: QWeatherNowEntity?,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId",
        entity = QWeatherDailyEntity::class
    )
    val daily: List<QWeatherDailyEntity>,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId",
        entity = QWeatherHourlyEntity::class
    )
    val hourly: List<QWeatherHourlyEntity>
)

@Entity(tableName = "gao_de_live_weather")
data class GaoDeLiveWeatherEntity(
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
    tableName = "gao_de_daily_weather",
    foreignKeys = [
        ForeignKey(
            entity = GaoDeLiveWeatherEntity::class,
            parentColumns = ["adCode"],
            childColumns = ["adCode"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GaoDeDailyWeatherEntity(
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

data class GaoDeWeatherWithDailyWeather(
    @Embedded val weather: GaoDeLiveWeatherEntity,
    @Relation(
        parentColumn = "adCode",
        entityColumn = "adCode"
    )
    val dailyWeather: List<GaoDeDailyWeatherEntity>
)