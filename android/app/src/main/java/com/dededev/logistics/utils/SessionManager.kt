package com.dededev.logistics.utils

import android.content.Context

class SessionManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setLoggedIn(isLoggedIn: Boolean, jabatan: String, lokasi: String) {
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("jabatan", jabatan)
        editor.putString("lokasi", lokasi)
        editor.apply()
    }

    fun getJabatan(): String? {
        return sharedPreferences.getString("jabatan", "")
    }

    fun getLokasi(): String? {
        return sharedPreferences.getString("lokasi", "")
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}