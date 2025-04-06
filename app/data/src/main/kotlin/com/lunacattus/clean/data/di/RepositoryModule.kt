package com.lunacattus.clean.data.di

import com.lunacattus.clean.data.repository.ResumeRepository
import com.lunacattus.clean.domain.repository.IResumeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideResumeRepository(impl: ResumeRepository): IResumeRepository
}