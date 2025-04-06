package com.lunacattus.clean.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lunacattus.clean.data.local.dao.ResumeDao
import com.lunacattus.clean.data.local.entity.ResumeEntity

@Database(
    entities = [ResumeEntity::class],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun resumeDao(): ResumeDao
}