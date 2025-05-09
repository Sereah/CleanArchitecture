package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.WeatherPagerAdapter
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherPagerBinding
import com.lunacattus.common.setOnClickListenerWithDebounce

class WeatherPagerFragment : BaseWeatherFragment<FragmentWeatherPagerBinding>(
    FragmentWeatherPagerBinding::inflate
) {

    override fun setupViews(savedInstanceState: Bundle?) {
        requireActivity().window.insetsController?.apply {
            setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
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
        binding.menu.setOnClickListenerWithDebounce {
            navCoordinator().execute(
                NavCommand.ToDirection(
                    direction = NavCommand.defaultNavDirection(R.id.action_weatherPager_to_cityOption)
                )
            )
        }
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
}