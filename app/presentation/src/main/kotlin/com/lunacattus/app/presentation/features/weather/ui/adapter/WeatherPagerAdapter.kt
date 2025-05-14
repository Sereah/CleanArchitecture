package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.presentation.common.ui.UniformItemDecoration
import com.lunacattus.clean.presentation.databinding.WidgetWeatherDetailBinding
import com.lunacattus.common.dpToPx

class WeatherPagerAdapter :
    ListAdapter<Weather, WeatherPagerAdapter.WeatherPagerViewHolder>(object :
        DiffUtil.ItemCallback<Weather>() {

        override fun areItemsTheSame(
            oldItem: Weather,
            newItem: Weather
        ): Boolean {
            return oldItem.geo.id == newItem.geo.id
        }

        override fun areContentsTheSame(
            oldItem: Weather,
            newItem: Weather
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherPagerViewHolder {
        val binding = WidgetWeatherDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WeatherPagerViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: WeatherPagerViewHolder,
        position: Int
    ) {
        val binding = holder.binding
        val weather = getItem(position)
        binding.name.text = weather.geo.name
        binding.temp.text = weather.nowWeather.temp.toString()
        if (weather.dailyWeather.isNotEmpty()) {
            val today = weather.dailyWeather[0]
            binding.maxTemp.text = "${today.tempMax}°"
            binding.minTemp.text = "${today.tempMin}°"
        }
        binding.weatherText.text = weather.nowWeather.weatherText.toString()
        holder.hourlyAdapter.submitList(weather.hourlyWeather.map {
            HourlyWeatherListAdapter.Companion.HourlyItem(it, weather.geo.timeZone)
        })
        holder.dailyAdapter.submitList(weather.dailyWeather.map {
            DailyWeatherListAdapter.Companion.DailyItem(it, weather.geo.timeZone)
        })
    }

    inner class WeatherPagerViewHolder(val binding: WidgetWeatherDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val hourlyAdapter by lazy { HourlyWeatherListAdapter(binding.root.context) }
        val dailyAdapter by lazy { DailyWeatherListAdapter(binding.root.context) }

        init {
            binding.hourlyList.apply {
                adapter = hourlyAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(
                    UniformItemDecoration(
                        isVertical = false,
                        maxItemCountShow = 6,
                        fixedSpacing = 10.dpToPx(context),
                        itemWidth = 40.dpToPx(context),
                        itemHigh = 80.dpToPx(context)
                    )
                )
            }
            binding.dailyList.apply {
                adapter = dailyAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    companion object {
        const val TAG = "WeatherPagerAdapter"
    }

}