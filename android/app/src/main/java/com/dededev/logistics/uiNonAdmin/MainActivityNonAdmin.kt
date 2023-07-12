package com.dededev.logistics.uiNonAdmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.databinding.ActivityMainNonAdminBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.adapter.NonAdminAdapter
import com.dededev.logistics.uiAdmin.adapter.ProcessAdapter
import com.dededev.logistics.uiAdmin.login.LoginActivity
import com.dededev.logistics.uiAdmin.process.ProcessFragment
import com.dededev.logistics.uiAdmin.process.ProcessViewModel
import com.dededev.logistics.utils.SessionManager

class MainActivityNonAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityMainNonAdminBinding
    private lateinit var nonAdminViewModel: NonAdminViewModel
    private lateinit var logisticList: MutableList<Logistic>
    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNonAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logisticList = mutableListOf()

        pref = SessionManager(this)

        val factory = ViewModelFactory.getInstance(application)
        nonAdminViewModel = ViewModelProvider(this, factory)[NonAdminViewModel::class.java]

        displayProcessedView()
    }
    private fun displayProcessedView() {
        binding.apply {
            rvProcessedKepala.layoutManager = LinearLayoutManager(application)
            rvProcessedKepala.isNestedScrollingEnabled = false
            rvProcessedBadan.layoutManager = LinearLayoutManager(application)
            rvProcessedBadan.isNestedScrollingEnabled = false
            rvProcessedKaki.layoutManager = LinearLayoutManager(application)
            rvProcessedKaki.isNestedScrollingEnabled = false
            rvProcessedPengenal.layoutManager = LinearLayoutManager(application)
            rvProcessedPengenal.isNestedScrollingEnabled = false
            rvProcessedKapdislap.layoutManager = LinearLayoutManager(application)
            rvProcessedKapdislap.isNestedScrollingEnabled = false
            rvProcessedKaplainlain.layoutManager = LinearLayoutManager(application)
            rvProcessedKaplainlain.isNestedScrollingEnabled = false
            rvProcessedKapsat.layoutManager = LinearLayoutManager(application)
            rvProcessedKapsat.isNestedScrollingEnabled = false
        }

        nonAdminViewModel.getAllLogistics().observe(this) {
            logisticList.addAll(it)
            Log.d("TAG", "displayProcessedView: $logisticList")
            val dataKepala = it.filter { item -> item.kategori == "Perlengkapan Kepala" }
            if (dataKepala.isNotEmpty()) {
                binding.tvProcessKepala.visibility = View.VISIBLE
                binding.rvProcessedKepala.adapter = NonAdminAdapter(dataKepala)
            }

            val dataBadan = it.filter { item -> item.kategori == "Tutup Badan" }
            if (dataBadan.isNotEmpty()) {
                binding.tvProcessBadan.visibility = View.VISIBLE
                binding.rvProcessedBadan.adapter = NonAdminAdapter(dataBadan)
            }

            val dataKaki = it.filter { item -> item.kategori == "Tutup Kaki" }
            if (dataKaki.isNotEmpty()) {
                binding.tvProcessKaki.visibility = View.VISIBLE
                binding.rvProcessedKaki.adapter = NonAdminAdapter(dataKaki)
            }

            val dataPengenal = it.filter { item -> item.kategori == "Tanda Pengenal" }
            if (dataPengenal.isNotEmpty()) {
                binding.tvProcessPengenal.visibility = View.VISIBLE
                binding.rvProcessedPengenal.adapter = NonAdminAdapter(dataPengenal)
            }

            val dataKapDislap = it.filter { item -> item.kategori == "Kap Dislap" }
            if (dataKapDislap.isNotEmpty()) {
                binding.tvProcessKapdislap.visibility = View.VISIBLE
                binding.rvProcessedKapdislap.adapter = NonAdminAdapter(dataKapDislap)
            }

            val dataKapLainLain = it.filter { item -> item.kategori == "Kap Lain-lain" }
            if (dataKapLainLain.isNotEmpty()) {
                binding.tvProcessKaplainlain.visibility = View.VISIBLE
                binding.rvProcessedKaplainlain.adapter = NonAdminAdapter(dataKapLainLain)
            }

            val dataKapsat = it.filter { item -> item.kategori == "Kapsat & Almount Kapsat" }
            if (dataKapsat.isNotEmpty()) {
                binding.tvProcessKapsat.visibility = View.VISIBLE
                binding.rvProcessedKapsat.adapter = NonAdminAdapter(dataKapsat)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                pref.setLoggedIn(false, "")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}