package com.lunacattus.app.presentation.features.weather.mvi

import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.common.ui.base.IUiIntent

sealed interface WeatherUiIntent : IUiIntent {
    data class SearchCity(val key: String) : WeatherUiIntent
    data class SearchCityWeather(val geo: WeatherGeo) : WeatherUiIntent
    data class AddCity(val id: String) : WeatherUiIntent
    data class QueryCity(val id: String) : WeatherUiIntent
    data class DeleteCity(val id: String) : WeatherUiIntent
    data class SelectCityPage(val id: String) : WeatherUiIntent
}