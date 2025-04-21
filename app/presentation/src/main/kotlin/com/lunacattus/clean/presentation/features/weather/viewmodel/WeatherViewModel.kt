package com.lunacattus.clean.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.domain.model.weather.WeatherException
import com.lunacattus.clean.domain.usecase.weather.GetCurrentLiveWeatherUseCase
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiState
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
                        is WeatherException.EmptyWeatherInfo -> {
                            sendSideEffect(WeatherSideEffect.ShowGetWeatherInfoEmptyToast)
                        }

                        is WeatherException.EmptyAdCode -> {
                            sendSideEffect(WeatherSideEffect.ShowAdCodeEmptyToast)
                        }

                        is WeatherException.FailNetworkRequest -> {
                            sendSideEffect(WeatherSideEffect.ShowNetworkRequestFailToast)
                        }
                    }
                }
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}