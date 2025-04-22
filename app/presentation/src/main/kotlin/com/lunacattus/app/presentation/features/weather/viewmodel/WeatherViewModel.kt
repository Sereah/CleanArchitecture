package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.clean.common.Logger
import com.lunacattus.app.domain.model.weather.WeatherException
import com.lunacattus.app.domain.usecase.weather.GetCurrentLiveWeatherUseCase
import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLiveWeatherUseCase: GetCurrentLiveWeatherUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        getLiveWeather()
    }

    override val initUiState: WeatherUiState get() = WeatherUiState()

    override fun processUiIntent(intent: WeatherUiIntent) {

    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    private fun getLiveWeather() {
        viewModelScope.launch {
            val result = getCurrentLiveWeatherUseCase.invoke()
            result
                .onSuccess { livesWeather ->
                    Logger.d(TAG, "getLiveWeather: $livesWeather")
                    updateUiState {
                        it.copy(loading = false, weatherInfo = livesWeather)
                    }
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

    companion object {
        const val TAG = "WeatherViewModel"
    }
}