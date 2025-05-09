package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lunacattus.app.presentation.features.weather.ui.page.WeatherFragment

class WeatherPagerAdapter(
    fragment: Fragment,
    private val size: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment().apply {
            arguments = Bundle().apply {
                putInt("POSITION", position)
            }
        }
    }
}