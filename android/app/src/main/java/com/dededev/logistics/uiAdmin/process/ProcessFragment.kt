package com.dededev.logistics.uiAdmin.process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.databinding.FragmentProcessBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.adapter.ProcessAdapter
import com.dededev.logistics.utils.predictPrioritas
import kotlin.math.floor

class ProcessFragment : Fragment() {

    private var _binding: FragmentProcessBinding? = null
    private lateinit var processViewModel: ProcessViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var allList: MutableList<Logistic>
    private lateinit var processedList: MutableList<Logistic>

    private val region: List<String> = listOf(
        "Medan",
        "Pematang Siantar",
        "Sibolga",
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        processViewModel = obtainViewModel(this@ProcessFragment)

        _binding = FragmentProcessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        allList = mutableListOf()
        processedList = mutableListOf()
        processViewModel.getAllLogistics().observe(viewLifecycleOwner) {
            allList.clear()
            allList.addAll(it)
        }

        spinnerProsesDaerah()
        displayProcessedView()

        return root
    }

    private fun displayProcessedView() {
        binding.apply {
            rvProcessedKepala.layoutManager = LinearLayoutManager(context)
            rvProcessedKepala.isNestedScrollingEnabled = false
            rvProcessedBadan.layoutManager = LinearLayoutManager(context)
            rvProcessedBadan.isNestedScrollingEnabled = false
            rvProcessedKaki.layoutManager = LinearLayoutManager(context)
            rvProcessedKaki.isNestedScrollingEnabled = false
            rvProcessedPengenal.layoutManager = LinearLayoutManager(context)
            rvProcessedPengenal.isNestedScrollingEnabled = false
            rvProcessedKapdislap.layoutManager = LinearLayoutManager(context)
            rvProcessedKapdislap.isNestedScrollingEnabled = false
            rvProcessedKaplainlain.layoutManager = LinearLayoutManager(context)
            rvProcessedKaplainlain.isNestedScrollingEnabled = false
            rvProcessedKapsat.layoutManager = LinearLayoutManager(context)
            rvProcessedKapsat.isNestedScrollingEnabled = false
        }

        binding.btnProcess.setOnClickListener {

            val processedPusat = allList.filter { item -> item.wilayah == "Pusat" }
            val processedWilayah = allList.filter { item -> item.wilayah == processViewModel.selectedRegion }
            for (i in processedPusat.indices) {
                val pusatItem = processedPusat[i]
                val wilayahItem = processedWilayah[i]
                processViewModel.update(predictPrioritas(pusatItem, wilayahItem))
            }
            process()
            processViewModel.setProcessedAlready(true)
        }

        processViewModel.processedAlready.observe(viewLifecycleOwner) {
            if (it) {
                binding.apply {
                    textView.text = getString(R.string.konfirmasi_proses)
                    btnProcess.visibility = View.INVISIBLE
                    btnConfirmRestart.visibility = View.VISIBLE
                    btnConfirmYes.visibility = View.VISIBLE
                    binding.layoutProcessed.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    textView.text = getString(R.string.mulai_proses)
                    btnProcess.visibility = View.VISIBLE
                    btnConfirmRestart.visibility = View.INVISIBLE
                    btnConfirmYes.visibility = View.INVISIBLE
                    binding.layoutProcessed.visibility = View.INVISIBLE
                }
            }
        }

        binding.btnConfirmRestart.setOnClickListener {
            processViewModel.setProcessedAlready(false)
            processViewModel.getProcessedLogistics().removeObservers(viewLifecycleOwner)

        }

        binding.btnConfirmYes.setOnClickListener {

            processViewModel.setProcessedAlready(false)
            processViewModel.getProcessedLogistics().removeObservers(viewLifecycleOwner)
            calculate()

        }
    }

