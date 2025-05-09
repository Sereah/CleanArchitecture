package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.ItemTextViewBinding
import com.lunacattus.clean.presentation.databinding.ItemWeatherCityBinding
import com.lunacattus.common.isToday

class CityListAdapter(val context: Context) : ListAdapter<CityListAdapter.Companion.CityListItem, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<CityListItem>() {
        override fun areItemsTheSame(
            oldItem: CityListItem,
            newItem: CityListItem
        ): Boolean {
            return if (oldItem is CityListItem.City && newItem is CityListItem.City) {
                oldItem.weather.geo.id == newItem.weather.geo.id
            } else {
                oldItem.javaClass == newItem.javaClass
            }
        }

        override fun areContentsTheSame(
            oldItem: CityListItem,
            newItem: CityListItem
        ): Boolean {
            return if (oldItem is CityListItem.City && newItem is CityListItem.City) {
                oldItem.weather.geo.name == newItem.weather.geo.name &&
                        oldItem.weather.nowWeather.temp == newItem.weather.nowWeather.temp &&
                        oldItem.weather.nowWeather.weatherText == newItem.weather.nowWeather.weatherText &&
                        oldItem.weather.dailyWeather.isNotEmpty() &&
                        newItem.weather.dailyWeather.isNotEmpty() &&
                        oldItem.weather.dailyWeather[0].tempMax == newItem.weather.dailyWeather[0].tempMax &&
                        oldItem.weather.dailyWeather[0].tempMin == newItem.weather.dailyWeather[0].tempMin
            } else {
                oldItem == newItem
            }
        }
    }
) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CityListItem.City -> TYPE_CITY
            is CityListItem.Title -> TYPE_TITLE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CITY -> {
                val binding = ItemWeatherCityBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemCityViewHolder(binding)
            }

            TYPE_TITLE -> {
                val binding = ItemTextViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                binding.textView.setTextColor(context.getColor(R.color.white))
                binding.textView.textSize = 20f
                ItemTitleViewHolder(binding)
            }

            else -> throw Exception("Unknown view type.")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        Logger.d(TAG, "bind: $holder, $position")
        when (holder) {
            is ItemTitleViewHolder -> {
                val item = getItem(position) as CityListItem.Title
                val lp = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
                lp.bottomMargin = 20
                holder.binding.root.layoutParams = lp
                holder.binding.textView.text = item.text
            }

            is ItemCityViewHolder -> {
                val lp = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
                lp.bottomMargin = 20
                holder.binding.root.layoutParams = lp
                val item = getItem(position) as CityListItem.City
                holder.binding.name.text = item.weather.geo.name
                holder.binding.weatherText.text = item.weather.nowWeather.weatherText.toString()
                holder.binding.temp.text = "${item.weather.nowWeather.temp}°"
                val today = item.weather.dailyWeather.toMutableList().firstOrNull { it.date.isToday() }
                today?.let {
                    holder.binding.maxTemp.text = "${it.tempMax}°"
                    holder.binding.minTemp.text = "${it.tempMin}°"
                }
            }
        }
    }

    inner class ItemCityViewHolder(val binding: ItemWeatherCityBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ItemTitleViewHolder(val binding: ItemTextViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        sealed interface CityListItem {
            data class City(val weather: Weather) : CityListItem
            data class Title(val text: String) : CityListItem
        }

        const val TYPE_CITY = 0
        const val TYPE_TITLE = 1

        const val TAG = "CityListAdapter"
    }
}