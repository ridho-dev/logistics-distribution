package com.dededev.logistics.uiAdmin.home.daerah

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.database.LogisticRepository

class HomeDaerahViewModel(application: Application) : ViewModel() {
    private val mLogisticRepository: LogisticRepository = LogisticRepository(application)

    var selectedMenu = "Semua"
    var selectedRegion = "Medan"

    fun getAllLogistics() = mLogisticRepository.getAllLogistics()

    fun update(logistic: Logistic) {
        mLogisticRepository.update(logistic)
    }
}