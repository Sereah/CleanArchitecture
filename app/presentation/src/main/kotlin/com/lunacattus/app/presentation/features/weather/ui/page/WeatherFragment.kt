package com.lunacattus.app.presentation.features.weather.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.presentation.common.ui.UniformItemDecoration
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.DailyWeatherListAdapter
import com.lunacattus.app.presentation.features.weather.ui.adapter.HourlyWeatherListAdapter
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import com.lunacattus.common.dpToPx
import com.lunacattus.common.isToday
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : BaseWeatherFragment<FragmentWeatherBinding>(
    FragmentWeatherBinding::inflate
) {

    private var position = 0
    private lateinit var hourlyAdapter: HourlyWeatherListAdapter
    private lateinit var dailyAdapter: DailyWeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("POSITION", 0)
        }
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        hourlyAdapter = HourlyWeatherListAdapter(requireContext())
        binding.hourlyList.apply {
            adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            addItemDecoration(
                UniformItemDecoration(
                    isVertical = false,
                    maxItemCountShow = 6,
                    fixedSpacing = 10.dpToPx(requireContext()),
                    itemWidth = 40.dpToPx(requireContext()),
                    itemHigh = 80.dpToPx(requireContext())
                )
            )
        }
        dailyAdapter = DailyWeatherListAdapter(requireContext())
        binding.dailyList.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Loading> {
            Logger.d(TAG, "Loading----")
        }

        collectState<WeatherUiState.Success.WeatherList, Weather>(
            mapFn = { weatherList ->
                weatherList.weathers[position]
            },
            filterFn = {
                it.nowWeather.id.isNotEmpty() &&
                        it.dailyWeather.isNotEmpty() &&
                        it.hourlyWeather.isNotEmpty()
            }
        ) { weather ->
            Logger.d(TAG, "collect now weather: ${weather.nowWeather}")
            Logger.d(TAG, "collect daily weather: ${weather.dailyWeather}")
            Logger.d(TAG, "collect hourly weather: ${weather.hourlyWeather}")
            binding.name.text = weather.geo.name
            bindCurrentLocationWeatherUI(weather)
        }
        restoreViewStates(binding.dailyList)
    }

    override fun onDestroyView() {
        saveViewStates(binding.dailyList)
        super.onDestroyView()
    }

    @SuppressLint("SetTextI18n")
    private fun bindCurrentLocationWeatherUI(weather: Weather) {
        binding.temp.text = weather.nowWeather.temp.toString()
        val today = weather.dailyWeather.first { it.date.isToday() }
        binding.maxTemp.text = "${today.tempMax}°"
        binding.minTemp.text = "${today.tempMin}°"
        binding.weatherText.text = weather.nowWeather.weatherText.toString()
        hourlyAdapter.submitList(weather.hourlyWeather)
        dailyAdapter.submitList(weather.dailyWeather)
        val bg = if (weather.nowWeather.obsTime < today.sunset) {
            when (weather.nowWeather.weatherText) {
                WeatherText.SUNNY -> R.drawable.bg_sunny_weather
                WeatherText.FOGGY,
                WeatherText.WINDY,
                WeatherText.UNKNOWN,
                WeatherText.CLOUDY -> R.drawable.bg_cloudy_weather

                WeatherText.STORMY,
                WeatherText.SNOWY,
                WeatherText.THUNDERSTORM,
                WeatherText.RAINY -> R.drawable.bg_weather_rain
            }
        } else {
            R.drawable.bg_weather_night
        }
        binding.root.setBackgroundResource(bg)
    }

    companion object {
        const val TAG = "WeatherFragment"
    }
}