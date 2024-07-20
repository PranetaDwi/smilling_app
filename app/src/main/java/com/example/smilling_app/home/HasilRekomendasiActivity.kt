package com.example.smilling_app.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityHasilRekomendasiBinding
import com.example.smilling_app.databinding.ActivityPemilihanHistoryBinding

class HasilRekomendasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilRekomendasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilRekomendasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
    }
}