package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.app.domain.model.weather.WeatherException
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
    private val searchCityUseCase: SearchCityUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        getWeather()
        searchCity()
    }

    private fun searchCity() {
        viewModelScope.launch {
            val result = searchCityUseCase.invoke("重庆")
            result.onSuccess {
                Logger.d(TAG, "-----$it")
            }.onFailure {
                Logger.e(TAG, "-----$it")
            }
        }
    }

    override val initUiState: WeatherUiState get() = WeatherUiState()

    override fun processUiIntent(intent: WeatherUiIntent) {
        when (intent) {
            WeatherUiIntent.OnNetworkWeatherInfoRequested -> {
                requestWeatherInfo()
            }

            WeatherUiIntent.OnCityOptionsRequested -> {
                sendSideEffect(WeatherSideEffect.NavigateToCityOption)
            }
        }
    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
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