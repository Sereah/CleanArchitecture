package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowWeatherDetailPage
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent.GetSearchCityWeather
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.SearchListAdapter
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.databinding.FragmentCitySearchBinding
import com.lunacattus.common.addTextChangeDebounceListener

class CitySearchFragment : BaseWeatherFragment<FragmentCitySearchBinding>(
    FragmentCitySearchBinding::inflate
) {

    private lateinit var searchAdapter: SearchListAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.cancel.setOnClickListener {
            navCoordinator().execute(NavCommand.Back)
        }
        binding.searchEdit.addTextChangeDebounceListener(500, lifecycleScope) {
            dispatchUiIntent(WeatherUiIntent.SearchCity(it))
        }
        searchAdapter = SearchListAdapter(requireContext()) {
//            dispatchUiIntent(GetSearchCityWeather(it))
            dispatchUiIntent(WeatherUiIntent.OnRequestAddCity(it.id))
        }
        binding.searchList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success.SearchGeoList> {
            Logger.d(TAG, "collect geo list: ${it.geo}")
            searchAdapter.submitList(it.geo)
        }
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {
        super.handleSideEffect(effect)
        when (effect) {
            is ShowWeatherDetailPage -> {
                val bundle = Bundle().apply {
                    putString("GEO_NAME", effect.geo.name)
                    putString("GEO_ID", effect.geo.id)
                }
            }

            else -> {}
        }
    }

    companion object {
        const val TAG = "CitySearchFragment"
    }

}