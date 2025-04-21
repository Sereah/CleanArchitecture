package com.lunacattus.clean.presentation.features.weather.ui

import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.presentation.features.weather.viewmodel.WeatherViewModel
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

    }

    override fun handleSideEffect(effect: WeatherSideEffect) {

    }

    companion object {
        const val TAG = "WeatherFragment"
    }
}