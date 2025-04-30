package com.lunacattus.app.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lunacattus.app.data.local.entity.GaoDeDailyWeatherEntity
import com.lunacattus.app.data.local.entity.GaoDeLiveWeatherEntity
import com.lunacattus.app.data.local.entity.GaoDeWeatherWithDailyWeather
import com.lunacattus.app.data.local.entity.QWeatherCombinedData
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherLocationEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertGaoDeLiveWeather(weather: GaoDeLiveWeatherEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertGaoDeDailyWeather(dailyWeathers: List<GaoDeDailyWeatherEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM gao_de_live_weather ORDER BY updateTime DESC LIMIT 1")
    fun getGaoDeLastWeatherWithDailyWeather(): Flow<GaoDeWeatherWithDailyWeather?>

    @Query("DELETE FROM gao_de_daily_weather WHERE adCode = :adCode")
    suspend fun clearGaoDeDailyWeather(adCode: Int)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherLocation(weatherLocation: QWeatherLocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherNow(weatherNow: QWeatherNowEntity): Long

    @Query("DELETE FROM q_weather_now WHERE locationId = :locationId")
    suspend fun clearQWeatherNow(locationId: String)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherDaily(weatherDaily: List<QWeatherDailyEntity>): List<Long>

    @Query("DELETE FROM q_weather_daily WHERE locationId = :locationId")
    suspend fun clearQWeatherDaily(locationId: String)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQWeatherHourly(weatherHourly: List<QWeatherHourlyEntity>): List<Long>

    @Query("DELETE FROM q_weather_hourly WHERE locationId = :locationId")
    suspend fun clearQWeatherHourly(locationId: String)

    @Transaction
    @Query("SELECT * FROM q_weather_location WHERE isCurrentLocation = 1")
    fun getCurrentLocationWeather(): Flow<QWeatherCombinedData?>
}