package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.presentation.common.ui.base.ISideEffect

sealed class WeatherSideEffect : ISideEffect {
    data class ShowNetworkRequestFailToast(val code: Int? = null, val msg: String) :
        WeatherSideEffect()
}