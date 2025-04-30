package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.app.domain.usecase.location.RequestLocationUseCase
import com.lunacattus.app.domain.usecase.weather.GetCurrentLocationIdAndInsertUseCase
import com.lunacattus.app.domain.usecase.weather.GetCurrentLocationWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.RequestAllWeatherInfoUseCase
import com.lunacattus.app.domain.usecase.weather.RequestNowWeatherInfoUseCase
import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationIdUseCase: GetCurrentLocationIdAndInsertUseCase,
    private val locationUseCase: RequestLocationUseCase,
    private val requestAllWeatherInfoUseCase: RequestAllWeatherInfoUseCase,
    private val getCurrentLocationWeatherUseCase: GetCurrentLocationWeatherUseCase,
    private val requestNowWeatherInfoUseCase: RequestNowWeatherInfoUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    private var nowJob: Job? = null

    init {
        Logger.d(TAG, "init.")
        updateUiState { WeatherUiState.Loading }
        observeCurrentLocation()
        observeCurrentLocationWeather()
    }

    override val initUiState: WeatherUiState get() = WeatherUiState.Initial

    override fun processUiIntent(intent: WeatherUiIntent) {

    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    private fun observeCurrentLocation() {
        viewModelScope.launch {
            locationUseCase.invoke().filterNotNull().distinctUntilChanged().collect { location ->
                Logger.d(TAG, "current location is: $location")
                getCurrentLocationIdUseCase.invoke(location.latitude, location.longitude)
                    .onSuccess { id ->
                        requestAllWeatherInfoUseCase.invoke(id)
                    }
                    .onFailure {

                    }
            }
        }
    }

    private fun observeCurrentLocationWeather() {
        viewModelScope.launch {
            launch {
                getCurrentLocationWeatherUseCase.invoke().filterNotNull()
                    .map { it.geo }.distinctUntilChanged().collect { geo ->
                        updateUiState { WeatherUiState.Success.Geo(geo) }
                    }
            }
            launch {
                getCurrentLocationWeatherUseCase.invoke().filterNotNull()
                    .map { it.nowWeather }.collect { nowWeather ->
                        Logger.d(TAG, "collect now: $nowWeather")
                        nowJob?.cancel()
                        nowJob = launch {
                            delay(10 * 1000)
                            Logger.d(TAG, "requestNowWeatherInfoUseCase: ${nowWeather.id}")
                            requestNowWeatherInfoUseCase.invoke(nowWeather.id)
                        }
                        updateUiState { WeatherUiState.Success.Now(nowWeather) }
                    }
            }
            launch {
                getCurrentLocationWeatherUseCase.invoke().filterNotNull()
                    .map { it.dailyWeather }.collect { dailyWeather ->
                        Logger.d(TAG, "collect dailyWeather: ${dailyWeather.size}")
                        updateUiState { WeatherUiState.Success.Daily(dailyWeather) }
                    }
            }
            launch {
                getCurrentLocationWeatherUseCase.invoke().filterNotNull()
                    .map { it.hourlyWeather }.distinctUntilChanged().collect { hourlyWeather ->
                        updateUiState { WeatherUiState.Success.Hourly(hourlyWeather) }
                    }
            }
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}