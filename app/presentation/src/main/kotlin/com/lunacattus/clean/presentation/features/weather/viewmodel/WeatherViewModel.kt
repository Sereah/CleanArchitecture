package com.lunacattus.clean.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.domain.usecase.weather.GetLiveWeatherUseCase
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getLiveWeatherUseCase: GetLiveWeatherUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        getLiveWeather()
    }

    override val initUiState: WeatherUiState get() = WeatherUiState()

    override suspend fun processUiIntent(intent: WeatherUiIntent) {

    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    private fun getLiveWeather() {
        viewModelScope.launch {
            val result = getLiveWeatherUseCase.invoke("500000")
            result
                .onSuccess {
                    Logger.d(TAG, "getLiveWeather: $it")
                }.onFailure {
                    Logger.e(TAG, "$it")
                }
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}