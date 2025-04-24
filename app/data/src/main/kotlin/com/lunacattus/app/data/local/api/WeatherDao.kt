package com.lunacattus.app.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lunacattus.app.data.local.entity.DailyWeatherEntity
import com.lunacattus.app.data.local.entity.WeatherEntity
import com.lunacattus.app.data.local.entity.WeatherWithDailyWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrUpdateWeather(weather: WeatherEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrUpdateDailyWeather(dailyWeathers: List<DailyWeatherEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM live_weather ORDER BY updateTime DESC LIMIT 1")
    fun getWeatherWithDailyWeather(): Flow<WeatherWithDailyWeather?>

    @Query("DELETE FROM daily_weather WHERE adCode = :adCode")
    suspend fun clearDailyWeather(adCode: Int)
}