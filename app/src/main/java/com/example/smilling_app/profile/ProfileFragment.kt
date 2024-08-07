package com.example.smilling_app.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smilling_app.PrefManager
import com.example.smilling_app.auth.LoginActivity
import com.example.smilling_app.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager

    private val firestore = FirebaseFirestore.getInstance()
    private val userDataCollectionRef = firestore.collection("userDatas")
    private lateinit var auth : FirebaseAuth

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
        auth = FirebaseAuth.getInstance()

        with (binding){
            logoutButton.setOnClickListener{
                prefManager.setLoggedIn(false)
                prefManager.clear()
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            navigateToEditProfileButton.setOnClickListener{
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }
            navigateToSettingToolButton.setOnClickListener {
                startActivity(Intent(requireContext(), AddDeviceActivity::class.java))
            }

            val userReferenceId = prefManager.getUserId()
            val user = auth.currentUser

            if (userReferenceId.isNotBlank()){
                val userDataDocumentRef = userDataCollectionRef.document(userReferenceId)
                userDataDocumentRef.addSnapshotListener { documentSnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        nameProfile.text = documentSnapshot.getString("name")
                        emailProfile.text = user?.email
                        phoneProfile.text = documentSnapshot.getString("phone")
                        expandProfile.text = documentSnapshot.getString("expand")
                        addressProfile.text = documentSnapshot.getString("address")
                    }
                }
            }

        }
    }
}