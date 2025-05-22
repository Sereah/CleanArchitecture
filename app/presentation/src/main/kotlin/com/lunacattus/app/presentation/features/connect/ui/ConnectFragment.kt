package com.lunacattus.app.presentation.features.connect.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunacattus.app.presentation.features.connect.mvi.ConnectSideEffect
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.databinding.FragmentConnectBinding
import com.lunacattus.common.setOnClickListenerWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class ConnectFragment : BaseConnectFragment<FragmentConnectBinding>(
    FragmentConnectBinding::inflate
) {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var foundListAdapter: FoundListAdapter
    private val discoveryDeviceList = mutableListOf<BluetoothDevice>()

    private val bluetoothReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state =
                            it.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        Logger.d(TAG, "bluetooth state: $state")
                        binding.bluetoothSwitch.isChecked =
                            (state == BluetoothAdapter.STATE_ON).apply {
                                bluetoothAdapter.cancelDiscovery()
                                bluetoothAdapter.startDiscovery()
                            }
                    }

                    BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                        val device =
                            it.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        val type = it.getIntExtra(
                            BluetoothDevice.EXTRA_PAIRING_VARIANT,
                            BluetoothDevice.ERROR
                        )
                        Logger.d(TAG, "device: $device, type: $type")
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                        Logger.d(TAG, "ACTION_DISCOVERY_STARTED")

                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                        Logger.d(TAG, "ACTION_DISCOVERY_FINISHED")
                    }

                    BluetoothDevice.ACTION_FOUND -> {
                        it.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                            ?.let { device ->
                                device.name.takeIf { it != "null" }
                                    ?.let {
                                        Logger.d(TAG, "found: $it, ${device.address}")
                                        discoveryDeviceList.add(device)
                                        foundListAdapter.submitList(discoveryDeviceList.toList())
                                    }
                            }
                    }

                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        it.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)?.let {
                            Logger.d(TAG, "ACTION_ACL_CONNECTED: ${it.name}")
                        }
                    }

                    BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        it.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)?.let {
                            Logger.d(TAG, "ACTION_ACL_DISCONNECTED: ${it.name}")
                        }
                    }

                    BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                        it.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)?.let {
                            Logger.d(TAG, "ACTION_BOND_STATE_CHANGED: ${it.name}, ${it.bondState}")
                        }
                    }
                }
            }
        }

    }

    private val bluetoothIntentFilter = IntentFilter().apply {
        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        addAction(BluetoothDevice.ACTION_FOUND)
        addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        requireActivity().registerReceiver(bluetoothReceiver, bluetoothIntentFilter)
        bluetoothManager =
            requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        binding.bluetoothSwitch.isChecked = bluetoothAdapter.isEnabled
        binding.bluetoothSwitch.setOnClickListenerWithDebounce {
            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable() // ACTION_REQUEST_DISABLE是系统api，disable和enable在sdk33后被废弃
            } else {
//                startActivity(Intent().apply { action = BluetoothAdapter.ACTION_REQUEST_ENABLE })
                bluetoothAdapter.enable()
            }
        }
        binding.refresh.setOnClickListenerWithDebounce {
            discoveryDeviceList.clear()
            bluetoothAdapter.cancelDiscovery()
            bluetoothAdapter.startDiscovery()
        }
        foundListAdapter = FoundListAdapter() {
            it.createBond()
        }
        binding.foundList.apply {
            adapter = foundListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        requireActivity().unregisterReceiver(bluetoothReceiver)
        super.onDestroyView()
    }

    override fun setupObservers() {

    }

    override fun handleSideEffect(effect: ConnectSideEffect) {

    }

    companion object {
        const val TAG = "ConnectFragment"
    }
}