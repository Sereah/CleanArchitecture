package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.common.ui.base.IUiIntent

sealed interface WeatherUiIntent : IUiIntent {
    data class SearchCity(val key: String) : WeatherUiIntent
    data class GetSearchCityWeather(val geo: WeatherGeo) : WeatherUiIntent
    data class OnRequestAddCity(val id: String): WeatherUiIntent
    data class QueryGeoById(val id: String): WeatherUiIntent
}