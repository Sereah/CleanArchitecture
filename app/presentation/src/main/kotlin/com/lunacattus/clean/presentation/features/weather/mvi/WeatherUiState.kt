package com.lunacattus.clean.presentation.features.weather.mvi

import com.lunacattus.clean.presentation.common.ui.base.IUiState

data class WeatherUiState(
    val loading: Boolean = true
) : IUiState