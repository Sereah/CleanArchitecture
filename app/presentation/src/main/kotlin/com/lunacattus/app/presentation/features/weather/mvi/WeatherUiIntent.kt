package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.common.ui.base.IUiIntent

sealed interface WeatherUiIntent : IUiIntent {
    data class OnRequestSearchCity(val key: String) : WeatherUiIntent
    data class OnRequestGetSearchCityWeather(val geo: WeatherGeo) : WeatherUiIntent
    data class AddCity(val id: String): WeatherUiIntent
}