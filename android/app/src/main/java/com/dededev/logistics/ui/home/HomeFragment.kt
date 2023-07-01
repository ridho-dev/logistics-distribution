package com.dededev.logistics.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dededev.logistics.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import android.content.ContentResolver
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dededev.logistics.data.Student
import com.dededev.logistics.data.adapter.AdapterKepala
import org.apache.poi.ss.usermodel.WorkbookFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnPickFile = binding.btnImport
        btnPickFile.setOnClickListener {
            val intent =  Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            pickFileLauncher.launch(intent)
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvKepala.layoutManager = layoutManager
        binding.rvKepala.isNestedScrollingEnabled = false
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvKepala.addItemDecoration(itemDecoration)


        val studentList = listOf(
            Student("Siswa 1", 80),
            Student("Siswa 2", 90),
            Student("Siswa 3", 75),
            Student("Siswa 4", 85),
            Student("Siswa 5", 95),
            Student("Siswa 6", 70),
            Student("Siswa 7", 88),
            Student("Siswa 8", 92),
            Student("Siswa 9", 78),
            Student("Siswa 10", 83)
        )

        val adapter = AdapterKepala(studentList)
        binding.rvKepala.adapter = adapter

        return root
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
    }

    private fun readExcelData(fileUri: Uri) {
        try {
            val inputStream : InputStream? = context?.contentResolver?.openInputStream(fileUri)
            val workbook = WorkbookFactory.create(inputStream)

            workbook.close()
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}