package com.lunacattus.app.presentation.features.weather.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherPagerBinding

class WeatherPagerFragment :
    BaseFragment<FragmentWeatherPagerBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentWeatherPagerBinding::inflate
    ) {
    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until binding.tabLayout.tabCount) {
                    val tab = binding.tabLayout.getTabAt(i)
                    val imageView = tab?.customView as? ImageView
                    if (position == 0) {
                        imageView?.setImageResource(R.drawable.ic_location_selected)
                        imageView?.isSelected = false
                    } else {
                        imageView?.setImageResource(R.drawable.ic_location_unselected)
                        imageView?.isSelected = position == i
                    }
                }
            }
        })
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success.WeatherList, Int>(
            mapFn = { it.weathers.size },
            filterFn = { it != 0 }
        ) {
            binding.viewPager.adapter = WeatherPagerAdapter(this, it)
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                val tabView = ImageView(requireContext()).apply {
                    if (position == 0) {
                        setImageResource(R.drawable.ic_location_selected)
                    } else {
                        setImageResource(R.drawable.ic_dot)
                    }
                }
                tab.customView = tabView
            }.attach()
        }
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {

    }
}