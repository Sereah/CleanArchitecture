package com.lunacattus.app.presentation.features.weather.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentCitySearchBinding
import com.lunacattus.common.debounce

class CitySearchFragment :
    BaseFragment<FragmentCitySearchBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentCitySearchBinding::inflate
    ) {

    private lateinit var adapter: CitySearchResultListAdapter

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.cancel.setOnClickListener {
            navCoordinator().execute(NavCommand.Back)
        }
        binding.searchEdit.apply {
            showSoftInputOnFocus = true
            debounce(300, lifecycleScope) {
                if (it.isEmpty()) {
                    adapter.submitList(emptyList())
                    return@debounce
                }
                dispatchUiIntent(WeatherUiIntent.OnSearchCityRequested(it))
            }
            post {
                requestFocus()
            }
        }
        adapter = CitySearchResultListAdapter(requireContext())
        binding.searchList.adapter = adapter
        binding.searchList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setupObservers() {
        observeUiStates(
            UiStateObserver(
                selector = { it.searchResult },
                filterCondition = { it.isNotEmpty() }
            ) {
                Logger.d("luna", "collect : $it")
                adapter.submitList(it)
            }
        )
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {

    }

}