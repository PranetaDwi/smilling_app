package com.example.smilling_app.home

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityDeviceListBinding
import com.example.smilling_app.databinding.ActivityLoginBinding
import com.example.smilling_app.views.FragmentActivity
import java.io.IOException
import java.util.UUID

class DeviceListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDeviceListBinding
    private val deviceNamesList = mutableListOf<String>()
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    @SuppressLint("MissingPermission")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDeviceListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bluetoothManager: BluetoothManager? = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        pairedDevices?.forEach { device ->
            val deviceName = device.name
            deviceNamesList.add(deviceName)
        }

        with(binding){

            scanDevices.setOnClickListener{
                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, filter)
            }

            backButton.setOnClickListener{
                startActivity(Intent(this@DeviceListActivity, FragmentActivity::class.java))
            }

            val adapter = ArrayAdapter(this@DeviceListActivity, android.R.layout.simple_list_item_1, deviceNamesList)
            deviceList.adapter = adapter

            deviceList.setOnItemClickListener{parent, view, position, id ->
                val selectedDeviceName = deviceNamesList[position]
                val selectedDevice = pairedDevices?.find {it.address == selectedDeviceName}
                selectedDevice?.let { connectToDevice(it) }
            }
        }

    }

    private val receiver = object : BroadcastReceiver(){
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String = intent?.action.toString()
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    deviceNamesList.add(deviceName.toString())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread(){
        val bluetoothManager: BluetoothManager? = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE){
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        override fun run(){
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?. let { socket ->
                try{
                    socket.connect()
                    Toast.makeText(this@DeviceListActivity, "Connect Berhasil", Toast.LENGTH_SHORT).show()
                } catch (e: IOException){
                    Toast.makeText(this@DeviceListActivity, "Connect Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice){
        val connectThread = ConnectThread(device)
        connectThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}