package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.domain.model.weather.CityInfo
import com.lunacattus.app.domain.model.weather.WeatherInfo
import com.lunacattus.app.presentation.common.ui.base.IUiState

data class WeatherUiState(
    val loading: Boolean = true,
    val weatherInfo: WeatherInfo? = null,
    val searchResult: List<CityInfo> = emptyList()
) : IUiState