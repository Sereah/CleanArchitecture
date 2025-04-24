package com.lunacattus.app.presentation.features.weather.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.domain.model.weather.CityInfo
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.ItemSearchCityResultBinding

class CitySearchResultListAdapter(private val context: Context) :
    ListAdapter<CityInfo, CitySearchResultListAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSearchCityResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        val binding = holder.binding
        binding.root.text = context.getString(R.string.name_to_pName, item.name, item.pName)
    }

    inner class ViewHolder(val binding: ItemSearchCityResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback = object : ItemCallback<CityInfo>() {
            override fun areItemsTheSame(
                oldItem: CityInfo,
                newItem: CityInfo
            ): Boolean {
                return oldItem.adCode == newItem.adCode
            }

            override fun areContentsTheSame(
                oldItem: CityInfo,
                newItem: CityInfo
            ): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.pName == newItem.pName
            }

        }
    }
}
