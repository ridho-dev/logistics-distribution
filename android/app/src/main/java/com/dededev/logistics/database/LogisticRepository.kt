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

    fun getList(): List<Logistic> = mLogisticDao.getList()

    fun getLogisticPusat(): LiveData<List<Logistic>> = mLogisticDao.getLogisticPusat()

    fun getProcessedLogistics(): LiveData<List<Logistic>> = mLogisticDao.getProcessedLogistic()

    fun getLogisticPerRegion(wilayah: String): LiveData<List<Logistic>> = mLogisticDao.getLogisticPerRegion(wilayah)

    fun update(logistic: Logistic) {
        executorService.execute { mLogisticDao.update(logistic) }
    }

    fun insert(logistic: Logistic) {
        executorService.execute { mLogisticDao.insert(logistic) }
    }
}