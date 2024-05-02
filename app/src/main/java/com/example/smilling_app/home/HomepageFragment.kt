package com.example.smilling_app.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.smilling_app.databinding.FragmentHomepageBinding

class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tipes = arrayOf("Sayuran", "Buah")
        val komoditasSayurans = arrayOf("Cabai", "Sawi", "Bawang")
        val komoditasBuahs = arrayOf("Leci", "Jeruk", "Mangga")

        with (binding) {
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

}