package com.lunacattus.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lunacattus.app.data.local.dao.ResumeDao
import com.lunacattus.app.data.local.entity.ResumeEntity

@Database(
    entities = [ResumeEntity::class],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun resumeDao(): ResumeDao
}