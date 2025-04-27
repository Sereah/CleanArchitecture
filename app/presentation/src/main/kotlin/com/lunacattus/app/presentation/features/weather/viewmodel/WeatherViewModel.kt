package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.app.domain.model.WeatherException
import com.lunacattus.app.domain.usecase.location.RequestLocationUseCase
import com.lunacattus.app.domain.usecase.weather.GetCurrentWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.RequestWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.SearchCityUseCase
import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val requestWeatherUseCase: RequestWeatherUseCase,
    private val searchCityUseCase: SearchCityUseCase,
    private val locationUseCase: RequestLocationUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        getWeather()
    }

    override val initUiState: WeatherUiState get() = WeatherUiState()

    override fun processUiIntent(intent: WeatherUiIntent) {
        when (intent) {
            WeatherUiIntent.OnNetworkWeatherInfoRequested -> {
                requestWeatherInfo()
            }

            is WeatherUiIntent.OnSearchCityRequested -> {
                searchCity(intent.keyword)
            }

            WeatherUiIntent.OnLocationRequested -> {
                viewModelScope.launch {
                    locationUseCase.invoke().collect {
                        Logger.d(TAG, "location: $it")
                    }
                }
            }
        }
    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    private fun searchCity(keyword: String) {
        Logger.d(TAG, "searchCity: $keyword")
        if (keyword.isEmpty()) return
        viewModelScope.launch {
            val result = searchCityUseCase.invoke(keyword)
            result.onSuccess { result ->
                updateUiState {
                    it.copy(searchResult = result)
                }
            }.onFailure { error ->
                Logger.e(TAG, "searchCity: $error")
                sendSideEffect(
                    WeatherSideEffect.ShowNetworkRequestFailToast(msg = error.toString())
                )
            }
        }
    }

    private fun requestWeatherInfo() {
        viewModelScope.launch {
            requestWeatherUseCase.invoke().onSuccess {
                Logger.d(TAG, "requestWeatherUseCase success")
            }.onFailure { error ->
                when (error) {
                    is WeatherException.ApiError -> {
                        sendSideEffect(
                            WeatherSideEffect.ShowNetworkRequestFailToast(
                                error.code,
                                error.msg
                            )
                        )
                    }

                    is WeatherException.OtherError -> {
                        sendSideEffect(
                            WeatherSideEffect.ShowNetworkRequestFailToast(msg = error.msg)
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            val result = getCurrentWeatherUseCase.invoke()
            result
                .onSuccess { weatherFlow ->
                    weatherFlow.collect { weather ->
                        updateUiState {
                            it.copy(loading = false, weatherInfo = weather)
                        }
                    }
                }.onFailure { error ->
                    Logger.e(TAG, "get weather error: $error")
                }
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}