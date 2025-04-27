package com.lunacattus.app.domain.usecase.location

import com.lunacattus.app.domain.model.Location
import com.lunacattus.app.domain.repository.location.ILocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestLocationUseCase @Inject constructor(
    private val repository: ILocationRepository
) {
    operator fun invoke(): Flow<Location> {
        return repository.getLocation()
    }
}