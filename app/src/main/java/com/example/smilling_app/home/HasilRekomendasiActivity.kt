package com.example.smilling_app.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.smilling_app.PrefManager
import com.example.smilling_app.ai.NpkModelHandler
import com.example.smilling_app.databinding.ActivityHasilRekomendasiBinding
import com.example.smilling_app.firebase.Histories
import com.example.smilling_app.firebase.SensorDatas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.Instant
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

        database = FirebaseDatabase.getInstance().getReference("data")
        databaseForHistory = FirebaseDatabase.getInstance().getReference("historiRekomendasi")
        database.orderByChild("Waktu").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    var nValue = 0f
                    var pValue = 0f
                    var kValue = 0f
                    var phValue = 0f
                    var tempValue = 0f

                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(SensorDatas::class.java)
                        if (data != null) {
                            nValue = data.N.toFloat()
                            pValue = data.P.toFloat()
                            kValue = data.K.toFloat()
                            phValue = data.pH.toFloat()
                            tempValue = data.Temp.toFloat()
                        } else {
                            Log.e("dataUntukRekomendasi", "Parsed data is null")
                        }
                    }

                    modelHandler = NpkModelHandler(this@HasilRekomendasiActivity)

                    val inputData = floatArrayOf(nValue, pValue, kValue, phValue, tempValue)

                    val prediction = modelHandler.predict(inputData)

                    modelHandler.close()

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    val formattedDate = dateFormat.format(Date())

                    val history = Histories(
                        N = nValue.toDouble(),
                        P = pValue.toDouble(),
                        K = kValue.toDouble(),
                        pH = phValue.toDouble(),
                        Temp = tempValue.toDouble(),
                        kadar = prediction[0].toDouble(),
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
                        hasilKadar.text = prediction[0].toString()
                        backButton.setOnClickListener {
                            startActivity(Intent(this@HasilRekomendasiActivity, PemilihanHistoryActivity::class.java))
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
