package com.dededev.logistics.uiNonAdmin.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.LogisticRepository

class SettingsViewModel(application: Application): ViewModel() {
    private val mRepository = LogisticRepository(application)

    fun getList() = mRepository.getList()
}