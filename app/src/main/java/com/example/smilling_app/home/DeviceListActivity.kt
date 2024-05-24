package com.example.smilling_app.home

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smilling_app.databinding.ActivityDeviceListBinding
import com.example.smilling_app.views.FragmentActivity
import java.io.IOException
import java.util.UUID

private const val APP_NAME = "SmilingApp"
private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

class DeviceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceListBinding
    private val deviceNamesList = mutableListOf<String>()
    private val messagesList = mutableListOf<String>()
    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val pairedDevices: MutableSet<BluetoothDevice> = mutableSetOf()

    private val handler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            MESSAGE_READ -> {
                val readBuffer = msg.obj as ByteArray
                val message = String(readBuffer, 0, msg.arg1)
                messagesList.add(message)

                Toast.makeText(this, "Received: $message", Toast.LENGTH_SHORT).show()
            }
            MESSAGE_WRITE -> {
                val writeBuffer = msg.obj as ByteArray
                // Handle writing message if needed
            }
            MESSAGE_TOAST -> {
                val toastMessage = msg.data.getString("toast")
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
        true
    })

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter

        bluetoothAdapter?.bondedDevices?.let { pairedDevices.addAll(it) }

        pairedDevices.forEach { device ->
            val deviceName = device.name
            deviceNamesList.add(deviceName)
        }

        with(binding) {
            scanDevices.setOnClickListener {
                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, filter)
                bluetoothAdapter?.startDiscovery()
            }

            backButton.setOnClickListener {
                startActivity(Intent(this@DeviceListActivity, FragmentActivity::class.java))
            }

            val adapter = ArrayAdapter(this@DeviceListActivity, android.R.layout.simple_list_item_1, deviceNamesList)
            deviceList.adapter = adapter

            val adapterMessage = ArrayAdapter(this@DeviceListActivity, android.R.layout.simple_list_item_1, messagesList)
            messageRead.adapter = adapterMessage

            deviceList.setOnItemClickListener { _, _, position, _ ->
                val selectedDeviceName = deviceNamesList[position]
                val selectedDevice = pairedDevices.find { it.name == selectedDeviceName }
                selectedDevice?.let { connectToDevice(it) }
            }

            temukanPerangkat.setOnClickListener {
                val requestCode = 1;
                val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                }
                startActivityForResult(discoverableIntent, requestCode)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device?.name
                deviceName?.let {
                    if (!deviceNamesList.contains(it)) {
                        deviceNamesList.add(it)
                        (binding.deviceList.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.fetchUuidsWithSdp()
            val uuids = device.uuids
            if (uuids != null && uuids.isNotEmpty()) {
                // Pilih UUID yang sesuai dengan layanan yang Anda butuhkan
                val uuid = uuids[0].uuid
                device.createRfcommSocketToServiceRecord(uuid)
            } else {
                null
            }
        }

        public override fun run() {
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.let { socket ->
                try {
                    socket.connect()
                    runOnUiThread {
                        binding.status.text = "Sukses Connect"
                    }
                    manageMyConnectedSocket(socket)
                } catch (e: IOException) {
                    Log.e(TAG, "Could not connect to the device", e)
                    runOnUiThread {
                        Toast.makeText(this@DeviceListActivity, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    try {
                        socket.close()
                    } catch (e2: IOException) {
                        Log.e(TAG, "Could not close the client socket", e2)
                    }
                    return
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

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        val myBluetoothService = MyBluetoothService(handler)
        val connectedThread = myBluetoothService.ConnectedThread(socket)
        connectedThread.start()
    }

    private fun connectToDevice(device: BluetoothDevice) {
        val connectThread = ConnectThread(device)
        connectThread.start()
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        bluetoothAdapter?.cancelDiscovery()
    }
}
