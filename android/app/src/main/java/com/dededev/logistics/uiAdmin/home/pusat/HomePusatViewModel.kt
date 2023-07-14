package com.dededev.logistics.uiAdmin.home.pusat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.database.LogisticRepository

class HomePusatViewModel(application: Application) : ViewModel() {
    private val mLogisticRepository: LogisticRepository = LogisticRepository(application)

    var selectedMenu :String = ""

    fun insert(logistic: Logistic) {
        mLogisticRepository.insert(logistic)
    }

    fun update(logistic: Logistic) {
        mLogisticRepository.update(logistic)
    }

    fun getAllLogistics(): LiveData<List<Logistic>> {
        return mLogisticRepository.getAllLogistics()
    }
    fun getLogisticPusat(): LiveData<List<Logistic>> {
        return mLogisticRepository.getLogisticPusat()
    }
}