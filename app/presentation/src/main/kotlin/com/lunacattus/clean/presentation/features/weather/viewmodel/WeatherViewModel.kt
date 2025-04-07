package com.lunacattus.clean.presentation.features.weather.viewmodel

import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor() :
    BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        sendSideEffect(WeatherSideEffect.RequestSDKInit)
    }

    override val initUiState: WeatherUiState get() = WeatherUiState()

    override suspend fun processUiIntent(intent: WeatherUiIntent) {

    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}