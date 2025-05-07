package com.lunacattus.app.presentation.features.weather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.clean.presentation.databinding.ItemWeatherDailyBinding
import com.lunacattus.common.isToday
import com.lunacattus.common.toChineseDayOfWeek

class DailyWeatherListAdapter(val context: Context) :
    ListAdapter<DailyWeather, DailyWeatherListAdapter.ViewHolder>(diffCallback) {

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
        binding.week.text = if (item.date.isToday()) "今天" else item.date.toChineseDayOfWeek()
        binding.weatherText.text = item.dayWeatherText.toString()
        binding.temp.text = "${item.tempMin}-${item.tempMax}°"
    }

    inner class ViewHolder(val binding: ItemWeatherDailyBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DailyWeather>() {
            override fun areItemsTheSame(
                oldItem: DailyWeather,
                newItem: DailyWeather
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: DailyWeather,
                newItem: DailyWeather
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}