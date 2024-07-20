package com.example.smilling_app.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smilling_app.adapter.SensorDataAdapter
import com.example.smilling_app.databinding.ActivityPemilihanHistoryBinding
import com.example.smilling_app.firebase.SensorDatas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PemilihanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPemilihanHistoryBinding
    private lateinit var sensorDataAdapter: SensorDataAdapter
    private lateinit var database: DatabaseReference
    private lateinit var dataList: MutableList<SensorDatas>

    companion object {
        const val N_VALUE = "N_VALUE"
        const val P_VALUE = "P_VALUE"
        const val K_VALUE = "K_VALUE"
        const val PH_VALUE = "PH_VALUE"
        const val TEMP_VALUE = "TEMP_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemilihanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataList = mutableListOf()
        sensorDataAdapter = SensorDataAdapter(dataList)

        // Set layout manager and adapter for RecyclerView
        binding.sensorDataLists.apply {
            layoutManager = LinearLayoutManager(this@PemilihanHistoryActivity)
            adapter = sensorDataAdapter
        }

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("data")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear() // Clear the list to avoid duplicates
                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(SensorDatas::class.java)
                    if (data != null) {
                        dataList.add(data)
                        Log.i("dataSensor", "Data fetched: $data")
                    } else {
                        Log.e("dataSensor", "Parsed data is null")
                    }
                }
                Log.i("dataSensor", "Data list size: ${dataList.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "loadPost:onCancelled", error.toException())
            }
        })

        binding.buttonRekomendasiPupuk.setOnClickListener{
            val intentToHasilRekomendasiActivity = Intent(this@PemilihanHistoryActivity, HasilRekomendasiActivity::class.java)
            startActivity(intentToHasilRekomendasiActivity)
        }
    }
}