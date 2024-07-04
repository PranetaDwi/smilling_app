package com.example.smilling_app.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.adapter.sensorDataAdapter
import com.example.smilling_app.databinding.ActivityPemilihanHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class PemilihanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPemilihanHistoryBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val sensorDataCollectionRef = firestore.collection("sensorDatas")
    private lateinit var sensorDataAdapter: sensorDataAdapter
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPemilihanHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}