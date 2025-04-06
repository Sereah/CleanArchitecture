package com.lunacattus.clean.data.repository

import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.data.local.dao.ResumeDao
import com.lunacattus.clean.data.mapper.ResumeMapper.mapper
import com.lunacattus.clean.domain.model.Resume
import com.lunacattus.clean.domain.repository.IResumeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ResumeRepository @Inject constructor(
    private val resumeDao: ResumeDao
) : IResumeRepository {

    override suspend fun addNewResume(resume: Resume) {
        Logger.d(TAG, "addNewResume: $resume")
        resumeDao.insert(resume.mapper())
    }

    override fun allResume(): Flow<List<Resume>> {
        Logger.d(TAG, "get allResume.")
        return resumeDao.allResumeFlow().map { list ->
            list.map { entity ->
                entity.mapper()
            }
        }
    }

    override suspend fun deleteResume(resume: Resume) {
        Logger.d(TAG, "deleteResume: $resume")
        resumeDao.deleteResume(resume.mapper())
    }

    override suspend fun updateResume(resume: Resume) {
        Logger.d(TAG, "updateResume: $resume")
        resumeDao.updateResume(resume.mapper())
    }

    override suspend fun getResumeById(id: String): Resume {
        Logger.d(TAG, "getResumeById: $id")
        return resumeDao.getResumeById(id).mapper()
    }

    companion object {
        private const val TAG = "ResumeRepository"
    }
}