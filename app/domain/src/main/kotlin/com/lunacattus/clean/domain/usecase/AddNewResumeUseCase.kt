package com.lunacattus.clean.domain.usecase

import com.lunacattus.clean.domain.model.Resume
import com.lunacattus.clean.domain.repository.IResumeRepository
import javax.inject.Inject

class AddNewResumeUseCase @Inject constructor(private val repository: IResumeRepository) {
    suspend operator fun invoke(resume: Resume) = repository.addNewResume(resume)
}