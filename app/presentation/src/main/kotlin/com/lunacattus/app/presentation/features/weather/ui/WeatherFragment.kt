package com.lunacattus.app.presentation.features.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.amap.api.maps.MapsInitializer
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import com.lunacattus.common.isToday
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment :
    BaseFragment<FragmentWeatherBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentWeatherBinding::inflate
    ) {

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        MapsInitializer.updatePrivacyShow(requireContext(), true, true)
        MapsInitializer.updatePrivacyAgree(requireContext(), true)
        requireActivity().window.insetsController?.apply {
            setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        collectState<WeatherUiState.Loading> {
            Logger.d(TAG, "Loading----")
        }

        collectState<WeatherUiState.Success.WeatherList, Weather>(
            mapFn = { weatherList ->
                weatherList.weathers.toMutableList().first { it.geo.isCurrentLocation }
            },
            filterFn = {
                it.nowWeather.id.isNotEmpty() &&
                        it.dailyWeather.isNotEmpty() &&
                        it.hourlyWeather.isNotEmpty()
            }
        ) { weather ->
            Logger.d(TAG, "collect current location all weather: $weather")
            binding.name.text = weather.geo.name
            binding.temp.text = weather.nowWeather.temp.toString()
            val today = weather.dailyWeather.first { it.date.isToday() }
            binding.maxTemp.text = "${today.tempMax}°"
            binding.minTemp.text = "${today.tempMin}°"
            binding.weatherText.text = weather.nowWeather.weatherText.toString()
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
        when (effect) {
            is WeatherSideEffect.ShowFailToast -> {
                Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
            }
        }
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