package com.lunacattus.clean.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lunacattus.clean.data.local.entity.ResumeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resumeEntity: ResumeEntity)

    @Query("SELECT * FROM resume")
    fun allResumeFlow(): Flow<List<ResumeEntity>>

    @Delete
    suspend fun deleteResume(resumeEntity: ResumeEntity)

    @Update
    suspend fun updateResume(resumeEntity: ResumeEntity)

    @Query("SELECT * FROM resume WHERE id = :id")
    suspend fun getResumeById(id: String): ResumeEntity
}