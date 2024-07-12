package com.example.smilling_app.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityAddDeviceBinding
import com.example.smilling_app.databinding.ActivityLoginBinding

class AddDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            backButton.setOnClickListener{
                finish()
            }
        }
    }
}