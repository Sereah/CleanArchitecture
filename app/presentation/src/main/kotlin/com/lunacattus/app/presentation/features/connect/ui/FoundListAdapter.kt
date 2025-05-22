package com.lunacattus.app.presentation.features.connect.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.databinding.ItemTextViewBinding
import com.lunacattus.common.setOnClickListenerWithDebounce

@SuppressLint("MissingPermission")
class FoundListAdapter(val click : (BluetoothDevice) -> Unit) : ListAdapter<BluetoothDevice, FoundListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            Logger.d("ConnectFragment", "areItemsTheSame: $oldItem, $newItem")
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            Logger.d("ConnectFragment", "areContentsTheSame: $oldItem, $newItem")
            return oldItem.name == newItem.name
        }

    }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemTextViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.textView.textSize = 25f
        val lp = binding.textView.layoutParams as ViewGroup.MarginLayoutParams
        lp.bottomMargin = 20
        binding.textView.layoutParams = lp
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        val binding = holder.binding
        binding.textView.text = item.name
        binding.root.setOnClickListenerWithDebounce {
            click(item)
        }
    }

    inner class ViewHolder(val binding: ItemTextViewBinding) : RecyclerView.ViewHolder(binding.root)
}