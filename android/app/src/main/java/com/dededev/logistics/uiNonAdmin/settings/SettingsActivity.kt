package com.dededev.logistics.uiNonAdmin.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dededev.logistics.R
import com.dededev.logistics.databinding.ActivitySettingsBinding
import com.dededev.logistics.uiAdmin.login.LoginActivity
import com.dededev.logistics.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var pref: SessionManager
    private val REQUEST_CODE:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel = SettingsViewModel(application)

        pref = SessionManager(this)

        binding.btnLogout.setOnClickListener {
            pref.setLoggedIn(false, "", "")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnExport.setOnClickListener {
            lifecycleScope.launch {
                val date = SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(Date())
                val file = File(Environment.getExternalStorageDirectory(), "/${pref.getLokasi()}$date.xlsx")
                export(file)
            }
        }

        binding.profileUserLocation.text = pref.getLokasi()
        binding.profileUserType.text = pref.getJabatan()

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Izin sudah diberikan, lanjutkan tindakan yang membutuhkan izin
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
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

                val listAll = settingsViewModel.getList()
                val list = listAll.filter { it.wilayah == pref.getLokasi().toString() }

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
                    Toast.makeText(this@SettingsActivity, "Data berhasil di export!", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SettingsActivity, "Export gagal! Eror: ${e.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

    }
}