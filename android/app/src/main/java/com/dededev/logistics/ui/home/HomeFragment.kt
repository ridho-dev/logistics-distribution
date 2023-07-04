package com.dededev.logistics.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dededev.logistics.databinding.FragmentHomeBinding
import java.io.InputStream
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.ui.ViewModelFactory
import com.dededev.logistics.ui.adapter.AdapterKepala
import org.apache.poi.ss.usermodel.WorkbookFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var logisticList: MutableList<Logistic>
    private lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = obtainViewModel(this@HomeFragment)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnPickFile = binding.btnImport
        btnPickFile.setOnClickListener {
            val intent =  Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            pickFileLauncher.launch(intent)
            Log.d("TAG", "onCreateView: ButtonClicked")
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvKepala.layoutManager = layoutManager
        binding.rvKepala.isNestedScrollingEnabled = false
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvKepala.addItemDecoration(itemDecoration)


        logisticList = mutableListOf()

        homeViewModel.getAllLogistics().observe(viewLifecycleOwner) {
            logisticList.clear()
            logisticList.addAll(it)
        }
        val adapter = AdapterKepala(logisticList, homeViewModel)
        binding.rvKepala.adapter = adapter

        return root
    }

    private fun obtainViewModel(fragment: HomeFragment): HomeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[HomeViewModel::class.java]
    }

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val fileUri = data.data
                if (fileUri != null) {
                    readExcelData(fileUri)
                }
            }
        }
        Log.d("TAG", "onCreateView: file picked")

    }

    private fun readExcelData(fileUri: Uri) {
        try {
            val inputStream : InputStream? = context?.contentResolver?.openInputStream(fileUri)
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)


            for (rowIndex in 1 until sheet.lastRowNum + 1) {
                val row = sheet.getRow(rowIndex)
                val id = row.getCell(0)?.numericCellValue.toString().toDouble().toInt()
                val namaBarang = row.getCell(1)?.stringCellValue ?: ""
                val kategori = row.getCell(2)?.stringCellValue ?: ""
                val wilayah = row.getCell(3)?.stringCellValue ?: ""
                val stokAwalPusat = row.getCell(4)?.numericCellValue.toString().toDouble().toInt()
                val stokAkhirPusat = row.getCell(5)?.numericCellValue.toString().toDouble().toInt()
                val stokAwalDaerah = row.getCell(7)?.numericCellValue.toString().toDouble().toInt()
                val stokAkhirDaerah = row.getCell(8)?.numericCellValue.toString().toDouble().toInt()
                val prioritasKirim = row.getCell(10)?.numericCellValue.toString().toDouble().toInt()
                val logistic = Logistic(
                    id, namaBarang, kategori, wilayah, stokAwalPusat, stokAkhirPusat, stokAwalDaerah, stokAkhirDaerah, prioritasKirim
                )
                homeViewModel.insert(logistic)
            }
            workbook.close()
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "catch: $e")
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAllLogistics().observe(viewLifecycleOwner) {
            logisticList.clear()
            logisticList.addAll(it)
        }
        val adapter = AdapterKepala(logisticList, homeViewModel)
        binding.rvKepala.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}