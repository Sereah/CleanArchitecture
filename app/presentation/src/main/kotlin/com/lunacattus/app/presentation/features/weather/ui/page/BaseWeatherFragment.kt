package com.lunacattus.app.presentation.features.weather.ui.page

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewbinding.ViewBinding
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.presentation.R

abstract class BaseWeatherFragment<VB : ViewBinding>(inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    BaseFragment<VB, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        inflateBinding
    ) {

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun handleSideEffect(effect: WeatherSideEffect) {
        when(effect) {
            is WeatherSideEffect.ShowFailToast -> {
                Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}

fun WeatherText.iconSource(isDay: Boolean): Int {
    return when (this) {
        WeatherText.SUNNY -> if (isDay) R.drawable.ic_weather_sunny_day else R.drawable.ic_weather_sunny_night
        WeatherText.CLOUDY -> if (isDay) R.drawable.ic_weather_cloudy_day else R.drawable.ic_weather_cloudy_night
        WeatherText.OVERCAST -> R.drawable.ic_weather_overcast
        WeatherText.RAINY -> R.drawable.ic_weather_rainy
        WeatherText.STORMY -> R.drawable.ic_weather_stormy
        WeatherText.SNOWY -> R.drawable.ic_weather_snowy
        WeatherText.THUNDERSTORM -> R.drawable.ic_weather_thunderstorm
        WeatherText.FOGGY -> R.drawable.ic_weather_foggy
        WeatherText.WINDY -> R.drawable.ic_weather_windy
        WeatherText.UNKNOWN -> R.drawable.ic_weather_unknown
    }
}