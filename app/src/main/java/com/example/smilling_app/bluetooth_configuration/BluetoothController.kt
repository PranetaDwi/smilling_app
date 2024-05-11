//package com.example.smilling_app.bluetooth_configuration
//
//import kotlinx.coroutines.flow.StateFlow
//
//interface BluetoothController {
//    val scannerDevices: StateFlow<List<BluetoothDevice>>
//    val pairedDevices: StateFlow<List<BluetoothDevice>>
//
//    fun startDiscovery()
//    fun stopDiscovery()
//    fun release()
//}