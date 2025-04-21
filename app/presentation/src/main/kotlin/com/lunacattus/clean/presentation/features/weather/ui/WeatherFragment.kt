package com.lunacattus.clean.presentation.features.weather.ui

import android.os.Bundle
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.github.promeg.pinyinhelper.Pinyin
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.domain.model.weather.LivesWeather
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.clean.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.presentation.features.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment :
    BaseFragment<FragmentWeatherBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentWeatherBinding::inflate
    ) {

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(requireContext())))
    }

    override fun setupObservers() {
        observeUiStates(
            UiStateObserver(
                selector = { it.weatherInfo },
                filterCondition = { it.city != "" }
            ) { weather ->
                Logger.d(TAG, "collect weatherInfo: $weather")
                bindWeatherInfo(weather)
            },
            UiStateObserver(selector = { it.loading }) {
                Logger.d(TAG, "collect loading: $it")
            },
        )
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {
        val msg = when (effect) {
            WeatherSideEffect.ShowAdCodeEmptyToast -> getString(R.string.empty_ad_code_exception)
            WeatherSideEffect.ShowGetWeatherInfoEmptyToast -> getString(R.string.empty_weather_exception)
            WeatherSideEffect.ShowNetworkRequestFailToast -> getString(R.string.network_fail_exception)
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun bindWeatherInfo(weather: LivesWeather) {
        binding.title.text = getString(R.string.today)
        binding.temp.text = weather.temperature.toInt().toString()
        binding.tempUnit.text = getString(R.string.temperature_unit)
        binding.cityChn.text = weather.city
        val cityArr = Pinyin.toPinyin(weather.city, ",").split(",")
        binding.cityEn.text = splitCityPinyin(cityArr)
        bindWeatherImg(weather.weather)
    }

    private fun splitCityPinyin(parts: List<String>): String {
        return when {
            parts.isEmpty() -> ""
            parts.size == 1 -> {
                val firstPart = parts[0]
                if (firstPart.length >= 2) {
                    firstPart.substring(0, 2)
                } else {
                    firstPart
                }
            }

            else -> {
                parts[0].takeIf { it.isNotEmpty() }?.first().toString() +
                        parts[1].takeIf { it.isNotEmpty() }?.first().toString()
            }
        }
    }

    private fun bindWeatherImg(weather: String) {
        when {
            weather.contains("晴") -> {
                binding.tempImg.setImageResource(R.drawable.img_sunny)
                binding.root.setBackgroundResource(R.color.weather_sunny_bg)
                bindBlackUI()
            }

            weather.contains("雨") -> {
                binding.tempImg.setImageResource(R.drawable.img_rain)
                binding.root.setBackgroundResource(R.color.weather_rain_bg)
                bindWhiteUI()
            }

            else -> {
                binding.tempImg.setImageResource(R.drawable.img_wind)
                binding.root.setBackgroundResource(R.color.weather_wind_bg)
                bindBlackUI()
            }
        }
    }

    private fun bindBlackUI() {
        binding.title.setTextColor(requireContext().getColor(R.color.black))
        binding.temp.setTextColor(requireContext().getColor(R.color.black))
        binding.tempUnit.setTextColor(requireContext().getColor(R.color.black))
        binding.icLocation.setImageResource(R.drawable.ic_location_black)
        binding.cityEn.setTextColor(requireContext().getColor(R.color.black))
        binding.cityChn.setTextColor(requireContext().getColor(R.color.black))
        binding.preBtn.setImageResource(R.drawable.ic_pre_black)
        binding.nextBtn.setImageResource(R.drawable.ic_next_black)
    }

    private fun bindWhiteUI() {
        binding.title.setTextColor(requireContext().getColor(R.color.white))
        binding.temp.setTextColor(requireContext().getColor(R.color.white))
        binding.tempUnit.setTextColor(requireContext().getColor(R.color.white))
        binding.icLocation.setImageResource(R.drawable.ic_location_white)
        binding.cityEn.setTextColor(requireContext().getColor(R.color.white))
        binding.cityChn.setTextColor(requireContext().getColor(R.color.white))
        binding.preBtn.setImageResource(R.drawable.ic_pre_white)
        binding.nextBtn.setImageResource(R.drawable.ic_next_white)
    }

    companion object {
        const val TAG = "WeatherFragment"
    }
}