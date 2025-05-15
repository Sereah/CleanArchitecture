package com.lunacattus.app.presentation.features.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.presentation.features.home.ui.FeatureListAdapter.Companion.FeatureItem
import com.lunacattus.clean.presentation.databinding.ItemHomeFeatureBinding
import com.lunacattus.common.setOnClickListenerWithDebounce

class FeatureListAdapter(val onItemClick: (String) -> Unit) :
    ListAdapter<FeatureItem, FeatureListAdapter.FeatureViewHolder>(
        object : DiffUtil.ItemCallback<FeatureItem>() {
            override fun areItemsTheSame(
                oldItem: FeatureItem,
                newItem: FeatureItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FeatureItem,
                newItem: FeatureItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeatureViewHolder {
        val binding =
            ItemHomeFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeatureViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FeatureViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.binding.img.setImageResource(item.imgSource)
        holder.binding.desc.text = item.desc
        holder.binding.root.setOnClickListenerWithDebounce {
            onItemClick(item.id)
        }
    }

    inner class FeatureViewHolder(val binding: ItemHomeFeatureBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        data class FeatureItem(
            val id: String,
            val imgSource: Int,
            val desc: String
        )
    }
}