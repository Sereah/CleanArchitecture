package com.lunacattus.app.presentation.common.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val background: Color
)

internal val LocalColorScheme = staticCompositionLocalOf { appLightTheme }

internal val appDarkTheme = ColorScheme(
    background = Color(0xFF000000)
)

internal val appLightTheme = ColorScheme(
    background = Color(0xFFFFFFFF)
)