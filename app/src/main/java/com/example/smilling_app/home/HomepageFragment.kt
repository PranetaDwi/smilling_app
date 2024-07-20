package com.example.smilling_app.home

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.auth.LoginActivity
import com.example.smilling_app.databinding.FragmentHomepageBinding
import com.example.smilling_app.views.SplashActivity
import com.google.firebase.firestore.FirebaseFirestore

class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager

    // inisiasi firestore
    private val firestore = FirebaseFirestore.getInstance()
    private val userDataCollectionRef = firestore.collection("userDatas")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager.getInstance(requireContext())

        val userReferenceId = prefManager.getUserId()

        val tipes = arrayOf("Sayuran", "Buah")
        val komoditasSayurans = arrayOf("Cabai", "Sawi", "Bawang")
        val komoditasBuahs = arrayOf("Leci", "Jeruk", "Mangga")

        with (binding) {
            if (userReferenceId.isNotBlank()){
                val userDataDocumentRef = userDataCollectionRef.document(userReferenceId)
                userDataDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()){
                        deviceNameText.text = documentSnapshot.getString("deviceName")
                    }
                }
            }

            val tipeAdapter = ArrayAdapter(
                requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tipes
            )

            tipeAdapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
            )

            tipeInputDropdown.adapter = tipeAdapter

            tipeInputDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedMethod: String = tipes[p2]
                    when (selectedMethod){
                        tipes[0] -> {
                            val komoditasSayuranAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, komoditasSayurans)
                            komoditasSayuranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            komoditasInputDropdown.adapter = komoditasSayuranAdapter
                        }
                        tipes[1] -> {
                            val komoditasBuahAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, komoditasBuahs)
                            komoditasBuahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            komoditasInputDropdown.adapter = komoditasBuahAdapter
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            val intentToPemilihanHistoryActivity = Intent(requireContext(), PemilihanHistoryActivity::class.java)

            nextButton.setOnClickListener{
                startActivity(intentToPemilihanHistoryActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
