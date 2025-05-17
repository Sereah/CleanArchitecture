package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.presentation.features.weather.ui.page.iconSource
import com.lunacattus.clean.presentation.databinding.ItemWeatherHourlyBinding
import com.lunacattus.common.parseToTimestamp
import com.lunacattus.common.toFormattedDateTime
import java.time.ZoneId

class HourlyWeatherListAdapter(val context: Context) :
    ListAdapter<HourlyWeatherListAdapter.Companion.HourlyItem, HourlyWeatherListAdapter.HourViewHolder>(
        object :
            DiffUtil.ItemCallback<HourlyItem>() {
            override fun areItemsTheSame(
                oldItem: HourlyItem,
                newItem: HourlyItem
            ): Boolean {
                return oldItem.weather.time == newItem.weather.time
            }

            override fun areContentsTheSame(
                oldItem: HourlyItem,
                newItem: HourlyItem
            ): Boolean {
                return oldItem == newItem
            }

        }) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourViewHolder {
        val binding = ItemWeatherHourlyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HourViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: HourViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        val binding = holder.binding
        binding.time.text =
            "${
                item.weather.time.parseToTimestamp(ZoneId.of(item.timeZone))
                    .toFormattedDateTime("HH", ZoneId.of(item.timeZone))
            }点"
        if (item.weather.weatherText == WeatherText.UNKNOWN) {
            binding.weatherText.apply {
                text = item.weather.weatherText.toString()
                visibility = View.VISIBLE
            }
            binding.icon.visibility = View.GONE
        } else {
            binding.icon.apply {
                setImageResource(item.weather.weatherText.iconSource(item.isDay))
                visibility = View.VISIBLE
            }
            binding.weatherText.visibility = View.GONE
        }
        binding.temp.text = "${item.weather.temp}°"
    }

    class HourViewHolder(val binding: ItemWeatherHourlyBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        data class HourlyItem(
            val weather: HourlyWeather,
            val timeZone: String,
            val isDay: Boolean
        )
    }
}