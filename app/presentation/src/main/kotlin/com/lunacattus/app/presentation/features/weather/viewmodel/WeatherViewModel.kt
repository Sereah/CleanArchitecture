package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.app.domain.usecase.location.RequestLocationUseCase
import com.lunacattus.app.domain.usecase.weather.RequestAllWeatherInfoUseCase
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
    private val requestAllWeatherUseCase: RequestAllWeatherInfoUseCase,
    private val locationUseCase: RequestLocationUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        viewModelScope.launch {
            requestAllWeatherUseCase().onSuccess {
                Logger.d(TAG, "requestWeatherUseCase success")
            }.onFailure {
                Logger.e(TAG, it.localizedMessage ?: "")
            }
        }
    }

    override val initUiState: WeatherUiState get() = WeatherUiState()

    override fun processUiIntent(intent: WeatherUiIntent) {
        when (intent) {
            WeatherUiIntent.OnNetworkWeatherInfoRequested -> {
            }

            is WeatherUiIntent.OnSearchCityRequested -> {
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

    companion object {
        const val TAG = "WeatherViewModel"
    }
}