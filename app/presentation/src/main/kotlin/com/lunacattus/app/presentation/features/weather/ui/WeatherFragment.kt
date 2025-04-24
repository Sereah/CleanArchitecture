package com.lunacattus.app.presentation.features.weather.ui

import android.os.Bundle
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.promeg.pinyinhelper.Pinyin
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict
import com.lunacattus.app.domain.model.weather.WeatherCondition
import com.lunacattus.app.domain.model.weather.WeatherInfo
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.common.ui.UniformItemDecoration
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.app.presentation.features.weather.viewmodel.WeatherViewModel
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentWeatherBinding
import com.lunacattus.common.dpToPx
import com.lunacattus.common.toFormattedDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment :
    BaseFragment<FragmentWeatherBinding, WeatherUiIntent, WeatherUiState, WeatherSideEffect, WeatherViewModel>(
        FragmentWeatherBinding::inflate
    ) {

    private lateinit var adapter: DailyWeatherListAdapter

    override val viewModel: WeatherViewModel by hiltNavGraphViewModels(R.id.weather_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(requireContext())))
        adapter = DailyWeatherListAdapter(requireContext()).apply {
            binding.dailyList.adapter = this
        }
        binding.dailyList.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
            addItemDecoration(
                UniformItemDecoration(
                    false,
                    6,
                    20.dpToPx(context),
                    itemWidth = 40.dpToPx(context)
                )
            )
        }
        binding.option.setOnClickListener {
            dispatchUiIntent(WeatherUiIntent.OnCityOptionsRequested)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    dispatchUiIntent(WeatherUiIntent.OnNetworkWeatherInfoRequested)
                    delay(300000) //5min请求一次网络
                }
            }
        }
    }

    override fun setupObservers() {
        observeUiStates(
            UiStateObserver(
                selector = { it.weatherInfo },
                filterCondition = { it != null && it.dailyForecast.isNotEmpty() }
            ) { weather ->
                Logger.d(TAG, "collect weatherInfo: $weather")
                weather?.let { bindWeatherInfo(it) }
            },
            UiStateObserver(selector = { it.loading }) {
                Logger.d(TAG, "collect loading: $it")
            },
        )
    }

    override fun handleSideEffect(effect: WeatherSideEffect) {
        when (effect) {
            is WeatherSideEffect.ShowNetworkRequestFailToast -> {
                val msg = getString(
                    R.string.weather_fail_exception,
                    effect.code ?: "",
                    effect.msg
                )
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }

            WeatherSideEffect.NavigateToCityOption -> {
                navCoordinator().execute(
                    NavCommand.ToDirection(
                        object : NavDirections {
                            override val actionId: Int = R.id.action_weather_to_city_option
                            override val arguments: Bundle = Bundle()
                        }
                    )
                )
            }
        }
    }

    private fun bindWeatherInfo(weather: WeatherInfo) {
        binding.title.text = getString(R.string.today)
        binding.temp.text = weather.temperature.toInt().toString()
        binding.tempUnit.text = getString(R.string.temperature_unit)
        binding.cityChn.text = weather.city
        binding.reportTime.text = weather.updateTime.toFormattedDateTime()
        val cityArr = Pinyin.toPinyin(weather.city, ",").split(",")
        binding.cityEn.text = splitCityPinyin(cityArr)
        bindWeatherImg(weather.condition)
        adapter.submitList(weather.dailyForecast)
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

    private fun bindWeatherImg(weather: WeatherCondition) {
        when (weather) {
            WeatherCondition.SUNNY -> {
                binding.tempImg.setImageResource(R.drawable.img_sunny)
                binding.root.setBackgroundResource(R.color.weather_sunny_bg)
                binding.imgLine.setImageResource(R.drawable.img_line_sunny)
                bindBlackUI()
            }

            WeatherCondition.RAINY,
            WeatherCondition.THUNDERSTORM -> {
                binding.tempImg.setImageResource(R.drawable.img_rain)
                binding.root.setBackgroundResource(R.color.weather_rain_bg)
                binding.imgLine.setImageResource(R.drawable.img_line_rain)
                bindWhiteUI()
            }

            else -> {
                binding.tempImg.setImageResource(R.drawable.img_cloudy)
                binding.root.setBackgroundResource(R.color.weather_wind_bg)
                binding.imgLine.setImageResource(R.drawable.img_line_cloudy)
                bindBlackUI()
            }
        }
    }

    private fun bindBlackUI() {
        binding.option.setImageResource(R.drawable.ic_option_black)
        binding.title.setTextColor(requireContext().getColor(R.color.black))
        binding.temp.setTextColor(requireContext().getColor(R.color.black))
        binding.tempUnit.setTextColor(requireContext().getColor(R.color.black))
        binding.reportTime.setTextColor(requireContext().getColor(R.color.black))
        binding.icLocation.setImageResource(R.drawable.ic_location_black)
        binding.cityEn.setTextColor(requireContext().getColor(R.color.black))
        binding.cityChn.setTextColor(requireContext().getColor(R.color.black))
        binding.preBtn.setImageResource(R.drawable.ic_pre_black)
        binding.nextBtn.setImageResource(R.drawable.ic_next_black)
        adapter.notifyTextColor(true)
    }

    private fun bindWhiteUI() {
        binding.option.setImageResource(R.drawable.ic_option_white)
        binding.title.setTextColor(requireContext().getColor(R.color.white))
        binding.temp.setTextColor(requireContext().getColor(R.color.white))
        binding.tempUnit.setTextColor(requireContext().getColor(R.color.white))
        binding.reportTime.setTextColor(requireContext().getColor(R.color.white))
        binding.icLocation.setImageResource(R.drawable.ic_location_white)
        binding.cityEn.setTextColor(requireContext().getColor(R.color.white))
        binding.cityChn.setTextColor(requireContext().getColor(R.color.white))
        binding.preBtn.setImageResource(R.drawable.ic_pre_white)
        binding.nextBtn.setImageResource(R.drawable.ic_next_white)
        adapter.notifyTextColor(false)
    }

    companion object {
        const val TAG = "WeatherFragment"
    }
}