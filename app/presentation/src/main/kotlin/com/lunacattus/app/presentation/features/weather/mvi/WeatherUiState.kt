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

    data class Success(
        val weatherList: List<Weather> = emptyList(),
        val searchGeoList: List<WeatherGeo> = emptyList(),
        val searchNowWeather: NowWeather? = null,
        val searchDailyWeather: List<DailyWeather> = emptyList(),
        val searchHourlyWeather: List<HourlyWeather> = emptyList(),
        val queryGeo: WeatherGeo? = null,
        val selectedCityId: String = ""
    ) : WeatherUiState

    data class Error(val msg: String) : WeatherUiState
}