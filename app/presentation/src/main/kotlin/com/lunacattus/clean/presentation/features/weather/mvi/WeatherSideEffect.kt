package com.lunacattus.clean.presentation.features.weather.mvi

import com.lunacattus.clean.presentation.common.ui.base.ISideEffect

sealed class WeatherSideEffect : ISideEffect {
    data object RequestSDKInit : WeatherSideEffect()
}