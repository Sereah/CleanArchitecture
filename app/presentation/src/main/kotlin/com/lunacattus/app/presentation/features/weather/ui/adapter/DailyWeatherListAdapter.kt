package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.presentation.features.weather.ui.page.iconSource
import com.lunacattus.clean.presentation.databinding.ItemWeatherDailyBinding
import com.lunacattus.common.isToday
import com.lunacattus.common.parseToTimestamp
import com.lunacattus.common.toChineseDayOfWeek
import java.time.ZoneId

class DailyWeatherListAdapter(val context: Context) :
    ListAdapter<DailyWeatherListAdapter.Companion.DailyItem, DailyWeatherListAdapter.ViewHolder>(
        diffCallback
    ) {

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        val binding = holder.binding
        binding.week.text =
            if (item.weather.date.parseToTimestamp(ZoneId.of(item.timeZone))
                    .isToday(ZoneId.of(item.timeZone))
            ) "今天"
            else item.weather.date.parseToTimestamp(ZoneId.of(item.timeZone))
                .toChineseDayOfWeek(ZoneId.of(item.timeZone))
        if (item.weather.dayWeatherText == WeatherText.UNKNOWN) {
            binding.weatherText.apply {
                text = item.weather.dayWeatherText.toString()
                visibility = View.VISIBLE
            }
            binding.icon.visibility = View.GONE
        } else {
            binding.icon.apply {
                setImageResource(item.weather.dayWeatherText.iconSource(true))
                visibility = View.VISIBLE
            }
            binding.weatherText.visibility = View.GONE
        }
        binding.temp.text = "${item.weather.tempMin}-${item.weather.tempMax}°"
    }

    inner class ViewHolder(val binding: ItemWeatherDailyBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val TAG = "DailyWeatherListAdapter"

        data class DailyItem(val weather: DailyWeather, val timeZone: String)

        private val diffCallback = object : DiffUtil.ItemCallback<DailyItem>() {
            override fun areItemsTheSame(
                oldItem: DailyItem,
                newItem: DailyItem
            ): Boolean {
                return oldItem.weather.date == newItem.weather.date
            }

            override fun areContentsTheSame(
                oldItem: DailyItem,
                newItem: DailyItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}