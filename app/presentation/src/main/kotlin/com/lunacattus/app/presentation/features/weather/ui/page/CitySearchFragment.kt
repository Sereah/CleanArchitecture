package com.lunacattus.app.presentation.features.weather.ui.page

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.common.navigation.NavCommand.Companion.defaultNavDirection
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowWeatherDetailPage
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent.SearchCityWeather
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.ui.adapter.SearchListAdapter
import com.lunacattus.app.presentation.features.weather.ui.page.WeatherSearchDetailDialog.Companion.GEO_ID
import com.lunacattus.app.presentation.features.weather.ui.page.WeatherSearchDetailDialog.Companion.GEO_NAME
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentCitySearchBinding
import com.lunacattus.common.addTextChangeDebounceListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CitySearchFragment : BaseWeatherFragment<FragmentCitySearchBinding>(
    FragmentCitySearchBinding::inflate
) {

    private lateinit var searchAdapter: SearchListAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.cancel.setOnClickListener {
            navCoordinator.execute(NavCommand.Up)
        }
        binding.searchEdit.addTextChangeDebounceListener(500, lifecycleScope) {
            dispatchUiIntent(WeatherUiIntent.SearchCity(it))
        }
        searchAdapter = SearchListAdapter(requireContext()) {
            dispatchUiIntent(SearchCityWeather(it))
        }
        binding.searchList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupObservers() {
        collectState<WeatherUiState.Success, List<WeatherGeo>>(
            mapFn = {it.searchGeoList},
            filterFn = {it.isNotEmpty()}
        ) {
            Logger.d(TAG, "collect geo list: $it")
            searchAdapter.submitList(it)
        }
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {
        super.handleSideEffect(effect)
        Logger.d(TAG, "handleSideEffect: $effect")
        when (effect) {
            is ShowWeatherDetailPage -> {
                val bundle = Bundle().apply {
                    putString(GEO_ID, effect.geo.id)
                    putString(GEO_NAME, effect.geo.name)
                }
                navCoordinator.execute(
                    NavCommand.ToDirection(
                        direction = defaultNavDirection(
                            R.id.action_citySearch_to_dialog_detail_dest,
                            bundle
                        ),
                        options = null
                    )
                )
            }

            is WeatherSideEffect.BackToCityOptionPage -> {
                lifecycleScope.launch {
                    delay(500)
                    navCoordinator.execute(NavCommand.Up)
                }
            }

            else -> {}
        }
    }

    companion object {
        const val TAG = "CitySearchFragment"
    }

}