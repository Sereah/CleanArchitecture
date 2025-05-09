package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.app.domain.usecase.location.GetLocationUseCase
import com.lunacattus.app.domain.usecase.weather.GetDailyWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.GetHourlyWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.GetNowWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.GetWeatherGeoListUseCase
import com.lunacattus.app.domain.usecase.weather.QueryAllWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.RequestAndSaveWeatherUseCase
import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowFailToast
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowWeatherDetailPage
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState.Success.SearchDaily
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState.Success.SearchGeoList
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState.Success.SearchHourly
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState.Success.SearchNow
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState.Success.WeatherList
import com.lunacattus.clean.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationUseCase: GetLocationUseCase,
    private val requestAndSaveWeatherUseCase: RequestAndSaveWeatherUseCase,
    private val queryAllWeatherUseCase: QueryAllWeatherUseCase,
    private val getWeatherGeoListUseCase: GetWeatherGeoListUseCase,
    private val getNowWeatherUseCase: GetNowWeatherUseCase,
    private val getDailyWeatherUseCase: GetDailyWeatherUseCase,
    private val getHourlyWeatherUseCase: GetHourlyWeatherUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        updateUiState { WeatherUiState.Loading }
        observeCurrentLocation()
        observeAllWeather()
    }

    override val initUiState: WeatherUiState get() = WeatherUiState.Initial

    override fun processUiIntent(intent: WeatherUiIntent) {
        when (intent) {
            is WeatherUiIntent.OnRequestSearchCity -> {
                viewModelScope.launch {
                    getWeatherGeoListUseCase.invoke(intent.key).onSuccess { list ->
                        updateUiState { SearchGeoList(list) }
                    }.onFailure {
                        sendSideEffect(
                            ShowFailToast(
                                msg = it.localizedMessage ?: ""
                            )
                        )
                    }
                }
            }

            is WeatherUiIntent.OnRequestGetSearchCityWeather -> {
                viewModelScope.launch {
                    sendSideEffect(ShowWeatherDetailPage(intent.geo))
                    getNowWeatherUseCase.invoke(intent.geo.id).onSuccess { now ->
                        updateUiState { SearchNow(now) }
                    }.onFailure {
                        sendSideEffect(
                            ShowFailToast(
                                msg = it.localizedMessage ?: ""
                            )
                        )
                    }
                    getDailyWeatherUseCase.invoke(intent.geo.id).onSuccess { daily ->
                        updateUiState { SearchDaily(daily) }
                    }.onFailure {
                        sendSideEffect(
                            ShowFailToast(
                                msg = it.localizedMessage ?: ""
                            )
                        )
                    }
                    getHourlyWeatherUseCase.invoke(intent.geo.id).onSuccess { hourly ->
                        updateUiState { SearchHourly(hourly) }
                    }.onFailure {
                        sendSideEffect(
                            ShowFailToast(
                                msg = it.localizedMessage ?: ""
                            )
                        )
                    }
                }
            }

            is WeatherUiIntent.AddCity -> {
                viewModelScope.launch {
                    requestAndSaveWeatherUseCase.invoke(intent.id, false)
                }
            }
        }
    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    private fun observeCurrentLocation() {
        viewModelScope.launch {
            locationUseCase.invoke().filterNotNull().distinctUntilChanged().collect { location ->
                Logger.d(TAG, "collect current location: $location")
                if (location.latitude == 0.0 && location.longitude == 0.0) {
                    sendSideEffect(
                        ShowFailToast(
                            msg = "get location fail"
                        )
                    )
                    return@collect
                }
                requestAndSaveWeatherUseCase.invoke(
                    "${location.longitude},${location.latitude}",
                    true
                ).onSuccess {
                    Logger.d(TAG, "request and save current location weather success!")
                }.onFailure {
                    sendSideEffect(
                        ShowFailToast(
                            msg = it.localizedMessage ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun observeAllWeather() {
        viewModelScope.launch {
            queryAllWeatherUseCase.invoke().onSuccess { flow ->
                flow.filter { it.isNotEmpty() }.distinctUntilChanged().collect { weather ->
                    Logger.d(TAG, "query all weather: ${weather.size}")
                    updateUiState {
                        WeatherList(weather)
                    }
                }
            }.onFailure {
                sendSideEffect(ShowFailToast(msg = it.localizedMessage ?: ""))
            }
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}