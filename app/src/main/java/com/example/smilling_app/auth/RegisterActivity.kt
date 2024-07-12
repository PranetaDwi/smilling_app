package com.example.smilling_app.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smilling_app.databinding.ActivityRegisterBinding
import com.example.smilling_app.firebase.UserDatas
import com.example.smilling_app.views.FragmentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        val db = Firebase.firestore

        with(binding) {
            backButton.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            loginButtonFromRegister.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            registerButton.setOnClickListener {
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val phone = phoneInput.text.toString()
                val password = passwordInput.text.toString()

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()){
                    Toast.makeText(baseContext, "Lengkapi Data terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@RegisterActivity) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                val uid = user?.uid

                                val dataPribadi = UserDatas(
                                    name,
                                    phone,
                                    0,
                                    "Belum Diatur",
                                    "Belum Diatur"
                                )
                                
                                db.collection("userDatas").document(uid.toString())
                                    .set(dataPribadi)
                                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                                Toast.makeText(baseContext, "Berhasil Mendaftarkan Akun", Toast.LENGTH_SHORT,).show()
                                startActivity(Intent(this@RegisterActivity, FragmentActivity::class.java))
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                            }
                        }
                }
            }

        }
    }
}