package com.lunacattus.app.presentation.features.weather.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.clean.presentation.databinding.ItemWeatherDailyBinding

class DailyWeatherListAdapter(val context: Context) :
    ListAdapter<DailyWeather, DailyWeatherListAdapter.ViewHolder>(diffCallback) {

    private var useBlackColor = false

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

    }

    inner class ViewHolder(val binding: ItemWeatherDailyBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun notifyTextColor(isBlack: Boolean) {
        useBlackColor = isBlack
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DailyWeather>() {
            override fun areItemsTheSame(
                oldItem: DailyWeather,
                newItem: DailyWeather
            ): Boolean {
                return oldItem == newItem
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