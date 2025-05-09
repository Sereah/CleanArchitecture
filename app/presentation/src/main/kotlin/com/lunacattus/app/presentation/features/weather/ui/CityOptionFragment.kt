package com.lunacattus.app.presentation.features.weather.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.CityListAdapter.Companion.CityListItem
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentCityOptionBinding

class CityOptionFragment : BaseWeatherFragment<FragmentCityOptionBinding>(
    FragmentCityOptionBinding::inflate
) {

    private lateinit var cityAdapter: CityListAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        cityAdapter = CityListAdapter(requireContext())
        binding.cityList.apply {
            adapter = cityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.searchView.setOnClickListener {
            navCoordinator().execute(
                NavCommand.ToDirection(
                    direction = NavCommand.defaultNavDirection(R.id.action_cityOption_to_citySearch)
                )
            )
        }
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success.WeatherList>(
            filterFn = { it.weathers.isNotEmpty() }
        ) {
            val itemList = mutableListOf<CityListItem>()
            for ((index, weather) in it.weathers.withIndex()) {
                if (index == 0) {
                    itemList.add(CityListItem.Title("当前定位"))
                }
                if (index == 2) {
                    itemList.add(CityListItem.Title("已添加城市"))
                }
                itemList.add(CityListItem.City(weather))
            }
            Logger.d(TAG, "collect item list: ${itemList.size}")
            cityAdapter.submitList(itemList)
        }
    }

    companion object {
        const val TAG = "CityOptionFragment"
    }
}