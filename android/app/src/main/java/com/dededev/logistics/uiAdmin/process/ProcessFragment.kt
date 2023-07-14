package com.dededev.logistics.uiAdmin.process

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.databinding.FragmentProcessBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.adapter.ProcessAdapter
//import com.dededev.logistics.utils.predict

class ProcessFragment : Fragment() {

    private var _binding: FragmentProcessBinding? = null
    private lateinit var processViewModel: ProcessViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var processedList: MutableList<Logistic>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        processViewModel = obtainViewModel(this@ProcessFragment)

        _binding = FragmentProcessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        processedList = mutableListOf()

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

//        binding.btnConfirmYes.setOnClickListener {
//            for (logistic in processedList) {
//                logistic.stokAkhirDaerah = calculateDaerah(logistic.stokAkhirPusat, logistic.stokAkhirDaerah)
//                logistic.stokAkhirPusat = calculatePusat(logistic.stokAkhirPusat)
//
//                Log.d("TAG", "displayProcessedView: ${logistic.namaBarang}; ${logistic.stokAkhirPusat};${logistic.stokAkhirDaerah};")
//                processViewModel.update(predict(logistic))
//            }
//            processViewModel.setProcessedAlready(false)
//            processViewModel.getProcessedLogistics().removeObservers(viewLifecycleOwner)
//        }
    }

    private fun calculatePusat(stokPusat: Int): Int {
        return stokPusat - (stokPusat*0.1).toInt()
    }

    private fun calculateDaerah(stokPusat: Int, stokDaerah: Int): Int {
        return stokDaerah + (stokPusat*0.1).toInt()
    }

    private fun process() {
        processViewModel.getProcessedLogistics().observe(viewLifecycleOwner) {
            processedList.addAll(it)
            val dataKepala = it.filter { item -> item.kategori == "Perlengkapan Kepala" }
            if (dataKepala.isNotEmpty()) {
                binding.tvProcessKepala.visibility = View.VISIBLE
                binding.rvProcessedKepala.adapter = ProcessAdapter(dataKepala)
            }

            val dataBadan = it.filter { item -> item.kategori == "Tutup Badan" }
            if (dataBadan.isNotEmpty()) {
                binding.tvProcessBadan.visibility = View.VISIBLE
                binding.rvProcessedBadan.adapter = ProcessAdapter(dataBadan)
            }

            val dataKaki = it.filter { item -> item.kategori == "Tutup Kaki" }
            if (dataKaki.isNotEmpty()) {
                binding.tvProcessKaki.visibility = View.VISIBLE
                binding.rvProcessedKaki.adapter = ProcessAdapter(dataKaki)
            }

            val dataPengenal = it.filter { item -> item.kategori == "Tanda Pengenal" }
            if (dataPengenal.isNotEmpty()) {
                binding.tvProcessPengenal.visibility = View.VISIBLE
                binding.rvProcessedPengenal.adapter = ProcessAdapter(dataPengenal)
            }

            val dataKapDislap = it.filter { item -> item.kategori == "Kap Dislap" }
            if (dataKapDislap.isNotEmpty()) {
                binding.tvProcessKapdislap.visibility = View.VISIBLE
                binding.rvProcessedKapdislap.adapter = ProcessAdapter(dataKapDislap)
            }

            val dataKapLainLain = it.filter { item -> item.kategori == "Kap Lain-lain" }
            if (dataKapLainLain.isNotEmpty()) {
                binding.tvProcessKaplainlain.visibility = View.VISIBLE
                binding.rvProcessedKaplainlain.adapter = ProcessAdapter(dataKapLainLain)
            }

            val dataKapsat = it.filter { item -> item.kategori == "Kapsat & Almount Kapsat" }
            if (dataKapsat.isNotEmpty()) {
                binding.tvProcessKapsat.visibility = View.VISIBLE
                binding.rvProcessedKapsat.adapter = ProcessAdapter(dataKapsat)
            }
        }
    }


    private fun obtainViewModel(fragment: ProcessFragment): ProcessViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[ProcessViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        processViewModel.processedAlready.observe(viewLifecycleOwner) { if (it) process() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}