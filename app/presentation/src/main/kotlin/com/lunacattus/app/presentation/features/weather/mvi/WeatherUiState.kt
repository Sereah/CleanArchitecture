package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.common.ui.base.IUiState

sealed interface WeatherUiState : IUiState {
    data object Initial : WeatherUiState
    data object Loading : WeatherUiState

    sealed interface Success : WeatherUiState {
        data class WeatherList(val weathers: List<Weather>) : Success
        data class SearchGeoList(val geo: List<WeatherGeo>) : Success
        data class SearchNow(val now: NowWeather) : Success
        data class SearchDaily(val daily: List<DailyWeather>) : Success
        data class SearchHourly(val hourly: List<HourlyWeather>) : Success
    }

    data class Error(val msg: String) : WeatherUiState
}