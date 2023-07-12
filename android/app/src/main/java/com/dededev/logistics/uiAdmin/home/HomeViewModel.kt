package com.dededev.logistics.uiAdmin.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.database.LogisticRepository

class HomeViewModel(application: Application) : ViewModel() {

    private val mLogisticRepository: LogisticRepository = LogisticRepository(application)

    var isPusatActive = true

}