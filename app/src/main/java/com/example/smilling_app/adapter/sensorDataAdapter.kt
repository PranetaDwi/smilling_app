package com.example.smilling_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smilling_app.databinding.ItemSensorDataBinding
import com.example.smilling_app.firebase.SensorDatas

class sensorDataAdapter (private val context: Context, private var listSensorDatas: MutableList<SensorDatas>): RecyclerView.Adapter<sensorDataAdapter.ItemSensorDataViewHolder>(){
    inner class ItemSensorDataViewHolder(private val binding: ItemSensorDataBinding):

        RecyclerView.ViewHolder(binding.root){

        fun bindItem(data: SensorDatas){
            with(binding){
                dataNitrogen.text = data.nitrogen.toString()
                dataFosfor.text = data.fosfor.toString()
                dataPh.text = data.ph.toString()
                dataKalium.text = data.kalium.toString()
                dataKelembaban.text = data.kelembaban.toString()
            }
        }
    }

    fun setData(newData: List<SensorDatas>) {
        listSensorDatas.clear()
        listSensorDatas.addAll(newData)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSensorDataViewHolder {
        val binding = ItemSensorDataBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
        return ItemSensorDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ItemSensorDataViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}