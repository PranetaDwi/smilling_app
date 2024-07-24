package com.example.smilling_app.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
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
        sensorDataAdapter = SensorDataAdapter(dataList, onSelectedClick = { selectedClick -> })

        // Set layout manager and adapter for RecyclerView
        binding.sensorDataLists.apply {
            layoutManager = LinearLayoutManager(this@PemilihanHistoryActivity)
            adapter = sensorDataAdapter
        }

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("data")
        database.orderByChild("Waktu").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                val tempList = mutableListOf<SensorDatas>()
                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(SensorDatas::class.java)
                    if (data != null) {
                        tempList.add(data)
                        Log.i("dataSensor", "Data fetched: $data")
                    } else {
                        Log.e("dataSensor", "Parsed data is null")
                    }
                }

                tempList.reverse()
                dataList.addAll(tempList)
                Log.i("dataSensor", "Data list size: ${dataList.size}")
                sensorDataAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "loadPost:onCancelled", error.toException())
            }
        })

        sensorDataAdapter.onSelectedClick = {
            data ->
            val intent = Intent(this@PemilihanHistoryActivity, HasilRekomendasiActivity::class.java)
            intent.putExtra(N_VALUE, data.N.toFloat())
            intent.putExtra(P_VALUE, data.P.toFloat())
            intent.putExtra(K_VALUE, data.K.toFloat())
            intent.putExtra(PH_VALUE, data.pH.toFloat())
            intent.putExtra(TEMP_VALUE, data.Temp.toFloat())
            startActivity(intent)
        }

        binding.buttonRekomendasiPupuk.setOnClickListener{
            val intent = Intent(this@PemilihanHistoryActivity, HasilRekomendasiActivity::class.java)
            database.orderByChild("Waktu").limitToLast(1)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {
                            val data = dataSnapshot.getValue(SensorDatas::class.java)
                            if (data != null) {
                                intent.putExtra(N_VALUE, data.N.toFloat())
                                intent.putExtra(P_VALUE, data.P.toFloat())
                                intent.putExtra(K_VALUE, data.K.toFloat())
                                intent.putExtra(PH_VALUE, data.pH.toFloat())
                                intent.putExtra(TEMP_VALUE, data.Temp.toFloat())
                                startActivity(intent)
                            } else {
                                Log.e("dataUntukRekomendasi", "Parsed data is null")
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("MainActivity", "loadPost:onCancelled", error.toException())
                    }
                }
            )
        }
    }
}