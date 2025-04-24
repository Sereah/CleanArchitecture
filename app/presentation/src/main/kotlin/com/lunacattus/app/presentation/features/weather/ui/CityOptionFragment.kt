package com.lunacattus.app.presentation.features.weather.ui

import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentCityOptionBinding

class CityOptionFragment :
    BaseFragment<FragmentCityOptionBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentCityOptionBinding::inflate
    ) {

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {

    }

    override fun setupObservers() {

    }

    override fun handleSideEffect(effect: WeatherSideEffect) {

    }
}