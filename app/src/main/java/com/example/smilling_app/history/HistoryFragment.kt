package com.example.smilling_app.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smilling_app.PrefManager
import com.example.smilling_app.R
import com.example.smilling_app.adapter.SensorDataAdapter
import com.example.smilling_app.adapter.historyAdapter
import com.example.smilling_app.databinding.FragmentHistoryBinding
import com.example.smilling_app.databinding.FragmentHomepageBinding
import com.example.smilling_app.firebase.Histories
import com.example.smilling_app.firebase.SensorDatas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager

    private lateinit var databaseForHistory: DatabaseReference

    private lateinit var historyAdapter: historyAdapter
    private lateinit var dataList: MutableList<Histories>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager.getInstance(requireContext())

        dataList = mutableListOf()
        historyAdapter = historyAdapter(dataList)

        binding.historyLists.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        databaseForHistory = FirebaseDatabase.getInstance().getReference("historiRekomendasi").child(prefManager.getUserId())
        databaseForHistory.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(Histories::class.java)
                    if (data != null) {
                        dataList.add(data)
                        Log.i("histories", "Data fetched: $data")
                    } else {
                        Log.e("histories", "Parsed data is null")
                    }
                }
                Log.i("histories", "Data list size: ${dataList.size}")
                historyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("FragmentHistory", "loadPost:onCancelled", error.toException())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
