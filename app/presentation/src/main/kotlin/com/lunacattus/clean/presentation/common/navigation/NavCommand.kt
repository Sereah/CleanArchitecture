package com.lunacattus.clean.presentation.common.navigation

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.lunacattus.clean.presentation.R

sealed class NavCommand {

    companion object {
        val defaultNavOptions = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
    }

    data class ToDirection(
        val direction: NavDirections,
        val options: NavOptions = defaultNavOptions,
    ) : NavCommand()

}