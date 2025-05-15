package com.lunacattus.app.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherGeoEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("DELETE FROM q_weather_geo WHERE isCurrentLocation = 1")
    suspend fun deleteCurrentLocationQWeatherGeo()

    @Query("DELETE FROM q_weather_geo WHERE locationId = :locationId")
    suspend fun deleteQWeatherGeo(locationId: String)

    @Query("DELETE FROM q_weather_now WHERE locationId = :locationId")
    suspend fun deleteQWeatherNow(locationId: String)

    @Query("DELETE FROM q_weather_daily WHERE locationId = :locationId")
    suspend fun deleteQWeatherDaily(locationId: String)

    @Query("DELETE FROM q_weather_hourly WHERE locationId = :locationId")
    suspend fun deleteQWeatherHourly(locationId: String)

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertQWeatherGeo(geo: QWeatherGeoEntity): Long

    @Update
    suspend fun updateQWeatherGeo(geo: QWeatherGeoEntity)

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

    @Query("SELECT * FROM q_weather_geo WHERE isCurrentLocation = :isCurrentLocation")
    suspend fun queryGeo(isCurrentLocation: Boolean): List<QWeatherGeoEntity>

    @Query("SELECT * FROM q_weather_geo WHERE locationId = :id")
    suspend fun queryGeo(id: String): QWeatherGeoEntity?
}