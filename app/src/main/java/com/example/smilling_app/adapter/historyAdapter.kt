package com.example.smilling_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smilling_app.databinding.ItemHistoryBinding
import com.example.smilling_app.databinding.ItemSensorDataBinding
import com.example.smilling_app.firebase.Histories
import com.example.smilling_app.firebase.SensorDatas

class historyAdapter(private val listHistories: List<Histories>) : RecyclerView.Adapter<historyAdapter.ItemHistoryViewHolder>() {

    inner class ItemHistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(data: Histories) {
            val waktu = data.Waktu.split(" ")
            val dateSensor = waktu[0]
            val timeSensor = waktu[1]
            with(binding) {
                hasilKadarPupuk.text = data.kadar.toString()
                tanggalCek.text = dateSensor
                waktuCek.text = timeSensor
                Log.i("SensorDataAdapter", "Binding data: $data")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = listHistories.size

    override fun onBindViewHolder(holder: ItemHistoryViewHolder, position: Int) {
        val currentItem = listHistories[position]
        holder.bindItem(currentItem)
        Log.i("SensorDataAdapter", "Binding item at position: $position")
    }
}