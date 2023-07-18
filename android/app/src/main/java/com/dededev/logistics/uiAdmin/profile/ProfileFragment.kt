package com.dededev.logistics.uiAdmin.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dededev.logistics.databinding.FragmentProfileBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.login.LoginActivity
import com.dededev.logistics.uiAdmin.process.ProcessFragment
import com.dededev.logistics.uiAdmin.process.ProcessViewModel
import com.dededev.logistics.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var pref: SessionManager
    private lateinit var profileViewModel: ProfileViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var REQUEST_CODE: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pref = SessionManager(context!!)

        profileViewModel = obtainViewModel(this@ProfileFragment)

        binding.btnLogout.setOnClickListener {
            pref.setLoggedIn(false, "", "")
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnExport.setOnClickListener {
            lifecycleScope.launch {
                val date = SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(Date())
                val file = File(Environment.getExternalStorageDirectory(), "/${pref.getJabatan()}$date.xlsx")
                export(file)
            }
        }

        binding.profileUserType.text = pref.getJabatan()
        binding.profileUserLocation.text = pref.getLokasi()

        return root
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.loadingLayout.visibility = View.VISIBLE
            binding.btnLogout.isEnabled = false
            binding.btnExport.isEnabled = false
        } else {
            binding.loadingLayout.visibility = View.GONE
            binding.btnLogout.isEnabled = true
            binding.btnExport.isEnabled = true
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Izin sudah diberikan, lanjutkan tindakan yang membutuhkan izin
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }


    private suspend fun export(file: File) {
        checkPermission()
        showLoading(true)
        withContext(Dispatchers.IO) {
            try {
                val workbook = XSSFWorkbook()

                val headerStyle = workbook.createCellStyle()
                headerStyle.alignment = HorizontalAlignment.CENTER

                val sheet = workbook.createSheet("data")
                var row = sheet.createRow(0)

                var cell = row.createCell(0)
                cell.setCellValue("Bekal ID")

                cell = row.createCell(1)
                cell.setCellValue("Nama Barang")

                cell = row.createCell(2)
                cell.setCellValue("Kategori")

                cell = row.createCell(3)
                cell.setCellValue("Wilayah")

                cell = row.createCell(4)
                cell.setCellValue("Stok Awal")

                cell = row.createCell(5)
                cell.setCellValue("Stok Akhir")

                val list = profileViewModel.getList()

                for (i in list.indices) {
                    row = sheet.createRow(i+1)
                    cell = row.createCell(0)
                    cell.setCellValue(list[i].id.toString())

                    cell = row.createCell(1)
                    cell.setCellValue(list[i].namaBarang)

                    cell = row.createCell(2)
                    cell.setCellValue(list[i].kategori)

                    cell = row.createCell(3)
                    cell.setCellValue(list[i].wilayah)

                    cell = row.createCell(4)
                    cell.setCellValue(list[i].stokAwal.toString())

                    cell = row.createCell(5)
                    cell.setCellValue(list[i].stokAkhir.toString())
                }

                val outputStream = FileOutputStream(file)

                workbook.write(outputStream)
                outputStream.close()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Data berhasil di export!", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Export gagal! Eror: ${e.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

    }

    private fun obtainViewModel(fragment: ProfileFragment): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[ProfileViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}