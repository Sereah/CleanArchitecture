package com.lunacattus.app.presentation.features.weather.ui.page

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewbinding.ViewBinding
import com.lunacattus.app.presentation.common.ui.base.BaseDialogFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.presentation.R

abstract class BaseWeatherDialog<VB : ViewBinding>(inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    BaseDialogFragment<VB, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        inflateBinding
    ) {

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)
}