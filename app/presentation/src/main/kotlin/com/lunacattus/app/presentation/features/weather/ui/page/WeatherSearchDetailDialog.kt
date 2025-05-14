package com.lunacattus.app.presentation.features.weather.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.databinding.DialogSearchDetailBinding
import com.lunacattus.common.setOnClickListenerWithDebounce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeatherSearchDetailDialog : BaseWeatherDialog<DialogSearchDetailBinding>(
    DialogSearchDetailBinding::inflate
) {
    private val id: String by lazy { arguments?.getString(GEO_ID) ?: "" }
    private val name: String by lazy { arguments?.getString(GEO_NAME) ?: "" }

    override fun setupView(savedInstanceState: Bundle?) {
        dispatchUiIntent(WeatherUiIntent.QueryGeoById(id))
        binding.name.text = name
        binding.cancel.setOnClickListenerWithDebounce {
            dismiss()
        }
        binding.add.setOnClickListenerWithDebounce {
            dispatchUiIntent(WeatherUiIntent.OnRequestAddCity(id))
            dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState
                .filterIsInstance<WeatherUiState.Success.SearchNow>()
                .map { it.now }
                .filter { it.id != "" }
                .distinctUntilChanged()
                .combine(
                    viewModel.uiState
                        .filterIsInstance<WeatherUiState.Success.SearchDaily>()
                        .map { it.daily }
                        .filter { it.isNotEmpty() }
                        .distinctUntilChanged()
                ) { now, daily ->
                    binding.temp.text = now.temp.toString()
                    val today = daily[0]
                    binding.maxTemp.text = "${today.tempMax}°"
                    binding.minTemp.text = "${today.tempMin}°"
                }.collect()
        }
        collectState<WeatherUiState.Success.QueryGeo> {
            Logger.d(TAG, "QueryGeo: $it")
            if (it.geo == null) {
                binding.add.visibility = View.VISIBLE
            } else {
                binding.add.visibility = View.GONE
            }
        }
    }

    companion object {
        const val GEO_ID = "SearchDetailDialog_geo_id"
        const val GEO_NAME = "SearchDetailDialog_geo_name"
        const val TAG = "WeatherSearchDetailDialog"
    }

}