package com.lunacattus.app.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherGeoEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("DELETE FROM q_weather_geo WHERE isCurrentLocation = 1")
    suspend fun deleteOldLocationQWeatherGeo()

    @Query("DELETE FROM q_weather_now WHERE isCurrentLocation = 1")
    suspend fun deleteOldLocationQWeatherNow()

    @Query("DELETE FROM q_weather_daily WHERE isCurrentLocation = 1")
    suspend fun deleteOldLocationQWeatherDaily()

    @Query("DELETE FROM q_weather_hourly WHERE isCurrentLocation = 1")
    suspend fun deleteOldLocationQWeatherHourly()

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherGeo(geo: QWeatherGeoEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherNow(now: QWeatherNowEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherDaily(daily: List<QWeatherDailyEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherHourly(hourly: List<QWeatherHourlyEntity>)

    @Query("SELECT * FROM q_weather_now WHERE locationId= :locationId")
    fun queryQWeatherNow(locationId: String): Flow<QWeatherNowEntity>

    @Query("SELECT * FROM q_weather_daily WHERE locationId= :locationId")
    fun queryQWeatherDaily(locationId: String): Flow<List<QWeatherDailyEntity>>

    @Query("SELECT * FROM q_weather_hourly WHERE locationId= :locationId")
    fun queryQWeatherHourly(locationId: String): Flow<List<QWeatherHourlyEntity>>

    @Transaction
    @Query("SELECT * FROM q_weather_geo")
    fun queryAllQWeather(): Flow<List<QWeatherEntity>>
}