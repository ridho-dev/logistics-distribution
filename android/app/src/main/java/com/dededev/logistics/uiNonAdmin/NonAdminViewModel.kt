package com.dededev.logistics.uiNonAdmin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.database.LogisticRepository

class NonAdminViewModel(application: Application): ViewModel() {
    private val mRepository = LogisticRepository(application)

    fun getLogisticPerRegion(wilayah: String): LiveData<List<Logistic>> {
        return mRepository.getLogisticPerRegion(wilayah)
    }

    fun update(logistic: Logistic) {
        mRepository.update(logistic)
    }
}