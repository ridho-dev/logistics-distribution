package com.dededev.logistics.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LogisticRepository(application: Application) {
    private val mLogisticDao: LogisticDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = LogisticDatabase.getDatabase(application)
        mLogisticDao = db.logisticDao()
    }

    fun getAllLogistics(): LiveData<List<Logistic>> = mLogisticDao.getAllLogistic()

    fun getPerlengkapanKepala(): LiveData<List<Logistic>> = mLogisticDao.getPerlengkapanKepala()

    fun getProcessedLogistics(): LiveData<List<Logistic>> = mLogisticDao.getProcessedLogistic()

    fun getTutupBadan(): LiveData<List<Logistic>> = mLogisticDao.getTutupBadan()

    fun update(logistic: Logistic) {
        executorService.execute { mLogisticDao.update(logistic) }
    }

    fun insert(logistic: Logistic) {
        executorService.execute { mLogisticDao.insert(logistic) }
    }
}