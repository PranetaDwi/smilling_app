package com.example.smilling_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smilling_app.databinding.ItemSensorDataBinding
import com.example.smilling_app.firebase.SensorDatas

class SensorDataAdapter(private val listSensorDatas: List<SensorDatas>) : RecyclerView.Adapter<SensorDataAdapter.ItemSensorDataViewHolder>() {

    inner class ItemSensorDataViewHolder(private val binding: ItemSensorDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(data: SensorDatas) {
            val waktu = data.Waktu.split(" ")
            val dateSensor = waktu[0]
            val timeSensor = waktu[1]
            with(binding) {
                dataNitrogen.text = data.N.toString()
                dataFosfor.text = data.P.toString()
                dataPh.text = data.pH.toString()
                dataKalium.text = data.K.toString()
                dataKelembaban.text = data.Moist.toString()
                tanggalCek.text = dateSensor
                waktuCek.text = timeSensor
                Log.i("SensorDataAdapter", "Binding data: $data")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSensorDataViewHolder {
        val binding = ItemSensorDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSensorDataViewHolder(binding)
    }

    override fun getItemCount(): Int = listSensorDatas.size

    override fun onBindViewHolder(holder: ItemSensorDataViewHolder, position: Int) {
        val currentItem = listSensorDatas[position]
        holder.bindItem(currentItem)
        Log.i("SensorDataAdapter", "Binding item at position: $position")
    }
}