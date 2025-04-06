package com.lunacattus.clean.domain.usecase

import com.lunacattus.clean.domain.model.Resume
import com.lunacattus.clean.domain.repository.IResumeRepository
import javax.inject.Inject

class GetResumeByIdUseCase @Inject constructor(private val resumeRepository: IResumeRepository) {
    suspend operator fun invoke(id: String): Resume = resumeRepository.getResumeById(id)
}