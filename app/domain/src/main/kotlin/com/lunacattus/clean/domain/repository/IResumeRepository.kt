package com.lunacattus.clean.domain.repository

import com.lunacattus.clean.domain.model.Resume
import kotlinx.coroutines.flow.Flow

interface IResumeRepository {
    suspend fun addNewResume(resume: Resume)
    fun allResume(): Flow<List<Resume>>
    suspend fun deleteResume(resume: Resume)
    suspend fun updateResume(resume: Resume)
    suspend fun getResumeById(id: String): Resume
}