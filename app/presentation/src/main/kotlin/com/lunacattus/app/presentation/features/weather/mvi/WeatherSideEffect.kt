package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.presentation.common.ui.base.ISideEffect

sealed interface WeatherSideEffect : ISideEffect {
    data class ShowFailToast(val code: Int? = null, val msg: String) :
        WeatherSideEffect
}