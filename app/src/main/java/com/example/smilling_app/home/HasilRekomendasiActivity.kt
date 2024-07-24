package com.example.smilling_app.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.smilling_app.PrefManager
import com.example.smilling_app.ai.NpkModelHandler
import com.example.smilling_app.databinding.ActivityHasilRekomendasiBinding
import com.example.smilling_app.firebase.Histories
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class HasilRekomendasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilRekomendasiBinding
    private lateinit var modelHandler: NpkModelHandler
    private lateinit var database: DatabaseReference
    private lateinit var databaseForHistory: DatabaseReference
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilRekomendasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        var nValue = 0f
        var pValue = 0f
        var kValue = 0f
        var phValue = 0f
        var tempValue = 0f

        nValue = intent.getFloatExtra(PemilihanHistoryActivity.N_VALUE, 0f)
        pValue = intent.getFloatExtra(PemilihanHistoryActivity.P_VALUE, 0f)
        kValue = intent.getFloatExtra(PemilihanHistoryActivity.K_VALUE, 0f)
        phValue = intent.getFloatExtra(PemilihanHistoryActivity.PH_VALUE, 0f)
        tempValue = intent.getFloatExtra(PemilihanHistoryActivity.TEMP_VALUE, 0f)

        Log.i("fetchDataIntent", nValue.toString())
        Log.i("fetchDataIntent", pValue.toString())
        Log.i("fetchDataIntent", kValue.toString())
        Log.i("fetchDataIntent", phValue.toString())
        Log.i("fetchDataIntent", tempValue.toString())

        database = FirebaseDatabase.getInstance().getReference("data")
        databaseForHistory = FirebaseDatabase.getInstance().getReference("historiRekomendasi")

        val maxValue = maxOf(nValue, pValue, kValue)
        Log.i("dataMax", maxValue.toString())
        val prediction = maxValue/0.15


        //                    modelHandler = NpkModelHandler(this@HasilRekomendasiActivity)
        //
        //                    val inputData = floatArrayOf(nValue, pValue, kValue, phValue, tempValue)
        //
        //                    val prediction = modelHandler.predict(inputData)
        //
        //                    modelHandler.close()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val formattedDate = dateFormat.format(Date())

        val history = Histories(
            N = nValue.toDouble(),
            P = pValue.toDouble(),
            K = kValue.toDouble(),
            pH = phValue.toDouble(),
            Temp = tempValue.toDouble(),
            kadar = String.format("%.2f", prediction).toDouble(),
            Waktu = formattedDate
        )

        databaseForHistory.child(prefManager.getUserId()).push().setValue(history)
            .addOnSuccessListener {
                Log.d("HasilRekomendasiActivity", "History saved successfully")
            }
            .addOnFailureListener {
                Log.e("HasilRekomendasiActivity", "Failed to save history", it)
            }

        with(binding) {
            hasilKadar.text = String.format("%.2f", prediction)
            backButton.setOnClickListener {
                startActivity(Intent(this@HasilRekomendasiActivity, PemilihanHistoryActivity::class.java))
            }
        }
    }
}