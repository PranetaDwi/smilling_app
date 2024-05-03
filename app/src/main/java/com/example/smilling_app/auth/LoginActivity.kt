package com.example.smilling_app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityLoginBinding
import com.example.smilling_app.home.HomepageFragment
import com.example.smilling_app.views.FragmentActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        with(binding){
            val intentToHomeActivity = Intent(this@LoginActivity, FragmentActivity::class.java)

            loginButton.setOnClickListener{
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        val uid = user?.uid
                        prefManager.setLoggedIn(true)
                        prefManager.saveUserId(uid.toString())
                        startActivity(intentToHomeActivity)
                    }
                }.addOnFailureListener{exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }

            registerBUttonFromLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if(isLoggedIn){
            startActivity(Intent(this@LoginActivity, FragmentActivity::class.java))
        }
    }
}