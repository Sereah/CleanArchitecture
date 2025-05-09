package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.clean.presentation.databinding.ItemWeatherHourlyBinding
import com.lunacattus.common.toFormattedDateTime

class HourlyWeatherListAdapter(val context: Context) :
    ListAdapter<HourlyWeather, HourlyWeatherListAdapter.HourViewHolder>(object :
        DiffUtil.ItemCallback<HourlyWeather>() {
        override fun areItemsTheSame(
            oldItem: HourlyWeather,
            newItem: HourlyWeather
        ): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(
            oldItem: HourlyWeather,
            newItem: HourlyWeather
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
        binding.time.text = "${item.time.toFormattedDateTime("HH")}点"
        binding.weatherText.text = item.weatherText.toString()
        binding.temp.text = "${item.temp}°"
    }

    class HourViewHolder(val binding: ItemWeatherHourlyBinding) :
        RecyclerView.ViewHolder(binding.root)
}