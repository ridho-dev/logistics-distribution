package com.dededev.logistics.uiAdmin.home.daerah

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.databinding.FragmentHomeDaerahBinding
import com.dededev.logistics.databinding.FragmentHomePusatBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.adapter.LogisticAdapter
import com.dededev.logistics.uiAdmin.adapter.LogisticDaerahAdapter
import com.dededev.logistics.uiAdmin.home.pusat.HomePusatViewModel

class HomeDaerahFragment : Fragment() {

    private var _binding: FragmentHomeDaerahBinding? = null
    private lateinit var homeDaerahViewModel: HomeDaerahViewModel
    private lateinit var logisticList: MutableList<Logistic>
    private lateinit var adapter: LogisticDaerahAdapter

    private val binding get() = _binding!!

    private val type: List<String> = listOf(
        "Semua",
        "Perlengkapan Kepala",
        "Tutup Badan",
        "Tutup Kaki",
        "Tanda Pengenal",
        "Kap Dislap",
        "Kap Lain-lain",
        "Kapsat & Almount Kapsat"
    )

    private val region: List<String> = listOf(
        "Medan",
        "Pematang Siantar",
        "Sibolga",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDaerahBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(this.requireActivity().application)
        homeDaerahViewModel = ViewModelProvider(this, factory)[HomeDaerahViewModel::class.java]

        logisticList = mutableListOf()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvDaerah.layoutManager = layoutManager

        adapter = LogisticDaerahAdapter(logisticList, homeDaerahViewModel)
        homeDaerahViewModel.getAllLogistics().observe(viewLifecycleOwner) {
            if (logisticList.isEmpty()) {
                logisticList.addAll(it)
                val filteredList = logisticList.filter { item ->
                    item.wilayah == region[0]
                }
                adapter.updateData(filteredList)
                adapter.notifyDataSetChanged()
            }
        }

        binding.rvDaerah.adapter = adapter

        val spinnerKategoriAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, type)
        val actvKategori = binding.actvDaerahKategori
        actvKategori.apply {
            setAdapter(spinnerKategoriAdapter)
            setText(homeDaerahViewModel.selectedMenu, false)
        }

        actvKategori.setOnItemClickListener { _, _, position, _ ->
            val filteredList = filterList(actvKategori.text.toString(), homeDaerahViewModel.selectedRegion)
            homeDaerahViewModel.selectedMenu = type[position]
            adapter.updateData(filteredList)
            binding.rvDaerah.adapter = adapter
        }

        val spinnerWilayahAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, region)
        val actvWilayah = binding.actvDaerahWilayah
        actvWilayah.apply {
            setAdapter(spinnerWilayahAdapter)
            setText(homeDaerahViewModel.selectedRegion, false)
        }

        actvWilayah.setOnItemClickListener { _, _, position, _ ->
            val filteredList = filterList(homeDaerahViewModel.selectedMenu, actvWilayah.text.toString())
            homeDaerahViewModel.selectedRegion = region[position]
            adapter.updateData(filteredList)
            binding.rvDaerah.adapter = adapter
        }
        return binding.root
    }

    fun filterList(menuType: String, region: String): List<Logistic> {
        val filteredList = if (menuType == type[0]) {
            logisticList.filter { item ->
                item.wilayah == region
            }
        } else {
            logisticList.filter { item ->
                item.kategori == menuType && item.wilayah == region
            }
        }
        return filteredList
    }

}