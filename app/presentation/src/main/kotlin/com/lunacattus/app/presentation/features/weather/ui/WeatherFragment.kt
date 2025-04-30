package com.lunacattus.app.presentation.features.weather.ui

import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment :
    BaseFragment<FragmentWeatherBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentWeatherBinding::inflate
    ) {

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Loading> {
            Logger.d(TAG, "Loading---")
        }

        collectState<WeatherUiState.Success.Now>(
            filterFn = { it.now.id.isNotEmpty() }
        ) {
            Logger.d(TAG, "NowWeather: $it")
        }

        collectState<WeatherUiState.Success.Geo> {
            Logger.d(TAG, "WeatherGeo: $it")
        }

        collectState<WeatherUiState.Success.Daily, Int>(
            mapFn = { it.daily.size },
            filterFn = { it > 0 }
        ) {
            Logger.d(TAG, "DailyWeather: $it")
        }

        collectState<WeatherUiState.Success.Hourly>(
            filterFn = { it.hourly.isNotEmpty() }
        ) {
            Logger.d(TAG, "HourlyWeather: ${it.hourly.size}")
        }
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {

    }

    override fun onStop() {
        super.onStop()
        Logger.d(TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Logger.d(TAG, "onStart")
    }

    override fun onDestroyView() {
        Logger.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    companion object {
        const val TAG = "WeatherFragment"
    }
}