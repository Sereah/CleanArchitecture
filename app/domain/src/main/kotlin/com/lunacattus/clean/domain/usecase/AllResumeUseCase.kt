package com.lunacattus.clean.domain.usecase

import com.lunacattus.clean.domain.model.Resume
import com.lunacattus.clean.domain.repository.IResumeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllResumeUseCase @Inject constructor(private val resumeRepository: IResumeRepository) {
    operator fun invoke(): Flow<List<Resume>> = resumeRepository.allResume()
}