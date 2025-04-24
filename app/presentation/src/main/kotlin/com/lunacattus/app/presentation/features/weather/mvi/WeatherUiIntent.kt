package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.presentation.common.ui.base.IUiIntent

sealed class WeatherUiIntent : IUiIntent {
    data object OnNetworkWeatherInfoRequested : WeatherUiIntent()
    data object OnCityOptionsRequested: WeatherUiIntent()
}