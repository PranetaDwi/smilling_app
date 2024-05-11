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
import com.example.smilling_app.R
import com.example.smilling_app.databinding.FragmentHomepageBinding

class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val REQUEST_ENABLE_BT = 123
        private const val REQUEST_BLUETOOTH_PERMISSION = 1001
    }

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

        val bluetoothManager: BluetoothManager? = requireActivity().getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        with (binding) {
            hubungkanPerangkat.setOnClickListener{
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BLUETOOTH
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.BLUETOOTH), REQUEST_BLUETOOTH_PERMISSION)
                } else {
                    if (bluetoothAdapter == null) {
                        Toast.makeText(requireContext(), "Bluetooth is not supported", Toast.LENGTH_SHORT).show()
                    } else {
                        if (!bluetoothAdapter.isEnabled) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

                        } else {
                            startActivity(Intent(requireContext(), DeviceListActivity::class.java))
                        }
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
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(Intent(requireContext(), DeviceListActivity::class.java))
            } else {
                Toast.makeText(requireContext(), "Bluetooth activation cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
