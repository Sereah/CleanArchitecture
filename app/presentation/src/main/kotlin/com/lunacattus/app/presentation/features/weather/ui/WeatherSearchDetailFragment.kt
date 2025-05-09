package com.lunacattus.app.presentation.features.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.common.ui.UniformItemDecoration
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import com.lunacattus.common.dpToPx
import com.lunacattus.common.isToday
import com.lunacattus.common.setOnClickListenerWithDebounce

class WeatherSearchDetailFragment : BaseWeatherFragment<FragmentWeatherBinding>(
    FragmentWeatherBinding::inflate
) {

    private lateinit var hourlyAdapter: HourlyWeatherListAdapter
    private lateinit var dailyAdapter: DailyWeatherListAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.name.text = arguments?.getString("GEO_NAME") ?: ""
        binding.add.apply {
            visibility = View.VISIBLE
            setOnClickListenerWithDebounce {
                arguments?.getString("GEO_ID")?.let {
                    dispatchUiIntent(WeatherUiIntent.AddCity(it))
                    navCoordinator().execute(
                        NavCommand.Back
                    )
                }
            }
        }
        binding.cancel.apply {
            visibility = View.VISIBLE
            setOnClickListenerWithDebounce {
                navCoordinator().execute(
                    NavCommand.Back
                )
            }
        }
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

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        collectState<WeatherUiState.Success.SearchNow>(
            filterFn = { it.now.id.isNotEmpty() }
        ) {
            binding.temp.text = it.now.temp.toString()
            binding.weatherText.text = it.now.weatherText.toString()
            val bg = when (it.now.weatherText) {
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
            binding.root.setBackgroundResource(bg)
        }

        collectState<WeatherUiState.Success.SearchDaily>(
            filterFn = { it.daily.isNotEmpty() }
        ) {
            val today = it.daily.first { it.date.isToday() }
            binding.maxTemp.text = "${today.tempMax}°"
            binding.minTemp.text = "${today.tempMin}°"
            dailyAdapter.submitList(it.daily)
        }

        collectState<WeatherUiState.Success.SearchHourly>(
            filterFn = { it.hourly.isNotEmpty() }
        ) {
            hourlyAdapter.submitList(it.hourly)
        }
    }
}