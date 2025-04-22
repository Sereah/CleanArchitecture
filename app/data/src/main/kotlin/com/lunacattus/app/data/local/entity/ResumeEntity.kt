package com.lunacattus.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resume")
data class ResumeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val age: Int,
    val email: String,
    val phone: String,
    val educationList: String,
    val workExperienceList: String,
    val skillList: String
)