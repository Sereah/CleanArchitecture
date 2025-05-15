package com.lunacattus.app.presentation.features.weather.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.databinding.DialogSearchDetailBinding
import com.lunacattus.common.setOnClickListenerWithDebounce

class WeatherSearchDetailDialog : BaseWeatherDialog<DialogSearchDetailBinding>(
    DialogSearchDetailBinding::inflate
) {
    private val id: String by lazy { arguments?.getString(GEO_ID) ?: "" }
    private val name: String by lazy { arguments?.getString(GEO_NAME) ?: "" }

    override fun setupView(savedInstanceState: Bundle?) {
        dispatchUiIntent(WeatherUiIntent.QueryCity(id))
        binding.name.text = name
        binding.cancel.setOnClickListenerWithDebounce {
            dismiss()
        }
        binding.add.setOnClickListenerWithDebounce {
            dispatchUiIntent(WeatherUiIntent.AddCity(id))
            dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        collectCombined(
            flowA = FlowConfig<WeatherUiState.Success, NowWeather?>(
                mapFn = { it.searchNowWeather },
                filterFn = { it != null }
            ),
            flowB = FlowConfig<WeatherUiState.Success, List<DailyWeather>>(
                mapFn = { it.searchDailyWeather },
                filterFn = { it.isNotEmpty() }
            )
        ) { now, daily ->
            binding.temp.text = now!!.temp.toString()
            val today = daily[0]
            binding.maxTemp.text = "${today.tempMax}°"
            binding.minTemp.text = "${today.tempMin}°"
        }
        collectState<WeatherUiState.Success, WeatherGeo?>(
            mapFn = { it.queryGeo }
        ) {
            Logger.d(TAG, "QueryGeo: $it")
            if (it == null) {
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