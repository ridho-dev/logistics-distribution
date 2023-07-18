package com.dededev.logistics.uiAdmin.process

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.database.LogisticRepository

class ProcessViewModel(application: Application) : ViewModel() {
    private val mRepository = LogisticRepository(application)

    var selectedRegion = "Medan"

    private val _processedAlready = MutableLiveData<Boolean>()
    val processedAlready: LiveData<Boolean>
        get() = _processedAlready

    fun setProcessedAlready(value: Boolean) {
        _processedAlready.value = value
    }

    fun getProcessedLogistics() = mRepository.getProcessedLogistics()

    fun getAllLogistics() = mRepository.getAllLogistics()

    fun update(logistic: Logistic) {
        mRepository.update(logistic)
    }

}