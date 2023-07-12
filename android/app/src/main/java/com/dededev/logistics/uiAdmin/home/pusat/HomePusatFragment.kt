package com.dededev.logistics.uiAdmin.home.pusat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.databinding.FragmentHomePusatBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.adapter.LogisticAdapter
import com.dededev.logistics.utils.predict
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class HomePusatFragment : Fragment() {
    private var _binding: FragmentHomePusatBinding? = null
    private lateinit var homePusatViewModel: HomePusatViewModel
    private lateinit var logisticList: MutableList<Logistic>
    private lateinit var adapter: LogisticAdapter

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

    private var selectedMenu: String = type[0]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePusatBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(this.requireActivity().application)
        homePusatViewModel = ViewModelProvider(this, factory)[HomePusatViewModel::class.java]

        setHasOptionsMenu(true)
        logisticList = mutableListOf()

        val layoutManager = LinearLayoutManager(context)
        binding.rvKepala.layoutManager = layoutManager

        adapter = LogisticAdapter(logisticList, homePusatViewModel)
        homePusatViewModel.getAllLogistics().observe(viewLifecycleOwner) {
            if (logisticList.isEmpty()) {
                logisticList.addAll(it)
                adapter.updateData(logisticList)
                adapter.notifyDataSetChanged()
            }
        }

        binding.rvKepala.adapter = adapter

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, type)
        val autoCompleteTextView = binding.filledExposed
        autoCompleteTextView.apply {
            setAdapter(spinnerAdapter)
            setSelection(0)
        }
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->

            val filteredList = when (autoCompleteTextView.text.toString()) {
                type[0] -> {
                    logisticList
                }
                type[1] -> {
                    logisticList.filter { it.kategori == type[1] }
                }
                type[2] -> {
                    logisticList.filter { it.kategori == type[2] }
                }
                type[3] -> {
                    logisticList.filter { it.kategori == type[3] }
                }
                type[4] -> {
                    logisticList.filter { it.kategori == type[4] }
                }
                type[5] -> {
                    logisticList.filter { it.kategori == type[5] }
                }
                type[6] -> {
                    logisticList.filter { it.kategori == type[6] }
                }
                type[7] -> {
                    logisticList.filter { it.kategori == type[7] }
                }
                else -> {
                    logisticList
                }
            }
            selectedMenu = type[position]
            adapter.updateData(filteredList)
            binding.rvKepala.adapter = adapter
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.admin_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_import_excel -> {
                val intent =  Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                pickFileLauncher.launch(intent)
                Log.d("TAG", "onCreateView: ButtonClicked")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                var logistic = Logistic(
                    id, namaBarang, kategori, wilayah, stokAwalPusat, stokAkhirPusat, stokAwalDaerah, stokAkhirDaerah, prioritasKirim
                )
                logistic = predict(logistic)
                homePusatViewModel.insert(logistic)
            }
            if (logisticList.isNotEmpty()) logisticList.clear()
            workbook.close()
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "catch: $e")
        }
    }

    override fun onResume() {
        super.onResume()
//
//        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, type)
//        val autoCompleteTextView = binding.filledExposed
//        autoCompleteTextView.apply {
//            setAdapter(spinnerAdapter)
//            setSelection(type.indexOf(selectedMenu))
//            setText(selectedMenu, false)
//        }
//        adapter = LogisticAdapter(logisticList, homePusatViewModel)
//        binding.rvKepala.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}