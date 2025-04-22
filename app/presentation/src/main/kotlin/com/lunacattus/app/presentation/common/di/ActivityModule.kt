package com.lunacattus.app.presentation.common.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lunacattus.clean.presentation.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @MainNavController
    fun provideMainNavController(
        @ActivityContext context: Context
    ): NavController {
        return (context as FragmentActivity)
            .supportFragmentManager
            .findFragmentById(R.id.main_host_fragment)
            ?.findNavController() ?: throw IllegalStateException("Main host fragment not found.")
    }
}