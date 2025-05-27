package com.lunacattus.app.presentation.enter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.lunacattus.app.presentation.common.theme.AppTheme
import com.lunacattus.app.presentation.features.home.HomeContent
import com.lunacattus.app.presentation.features.weather.WeatherContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        setContent {
            AppTheme {
                Surface(
                    color = AppTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val backStack = remember {
                        mutableStateListOf<Route>(Home)
                    }
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when (key) {
                                is Home -> NavEntry(key) {
                                    HomeContent {
                                        backStack.add(Weather)
                                    }
                                }

                                is Weather -> NavEntry(key) {
                                    WeatherContent()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

sealed interface Route
data object Home : Route
data object Weather : Route