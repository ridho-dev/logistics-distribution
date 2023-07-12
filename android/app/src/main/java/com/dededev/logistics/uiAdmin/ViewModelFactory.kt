package com.dededev.logistics.uiAdmin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dededev.logistics.uiAdmin.process.ProcessViewModel
import com.dededev.logistics.uiAdmin.home.HomeViewModel
import com.dededev.logistics.uiAdmin.home.daerah.HomeDaerahViewModel
import com.dededev.logistics.uiAdmin.home.pusat.HomePusatViewModel
import com.dededev.logistics.uiNonAdmin.NonAdminViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(private val mApplication: Application)
    : ViewModelProvider.NewInstanceFactory() {
    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(ProcessViewModel::class.java)) {
            return ProcessViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(NonAdminViewModel::class.java)) {
            return NonAdminViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(HomePusatViewModel::class.java)) {
            return HomePusatViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(HomeDaerahViewModel::class.java)) {
            return HomeDaerahViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}