package com.lunacattus.app.presentation.common.navigation

import androidx.navigation.NavController
import com.lunacattus.app.presentation.common.di.MainNavController
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NavCoordinator @Inject constructor(
    @MainNavController private val mainNavController: NavController
) {
    fun execute(command: NavCommand) {
        when (command) {
            is NavCommand.ToDirection -> {
                mainNavController.navigate(command.direction, command.options)
            }

            NavCommand.Up -> {
                mainNavController.navigateUp()
            }
        }
    }
}