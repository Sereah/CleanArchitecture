package com.lunacattus.app.presentation.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) appDarkTheme else appLightTheme

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        content = content
    )

}