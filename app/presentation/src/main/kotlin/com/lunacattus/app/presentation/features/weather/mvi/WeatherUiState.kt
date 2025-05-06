package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.common.ui.base.IUiState

sealed interface WeatherUiState : IUiState {
    object Initial : WeatherUiState
    object Loading : WeatherUiState

    sealed interface Success : WeatherUiState {
        data class Geo(val geo: WeatherGeo) : Success
        data class Now(val now: NowWeather) : Success
        data class Daily(val daily: List<DailyWeather>) : Success
        data class Hourly(val hourly: List<HourlyWeather>) : Success
    }

    data class Error(val msg: String) : WeatherUiState
}