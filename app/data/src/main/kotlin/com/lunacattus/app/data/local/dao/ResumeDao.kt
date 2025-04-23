package com.lunacattus.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lunacattus.app.data.local.entity.LiveWeatherEntity
import com.lunacattus.app.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(resumeEntity: WeatherEntity)

    @Query("SELECT * FROM weather")
    fun allResumeFlow(): Flow<List<WeatherEntity>>

}