package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.domain.model.isDay
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.WeatherPagerAdapter
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherPagerBinding
import com.lunacattus.clean.presentation.databinding.TabWeatherPagerBinding
import com.lunacattus.common.setOnClickListenerWithDebounce
import java.time.ZoneId
import java.time.ZonedDateTime

class WeatherPagerFragment : BaseWeatherFragment<FragmentWeatherPagerBinding>(
    FragmentWeatherPagerBinding::inflate
) {

    private lateinit var pagerAdapter: WeatherPagerAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        setStatusBarColor()
        initViewPager()
        binding.menu.setOnClickListenerWithDebounce {
            navCoordinator.execute(
                NavCommand.ToDirection(
                    direction = NavCommand.defaultNavDirection(R.id.action_weatherPager_to_cityOption)
                )
            )
        }
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success, List<Weather>>(
            mapFn = { it.weatherList },
            filterFn = { it.isNotEmpty() },
        ) {
            Logger.d(TAG, "collect weatherList, size=${it.size}")
            pagerAdapter.submitList(it)
        }
        collectState<WeatherUiState.Success, String>(
            mapFn = { it.selectedCityId },
            filterFn = { it.isNotEmpty() }
        ) { id ->
            Logger.d(TAG, "collect selectedCityId: $id")
            val index = pagerAdapter.currentList.indexOfFirst { it.geo.id == id }
            if (binding.viewPager.currentItem != index) {
                binding.viewPager.setCurrentItem(index, false)
            }
        }
    }

    override fun onDestroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(pagerCallback)
        binding.viewPager.adapter = null
        super.onDestroyView()
    }

    private fun setStatusBarColor() {
        requireActivity().window.insetsController?.apply {
            setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }

    private fun initViewPager() {
        (binding.viewPager.getChildAt(0) as RecyclerView).apply {
            overScrollMode = View.OVER_SCROLL_NEVER
        }
        pagerAdapter = WeatherPagerAdapter()
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.registerOnPageChangeCallback(pagerCallback)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = TabWeatherPagerBinding.inflate(layoutInflater)
            when (position) {
                0 -> tabView.tabIcon.setImageResource(R.drawable.ic_location)
                else -> tabView.tabIcon.setImageResource(R.drawable.ic_dot)
            }
            tab.customView = tabView.root
        }.attach()
    }

    private fun setBackGround(weather: Weather) {
        val bg = if (weather.isDay(
                ZonedDateTime.now(ZoneId.of(weather.geo.timeZone)).toInstant().toEpochMilli()
            )
        ) {
            when (weather.nowWeather.weatherText) {
                WeatherText.SUNNY -> R.drawable.bg_sunny_weather
                WeatherText.FOGGY,
                WeatherText.WINDY,
                WeatherText.UNKNOWN,
                WeatherText.OVERCAST,
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

    private val pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val weather = pagerAdapter.currentList[position]
            dispatchUiIntent(WeatherUiIntent.SelectCityPage(weather.geo.id))
            setBackGround(weather)
        }
    }

    companion object {
        const val TAG = "WeatherPagerFragment"
    }
}