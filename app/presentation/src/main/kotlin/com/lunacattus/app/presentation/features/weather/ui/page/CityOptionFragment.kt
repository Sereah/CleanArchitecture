package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.CityListAdapter
import com.lunacattus.app.presentation.features.weather.ui.adapter.CityListAdapter.Companion.CityListItem
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentCityOptionBinding

class CityOptionFragment : BaseWeatherFragment<FragmentCityOptionBinding>(
    FragmentCityOptionBinding::inflate
) {

    private lateinit var cityAdapter: CityListAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        cityAdapter = CityListAdapter(
            requireContext().applicationContext,
            {
                dispatchUiIntent(WeatherUiIntent.SelectCityPage(it))
                navCoordinator.execute(NavCommand.Up)
            }, {
                dispatchUiIntent(WeatherUiIntent.DeleteCity(it))
            })
        binding.cityList.apply {
            adapter = cityAdapter
            layoutManager = LinearLayoutManager(requireContext())
            overScrollMode = View.OVER_SCROLL_NEVER
        }
        binding.searchView.setOnClickListener {
            navCoordinator.execute(
                NavCommand.ToDirection(
                    direction = NavCommand.defaultNavDirection(R.id.action_cityOption_to_citySearch)
                )
            )
        }
    }

    override fun onDestroyView() {
        binding.cityList.adapter = null
        super.onDestroyView()
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success, List<Weather>>(
            mapFn = { it.weatherList },
            filterFn = { it.isNotEmpty() }
        ) {
            Logger.d(TAG, "collect weather list.")
            val itemList = mutableListOf<CityListItem>()
            for ((index, weather) in it.withIndex()) {
                if (index == 0) {
                    itemList.add(CityListItem.Title("当前定位"))
                }
                if (index == 1) {
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