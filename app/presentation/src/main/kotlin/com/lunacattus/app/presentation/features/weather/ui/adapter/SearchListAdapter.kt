package com.lunacattus.app.presentation.features.weather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.ItemTextViewBinding
import com.lunacattus.common.setOnClickListenerWithDebounce

class SearchListAdapter(private val context: Context, private val onSelect: (geo: WeatherGeo) -> Unit) :
    ListAdapter<WeatherGeo, SearchListAdapter.SearchViewHolder>(
        object : DiffUtil.ItemCallback<WeatherGeo>() {
            override fun areItemsTheSame(
                oldItem: WeatherGeo,
                newItem: WeatherGeo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: WeatherGeo,
                newItem: WeatherGeo
            ): Boolean {
                return oldItem == newItem
            }

        }
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding = ItemTextViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.textView.setTextColor(context.getColor(R.color.white))
        binding.textView.textSize = 20f
        return SearchViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        val lp = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
        lp.bottomMargin = 40
        holder.binding.root.layoutParams = lp
        holder.binding.textView.text = "${item.name}-${item.city}-${item.province}-${item.country}"
        holder.binding.root.setOnClickListenerWithDebounce {
            onSelect(item)
        }
    }

    inner class SearchViewHolder(val binding: ItemTextViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}