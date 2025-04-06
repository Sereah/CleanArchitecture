package com.lunacattus.clean.presentation.common.di

import com.lunacattus.clean.presentation.common.navigation.NavCoordinator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface NavCoordinatorEntryPoint {
    fun navCoordinator(): NavCoordinator
}