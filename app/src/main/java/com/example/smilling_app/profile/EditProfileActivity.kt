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
import com.example.smilling_app.databinding.ActivityEditProfileBinding
import com.example.smilling_app.databinding.ActivityLoginBinding
import com.example.smilling_app.firebase.UserDatas
import com.example.smilling_app.views.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var prefManager: PrefManager
    
    // inisiasi firestore
    private val firestore = FirebaseFirestore.getInstance()
    private val userDataCollectionRef = firestore.collection("userDatas")
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        auth = FirebaseAuth.getInstance()

        with(binding){
            backButton.setOnClickListener{
                finish()
            }

            val userReferenceId = prefManager.getUserId()
            val user = auth.currentUser

            if (userReferenceId.isNotBlank()){
                val userDataDocumentRef = userDataCollectionRef.document(userReferenceId)
                userDataDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()){
                        nameEditInput.setText(documentSnapshot.getString("name"))
                        emailEditInput.setText(user?.email)
                        phoneEditInput.setText(documentSnapshot.getString("phone"))
                        luasEditInput.setText(documentSnapshot.getString("expand"))
                        alamatEditInput.setText(documentSnapshot.getString("address"))
                    }
                }
            }

            saveEdit.setOnClickListener {
                val nameEdit = nameEditInput.text.toString()
                val phoneEdit = phoneEditInput.text.toString()
                val expandEdit = luasEditInput.text.toString()
                val addressEdit = alamatEditInput.text.toString()

                userDataCollectionRef.document(userReferenceId)
                    .update(
                        mapOf(
                            "name" to nameEdit,
                            "phone" to phoneEdit,
                            "expand" to expandEdit,
                            "address" to addressEdit
                        ),
                    )
                    .addOnCompleteListener(this@EditProfileActivity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Berhasil mengubah data profile", Toast.LENGTH_SHORT,).show()
                            startActivity(Intent(this@EditProfileActivity, ProfileFragment::class.java))
                        } else {
                            Log.w(TAG, "Gagal Update Profile", task.exception)
                            Toast.makeText(baseContext, "Gagal mengubah data profile", Toast.LENGTH_SHORT,).show()
                        }
                    }
            }

        }
    }
}