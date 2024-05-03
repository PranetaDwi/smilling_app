package com.example.smilling_app.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.auth.LoginActivity
import com.example.smilling_app.databinding.FragmentHomepageBinding
import com.example.smilling_app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager.getInstance(requireContext())

        with (binding){
            logoutButton.setOnClickListener{
                prefManager.setLoggedIn(false)
                prefManager.clear()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
    }



}