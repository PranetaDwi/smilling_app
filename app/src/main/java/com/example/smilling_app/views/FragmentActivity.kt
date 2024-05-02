package com.example.smilling_app.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.smilling_app.R
import com.example.smilling_app.databinding.ActivityFragmentBinding
import com.example.smilling_app.history.HistoryFragment
import com.example.smilling_app.home.HomepageFragment
import com.example.smilling_app.profile.ProfileFragment
import com.google.api.ResourceDescriptor.History

class FragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        replaceFragment(HomepageFragment())

        supportActionBar?.hide()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.nav_home -> replaceFragment(HomepageFragment())
                R.id.nav_history -> replaceFragment(HistoryFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}