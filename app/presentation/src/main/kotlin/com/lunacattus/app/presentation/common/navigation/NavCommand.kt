package com.lunacattus.app.presentation.common.navigation

import android.os.Bundle
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

        fun defaultNavDirection(actionId: Int) = object : NavDirections {
            override val actionId: Int
                get() = actionId
            override val arguments: Bundle = Bundle()
        }

        fun defaultNavDirection(actionId: Int, bundle: Bundle) = object : NavDirections {
            override val actionId: Int get() = actionId
            override val arguments: Bundle get() = bundle
        }
    }

    data class ToDirection(
        val direction: NavDirections,
        val options: NavOptions? = defaultNavOptions,
    ) : NavCommand()

    data object Up : NavCommand()

}