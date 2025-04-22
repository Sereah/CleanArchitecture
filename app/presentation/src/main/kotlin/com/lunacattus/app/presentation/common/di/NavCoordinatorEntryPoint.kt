package com.lunacattus.app.presentation.common.di

import com.lunacattus.app.presentation.common.navigation.NavCoordinator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface NavCoordinatorEntryPoint {
    fun navCoordinator(): NavCoordinator
}