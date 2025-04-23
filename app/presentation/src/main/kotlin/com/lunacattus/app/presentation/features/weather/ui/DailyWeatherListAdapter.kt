package com.lunacattus.app.presentation.features.weather.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.weather.DailyForecast
import com.lunacattus.app.domain.model.weather.WeatherCondition
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.ItemWeatherDailyBinding
import com.lunacattus.common.isToday
import com.lunacattus.common.toChineseOrEmpty

class DailyWeatherListAdapter(val context: Context) :
    ListAdapter<DailyForecast, DailyWeatherListAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemWeatherDailyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        val binding = holder.binding
        binding.week.text = if (item.date.isToday()) {
            context.getString(R.string.today)
        } else {
            context.getString(R.string.week, item.week.toChineseOrEmpty())
        }
        val weatherImg = when (item.condition) {
            WeatherCondition.SUNNY -> R.drawable.img_sunny_small
            WeatherCondition.RAINY,
            WeatherCondition.THUNDERSTORM -> R.drawable.img_rain_small

            else -> R.drawable.img_cloudy_small
        }
        binding.imgWeather.setImageResource(weatherImg)
        binding.temp.text =
            context.getString(R.string.temp_min_to_max, item.minTemp.toInt(), item.maxTemp.toInt())
    }

    inner class ViewHolder(val binding: ItemWeatherDailyBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem.week == newItem.week &&
                        oldItem.condition == newItem.condition &&
                        oldItem.minTemp == newItem.minTemp &&
                        oldItem.maxTemp == newItem.maxTemp
            }

        }
    }
}