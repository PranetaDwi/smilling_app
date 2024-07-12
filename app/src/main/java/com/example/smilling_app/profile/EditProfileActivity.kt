package com.example.smilling_app.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityEditProfileBinding
import com.example.smilling_app.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var prefManager: PrefManager
    
    // inisiasi firestore
    private val firestore = FirebaseFirestore.getInstance()
    private val userDataCollectionRef = firestore.collection("userDatas")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            backButton.setOnClickListener{
                finish()
            }
        }
    }
}