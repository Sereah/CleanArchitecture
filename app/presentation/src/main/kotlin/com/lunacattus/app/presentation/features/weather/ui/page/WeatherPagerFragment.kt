package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import android.view.WindowInsetsController
import androidx.viewpager2.widget.ViewPager2
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.WeatherPagerAdapter
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherPagerBinding
import com.lunacattus.common.setOnClickListenerWithDebounce

class WeatherPagerFragment : BaseWeatherFragment<FragmentWeatherPagerBinding>(
    FragmentWeatherPagerBinding::inflate
) {

    private lateinit var pagerAdapter: WeatherPagerAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        requireActivity().window.insetsController?.apply {
            setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        pagerAdapter = WeatherPagerAdapter()
        binding.viewPager.adapter = pagerAdapter
        binding.menu.setOnClickListenerWithDebounce {
            navCoordinator().execute(
                NavCommand.ToDirection(
                    direction = NavCommand.defaultNavDirection(R.id.action_weatherPager_to_cityOption)
                )
            )
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val weather = pagerAdapter.currentList[position]
//                Logger.d(TAG, "onPageSelected: $weather")
                val bg =
                    if (weather.dailyWeather.isNotEmpty() && weather.dailyWeather[0].sunset < System.currentTimeMillis()) {
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
        })
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success.WeatherList, List<Weather>>(
            mapFn = { it.weathers },
            filterFn = { it.isNotEmpty() }
        ) {
            pagerAdapter.submitList(it)
        }
    }

    companion object {
        const val TAG = "WeatherPagerFragment"
    }
}