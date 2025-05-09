package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowWeatherDetailPage
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent.OnRequestGetSearchCityWeather
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.SearchListAdapter
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
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
            dispatchUiIntent(WeatherUiIntent.OnRequestSearchCity(it))
        }
        searchAdapter = SearchListAdapter(requireContext()) {
            dispatchUiIntent(OnRequestGetSearchCityWeather(it))
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
                navCoordinator().execute(
                    NavCommand.ToDirection(
                        NavCommand.defaultNavDirection(R.id.action_citySearch_to_searchDetail, bundle),
                        options = navOptions {
                            anim {
                                enter = R.anim.slide_in_bottom
                                exit = R.anim.slide_out_top
                                popEnter = R.anim.slide_in_top
                                popExit = R.anim.slide_out_bottom
                            }
                            launchSingleTop = true
                        }
                    )
                )
            }

            else -> {}
        }
    }

    companion object {
        const val TAG = "CitySearchFragment"
    }

}