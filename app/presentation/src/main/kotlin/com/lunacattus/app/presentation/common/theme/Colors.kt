package com.lunacattus.app.presentation.common.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val background: Color,
    val mainText: Color
)

internal val LocalColorScheme = staticCompositionLocalOf { appLightTheme }

internal val appDarkTheme = ColorScheme(
    background = Color(0xFF000000),
    mainText = Color(0xFFFFFFFF),
)

internal val appLightTheme = ColorScheme(
    background = Color(0xFFFFFFFF),
    mainText = Color(0xFF000000)
)