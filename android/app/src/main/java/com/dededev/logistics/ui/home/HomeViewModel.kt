package com.dededev.logistics.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.database.LogisticRepository

class HomeViewModel(application: Application) : ViewModel() {

    private val mLogisticRepository: LogisticRepository = LogisticRepository(application)

    fun insert(logistic: Logistic) {
        mLogisticRepository.insert(logistic)
    }

    fun update(logistic: Logistic) {
        mLogisticRepository.update(logistic)
    }

    fun getAllLogistics(): LiveData<List<Logistic>> {
        return mLogisticRepository.getAllLogistics()
    }

    fun getPerlengkapanKepala(): LiveData<List<Logistic>> = mLogisticRepository.getPerlengkapanKepala()

    fun getTutupBadan(): LiveData<List<Logistic>> = mLogisticRepository.getTutupBadan()
}