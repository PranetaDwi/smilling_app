package com.example.smilling_app.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.R
import com.example.smilling_app.ai.NpkModelHandler
import com.example.smilling_app.databinding.ActivityHasilRekomendasiBinding
import com.example.smilling_app.databinding.ActivityPemilihanHistoryBinding

class HasilRekomendasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilRekomendasiBinding
    private lateinit var modelHandler: NpkModelHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilRekomendasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelHandler = NpkModelHandler(this)

        val inputData = floatArrayOf(23f, 4.0f,	1.1f, 0.30f, 0.50f)

        val prediction = modelHandler.predict(inputData)

        Log.d("TFLiteModel", "Prediction: ${prediction[0]}")

        modelHandler.close()

        with(binding){
            backButton.setOnClickListener{
                startActivity(Intent(this@HasilRekomendasiActivity, PemilihanHistoryActivity::class.java))
            }

            hasilKadar.text = prediction[0].toString()
        }
    }
}