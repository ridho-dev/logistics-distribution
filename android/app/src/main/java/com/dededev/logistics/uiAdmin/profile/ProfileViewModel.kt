package com.dededev.logistics.uiAdmin.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.LogisticRepository

class ProfileViewModel(application: Application) : ViewModel() {
    private val mRepository = LogisticRepository(application)

    fun getList() = mRepository.getList()
}