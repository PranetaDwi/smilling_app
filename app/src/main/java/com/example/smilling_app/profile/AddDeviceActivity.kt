package com.example.smilling_app.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityAddDeviceBinding
import com.example.smilling_app.databinding.ActivityLoginBinding
import com.example.smilling_app.home.HomepageFragment
import com.google.firebase.firestore.FirebaseFirestore

class AddDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDeviceBinding
    private lateinit var prefManager: PrefManager

    // inisiasi firestore
    private val firestore = FirebaseFirestore.getInstance()
    private val userDataCollectionRef = firestore.collection("userDatas")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding){
            backButton.setOnClickListener{
                finish()
            }

            val userReferenceId = prefManager.getUserId()

            if (userReferenceId.isNotBlank()){
                val userDataDocumentRef = userDataCollectionRef.document(userReferenceId)
                userDataDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()){
                        addDeviceInput.setText(documentSnapshot.getString("deviceName"))
                    }
                }
            }

            saveButton.setOnClickListener {
                val deviceName = addDeviceInput.text.toString()

                userDataCollectionRef.document(userReferenceId)
                    .update(
                        mapOf(
                            "deviceName" to deviceName,
                        ),
                    )
                    .addOnCompleteListener(this@AddDeviceActivity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Berhasil mengatur device", Toast.LENGTH_SHORT,).show()
                            startActivity(Intent(this@AddDeviceActivity, HomepageFragment::class.java))
                        } else {
                            Log.w(TAG, "Gagal Update Profile", task.exception)
                            Toast.makeText(baseContext, "mengatur device", Toast.LENGTH_SHORT,).show()
                        }
                    }
            }

        }
    }
}