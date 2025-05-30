package com.lunacattus.app.presentation.features.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.app.presentation.features.home.ui.FeatureListAdapter.Companion.FeatureItem
import com.lunacattus.clean.presentation.databinding.ItemHomeFeatureBinding
import com.lunacattus.common.setOnClickListenerWithDebounce
import java.lang.ref.WeakReference

class FeatureListAdapter(private val onItemClick: WeakReference<(String) -> Unit>) :
    ListAdapter<FeatureItem, FeatureListAdapter.FeatureViewHolder>(
        object : DiffUtil.ItemCallback<FeatureItem>() {
            override fun areItemsTheSame(
                oldItem: FeatureItem,
                newItem: FeatureItem
            ): Boolean {
                return oldItem.name == newItem.name
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
        holder.binding.main.setBackgroundResource(item.bgSource)
        holder.binding.img.setImageResource(item.imgSource)
        holder.binding.title.text = item.name
        holder.binding.root.setOnClickListenerWithDebounce {
            onItemClick.get()?.invoke(item.name)
        }
    }

    inner class FeatureViewHolder(val binding: ItemHomeFeatureBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        data class FeatureItem(
            val name: String,
            val desc: String,
            val imgSource: Int,
            val bgSource: Int
        )
    }
}