    fun calculate() {
        val chosenList = allList.filter { item -> item.wilayah == processViewModel.selectedRegion && item.prioritasKirim == 1 }
        for (choosen in chosenList) {
            val itemPusat = allList.find { item ->
                item.wilayah == "Pusat" && item.namaBarang == choosen.namaBarang
            }
            val tenthPusat = itemPusat?.stokAkhir?.times(0.1)?.let { floor(it).toInt() }
            itemPusat?.stokAkhir = itemPusat?.stokAkhir!! - tenthPusat!!
            choosen.stokAkhir = choosen.stokAkhir + tenthPusat
            processViewModel.update(itemPusat)
            processViewModel.update(choosen)
        }
    }

    private fun spinnerProsesDaerah() {
        val spinnerProsesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, region)
        val actvWilayah = binding.actvProsesWilayah
        actvWilayah.apply {
            setAdapter(spinnerProsesAdapter)
            setText(processViewModel.selectedRegion, false)
        }

        actvWilayah.setOnItemClickListener { _, _, position, _ ->
            processViewModel.selectedRegion = region[position]
        }
    }

    private fun process() {
        processViewModel.getProcessedLogistics().observe(viewLifecycleOwner) {
            val dataKepala = it.filter { item -> item.kategori == "Perlengkapan Kepala" && item.wilayah == processViewModel.selectedRegion}
            if (dataKepala.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataKepala) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessKepala.visibility = View.VISIBLE
                binding.rvProcessedKepala.adapter = ProcessAdapter(data)
            }

            val dataBadan = it.filter { item -> item.kategori == "Tutup Badan" && item.wilayah == processViewModel.selectedRegion }
            if (dataBadan.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataBadan) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessBadan.visibility = View.VISIBLE
                binding.rvProcessedBadan.adapter = ProcessAdapter(data)
            }

            val dataKaki = it.filter { item -> item.kategori == "Tutup Kaki" && item.wilayah == processViewModel.selectedRegion }
            if (dataKaki.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataKaki) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessKaki.visibility = View.VISIBLE
                binding.rvProcessedKaki.adapter = ProcessAdapter(data)
            }

            val dataPengenal = it.filter { item -> item.kategori == "Tanda Pengenal" && item.wilayah == processViewModel.selectedRegion }
            if (dataPengenal.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataPengenal) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessPengenal.visibility = View.VISIBLE
                binding.rvProcessedPengenal.adapter = ProcessAdapter(data)
            }

            val dataKapDislap = it.filter { item -> item.kategori == "Kap Dislap" && item.wilayah == processViewModel.selectedRegion }
            if (dataKapDislap.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataKapDislap) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessKapdislap.visibility = View.VISIBLE
                binding.rvProcessedKapdislap.adapter = ProcessAdapter(data)
            }

            val dataKapLainLain = it.filter { item -> item.kategori == "Kap Lain-lain" && item.wilayah == processViewModel.selectedRegion }
            if (dataKapLainLain.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataKapLainLain) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessKaplainlain.visibility = View.VISIBLE
                binding.rvProcessedKaplainlain.adapter = ProcessAdapter(data)
            }

            val dataKapsat = it.filter { item -> item.kategori == "Kapsat & Almount Kapsat" && item.wilayah == processViewModel.selectedRegion }
            if (dataKapsat.isNotEmpty()) {
                val data = mutableListOf<Logistic>()
                for (dataItem in dataKapsat) {
                    val dataDiPusat = allList.find { item -> item.wilayah == "Pusat" && item.namaBarang == dataItem.namaBarang }
                    if (dataDiPusat != null) {
                        data.add(dataDiPusat)
                    }
                }
                binding.tvProcessKapsat.visibility = View.VISIBLE
                binding.rvProcessedKapsat.adapter = ProcessAdapter(data)
            }
        }
    }

    private fun obtainViewModel(fragment: ProcessFragment): ProcessViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[ProcessViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        spinnerProsesDaerah()
        processViewModel.processedAlready.observe(viewLifecycleOwner) {
            if (it == true) {
                process()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}