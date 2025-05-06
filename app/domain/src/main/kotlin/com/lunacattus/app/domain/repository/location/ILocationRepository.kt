package com.lunacattus.app.domain.repository.location

import com.lunacattus.app.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getLocation(): Flow<Location>
    fun getLocationByGaoDe(): Flow<Location>
}