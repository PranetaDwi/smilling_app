//package com.example.smilling_app.bluetooth_configuration
//
//import android.bluetooth.BluetoothManager
//import android.content.Context
//import android.content.pm.PackageManager
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//class AndroidBluetoothController(
//    private val context: Context
//): BluetoothController {
//
//    private val bluetoohManager by lazy {
//        context.getSystemService(BluetoothManager::class.java)
//    }
//
//    private val bluetoohAdapter by lazy {
//        bluetoohManager?.adapter
//    }
//
//    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
//    override val scannerDevices: StateFlow<List<BluetoothDevice>>
//        get() = _scannedDevices.asStateFlow()
//
//    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
//    override val pairedDevices: StateFlow<List<BluetoothDevice>>
//        get() = _pairedDevices.asStateFlow()
//
//    override fun startDiscovery() {
//        TODO("Not yet implemented")
//    }
//
//    override fun stopDiscovery() {
//        TODO("Not yet implemented")
//    }
//
//    override fun release() {
//        TODO("Not yet implemented")
//    }
//
////    private fun updatePairedDevices(){
////        if (!hasPermission(Manifest.permission.){
////            return
////        }
////        bluetoohAdapter
////            ?.bondedDevices
////            ?.map {  }
////    }
//
//    private fun hasPermission(permission: String): Boolean{
//        return context.checkSelfPermission(permission)  == PackageManager.PERMISSION_GRANTED
//    }
//}